package it.unibo.samplejavafx.mvc.controller.coordinator;

import java.nio.file.Path;

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

    void resetGame();

    /**
     * Initializes the game.
     */
    void initGame();

    /**
     * placeholder.
     */
    void showGame();

    void loadPieces();

    void loadGame(Path path);

    boolean saveGame(Path fileToSave);

    Path getCurrentSaveFile();
}
