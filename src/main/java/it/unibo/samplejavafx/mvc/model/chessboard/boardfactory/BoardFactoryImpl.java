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
 * placeholder.
 */
public class BoardFactoryImpl implements BoardFactory {
    private final LoaderController loaderController;
    private int gameId;

    /**
     * placeholder.
     *
     * @param loaderController placeholder.
     */
    public BoardFactoryImpl(final LoaderController loaderController) {
        this.loaderController = loaderController;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChessBoard createPopulatedChessboard(final Loadout whiteLoadout,
                                                final Loadout blackLoadout,
                                                final BoardObserver boardObserver) {
        final ChessBoard board = new ChessBoardImpl();
        board.addObserver(boardObserver);

        whiteLoadout.getEntries().forEach(wEntry -> addPieceToBoard(board, wEntry, PlayerColor.WHITE));
        blackLoadout.getEntries().forEach(bEntry -> addPieceToBoard(board, bEntry, PlayerColor.BLACK));
        return board;
    }

    private void addPieceToBoard(final ChessBoard board, final LoadoutEntry entry, final PlayerColor color) {
        board.setEntity(entry.position(), new Piece.Builder()
                .entityDefinition((PieceDefinition) loaderController.getEntityCache()
                        .get(entry.packId()).get(entry.pieceId()))
                .gameId(gameId)
                .playerColor(color)
                .setHasMoved(false)
                .build()
        );
        gameId++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createNewPiece(final Point2D pos, final ChessBoard board, final PieceDefinition pieceDefinition) {
        final var newPiece = new Piece.Builder()
                .entityDefinition(pieceDefinition)
                .gameId(gameId)
                .setHasMoved(false)
                .build();

        board.setEntity(pos, newPiece);

        this.gameId++;
    }
}
