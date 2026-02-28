package it.unibo.samplejavafx.mvc.model.chessmatch;

import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.handler.GameState;

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

    /**
     * placeholder.
     *
     * @param gameState placeholder.
     * @param playerColor placeholder.
     */
    void onGameStateUpdated(GameState gameState, PlayerColor playerColor);

    // TODO: add for the timer
    //void onTimerUpdated(final PlayerColor playerColor);
}
