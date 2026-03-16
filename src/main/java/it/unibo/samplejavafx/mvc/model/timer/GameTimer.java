package it.unibo.samplejavafx.mvc.model.timer;

import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;

/**
 * Interface for the game timer.
 */
public interface GameTimer {

    /**
     * Starts the timer for the current player.
     */
    void start();

    /**
     * Stops the timer.
     */
    void stop();

    /**
     * Switches the timer to the other player.
     */
    void switchTurn();

    /**
     * Gets the time remaining for a player.
     *
     * @param player the player to get the time for.
     * @return the time remaining in seconds.
     */
    long getTimeRemaining(PlayerColor player);

    /**
     * Sets the active player.
     *
     * @param player the player to set as active.
     */
    void setActivePlayer(PlayerColor player);

    /**
     * Shuts down the timer resources.
     */
    void shutdown();
}
