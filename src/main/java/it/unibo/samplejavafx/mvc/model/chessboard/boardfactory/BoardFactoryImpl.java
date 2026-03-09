package it.unibo.samplejavafx.mvc.model.chessboard.boardfactory;

import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderController;
import it.unibo.samplejavafx.mvc.model.chessboard.BoardObserver;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutEntry;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * A concrete implementation of the {@link BoardFactory} interface.
 *
 * <p>
 * This class uses a {@link LoaderController} to fetch the necessary entity definitions from disk or cache.
 * It manages the creation of unique game IDs for each instantiated piece to ensure proper tracking during the match.
 * </p>
 */
public class BoardFactoryImpl implements BoardFactory {
    private final LoaderController loaderController;
    private int gameId;

    /**
     * Constructs a new {@code BoardFactoryImpl}.
     *
     * @param loaderController The {@link LoaderController} responsible for providing access to loaded entity definitions.
     */
    public BoardFactoryImpl(final LoaderController loaderController) {
        this.loaderController = loaderController;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Iterates through the entries in the provided loadouts and places corresponding pieces onto a new board instance.
     * </p>
     */
    @Override
    public ChessBoard createPopulatedChessboard(
        final Loadout whiteLoadout,
        final Loadout blackLoadout,
        final BoardObserver viewController
    ) {
        final ChessBoard board = new ChessBoardImpl();
        board.addObserver(viewController);

        whiteLoadout.getEntries().forEach(wEntry -> addPieceToBoard(board, wEntry, PlayerColor.WHITE));
        blackLoadout.getEntries().forEach(bEntry -> addPieceToBoard(board, bEntry, PlayerColor.BLACK));
        return board;
    }

    private void addPieceToBoard(final ChessBoard board, final LoadoutEntry entry, final PlayerColor color) {
        board.setEntity(entry.position(), new Piece.Builder()
                .entityDefinition(
                    (PieceDefinition) loaderController.getEntityCache()
                        .get(entry.packId())
                        .get(entry.pieceId())
                )
                .gameId(gameId)
                .playerColor(color)
                .hasMoved(false)
                .build()
        );
        gameId++;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Utilizes the internal game ID counter to instantiate a new piece with the given definition
     * and places it at the specified coordinate on the board.
     * In that way, newly added pieces will have a different ID from the ones already instantiated.
     * </p>
     */
    @Override
    public void createNewPiece(final Point2D pos, final ChessBoard board, 
                               final PieceDefinition pieceDefinition, final PlayerColor color) {
        final var newPiece = new Piece.Builder()
                .entityDefinition(pieceDefinition)
                .gameId(gameId)
                .hasMoved(false)
                .playerColor(color)
                .build();

        board.setEntity(pos, newPiece);

        this.gameId++;
    }
}
