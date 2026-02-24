package it.unibo.samplejavafx;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import it.unibo.samplejavafx.mvc.controller.uicontroller.MainMenuController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * placeholder.
 */
public final class App extends Application {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;
    private static final int MIN_WINDOW_SIZE = 500;
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/MainMenu.fxml"));

    /**
     * placeholder.
     *
     * @param stage placeholder.
     * @throws Exception placeholder.
     */
    @Override
    public void start(final Stage stage) throws Exception {
        try {
            stage.setMinHeight(MIN_WINDOW_SIZE);
            stage.setMinWidth(MIN_WINDOW_SIZE);
            final GameCoordinator coordinator = new GameCoordinator(stage);
            loader.setControllerFactory(c -> new MainMenuController(coordinator));
            final Parent root = loader.load();
            final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

            final var cssLocation = getClass().getResource("/css/MainMenu.css");
            if (cssLocation != null) {
                scene.getStylesheets().add(cssLocation.toExternalForm());
            } else {
            LOGGER.warn("Warning: MainMenu.css not found, loading without styles.");
        }

        final MainMenuController controller = loader.getController();
        //controller.setCoordinator(coordinator);
        //TODO: Refactor with javafx factories
        controller.setup();

        stage.setTitle("TurboChess - Main Menù");
        stage.setScene(scene);
        stage.show();

        stage.show();

        // If a not-caught exception is thrown it will be managed here
        } catch (final RuntimeException e) {
            showFatalStartupError(e);
        }
    }

    private void showFatalStartupError(final Throwable e) {
        LOGGER.error("Fatal startup error", e);

        // An exception thrown by jackson is re-thrown as its own, so it needs to be unwrapped
        Throwable rootCause = e;
        while (rootCause.getCause() != null && !rootCause.getCause().equals(rootCause)) {
            rootCause = rootCause.getCause();
        }

        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Configuration Error");
        alert.setHeaderText("Invalid JSON Configuration");
        final String content = rootCause.getMessage() != null
                ? e.getMessage() + " \nReason: " + rootCause.getMessage()
                : "Unknown error";
        alert.setContentText(content);

        alert.showAndWait();

        Platform.exit();
        System.exit(1);
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

