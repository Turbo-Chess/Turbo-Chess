package it.unibo.samplejavafx.mvc.controller.uicontroller;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import lombok.Setter;

import javafx.event.ActionEvent;
import java.io.IOException;

public class MainMenuController {
    @Setter
    private GameCoordinator coordinator;

    public void initialize() {
    }

    public void startNewGame(ActionEvent e) throws IOException {
        this.coordinator.initGame();
    }

    public void setup() {
        coordinator.loadPieces();
    }
}
