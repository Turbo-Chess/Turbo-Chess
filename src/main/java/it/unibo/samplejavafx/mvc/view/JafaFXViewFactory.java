package it.unibo.samplejavafx.mvc.view;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.controller.uicontroller.BoardView;
import it.unibo.samplejavafx.mvc.controller.uicontroller.ChessboardViewController;
import it.unibo.samplejavafx.mvc.controller.uicontroller.ChessboardViewControllerImpl;
import it.unibo.samplejavafx.mvc.controller.uicontroller.LoadGameController;
import it.unibo.samplejavafx.mvc.controller.uicontroller.LoadoutEditorImpl;
import it.unibo.samplejavafx.mvc.controller.uicontroller.LoadoutSelectorImpl;
import it.unibo.samplejavafx.mvc.controller.uicontroller.MainMenuController;
import it.unibo.samplejavafx.mvc.controller.uicontroller.PromotionControllerImpl;
import it.unibo.samplejavafx.mvc.controller.uicontroller.SettingsController;
import it.unibo.samplejavafx.mvc.model.chessboard.BoardObserver;
import it.unibo.samplejavafx.mvc.model.chessboard.boardfactory.DefinitionRegistry;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchObserver;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Concrete implementation of {@link ViewFactory} using JavaFX.
 * Manages the creation and display of JavaFX scenes for the application.
 */
public final class JafaFXViewFactory implements ViewFactory {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;
    private static final String MAIN_MENU_CSS = "/css/MainMenu.css";
    private static final Logger LOGGER = LoggerFactory.getLogger(JafaFXViewFactory.class);

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "The stage is a mutable object by javafx"
            + "and it can't be defensive copied")
    private final Stage stage;
    private Scene gameScene;
    private Parent gameRoot;

    /**
     * Constructs a new {@code JafaFXViewFactory}.
     *
     * @param stage The primary JavaFX {@link Stage} for the application.
     */
    public JafaFXViewFactory(final Stage stage) {
        this.stage = stage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMainMenu(final GameCoordinator gameCoordinator) {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/MainMenu.fxml"));
            loader.setControllerFactory(c -> new MainMenuController(gameCoordinator));
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
     * {@inheritDoc}
     */
    @Override
    public void showSettings(final GameCoordinator gameCoordinator) {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/Settings.fxml"));
            loader.setControllerFactory(c -> new SettingsController(gameCoordinator));
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
     * {@inheritDoc}
     */
    @Override
    public void showLoadout(
            final GameController gameController,
            final GameCoordinator gameCoordinator,
            final LoadoutManager loadoutManager) {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/LoadoutSelector.fxml"));
            loader.setControllerFactory(c -> new LoadoutSelectorImpl(
                    gameController,
                    gameCoordinator,
                    loadoutManager
            ));
            final Parent root = loader.load();
            final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            stage.setTitle("TurboChess - Loadout Selector");
            stage.setScene(scene);
            stage.show();
        } catch (final IOException e) {
            LOGGER.error("Failed to load Loadout Selector", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showLoadoutEditor(
            final GameCoordinator gameCoordinator,
            final DefinitionRegistry entityCache,
            final LoadoutManager loadoutManager) {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/LoadoutEditor.fxml"));
            loader.setControllerFactory(c -> new LoadoutEditorImpl(
                    gameCoordinator,
                    entityCache,
                    loadoutManager
            ));

            final Parent root = loader.load();
            final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            stage.setTitle("TurboChess - Loadout Editor");
            stage.setScene(scene);
            stage.show();
        } catch (final IOException e) {
            LOGGER.error("Failed to load Loadout Editor", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initPromotion(
            final GameCoordinator gameCoordinator,
            final GameController gameController,
            final DefinitionRegistry entityCache) {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/Promotion.fxml"));
            loader.setControllerFactory(c -> new PromotionControllerImpl(gameCoordinator, gameController, entityCache));

            final Parent root = loader.load();
            final PromotionControllerImpl prom = loader.getController();
            prom.init(gameController.getMatch().getCurrentPlayer());
            final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

            stage.setScene(scene);
            stage.show();
        } catch (final IOException e) {
            LOGGER.error("Failed to load Promotion GUI", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initLoadGame(final GameCoordinator gameCoordinator) {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/LoadGame.fxml"));
            loader.setControllerFactory(c -> new LoadGameController(gameCoordinator));
            final Parent root = loader.load();
            final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            final var cssLocation = getClass().getResource(MAIN_MENU_CSS);
            if (cssLocation != null) {
                scene.getStylesheets().add(cssLocation.toExternalForm());
            }
            stage.setTitle("TurboChess - Load Game");
            stage.setScene(scene);
            stage.show();
        } catch (final IOException e) {
            LOGGER.error("Failed to load Load Game", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initGameUI(final GameController gameController, final GameCoordinator gameCoordinator) {
        if (this.gameRoot != null) {
            return;
        }

        try {
           final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/GameLayout.fxml"));
           loader.setControllerFactory(c -> new ChessboardViewControllerImpl(gameController, gameCoordinator));

           this.gameRoot = loader.load();
           final ChessboardViewController chessboardViewController = loader.getController();
           gameController.getMatch().getBoard().addObserver((BoardObserver) chessboardViewController);
           gameController.getMatch().addObserver((ChessMatchObserver) chessboardViewController);
           gameController.setBoardView((BoardView) chessboardViewController);
           chessboardViewController.refreshBoardView(gameController.getMatch().getBoard());

           final var cssLocation = getClass().getResource("/css/GameLayout.css");
            this.gameScene = new Scene(gameRoot, WINDOW_WIDTH, WINDOW_HEIGHT);

           if (cssLocation != null) {
               this.gameScene.getStylesheets().add(cssLocation.toExternalForm());
           }
       } catch (final IOException e) {
            LOGGER.error("Failed to load Game Layout", e);
       }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quit() {
        stage.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGameBeingShown() {
        return this.gameRoot != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showGame() {
        stage.setTitle("TurboChess - Game");
        stage.setScene(this.gameScene);
        stage.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetGame() {
        this.gameRoot = null;
    }
}
