package it.unibo.samplejavafx.mvc.controller.uicontroller;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import it.unibo.samplejavafx.mvc.model.chessboard.BoardObserver;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
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
