package it.unibo.samplejavafx.mvc.controller.uicontroller;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinatorImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * @param coordinator the game coordinator.
     */
    public GameOverController(final GameCoordinator coordinator) {
        this.gameCoordinator = coordinator;
    }

    /**
     * placeholder.
     */
    @FXML
    public void initialize() {
        this.restartButton.setOnAction(e -> {
            this.restartButton.getScene().getWindow().hide();

            this.gameCoordinator.resetGame();
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
