package it.unibo.samplejavafx.mvc.model.chessmatch;

import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;


public interface ChessMatchObserver {
    void onTurnUpdated(final int turnNumber);

    void onPlayerUpdated(final PlayerColor playerColor);

    // TODO: add for the timer
    //void onTimerUpdated(final PlayerColor playerColor);
}
