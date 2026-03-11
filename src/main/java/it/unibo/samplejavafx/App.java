package it.unibo.samplejavafx;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinatorImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * placeholder.
 */
public final class App extends Application {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;

    /**
     * placeholder.
     *
     * @param stage placeholder.
     */
    @Override
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    // I'm catching all exception because I want this window to spawn an alert for different exceptions thrown
    // from different part of the program.
    public void start(final Stage stage) {

        stage.setMinHeight(WINDOW_HEIGHT);
        stage.setMinWidth(WINDOW_WIDTH);

        try {
            final GameCoordinator coordinator = new GameCoordinatorImpl(stage);

            // Load game resources
            coordinator.loadPieces();

            // Start with Main Menu
            coordinator.initMainMenu();
        } catch (final Exception e) { // CHECKSTYLE:OFF: IllegalCatch
            showFatalStartupError(e);
        } // CHECKSTYLE:ON: IllegalCatch

    }

    private void showFatalStartupError(final Exception e) {
        Platform.runLater(() -> {
            final Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Configuration Error");
            alert.setHeaderText("Invalid JSON Configuration");

            // Unwrap the exception to find the root cause (Jackson wrap the ex into another one)
            Throwable rootCause = e;
            while (rootCause.getCause() != null && !rootCause.getCause().equals(rootCause)) {
                rootCause = rootCause.getCause();
            }

            alert.setContentText(rootCause.getMessage() != null ? e.getMessage() + " \nReason: " + rootCause.getMessage() : "Unknown error");
            alert.showAndWait();
        });
    }

    /**
     * placeholder.
     *
     * @param args placeholder.
     */
    public static void main(final String[] args) {
        launch(args);
    }
}
