package it.unibo.samplejavafx.mvc.model.score;

import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;

/**
 * Interface for managing the score of the game.
 */
public interface ScoreManager {

    /**
     * Gets the current score of a player.
     *
     * @param player the player to get the score of.
     * @return the current score of the player.
     */
    int getScore(PlayerColor player);

    /**
     * Resets the score of both players to zero.
     */
    void reset();

    /**
     * Adds an observer to the score manager.
     *
     * @param observer the observer to add.
     */
    void addObserver(ScoreObserver observer);

    /**
     * Removes an observer from the score manager.
     *
     * @param observer the observer to remove.
     */
    void removeObserver(ScoreObserver observer);
    
    /**
     * Increases the score of a player.
     *
     * @param player the player who gains points.
     * @param points the points to add.
     */
    void addPoints(PlayerColor player, int points);

    /**
     * Decreases the score of a player.
     *
     * @param player the player who loses points.
     * @param points the points to subtract.
     */
    void removePoints(PlayerColor player, int points);
}
