package it.unibo.samplejavafx.mvc.controller.uicontroller;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import lombok.Setter;

import javafx.event.ActionEvent;
import java.io.IOException;

/**
 * placeholder.
 */
public final class MainMenuController {
    private final GameCoordinator coordinator;

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
     * placeholder.
     */
    public void setup() {
        coordinator.loadPieces();
    }
}
