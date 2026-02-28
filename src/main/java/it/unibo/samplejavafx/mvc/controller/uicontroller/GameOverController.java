package it.unibo.samplejavafx.mvc.controller.uicontroller;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * placeholder.
 */
public final class GameOverController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameOverController.class);

    @FXML
    private Label statusLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Button restartButton;

    @FXML
    private Button mainMenuButton;

    private final GameCoordinator gameCoordinator;

    /**
     * placeholder.
     *
     * @param gameCoordinator the game coordinator.
     */
    public GameOverController(final GameCoordinator gameCoordinator) {
        this.gameCoordinator = gameCoordinator;
    }

    /**
     * placeholder.
     */
    @FXML
    public void initialize() {
        this.restartButton.setOnAction(e -> {
            try {
                this.restartButton.getScene().getWindow().hide();
                this.gameCoordinator.initGame();
            } catch (final IOException ex) {
                LOGGER.error("Failed to restart game", ex);
            }
        });

        this.mainMenuButton.setOnAction(e -> {
            this.restartButton.getScene().getWindow().hide();
            this.gameCoordinator.initMainMenu();
        });
    }

    /**
     * placeholder.
     *
     * @param statusText  placeholder.
     * @param messageText placeholder.
     */
    public void setTextLabel(final String statusText, final String messageText) {
        this.statusLabel.setText(statusText);
        this.messageLabel.setText(messageText);
    }
}
