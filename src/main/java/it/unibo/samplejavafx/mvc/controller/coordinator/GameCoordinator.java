package it.unibo.samplejavafx.mvc.controller.coordinator;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameControllerImpl;
import it.unibo.samplejavafx.mvc.controller.replay.ReplayControllerImpl;
import it.unibo.samplejavafx.mvc.model.chessboard.boardfactory.BoardFactoryImpl;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchImpl;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.replay.GameHistory;
import it.unibo.samplejavafx.mvc.model.replay.ReplayManager;
import it.unibo.samplejavafx.mvc.controller.uicontroller.ChessboardViewControllerImpl;
import it.unibo.samplejavafx.mvc.controller.uicontroller.LoadGameController;
import it.unibo.samplejavafx.mvc.controller.uicontroller.LoadoutController;
import it.unibo.samplejavafx.mvc.controller.uicontroller.MainMenuController;
import it.unibo.samplejavafx.mvc.controller.uicontroller.SettingsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.NoSuchElementException;

/**
 * The Game Coordinator.
 */
public final class GameCoordinator {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;
    private static final String MAIN_MENU_CSS = "/css/MainMenu.css";
    private static final Logger LOGGER = LoggerFactory.getLogger(GameCoordinator.class);

    private final Stage stage;
    private final ReplayManager replayManager = new ReplayManager();
    private ChessMatch match;
    private GameController gameController;

    private Path currentSaveFile;

    /**
     * Constructor.
     *
     * @param stage the primary stage
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public GameCoordinator(final Stage stage) {
        this.stage = stage;
        this.match = new ChessMatchImpl();
        this.gameController = new GameControllerImpl(this.match);
    }

    /**
     * Loads the pieces.
     */
    public void loadPieces() {
        gameController.getLoaderController().load();
    }

    /**
     * Initializes the main menu.
     */
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
     * Initializes the settings scene.
     */
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
     * Initializes the loadout scene.
     */
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
     * Initializes the load game scene.
     */
    public void initLoadGame() {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/LoadGameView.fxml"));
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
            LOGGER.error("Failed to load Load Game view", e);
        }
    }

    /**
     * Quits the application.
     */
    public void quit() {
        stage.close();
    }

    /**
     * Saves the current game to the specified path.
     *
     * @param path the path to save the game to.
     * @return true if successful, false otherwise.
     * @throws IOException if an I/O error occurs.
     */
    public boolean saveGame(final Path path) throws IOException {
        this.currentSaveFile = path;
        return replayManager.saveGame(match.getGameHistory(), path);
    }

    /**
     * Gets the path of the current save file, if any.
     *
     * @return the current save file path, or null if this is a new game.
     */
    public Path getCurrentSaveFile() {
        return this.currentSaveFile;
    }

    /**
     * Loads a game from the specified path.
     *
     * @param path the path to load the game from.
     */
    public void loadGame(final Path path) {
        this.currentSaveFile = path;
        final var history = replayManager.loadGame(path);
        if (history != null) {
            try {
                initGame(history);
            } catch (final IOException e) {
                LOGGER.error("Failed to initialize game from save", e);
            }
        }
    }

    /**
     * Initializes the game.
     *
     * @throws IOException if the FXML file cannot be loaded.
     */
    public void initGame() throws IOException {
        initGame(null);
    }

    /**
     * Initializes the game, optionally from a history.
     *
     * @param history the game history to load, or null for a new game.
     * @throws IOException if loading fails.
     */
    public void initGame(final GameHistory history) throws IOException {
        // Create new match and controller
        this.match = new ChessMatchImpl();
        this.gameController = new GameControllerImpl(this.match);
        this.gameController.getLoaderController().load();

        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/GameLayout.fxml"));
        loader.setControllerFactory(c -> new ChessboardViewControllerImpl(this.gameController, this));
        final Parent root = loader.load();
        final ChessboardViewControllerImpl viewController = loader.getController();

        if (history == null) {
            try {
                // TODO: remove reference of the match in the view controller
                final String loadoutId = "standard-chess-loadout";
                final Loadout whiteLoadout = gameController.getLoadoutManager().load(loadoutId).get();
                final Loadout blackLoadout = gameController.getLoadoutManager().load(loadoutId).get().mirrored();
                final ChessMatch newMatch = new ChessMatchImpl(
                        new BoardFactoryImpl(gameController.getLoaderController()).createPopulatedChessboard(
                                whiteLoadout,
                                blackLoadout,
                                viewController
                        ));
                newMatch.getGameHistory().setWhiteLoadout(whiteLoadout);
                newMatch.getGameHistory().setBlackLoadout(blackLoadout);
                this.match = newMatch;
                this.gameController.setMatch(newMatch);
            } catch (final IllegalStateException | NoSuchElementException e) {
                LOGGER.error("Failed to load standard loadout", e);
            }
        } else {
            if (history.getWhiteLoadout() != null && history.getBlackLoadout() != null) {
                final ChessMatch newMatch = new ChessMatchImpl(
                        new BoardFactoryImpl(gameController.getLoaderController()).createPopulatedChessboard(
                                history.getWhiteLoadout(),
                                history.getBlackLoadout(),
                                viewController
                        ));
                newMatch.getGameHistory().setWhiteLoadout(history.getWhiteLoadout());
                newMatch.getGameHistory().setBlackLoadout(history.getBlackLoadout());
                this.match = newMatch;
                this.gameController.setMatch(newMatch);
            } else {
                LOGGER.warn("Missing loadouts in save file, attempting fallback to standard loadout.");
                try {
                    final String loadoutId = "standard-chess-loadout";
                    final Loadout whiteLoadout = gameController.getLoadoutManager().load(loadoutId).orElseThrow();
                    final Loadout blackLoadout = whiteLoadout.mirrored();
                    final ChessMatch newMatch = new ChessMatchImpl(
                            new BoardFactoryImpl(gameController.getLoaderController()).createPopulatedChessboard(
                                    whiteLoadout,
                                    blackLoadout,
                                    viewController
                            ));
                    this.match = newMatch;
                    this.gameController.setMatch(newMatch);
                } catch (final IllegalStateException | NoSuchElementException e) {
                    LOGGER.error("Failed to load fallback loadout", e);
                }
            }
            restoreGame(history);
        }

        match.addObserver(viewController);
        gameController.setChessboardViewController(viewController);

        final var cssLocation = getClass().getResource("/css/GameLayout.css");
        final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setTitle("TurboChess - Game");
        stage.setScene(scene);
        if (cssLocation != null) {
            scene.getStylesheets().add(cssLocation.toExternalForm());
        }
        stage.show();
    }

    private void restoreGame(final GameHistory history) {
        final var replayController = new ReplayControllerImpl(match.getBoard());
        replayController.loadHistory(history);
        replayController.jumpToEnd();

        // Restore turn number and player color
        if (!history.getEvents().isEmpty()) {
            final var lastEvent = history.getEvents().get(history.getEvents().size() - 1);
            final int nextTurn = lastEvent.getTurn() + 1;
            match.setTurnNumber(nextTurn);
            match.setPlayerColor(nextTurn % 2 != 0 ? PlayerColor.BLACK : PlayerColor.WHITE);

            // Sync history to match so new moves are appended correctly
            match.getGameHistory().setEvents(history.getEvents());
        } else {
            // New game (no events yet)
            match.setTurnNumber(1);
            match.setPlayerColor(PlayerColor.WHITE);
        }
    }
}
