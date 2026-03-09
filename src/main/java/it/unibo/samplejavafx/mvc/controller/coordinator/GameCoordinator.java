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
     * Initializes the loadout selector.
     */
    void initLoadout();

    /**
     * Initializes the loadout editor.
     */
    void initLoadoutEditor();

    /**
     * placeholder.
     */
    void initPromotion();

    /**
     * Quits the application.
     */
    void quit();

    /**
     * Resets the game.
     */
    void resetGame();

    /**
     * Initializes the game.
     */
    void initGame();

    /**
     * Reopens the scene of the current game.
     */
    void showGame();

    /**
     * Loads the pieces for the game.
     */
    void loadPieces();

    /**
     * Loads the last saved game. 
     * 
     * @param path the path of the save file.
     */
    void loadGame(Path path);

    /**
     * Saves the current game.
     * 
     * @param fileToSave the file path where to save the game.
     * @return {@code true} if the save is successful, {@code false} otherwise.
     */
    boolean saveGame(Path fileToSave);

    /**
     * Getter for the save file.
     * 
     * @return the {@link Path} of the save file. 
     */
    Path getCurrentSaveFile();
}
