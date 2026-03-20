package it.unibo.turbochess.controller.uicontroller.impl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.turbochess.controller.coordinator.api.GameCoordinator;
import it.unibo.turbochess.controller.uicontroller.api.MainMenuController;

/**
 * Controller for the main menu UI. Handles user interactions in the main menu
 * and delegates actions to the `GameCoordinator` for scene management and game flow.
 */
public final class MainMenuControllerImpl implements MainMenuController {
    private final GameCoordinator coordinator;

    /**
     * Constructs a new `MainMenuController` with the specified game coordinator.
     *
     * @param coordinator the `GameCoordinator` responsible for handling scene switching and game flow.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "The coordinator is made to be a mutable object"
            + "and used from other classes to make the MVC working")
    public MainMenuControllerImpl(final GameCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startNewGame() {
        this.coordinator.resetGame();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadGame() {
        this.coordinator.initLoadGame();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void openLoadout() {
        this.coordinator.initLoadout();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void openSettings() {
        this.coordinator.initSettings();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quit() {
        this.coordinator.quit();
    }
}
