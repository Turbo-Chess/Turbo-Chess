package it.unibo.turbochess.controller.coordinator.impl;

import it.unibo.turbochess.controller.coordinator.api.GameCoordinator;
import it.unibo.turbochess.controller.gamecontroller.api.GameController;
import it.unibo.turbochess.controller.gamecontroller.impl.GameControllerImpl;
import it.unibo.turbochess.controller.loadercontroller.api.LoaderController;
import it.unibo.turbochess.controller.loadercontroller.impl.LoaderControllerImpl;
import it.unibo.turbochess.model.chessboard.boardfactory.api.BoardFactory;
import it.unibo.turbochess.model.chessboard.boardfactory.impl.BoardFactoryImpl;
import it.unibo.turbochess.model.chessboard.boardfactory.api.DefinitionRegistry;
import it.unibo.turbochess.model.chessmatch.api.ChessMatch;
import it.unibo.turbochess.model.chessmatch.impl.ChessMatchImpl;
import it.unibo.turbochess.model.entity.impl.PlayerColor;
import it.unibo.turbochess.model.loadout.api.LoadoutManager;
import it.unibo.turbochess.model.loadout.impl.LoadoutManagerImpl;
import it.unibo.turbochess.model.replay.api.GameEvent;
import it.unibo.turbochess.model.replay.api.GameHistory;
import it.unibo.turbochess.model.replay.impl.MoveEvent;
import it.unibo.turbochess.model.replay.api.ReplayManager;
import it.unibo.turbochess.model.replay.impl.ReplayManagerImpl;
import it.unibo.turbochess.model.settings.impl.GameSettings;
import it.unibo.turbochess.model.settings.api.GameSettingsManager;
import it.unibo.turbochess.model.settings.impl.GameSettingsManagerImpl;
import it.unibo.turbochess.controller.replay.api.ReplayController;
import it.unibo.turbochess.controller.replay.impl.ReplayControllerImpl;
import it.unibo.turbochess.view.api.ViewFactory;

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

    private final BoardFactory boardFactory;
    private final LoadoutManager loadoutManager;
    private final GameController gameController;
    private final ReplayManager replayManager = new ReplayManagerImpl();
    private final GameSettingsManager settingsManager = new GameSettingsManagerImpl();
    private volatile long initialTimeSeconds = GameSettings.DEFAULT_INITIAL_TIME_SECONDS;
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
        this.loadoutManager = new LoadoutManagerImpl();
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
     * <p>
     * This prepares the UI for a pawn promotion event.
     * </p>
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
        final long timeSeconds = this.initialTimeSeconds;
        createNewMatch(timeSeconds, timeSeconds);
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

            final long defaultTimeSeconds = this.initialTimeSeconds;
            final long whiteTime = history.getWhiteTimeRemaining() > 0 ? history.getWhiteTimeRemaining() : defaultTimeSeconds;
            final long blackTime = history.getBlackTimeRemaining() > 0 ? history.getBlackTimeRemaining() : defaultTimeSeconds;

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
                    turn = move.turn() + 1;
                    player = (move.entityColor() == PlayerColor.WHITE) ? PlayerColor.BLACK : PlayerColor.WHITE;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public long getInitialTimeSeconds() {
        return this.initialTimeSeconds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInitialTimeSeconds(final long seconds) {
        final long sanitized = GameSettings.sanitizeInitialTimeSeconds(seconds);
        this.initialTimeSeconds = sanitized;
        settingsManager.save(new GameSettings(sanitized));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetInitialTimeSeconds() {
        this.initialTimeSeconds = GameSettings.DEFAULT_INITIAL_TIME_SECONDS;
        settingsManager.save(GameSettings.defaultSettings());
    }

    private void shutdownCurrentTimer() {
        final ChessMatch match = gameController.getMatch();
        if (match != null) {
            match.getGameTimer().shutdown();
        }
    }
}
