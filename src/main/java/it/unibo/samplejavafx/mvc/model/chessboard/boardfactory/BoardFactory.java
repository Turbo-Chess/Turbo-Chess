package it.unibo.samplejavafx.mvc.model.chessboard.boardfactory;

import it.unibo.samplejavafx.mvc.model.chessboard.BoardObserver;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;

/**
 * placeholder.
 */
@FunctionalInterface
public interface BoardFactory {
    /**
     * placeholder.
     *
     * @param whiteLoadout placeholder.
     * @param blackLoadout placeholder.
     * @param boardObserver placeholder.
     * @return placeholder.
     */
    ChessBoard createPopulatedChessboard(Loadout whiteLoadout,
                                         Loadout blackLoadout,
                                         BoardObserver boardObserver);
}
