package it.unibo.samplejavafx;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinatorImpl;
import it.unibo.samplejavafx.mvc.view.JafaFXViewFactory;
import it.unibo.samplejavafx.mvc.view.ViewFactory;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * The main JavaFX {@link Application} class.
 * <p>
 * This class is responsible for configuring the primary stage, initializing the
 * application architecture (Coordinator and View), and handling global exceptions
 * during startup.
 */
public final class App extends Application {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;

    /**
     * Starts the JavaFX application.
     * <p>
     * It configures the stage size, sets up global error handling, initializes
     * the {@link GameCoordinator}, and launches the main menu.
     *
     * @param stage the primary stage for this application, onto which
     *              the application scene can be set.
     */
    @Override
    public void start(final Stage stage) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            if (throwable instanceof Exception) {
                showFatalStartupError((Exception) throwable);
            } else {
                showFatalStartupError(new Exception(throwable));
            }
        });
        stage.setMinHeight(WINDOW_HEIGHT);
        stage.setMinWidth(WINDOW_WIDTH);

        try {
            final ViewFactory viewFactory = new JafaFXViewFactory(stage);
            final GameCoordinator coordinator = new GameCoordinatorImpl(viewFactory);

            // Start with Main Menu
            coordinator.initMainMenu();
        } catch (final Exception e) { // CHECKSTYLE:OFF: IllegalCatch
            showFatalStartupError(e);
        } // CHECKSTYLE:ON: IllegalCatch

    }

    private void showFatalStartupError(final Exception e) {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Configuration Error");
        alert.setHeaderText("Invalid JSON Configuration");

        // Unwrap the exception to find the root cause
        Throwable rootCause = e;
        while (rootCause.getCause() != null && !rootCause.getCause().equals(rootCause)) {
            rootCause = rootCause.getCause();
        }

        final String errorMsg = rootCause.getMessage() != null
                ? e.getMessage() + " \nReason: " + rootCause.getMessage()
                : "Unknown error";
        alert.setContentText(errorMsg);
        alert.showAndWait();
    }

    /**
     * The main entry point for the JavaFX application.
     *
     * @param args command line arguments passed to the application.
     */
    public static void main(final String[] args) {
        launch(args);
    }
}
