package it.unibo.samplejavafx.mvc.controller.uicontroller;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;

import javafx.event.ActionEvent;
import java.io.IOException;

/**
 * JavaFX controller for the main menu view.
 *
 * <p>
 * Exposes handlers bound to UI actions (new game, load game, settings, quit) and delegates navigation to the
 * {@link GameCoordinator}.
 * </p>
 */
public final class MainMenuController {
    private final GameCoordinator coordinator;

    /**
     * Creates a controller instance.
     *
     * @param coordinator the application coordinator used for navigation and game lifecycle actions
     */
    public MainMenuController(final GameCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    /**
     * Starts a new game, resetting the current match and showing the game view.
     *
     * @param e the UI action event
     * @throws IOException if the underlying reset operation requires IO and fails
     */
    public void startNewGame(final ActionEvent e) throws IOException {
        this.coordinator.resetGame();
    }

    /**
     * Opens the load game menu.
     *
     * @param e the action event.
     */
    public void loadGame(final ActionEvent e) {
        this.coordinator.initLoadGame();
    }

    /**
     * Opens the loadout menu.
     *
     * @param e the action event
     */
    public void openLoadout(final ActionEvent e) {
        this.coordinator.initLoadout();
    }

    /**
     * Opens the settings menu.
     *
     * @param e the action event
     */
    public void openSettings(final ActionEvent e) {
        this.coordinator.initSettings();
    }

    /**
     * Quits the application.
     *
     * @param e the action event
     */
    public void quit(final ActionEvent e) {
        this.coordinator.quit();
    }
}
