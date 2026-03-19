package it.unibo.turbochess.controller.uicontroller.api;

/**
 * Interface for the Main Menu controller.
 */
public interface MainMenuController {

    /**
     * Starts a new game by resetting the game state.
     */
    void startNewGame();

    /**
     * Opens the load game menu.
     */
    void loadGame();

    /**
     * Opens the loadout menu.
     */
    void openLoadout();

    /**
     * Opens the settings menu.
     */
    void openSettings();

    /**
     * Quits the application.
     */
    void quit();
}

