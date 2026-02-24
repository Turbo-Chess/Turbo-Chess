package it.unibo.samplejavafx.mvc.controller.uicontroller;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;

import javafx.event.ActionEvent;
import java.io.IOException;

/**
 * placeholder.
 */
public final class MainMenuController {
    private final GameCoordinator coordinator;

    /**
     * placeholder.
     *
     * @param coordinator placeholder.
     */
    public MainMenuController(final GameCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    /**
     * placeholder.
     *
     * @param e placeholder.
     * @throws IOException placeholder.
     */
    public void startNewGame(final ActionEvent e) throws IOException {
        this.coordinator.initGame();
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
