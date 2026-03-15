package it.unibo.samplejavafx.mvc.model.chessboard.boardfactory;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;

/**
 * Functional interface for populating boards.
 */
@FunctionalInterface
public interface BoardCreator {
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
     * @param board The {@link ChessBoard} instance to populate.
     */
    void populateChessboard(Loadout whiteLoadout, Loadout blackLoadout, ChessBoard board);
}
