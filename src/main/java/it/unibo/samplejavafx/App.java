package it.unibo.samplejavafx;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinatorImpl;
import it.unibo.samplejavafx.mvc.view.JafaFXViewFactory;
import it.unibo.samplejavafx.mvc.view.ViewFactory;
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
     */
    @Override
    public void start(final Stage stage) {

        stage.setMinHeight(WINDOW_HEIGHT);
        stage.setMinWidth(WINDOW_WIDTH);

        final ViewFactory viewFactory = new JafaFXViewFactory(stage);
        final GameCoordinator coordinator = new GameCoordinatorImpl(viewFactory);

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
