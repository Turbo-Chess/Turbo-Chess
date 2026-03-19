package it.unibo.turbochess.model.score.api;

import it.unibo.turbochess.model.entity.impl.PlayerColor;

/**
 * Interface for observing score changes.
 */
@FunctionalInterface
public interface ScoreObserver {
    /**
     * Called when the score of a player changes.
     *
     * @param player the player whose score changed.
     * @param newScore the new score of the player.
     */
    void onScoreChanged(PlayerColor player, int newScore);
}
