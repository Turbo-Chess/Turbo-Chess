package it.unibo.samplejavafx.mvc.controller.coordinator;

/**
 * Interface for the Game Coordinator.
 */
public interface GameCoordinator {

    /**
     * Initializes the main menu.
     */
    void initMainMenu();

    /**
     * Initializes the settings.
     */
    void initSettings();

    /**
     * Initializes the loadout.
     */
    void initLoadout();

    /**
     * placeholder.
     */
    void initPromotion();

    /**
     * Quits the application.
     */
    void quit();

    /**
     * Initializes the game.
     */
    void initGame();

    /**
     * placeholder.
     */
    void showGame();
}
