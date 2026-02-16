package it.unibo.samplejavafx;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import it.unibo.samplejavafx.mvc.controller.uicontroller.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * placeholder.
 */
public final class App extends Application {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;
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
        //var fxmlLocation = getClass().getResource( "/layouts/MainMenu.fxml");
        //if (fxmlLocation == null) {
        //    throw new RuntimeException("CRITICAL: MainMenu.fxml not found in the classpath!");
        //}
        final GameCoordinator coordinator = new GameCoordinator(stage);
        final Parent root = loader.load();
        final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        final var cssLocation = getClass().getResource("/css/MainMenu.css");
        if (cssLocation != null) {
            scene.getStylesheets().add(cssLocation.toExternalForm());
        } else {
            LOGGER.warn("Warning: MainMenu.css not found, loading without styles.");
        }

        final MainMenuController controller = loader.getController();
        controller.setCoordinator(coordinator);
        //TODO: Refactor with javafx factories
        controller.setup();

        stage.setTitle("TurboChess - Main Menù");
        stage.setScene(scene);
        stage.show();
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
