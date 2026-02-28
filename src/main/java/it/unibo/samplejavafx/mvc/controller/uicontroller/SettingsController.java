package it.unibo.samplejavafx.mvc.controller.uicontroller;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinatorImpl;
import javafx.event.ActionEvent;

/**
 * Controller for the Settings scene.
 */
public final class SettingsController {
    private final GameCoordinatorImpl coordinator;

    /**
     * Constructor.
     *
     * @param coordinator the game coordinator
     */
    public SettingsController(final GameCoordinatorImpl coordinator) {
        this.coordinator = coordinator;
    }

    /**
     * Handles the "Back" button action.
     *
     * @param e the action event
     */
    public void backToMenu(final ActionEvent e) {
        this.coordinator.initMainMenu();
    }
}
