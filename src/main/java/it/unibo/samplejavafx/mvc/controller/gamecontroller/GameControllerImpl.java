package it.unibo.samplejavafx.mvc.controller.gamecontroller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderController;
import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderControllerImpl;
import it.unibo.samplejavafx.mvc.controller.movecontroller.MoveCache;
import it.unibo.samplejavafx.mvc.controller.movecontroller.MoveCacheImpl;
import it.unibo.samplejavafx.mvc.controller.uicontroller.ChessboardViewController;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.boardfactory.BoardFactory;
import it.unibo.samplejavafx.mvc.model.chessboard.boardfactory.BoardFactoryImpl;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutEntry;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutManager;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.properties.GameProperties;
import it.unibo.samplejavafx.mvc.model.replay.GameHistory;
import it.unibo.samplejavafx.mvc.model.replay.GameHistoryRecorder;
import it.unibo.samplejavafx.mvc.model.rules.AdvancedRules;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A concrete implementation of the {@link GameController} interface.
 *
 * <p>
 * This class serves as the central hub for game logic execution, maintaining references to all critical
 * game subsystems (match, coordinator, factories). It processes user input from the view layer,
 * updates the model state, and reflects changes back to the UI.
 * </p>
 *
 * <p>
 * Key responsibilities include:
 * - Managing player loadouts and piece definitions.
 * - Coordinating move validation and execution through the {@link ChessMatch}.
 * - Handling special game events like promotion and surrender.
 * </p>
 */
public final class GameControllerImpl implements GameController {
    private static final List<String> PATHS = List.of(
            GameProperties.INTERNAL_ENTITIES_FOLDER.getPath(),
            GameProperties.EXTERNAL_MOD_FOLDER.getPath());

    // Will the taken from the selected loadout
    private static final String STANDARD_LOADOUT_ID = "standard-chess-loadout";
    @Getter
    private final LoaderController loaderController = new LoaderControllerImpl(PATHS);
    private final MoveCache moveCache = new MoveCacheImpl();
    @Getter
    // Loadout Manager is used as a "service" class to manage and load loadouts, so it's intended to be
    // passed as a mutable dependency
    @SuppressFBWarnings("EI_EXPOSE_REP")

    private final LoadoutManager loadoutManager = new LoadoutManager();
    // The match is intended to be accessed from the game controller to give data to classes
    // that modifies it to play the game correctly.
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    @Getter
    private ChessMatch match;
    @Setter
    private ChessboardViewController chessboardViewController;
    @Getter
    @Setter
    private Loadout whiteLoadout = loadoutManager.load(STANDARD_LOADOUT_ID).get();
    @Getter
    @Setter
    // TODO: Will be replaced with the effective black loadout, (for now is mirrored because the standard is the same
    private Loadout blackLoadout = loadoutManager.load(STANDARD_LOADOUT_ID).get().mirrored();
    @Getter
    // The board factory is created here as is part of the current game, but it needs to be
    // accessed also from outside to create new pieces in other classes.
    @SuppressFBWarnings("EI_EXPOSE_REP")
    private final BoardFactory boardFactory = new BoardFactoryImpl(loaderController);

    private final GameCoordinator gameCoordinator;

    private Point2D lastPointClicked;
    private final Set<Point2D> lastPossibleMoves = new HashSet<>();
    private GameHistoryRecorder historyRecorder;

    /**
     * Constructs a new {@code GameControllerImpl}.
     *
     * @param gameCoordinator The {@link GameCoordinator} that manages the overall application lifecycle.
     */
    public GameControllerImpl(final GameCoordinator gameCoordinator) {
        this.gameCoordinator = gameCoordinator;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Delegates to the internal {@link MoveCache} to retrieve pre-calculated moves.
     * </p>
     */
    @Override
    public List<Point2D> getAvailableCells(final int pieceGameId) {
        return moveCache.getAvailableCells(pieceGameId);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Delegates to the internal {@link MoveCache} to store valid moves.
     * </p>
     */
    @Override
    public void cacheAvailableCells(final int pieceGameId, final List<Point2D> moves) {
        moveCache.cacheAvailableCells(pieceGameId, moves);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Clears the move cache. Should be called whenever the board state changes significantly.
     * </p>
     */
    @Override
    public void clearCache() {
        moveCache.clearCache();
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Processes a click event on the board.
     * It interacts with the {@link it.unibo.samplejavafx.mvc.model.handler.TurnHandler} to determine valid actions
     * (move selection or piece movement) and updates the {@link ChessboardViewController} to show/hide move highlights.
     * </p>
     */
    @Override
    public void handleClick(final Point2D pointClicked) {
        // TODO: implement using the cache

        if (this.match == null) {
            throw new IllegalStateException("Board should be initialized before using it");
        }

        // final Set<Point2D> moves = new HashSet<>(match.getBoard().getEntity(pointClicked).get()
        //     .asMoveable().get().getValidMoves(pointClicked, match.getBoard()));
        final Set<Point2D> result = new HashSet<>(match.getTurnHandler().thinking(pointClicked));

        // Added this check to silence the spot bugs error
        // The view is created and injected after the controller instantiation
        // so it needs to be injected by a setter later
        if (chessboardViewController != null) {
            if (result.isEmpty()) {
                chessboardViewController.hideMovementCells(lastPossibleMoves);
            } else if (result.size() == 1 && pointClicked.equals(lastPointClicked)) {
                chessboardViewController.hideMovementCells(lastPossibleMoves);
            } else {
                chessboardViewController.hideMovementCells(lastPossibleMoves);
                chessboardViewController.showMovementCells(result);
            }

            chessboardViewController.hideMovementCells(Set.of(pointClicked));
        }

        lastPointClicked = pointClicked;
        lastPossibleMoves.clear();
        lastPossibleMoves.addAll(result);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Helper method to construct asset paths for different colored variants of a piece.
     * </p>
     */
    @Override
    public String calculateImageColorPath(final String imagePath, final PlayerColor playerColor, final String id) {
        final String color = playerColor == PlayerColor.WHITE ? "white" : "black";
        return "file:" + imagePath + "/" + color + "_" + id + ".png";
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Delegates the surrender action to the match's turn handler.
     * </p>
     */
    @Override
    public void surrender() {
        if (this.match != null) {
            match.getTurnHandler().surrender();
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Handles the pawn promotion process.
     * Identifies the promotion position, removes the pawn, and uses the {@link BoardFactory} to place the new piece.
     * </p>
     */
    @Override
    public void promote(final LoadoutEntry pieceEntry) {
        final Point2D pos = match.getPromotionPos();
        match.getBoard().removeEntity(pos);
        boardFactory.createNewPiece(pos, match.getBoard(),
                (PieceDefinition) loaderController.getEntityCache().get(pieceEntry.packId()).get(pieceEntry.pieceId()),
                AdvancedRules.swapColor(match.getCurrentPlayer()));
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Delegates to the coordinator to switch the scene to the main game view.
     * </p>
     */
    @Override
    public void showGame() {
        this.gameCoordinator.showGame();
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Scans the board to find the King of the opponent of the current player.
     * </p>
     *
     * @deprecated This method is deprecated and should be replaced by internal state tracking in {@link AdvancedRules}.
     */
    @Deprecated
    @Override
    public Point2D getKingPos() {
        if (this.match == null) {
            throw new IllegalStateException("Match should be initialized before using it");
        }
        return this.match.getBoard().getPosByEntity(this.match.getBoard().getBoard().inverse().keySet().stream()
                .filter(e -> e.getType() == PieceType.KING)
                // Get the king of the opposite player
                .filter(e -> e.getPlayerColor() != this.match.getCurrentPlayer())
                .findFirst().get()); // Impossible to not have a king of the specified color
    }

    @Override
    public void setMatch(final ChessMatch match) {
        this.match = match;
        this.historyRecorder = new GameHistoryRecorder(match::getTurnNumber, match::getScoreManager);
        // Record initial state
        this.match.getBoard().getBoard().forEach((pos, entity) -> {
             this.historyRecorder.onEntityAdded(pos, entity);
        });
        this.match.getBoard().addObserver(this.historyRecorder);
    }

    @Override
    public GameHistory getGameHistory() {
        if (this.match == null) {
            return new GameHistory();
        }
        return this.match.getGameHistory();
    }

    @Override
    public ChessBoard getLiveBoard() {
        if (this.match == null) {
            throw new IllegalStateException("Match not initialized");
        }
        return this.match.getBoard();
    }
}
