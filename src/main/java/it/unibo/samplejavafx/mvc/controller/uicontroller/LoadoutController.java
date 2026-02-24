package it.unibo.samplejavafx.mvc.controller.uicontroller;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import javafx.event.ActionEvent;

/**
 * Controller for the Loadout scene.
 */
public final class LoadoutController {
    private final GameCoordinator coordinator;

    /**
     * Constructor.
     *
     * @param coordinator the game coordinator
     */
    public LoadoutController(final GameCoordinator coordinator) {
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
