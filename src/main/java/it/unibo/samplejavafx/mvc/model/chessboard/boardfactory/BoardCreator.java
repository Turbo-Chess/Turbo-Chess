package it.unibo.samplejavafx.mvc.model.chessboard.boardfactory;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;

/**
 * Functional interface for populating boards.
 */
@FunctionalInterface
public interface BoardCreator {
    /**
     * Populates a chessboard with pieces according to the provided loadouts.
     *
     * @param whiteLoadout the loadout configuration for white pieces
     * @param blackLoadout the loadout configuration for black pieces
     * @param board the chessboard to populate
     */
    void populateChessboard(Loadout whiteLoadout, Loadout blackLoadout, ChessBoard board);
}
