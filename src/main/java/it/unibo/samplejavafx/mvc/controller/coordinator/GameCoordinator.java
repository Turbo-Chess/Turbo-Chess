package it.unibo.samplejavafx.mvc.controller.coordinator;

import java.io.IOException;

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
     * Quits the application.
     */
    void quit();

    /**
     * Initializes the game.
     *
     * @throws IOException if the FXML file cannot be loaded.
     */
    void initGame() throws IOException;
}
