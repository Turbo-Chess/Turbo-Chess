package it.unibo.samplejavafx.mvc.model.chessboard.boardfactory;

import it.unibo.samplejavafx.mvc.model.chessboard.BoardObserver;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;

/**
 * placeholder.
 */
public interface BoardFactory  {
    public ChessBoard createPopulatedChessboard(final Loadout whiteLoadout,
                                                final Loadout blackLoadout,
                                                final BoardObserver boardObserver);
}

