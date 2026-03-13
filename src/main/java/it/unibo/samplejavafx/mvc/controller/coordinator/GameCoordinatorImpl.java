package it.unibo.samplejavafx.mvc.controller.coordinator;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.samplejavafx.mvc.ControllerContext;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameControllerImpl;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchImpl;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
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
 * A concrete implementation of the {@link GameCoordinator} interface.
 *
 * <p>
 * This class uses JavaFX's {@link FXMLLoader} to dynamically load FXML layouts and instantiate their respective
 * controllers. It holds a reference to the main application {@link Stage} and manages scene transitions,
 * as well as the game creation with all its other components.
 * </p>
 *
 * <p>
 * It acts as the central router for the application, ensuring that the correct view is displayed and
 * properly initialized with necessary dependencies.
 * </p>
 */
public final class GameCoordinatorImpl implements GameCoordinator {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameCoordinatorImpl.class);

    private final ControllerContext controllerContext = ControllerContext.createDefaultContext();
    private final GameController gameController = new GameControllerImpl(this, controllerContext);
    private final ReplayManager replayManager = new ReplayManager();
    private Path currentSaveFile;
    private final ViewFactory viewFactory;

    /**
     * Constructs a new {@code GameCoordinatorImpl}.
     *
     * @param viewFactory The {@link ViewFactory} used to create view scenes.
     */
    // The stage is the main window passed from javafx library, and it's designed to be mutable
    // so it's correct in that case.
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public GameCoordinatorImpl(final ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Delegates resource loading to the {@link GameController}'s loader.
     * </p>
     */
    @Override
    public void loadPieces() {
        this.controllerContext.loaderController().load();
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Initializes the Main Menu view, loading the "MainMenu.fxml" layout and applying the corresponding stylesheet.
     * </p>
     */
    @Override
    public void initMainMenu() {
        viewFactory.showMainMenu(this);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Initializes the Settings view, loading the "Settings.fxml" layout.
     * </p>
     */
    @Override
    public void initSettings() {
        viewFactory.showSettings(this);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Initializes the Loadout configuration view.
     * </p>
     */
    @Override
    public void initLoadout() {
        viewFactory.showLoadout(this.gameController, this, controllerContext.loadoutManager());
    }

    /**
     * Initializes the loadout editor scene.
     */
    @Override
    public void initLoadoutEditor() {
        viewFactory.showLoadoutEditor(this, controllerContext.loaderController(), controllerContext.loadoutManager());
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Loads the "Promotion.fxml" layout and initializes its controller with the current player's context.
     * This prepares the UI for a pawn promotion event.
     * </p>
     */
    @Override
    public void initPromotion() {
        viewFactory.initPromotion(this.gameController, controllerContext.loaderController());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quit() {
        viewFactory.quit();
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Invalidates the current game root and re-initializes the game, effectively resetting the match.
     * </p>
     */
    @Override
    public void resetGame() {
        viewFactory.resetGame();
        initGame();
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Creates a new chess game recreating all the components needed for it.
     * </p>
     */
    @Override
    public void initGame() {
        createNewMatch();
        loadGameUI();
        showGame();
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Sets the scene of the primary stage to the game scene and shows it, without creating a new match.
     * </p>
     */
    @Override
    public void showGame() {
        viewFactory.showGame();
    }

    private void loadGameUI() {
        viewFactory.initGameUI(this.gameController, this);
    }

    private void createNewMatch() {
        final ChessMatch match = new ChessMatchImpl();
        this.gameController.setMatch(match);
        controllerContext.boardFactory().populateChessboard(
                gameController.getWhiteLoadout(),
                gameController.getBlackLoadout(),
                match.getBoard());
        match.getGameHistory().setWhiteLoadout(gameController.getWhiteLoadout());
        match.getGameHistory().setBlackLoadout(gameController.getBlackLoadout());
        //TODO: refreshBoardView
    }

    /**
     * placeholder.
     */
    @Override
    public void initLoadGame() {
        viewFactory.initLoadGame(this);
    }

    /**
     * placeholder.
     *
     * @param path placeholder.
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

            resetGame();

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
                    LOGGER.debug("Skipping event {}", lastEvent);
                }

                match.setTurnNumber(turn);
                match.setPlayerColor(player);
            }
        }
    }

    /**
     * placeholder.
     *
     * @param fileToSave placeholder.
     * @return placeholder.
     */
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

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    @Override
    public Path getCurrentSaveFile() {
        return this.currentSaveFile;
    }
}
