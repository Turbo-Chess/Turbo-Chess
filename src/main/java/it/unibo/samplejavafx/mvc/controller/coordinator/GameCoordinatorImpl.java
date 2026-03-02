package it.unibo.samplejavafx.mvc.controller.coordinator;

import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameControllerImpl;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchImpl;
import it.unibo.samplejavafx.mvc.controller.uicontroller.ChessboardViewControllerImpl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * placeholder.
 */
public final class GameCoordinatorImpl implements GameCoordinator {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;
    private static final String MAIN_MENU_CSS = "/css/MainMenu.css";
    private static final Logger LOGGER = LoggerFactory.getLogger(GameCoordinatorImpl.class);

    private final Stage stage;
    private final GameController gameController = new GameControllerImpl();

    /**
     * placeholder.
     *
     * @param stage placeholder.
     */
    public GameCoordinatorImpl(final Stage stage) {
        this.stage = stage;
    }

    /**
     * placeholder.
     */
    public void loadPieces() {
        gameController.getLoaderController().load();
    }

    /**
     * Initializes the main menu.
     */
    @Override
    public void initMainMenu() {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/MainMenu.fxml"));
            loader.setControllerFactory(c -> new it.unibo.samplejavafx.mvc.controller.uicontroller.MainMenuController(this));
            final Parent root = loader.load();
            final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            final var cssLocation = getClass().getResource(MAIN_MENU_CSS);
            if (cssLocation != null) {
                scene.getStylesheets().add(cssLocation.toExternalForm());
            }
            stage.setTitle("TurboChess - Main Menu");
            stage.setScene(scene);
            stage.show();
        } catch (final IOException e) {
            LOGGER.error("Failed to load Main Menu", e);
        }
    }

    /**
     * Initializes the settings scene.
     */
    @Override
    public void initSettings() {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/Settings.fxml"));
            loader.setControllerFactory(c -> new it.unibo.samplejavafx.mvc.controller.uicontroller.SettingsController(this));
            final Parent root = loader.load();
            final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            final var cssLocation = getClass().getResource(MAIN_MENU_CSS);
            if (cssLocation != null) {
                scene.getStylesheets().add(cssLocation.toExternalForm());
            }
            stage.setTitle("TurboChess - Settings");
            stage.setScene(scene);
            stage.show();
        } catch (final IOException e) {
            LOGGER.error("Failed to load Settings", e);
        }
    }

    /**
     * Initializes the loadout scene.
     */
    @Override
    public void initLoadout() {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/Loadout.fxml"));
            loader.setControllerFactory(c -> new it.unibo.samplejavafx.mvc.controller.uicontroller.LoadoutController(this));
            final Parent root = loader.load();
            final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            final var cssLocation = getClass().getResource(MAIN_MENU_CSS);
            if (cssLocation != null) {
                scene.getStylesheets().add(cssLocation.toExternalForm());
            }
            stage.setTitle("TurboChess - Loadout");
            stage.setScene(scene);
            stage.show();
        } catch (final IOException e) {
            LOGGER.error("Failed to load Loadout", e);
        }
    }

    /**
     * Quits the application.
     */
    @Override
    public void quit() {
        stage.close();
    }

    /**
     * placeholder.
     *
     */
    @Override
    public void initGame() throws IOException {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/GameLayout.fxml"));
        loader.setControllerFactory(c -> new ChessboardViewControllerImpl(this.gameController, this));
        final Parent root = loader.load();
        final ChessboardViewControllerImpl viewController = loader.getController();
        // TODO: remove reference of the match in the view controller

        final ChessMatch match = new ChessMatchImpl(
                gameController.getBoardFactory().createPopulatedChessboard(
                        gameController.getWhiteLoadout(),
                        gameController.getBlackLoadout(),
                        viewController
                ));
        this.gameController.setMatch(match);
        match.addObserver(viewController);
        gameController.setChessboardViewController(viewController);
        gameController.getLoaderController().load();

        final var cssLocation = getClass().getResource("/css/GameLayout.css");
        final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setTitle("TurboChess - Game");
        stage.setScene(scene);
        if (cssLocation != null) {
            scene.getStylesheets().add(cssLocation.toExternalForm());
        }
        stage.show();
    }
}
