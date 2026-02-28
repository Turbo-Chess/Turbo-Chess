package it.unibo.samplejavafx.mvc.controller.uicontroller;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class GameOverController {
    @FXML
    private Label statusLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Button restartButton;

    @FXML
    private Button mainMenuButton;

    private final GameCoordinator gameCoordinator;

    public GameOverController(final GameCoordinator gameCoordinator) {
        this.gameCoordinator = gameCoordinator;
    }

    @FXML
    public void initialize() {
        this.restartButton.setOnAction(e -> {
            try {
                this.restartButton.getScene().getWindow().hide();
                this.gameCoordinator.initGame();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        this.mainMenuButton.setOnAction(e -> {
            this.restartButton.getScene().getWindow().hide();
            this.gameCoordinator.initMainMenu();
        });
    }

    public void setTextLabel(final String statusText, final String messageText) {
        this.statusLabel.setText(statusText);
        this.messageLabel.setText(messageText);
    }
}
