package it.unibo.turbochess.controller.uicontroller.impl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.turbochess.controller.coordinator.api.GameCoordinator;
import it.unibo.turbochess.controller.uicontroller.api.GameOverController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controller for the Game Over screen.
 * Handles the user interactions and displays the game result.
 */
public final class GameOverControllerImpl implements GameOverController {

    @FXML
    private Label statusLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private Button restartButton;

    @FXML
    private Button mainMenuButton;

    private final GameCoordinator gameCoordinator;

    /**
     * Creates a new GameOverController.
     *
     * @param coordinator the game coordinator.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "The game coordinator is mutable and shared.")
    public GameOverControllerImpl(final GameCoordinator coordinator) {
        this.gameCoordinator = coordinator;
    }

    /**
     * Initializes the controller.
     * Sets up the action handlers for the restart and main menu buttons.
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
     * {@inheritDoc}
     */
    @Override
    public void setTextLabel(final String statusText, final String messageText, final String scoreText) {
        this.statusLabel.setText(statusText);
        this.messageLabel.setText(messageText);
        this.scoreLabel.setText(scoreText);
    }
}
