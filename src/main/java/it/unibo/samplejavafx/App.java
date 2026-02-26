package it.unibo.samplejavafx;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import javafx.application.Application;
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
        final GameCoordinator coordinator = new GameCoordinator(stage);

        // Load game resources
        coordinator.loadPieces();

        // Start with Main Menu
        coordinator.initMainMenu();
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

