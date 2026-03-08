package it.unibo.samplejavafx.mvc.controller.coordinator;

import java.nio.file.Path;

import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;

/**
 * The {@code GameCoordinator} interface acts as the high-level application controller, managing the
 * navigation between different screens (scenes) and the overall lifecycle of the game application.
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
     * Initializes and displays the Pawn Promotion dialog.
     */
    void initPromotion();

    /**
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

    void loadPieces();

    void loadGame(Path path);

    boolean saveGame(Path fileToSave);

    Path getCurrentSaveFile();

    /**
     * @return the game controller.
     */
    GameController getGameController();
}
