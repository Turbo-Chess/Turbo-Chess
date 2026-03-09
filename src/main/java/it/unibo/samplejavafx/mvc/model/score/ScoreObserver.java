package it.unibo.samplejavafx.mvc.model.score;

import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;

/**
 * Interface for observing score changes.
 */
public interface ScoreObserver {
    /**
     * Called when the score of a player changes.
     *
     * @param player the player whose score changed.
     * @param newScore the new score of the player.
     */
    void onScoreChanged(PlayerColor player, int newScore);
}
