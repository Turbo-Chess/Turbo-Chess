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
     * @throws Exception placeholder.
     */
    @Override
    public void start(final Stage stage) throws Exception {

            // Don't let the window be resized less than 500
            stage.setMinHeight(WINDOW_HEIGHT);
            stage.setMinWidth(WINDOW_WIDTH);
            stage.show();

        try {
            final GameCoordinator coordinator = new GameCoordinatorImpl(stage);

            // Load game resources
            coordinator.loadPieces();

            // Start with Main Menu
            coordinator.initMainMenu();
        } catch (final Throwable e) {
            System.out.println("maionese");
            showFatalStartupError(e);
        }

    }

    private void showFatalStartupError(final Throwable e) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Configuration Error");
            alert.setHeaderText("Invalid JSON Configuration");

            // Unwrap the exception to find the root cause (Jackson wrap the ex into another one)
            Throwable rootCause = e;
            while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
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
