package it.unibo.turbochess.controller.coordinator.api;

import java.nio.file.Path;

/**
 * The {@code GameCoordinator} acts as the high-level application controller, managing the
 * navigation between different screens (scenes) and the overall lifecycle of the game application.
 *
 * <p>
 * It provides methods to initialize and switch between the Main Menu, Settings, Loadout configuration,
 * and the active Game view.
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
     * Initializes the menu to load a saved game.
     */
    void initLoadGame();

    /**
     * Quits the application terminating the main stage.
     */
    void quit();

    /**
     * Resets the current game session.
     * Clears the game state and re-initializes the game, effectively restarting the match.
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

    /**
     * Returns the initial time (in seconds) allocated to each player for the entire game.
     *
     * @return the initial time in seconds
     */
    long getInitialTimeSeconds();

    /**
     * Sets the initial time (in seconds) allocated to each player for the entire game.
     *
     * @param seconds the desired initial time in seconds; must be non-negative
     */
    void setInitialTimeSeconds(long seconds);

    /**
     * Resets the initial time to its default value.
     * The exact default is implementation-dependent.
     */
    void resetInitialTimeSeconds();
}
