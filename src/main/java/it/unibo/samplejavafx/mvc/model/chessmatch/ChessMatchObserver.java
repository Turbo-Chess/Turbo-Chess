package it.unibo.samplejavafx.mvc.model.chessmatch;

import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;

/**
 * placeholder.
 */
public interface ChessMatchObserver {
    /**
     * placeholder.
     *
     * @param turnNumber placeholder.
     */
    void onTurnUpdated(int turnNumber);

    /**
     * placeholder.
     *
     * @param playerColor placeholder.
     */
    void onPlayerUpdated(PlayerColor playerColor);

    // TODO: add for the timer
    //void onTimerUpdated(final PlayerColor playerColor);
}
