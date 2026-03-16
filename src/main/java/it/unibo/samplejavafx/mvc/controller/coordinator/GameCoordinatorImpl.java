package it.unibo.samplejavafx.mvc.controller.coordinator;

import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameControllerImpl;
import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderController;
import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderControllerImpl;
import it.unibo.samplejavafx.mvc.model.chessboard.boardfactory.BoardFactory;
import it.unibo.samplejavafx.mvc.model.chessboard.boardfactory.BoardFactoryImpl;
import it.unibo.samplejavafx.mvc.model.chessboard.boardfactory.DefinitionRegistry;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchImpl;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutManager;
import it.unibo.samplejavafx.mvc.model.replay.GameEvent;
import it.unibo.samplejavafx.mvc.model.replay.GameHistory;
import it.unibo.samplejavafx.mvc.model.replay.MoveEvent;
import it.unibo.samplejavafx.mvc.model.replay.ReplayManager;
import it.unibo.samplejavafx.mvc.controller.replay.ReplayController;
import it.unibo.samplejavafx.mvc.controller.replay.ReplayControllerImpl;
import it.unibo.samplejavafx.mvc.view.ViewFactory;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Concrete implementation of the {@link GameCoordinator} interface.
 *
 * <p>
 * This class acts as the central hub for the program. It holds a reference to the {@link ViewFactory} to manage the
 * scene switching. It also contains other components crucial to the overall
 * game lifecycle like the {@link GameController} and the {@link BoardFactory}.
 * It basically makes every component "handshake" correctly, ensuring that every component is properly initialized
 * with necessary dependencies.
 * </p>
 */
public final class GameCoordinatorImpl implements GameCoordinator {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameCoordinatorImpl.class);
    private static final long DEFAULT_TIME_SECONDS = 600;

    private final BoardFactory boardFactory;
    private final LoadoutManager loadoutManager;
    private final GameController gameController;
    private final ReplayManager replayManager = new ReplayManager();
    private Path currentSaveFile;
    private final ViewFactory viewFactory;

    /**
     * Constructs a new {@code GameCoordinatorImpl}.
     *
     * @param viewFactory The {@link ViewFactory} used to create view scenes.
     */
    public GameCoordinatorImpl(final ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
        final LoaderController loaderController = new LoaderControllerImpl();
        loaderController.load();
        this.boardFactory = new BoardFactoryImpl(loaderController.getEntityDefinitionCacheEntries());
        this.loadoutManager = new LoadoutManager();
        this.gameController = new GameControllerImpl(this, boardFactory, this.loadoutManager);
    }

    /**
     * {@inheritDoc}
    */
    @Override
    public void initMainMenu() {
        shutdownCurrentTimer();
        viewFactory.showMainMenu(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initSettings() {
        shutdownCurrentTimer();
        viewFactory.showSettings(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initLoadout() {
        shutdownCurrentTimer();
        viewFactory.showLoadout(this.gameController, this, loadoutManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initLoadoutEditor() {
        shutdownCurrentTimer();
        viewFactory.showLoadoutEditor(this, (DefinitionRegistry) this.boardFactory, loadoutManager);
    }

    /**
     * {@inheritDoc}
     *
     * This prepares the UI for a pawn promotion event.
     */
    @Override
    public void initPromotion() {
        viewFactory.initPromotion(this, this.gameController, (DefinitionRegistry) boardFactory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quit() {
        shutdownCurrentTimer();
        viewFactory.quit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetGame() {
        shutdownCurrentTimer();
        viewFactory.resetGame();
        initGame();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initGame() {
        createNewMatch(DEFAULT_TIME_SECONDS, DEFAULT_TIME_SECONDS);
        loadGameUI();
        gameController.getMatch().getGameTimer().start();
        showGame();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showGame() {
        viewFactory.showGame();
    }

    private void loadGameUI() {
        viewFactory.initGameUI(this.gameController, this);
    }

    private void createNewMatch(final long whiteTimeSeconds, final long blackTimeSeconds) {
        final ChessMatch match = new ChessMatchImpl(whiteTimeSeconds, blackTimeSeconds);
        this.gameController.setMatch(match);
        boardFactory.populateChessboard(
                gameController.getWhiteLoadout(),
                gameController.getBlackLoadout(),
                match.getBoard());
        match.getGameHistory().setWhiteLoadout(gameController.getWhiteLoadout());
        match.getGameHistory().setBlackLoadout(gameController.getBlackLoadout());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initLoadGame() {
        shutdownCurrentTimer();
        viewFactory.initLoadGame(this);
    }

    /**
     * {@inheritDoc}
     */
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

            final long whiteTime = history.getWhiteTimeRemaining() > 0 ? history.getWhiteTimeRemaining() : DEFAULT_TIME_SECONDS;
            final long blackTime = history.getBlackTimeRemaining() > 0 ? history.getBlackTimeRemaining() : DEFAULT_TIME_SECONDS;

            shutdownCurrentTimer();
            viewFactory.resetGame();
            createNewMatch(whiteTime, blackTime);
            loadGameUI();
            showGame();

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
                    LOGGER.debug("Skipping event {}", lastEvent);
                }

                match.setTurnNumber(turn);
                match.setPlayerColor(player);
            }
            match.getGameTimer().start();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveGame(final Path fileToSave) {
        final GameHistory history = gameController.getGameHistory();
        history.setWhiteLoadout(gameController.getWhiteLoadout());
        history.setBlackLoadout(gameController.getBlackLoadout());

        final ChessMatch match = gameController.getMatch();
        if (match != null) {
            final var timer = match.getGameTimer();
            history.setWhiteTimeRemaining(timer.getTimeRemaining(PlayerColor.WHITE));
            history.setBlackTimeRemaining(timer.getTimeRemaining(PlayerColor.BLACK));
        }

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

    /**
     * {@inheritDoc}
     */
    @Override
    public Path getCurrentSaveFile() {
        return this.currentSaveFile;
    }

    private void shutdownCurrentTimer() {
        final ChessMatch match = gameController.getMatch();
        if (match != null) {
            match.getGameTimer().shutdown();
        }
    }
}
