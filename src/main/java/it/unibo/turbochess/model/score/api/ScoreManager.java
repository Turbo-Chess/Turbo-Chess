package it.unibo.turbochess.model.score.api;

import it.unibo.turbochess.model.chessboard.board.api.BoardObserver;
import it.unibo.turbochess.model.entity.impl.PlayerColor;

/**
 * Interface for managing the score of the game.
 */
public interface ScoreManager extends BoardObserver {

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
}
