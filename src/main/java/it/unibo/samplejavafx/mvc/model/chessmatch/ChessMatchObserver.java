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

    /**
     * Called when the score of a player changes.
     *
     * @param player the player whose score changed.
     * @param newScore the new score of the player.
     */
    void onScoreChanged(PlayerColor player, int newScore);

    // TODO: add for the timer
    //void onTimerUpdated(final PlayerColor playerColor);
}
