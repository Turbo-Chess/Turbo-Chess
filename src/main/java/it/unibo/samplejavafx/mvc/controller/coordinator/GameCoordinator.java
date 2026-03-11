package it.unibo.samplejavafx.mvc.controller.coordinator;

import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;

import java.nio.file.Path;

/**
 * The {@code GameCoordinator} interface acts as the high-level application controller, managing the
 * navigation between different screens (scenes) and the overall lifecycle of the game application.
 *
 * <p>
 * It provides methods to initialize and switch between the Main Menu, Settings, Loadout configuration,
 * and the active Game view. It also handles application-wide actions like quitting or resetting the game.
 * </p>
 */
public interface GameCoordinator {

    /**
     * Loads and displays the Main Menu scene.
     * This is typically the entry point for user interaction after application startup.
     */
    void initMainMenu();

    /**
     * Loads and displays the Settings scene.
     * Allows the user to configure application preferences.
     */
    void initSettings();

    /**
     * Loads and displays the Loadout configuration scene.
     * Allows players to select and customize their pieces selection before a match.
     */
    void initLoadout();

    /**
     * Initializes the loadout editor.
     */
    void initLoadoutEditor();

    /**
     * Initializes and displays the Pawn Promotion dialog.
     */
    void initPromotion();

    /**
     * Initializes the load game menu.
     */
    void initLoadGame();

    /**
     * Quits the application.
     * Terminates the application gracefully.
     * Closes the main stage and releases resources.
     */
    void quit();

    /**
     * Resets the current game session.
     * Clears the game state and re-initializes the game view, effectively restarting the match.
     */
    void resetGame();

    /**
     * Initializes a new game session.
     * Sets up the game environment, creates a new match, and transitions the view to the game board.
     */
    void initGame();

    /**
     * Switches the current scene to the active Game view.
     * Used to return to the game from other menus without re-initialize the game.
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
