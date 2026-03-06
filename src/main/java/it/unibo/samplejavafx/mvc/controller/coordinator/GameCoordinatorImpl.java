package it.unibo.samplejavafx.mvc.controller.coordinator;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameControllerImpl;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchImpl;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.controller.uicontroller.ChessboardViewControllerImpl;
import it.unibo.samplejavafx.mvc.controller.uicontroller.LoadoutSelector;
import it.unibo.samplejavafx.mvc.controller.uicontroller.PromotionController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;

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
    private Parent gameRoot;
    private Scene gameScene;
    private ChessboardViewControllerImpl chessboardViewController;
    private final GameController gameController = new GameControllerImpl(this);

    /**
     * placeholder.
     *
     * @param stage placeholder.
     */
    // The stage is the main window passed from javafx library and it's designed to be mutable
    // so it's correct in that case.
    @SuppressFBWarnings("EI_EXPOSE_REP2")
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
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/LoadoutSelector.fxml"));
            loader.setControllerFactory(c -> new LoadoutSelector(this.gameController, this));
            final Parent root = loader.load();
            final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            /*final var cssLocation = getClass().getResource(MAIN_MENU_CSS);
            if (cssLocation != null) {
                scene.getStylesheets().add(cssLocation.toExternalForm());
            }*/
            stage.setTitle("TurboChess - Loadout");
            stage.setScene(scene);
            stage.show();
        } catch (final IOException e) {
            LOGGER.error("Failed to load Loadout", e);
        }
    }

    /**
     * placeholder.
     */
    @Override
    public void initPromotion() {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/Promotion.fxml"));
            loader.setControllerFactory(c -> new PromotionController(this.gameController));
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
            LOGGER.error("Failed to load Promotion GUI", e);
        }
    }

    /**
     * Quits the application.
     */
    @Override
    public void quit() {
        stage.close();
    }

    @Override
    public void resetGame() {
        this.gameRoot = null;
        initGame();
    }

    /**
     * placeholder.
     */
    @Override
    public void initGame() {
        loadGameUI();
        createNewMatch();
        showGame();
    }

    @Override
    public void showGame() {
        stage.setTitle("TurboChess - Game");
        stage.setScene(this.gameScene);
        stage.show();
    }

    private void loadGameUI() {
        if (gameRoot != null) {
            return;
        }

       try {
           final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/GameLayout.fxml"));
           loader.setControllerFactory(c -> new ChessboardViewControllerImpl(this.gameController, this));

           this.gameRoot = loader.load();
           this.chessboardViewController = loader.getController();

           final var cssLocation = getClass().getResource("/css/GameLayout.css");
            this.gameScene = new Scene(gameRoot, WINDOW_WIDTH, WINDOW_HEIGHT);


           if (cssLocation != null) {
               this.gameScene.getStylesheets().add(cssLocation.toExternalForm());
           }
       } catch (final IOException e) {
           System.out.println("Maionese");
       }

    }

    private void createNewMatch() {
        final ChessMatch match = new ChessMatchImpl(
                gameController.getBoardFactory().createPopulatedChessboard(
                        gameController.getWhiteLoadout(),
                        gameController.getBlackLoadout(),
                        this.chessboardViewController
                ));
        this.gameController.setMatch(match);
        match.addObserver(this.chessboardViewController);
        gameController.setChessboardViewController(this.chessboardViewController);
        gameController.getLoaderController().load();
    }

    public void initLoadGame() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initLoadGame'");
    }

    @Override
    public void loadGame(Path path) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadGame'");
    }

    @Override
    public boolean saveGame(Path fileToSave) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveGame'");
    }

    @Override
    public Path getCurrentSaveFile() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentSaveFile'");
    }
}
