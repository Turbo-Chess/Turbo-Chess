package it.unibo.samplejavafx.mvc.controller.coordinator;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameControllerImpl;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchImpl;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.controller.uicontroller.ChessboardViewControllerImpl;
import it.unibo.samplejavafx.mvc.controller.uicontroller.LoadGameController;
import it.unibo.samplejavafx.mvc.controller.uicontroller.LoadoutController;
import it.unibo.samplejavafx.mvc.controller.uicontroller.MainMenuController;
import it.unibo.samplejavafx.mvc.controller.uicontroller.PromotionController;
import it.unibo.samplejavafx.mvc.controller.uicontroller.SettingsController;
import it.unibo.samplejavafx.mvc.model.replay.GameEvent;
import it.unibo.samplejavafx.mvc.model.replay.GameHistory;
import it.unibo.samplejavafx.mvc.model.replay.MoveEvent;
import it.unibo.samplejavafx.mvc.model.replay.ReplayManager;
import it.unibo.samplejavafx.mvc.controller.replay.ReplayController;
import it.unibo.samplejavafx.mvc.controller.replay.ReplayControllerImpl;
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
    private final ReplayManager replayManager = new ReplayManager();
    private Path currentSaveFile;

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
            loader.setControllerFactory(c -> new MainMenuController(this));
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
            loader.setControllerFactory(c -> new SettingsController(this));
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
            loader.setControllerFactory(c -> new LoadoutController(this));
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
           LOGGER.error("Failed to load Game Layout", e);
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
        
        this.chessboardViewController.refreshBoardView(match.getBoard());

        gameController.getLoaderController().load();
    }

    public void initLoadGame() {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/LoadGame.fxml"));
            loader.setControllerFactory(c -> new LoadGameController(this));
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

    @Override
    public void loadGame(final Path path) {
        final GameHistory history = replayManager.loadGame(path);
        if (history != null) {
            this.currentSaveFile = path;

            if (history.getWhiteLoadout() != null) {
                gameController.setWhiteLoadout(history.getWhiteLoadout());
            }
            if (history.getBlackLoadout() != null) {
                gameController.setBlackLoadout(history.getBlackLoadout());
            }

            initGame();

            final ChessMatch match = gameController.getMatch();
            
            final ReplayController replayController = new ReplayControllerImpl(match.getBoard());
            replayController.loadHistory(history);
            replayController.jumpToEnd();
            
            match.getGameHistory().setEvents(history.getEvents());

            final GameEvent lastEvent = history.getLastEvent();
            if (lastEvent != null) {
                int turn = lastEvent.getTurn();
                PlayerColor player = PlayerColor.WHITE;
                
                if (lastEvent instanceof MoveEvent move) {
                     player = move.entityColor() == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
                     if (move.entityColor() == PlayerColor.BLACK) {
                         turn++;
                     }
                } else {
                    // TODO: Handle spawn/despawn events
                }
                
                match.setTurnNumber(turn);
                match.setPlayerColor(player);
            }
        }
    }

    @Override
    public boolean saveGame(final Path fileToSave) {
        final GameHistory history = gameController.getGameHistory();
        history.setWhiteLoadout(gameController.getWhiteLoadout());
        history.setBlackLoadout(gameController.getBlackLoadout());
        
        LOGGER.info("Saving game history with {} events", history.getEvents().size());
        
        try {
            if (replayManager.saveGame(history, fileToSave)) {
                this.currentSaveFile = fileToSave;
                return true;
            }
        } catch (final IOException e) {
            LOGGER.error("Failed to save game", e);
        }
        return false;
    }

    @Override
    public Path getCurrentSaveFile() {
        return this.currentSaveFile;
    }

    @Override
    public GameController getGameController() {
        return this.gameController;
    }
}
