package it.unibo.samplejavafx.mvc.view;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderController;
import it.unibo.samplejavafx.mvc.controller.uicontroller.*;
import it.unibo.samplejavafx.mvc.model.chessboard.BoardObserver;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchObserver;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class JafaFXViewFactory implements ViewFactory {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;
    private static final String MAIN_MENU_CSS = "/css/MainMenu.css";

    private final Stage stage;
    private Scene gameScene;
    private Parent gameRoot;


    public JafaFXViewFactory(final Stage stage) {
        this.stage = stage;
    }
    @Override
    public void showMainMenu(GameCoordinator gameCoordinator) {
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
            //LOGGER.error("Failed to load Main Menu", e);
        }
    }

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
            //LOGGER.error("Failed to load Settings", e);
        }
    }

    @Override
    public void showLoadout(final GameController gameController, final GameCoordinator gameCoordinator, final LoadoutManager loadoutManager) {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/LoadoutSelector.fxml"));
            loader.setControllerFactory(c -> new LoadoutSelector(gameController, gameCoordinator, loadoutManager));
            final Parent root = loader.load();
            final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            /*final var cssLocation = getClass().getResource(MAIN_MENU_CSS);
            if (cssLocation != null) {
                scene.getStylesheets().add(cssLocation.toExternalForm());
            }*/
            stage.setTitle("TurboChess - Loadout Selector");
            stage.setScene(scene);
            stage.show();
        } catch (final IOException e) {
            //LOGGER.error("Failed to load Loadout Selector", e);
        }
    }

    @Override
    public void showLoadoutEditor(GameCoordinator gameCoordinator, LoaderController loaderController, LoadoutManager loadoutManager) {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/LoadoutEditor.fxml"));
            loader.setControllerFactory(c -> new LoadoutEditor(gameCoordinator, loaderController, loadoutManager));

            final Parent root = loader.load();
            final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            /*final var cssLocation = getClass().getResource(MAIN_MENU_CSS);
            if (cssLocation != null) {
                scene.getStylesheets().add(cssLocation.toExternalForm());
            }*/
            stage.setTitle("TurboChess - Loadout Editor");
            stage.setScene(scene);
            stage.show();
        } catch (final IOException e) {
            //LOGGER.error("Failed to load Loadout Editor", e);
        }
    }

    @Override
    public void initPromotion(GameController gameController, LoaderController loaderController) {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/Promotion.fxml"));
            loader.setControllerFactory(c -> new PromotionController(gameController, loaderController));

            final Parent root = loader.load();
            final PromotionController prom = loader.getController();
            prom.init(gameController.getMatch().getCurrentPlayer());
            final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

            /*if (cssLocation != null) {
                scene.getStylesheets().add(cssLocation.toExternalForm());
            }*/
            stage.setScene(scene);
            stage.show();
        } catch (final IOException e) {
            //LOGGER.error("Failed to load Promotion GUI", e);
        }
    }

    @Override
    public void initLoadGame(GameCoordinator gameCoordinator) {
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
            //LOGGER.error("Failed to load Load Game", e);
        }
    }

    @Override
    public void initGameUI(final GameController gameController, final GameCoordinator gameCoordinator) {
        if (this.gameRoot != null) {
            return;
        }

        try {
           final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/GameLayout.fxml"));
           loader.setControllerFactory(c -> new ChessboardViewControllerImpl(gameController, gameCoordinator));

           this.gameRoot = loader.load();
           ChessboardViewController chessboardViewController = loader.getController();
           gameController.getMatch().getBoard().addObserver((BoardObserver) chessboardViewController);
           gameController.getMatch().addObserver((ChessMatchObserver) chessboardViewController);
           gameController.setChessboardViewController(chessboardViewController);
           chessboardViewController.refreshBoardView(gameController.getMatch().getBoard());

           final var cssLocation = getClass().getResource("/css/GameLayout.css");
            this.gameScene = new Scene(gameRoot, WINDOW_WIDTH, WINDOW_HEIGHT);

           if (cssLocation != null) {
               this.gameScene.getStylesheets().add(cssLocation.toExternalForm());
           }
       } catch (final IOException e) {
            //LOGGER.error("Failed to load Game Layout", e);
       }
    }

    @Override
    public void quit() {
        stage.close();
    }

    @Override
    public boolean isGameBeingShown() {
        return !(this.gameRoot == null);
    }

    @Override
    public void showGame() {
        stage.setTitle("TurboChess - Game");
        stage.setScene(this.gameScene);
        stage.show();
    }

    @Override
    public void resetGame() {
        this.gameRoot = null;
    }
}
