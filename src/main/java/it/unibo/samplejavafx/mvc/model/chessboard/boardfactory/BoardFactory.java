package it.unibo.samplejavafx.mvc.model.chessboard.boardfactory;

import it.unibo.samplejavafx.mvc.model.chessboard.BoardObserver;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * The {@code BoardFactory} interface defines a factory for creating and populating {@link ChessBoard} instances.
 *
 * <p>
 * It provides methods to generate a fully populated board based on player loadouts, as well as utility methods
 * for creating individual game pieces dynamically during a match (e.g., for pawn promotion).
 * </p>
 */
public interface BoardFactory {
    /**
     * Creates a new {@link ChessBoard} populated with pieces according to the specified loadouts for both players.
     *
     * <p>
     * This method initializes the board state, places pieces in their starting positions, and attaches
     * the provided observer to monitor subsequent board events.
     * </p>
     *
     * @param whiteLoadout  The {@link Loadout} configuration for the White player.
     * @param blackLoadout  The {@link Loadout} configuration for the Black player.
     * @param boardObserver The {@link BoardObserver} to attach to the new board (e.g., for updating the UI).
     * @return A fully initialized and populated {@link ChessBoard}.
     */
    ChessBoard createPopulatedChessboard(Loadout whiteLoadout,
                                         Loadout blackLoadout,
                                         BoardObserver boardObserver);

    /**
     * Creates and places a new piece on an existing board at the specified position.
     *
     * <p>
     * This method is typically used for game mechanics that introduce new pieces during play,
     * such as pawn promotion.
     * </p>
     *
     * @param pos             The target {@link Point2D} position for the new piece.
     * @param board           The {@link ChessBoard} on which to place the piece.
     * @param pieceDefinition The {@link PieceDefinition} defining the type and attributes of the new piece.
     * @param color           The {@link PlayerColor} of the player who owns the new piece.
     */
    void createNewPiece(Point2D pos,
                        ChessBoard board,
                        PieceDefinition pieceDefinition,
                        PlayerColor color);
}
