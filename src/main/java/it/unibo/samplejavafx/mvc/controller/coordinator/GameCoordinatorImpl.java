package it.unibo.samplejavafx.mvc.controller.coordinator;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameControllerImpl;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchImpl;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.controller.uicontroller.ChessboardViewControllerImpl;
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
 * A concrete implementation of the {@link GameCoordinator} interface.
 * <p>
 * This class uses JavaFX's {@link FXMLLoader} to dynamically load FXML layouts and instantiate their respective
 * controllers. It holds a reference to the main application {@link Stage} and manages scene transitions,
 * as well as the game creation with all its other components.
 * </p>
 * <p>
 * It acts as the central router for the application, ensuring that the correct view is displayed and
 * properly initialized with necessary dependencies.
 * </p>
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
     * Constructs a new {@code GameCoordinatorImpl}.
     *
     * @param stage The primary {@link Stage} of the JavaFX application, used as the main window container.
     */
    // The stage is the main window passed from javafx library, and it's designed to be mutable
    // so it's correct in that case.
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public GameCoordinatorImpl(final Stage stage) {
        this.stage = stage;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Delegates resource loading to the {@link GameController}'s loader.
     * </p>
     */
    public void loadPieces() {
        gameController.getLoaderController().load();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Initializes the Main Menu view, loading the "MainMenu.fxml" layout and applying the corresponding stylesheet.
     * </p>
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
     * {@inheritDoc}
     * <p>
     * Initializes the Settings view, loading the "Settings.fxml" layout.
     * </p>
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
     * {@inheritDoc}
     * <p>
     * Initializes the Loadout configuration view.
     * </p>
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
     * {@inheritDoc}
     * <p>
     * Loads the "Promotion.fxml" layout and initializes its controller with the current player's context.
     * This prepares the UI for a pawn promotion event.
     * </p>
     */
    @Override
    public void initPromotion() {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/Promotion.fxml"));
            // TODO: pass the loader controller to constructor
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
     * {@inheritDoc}
     */
    @Override
    public void quit() {
        stage.close();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Invalidates the current game root and re-initializes the game, effectively resetting the match.
     * </p>
     */
    @Override
    public void resetGame() {
        this.gameRoot = null;
        initGame();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Creates a new chess game recreating all the components needed for it.
     * </p>
     */
    @Override
    public void initGame() {
        loadGameUI();
        createNewMatch();
        showGame();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Sets the scene of the primary stage to the game scene and shows it, without creating a new match.
     * </p>
     */
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
        // TODO: refactor this to now pass the view controller to the model
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
