package it.unibo.samplejavafx.mvc.controller.uicontroller;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * JavaFX controller for the game over dialog/window.
 *
 * <p>
 * Displays the final match status and allows the user to restart the match or go back to the main menu.
 * </p>
 */
public final class GameOverController {

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
     * Creates a controller instance.
     *
     * @param coordinator the application coordinator used to restart the match or navigate to the main menu
     */
    public GameOverController(final GameCoordinator coordinator) {
        this.gameCoordinator = coordinator;
    }

    /**
     * Initializes the controller by wiring UI actions to the coordinator.
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
     * Updates the displayed labels.
     *
     * @param statusText the main status text (e.g., "Checkmate", "Draw")
     * @param messageText the detailed message for the player
     * @param scoreText the final score summary
     */
    public void setTextLabel(final String statusText, final String messageText, final String scoreText) {
        this.statusLabel.setText(statusText);
        this.messageLabel.setText(messageText);
        this.scoreLabel.setText(scoreText);
    }
}
