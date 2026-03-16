package it.unibo.samplejavafx.mvc.controller.gamecontroller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import it.unibo.samplejavafx.mvc.controller.uicontroller.BoardView;
import it.unibo.samplejavafx.mvc.controller.uicontroller.ChessboardViewController;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.boardfactory.BoardFactory;
import it.unibo.samplejavafx.mvc.model.chessboard.boardfactory.PieceCreator;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutEntry;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutManager;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.replay.GameHistory;
import it.unibo.samplejavafx.mvc.model.replay.GameHistoryRecorder;
import it.unibo.samplejavafx.mvc.model.utils.RulesUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
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
    // Will the taken from the selected loadout
    private static final String STANDARD_LOADOUT_ID = "standard-chess-loadout";
    private final PieceCreator pieceCreator;
    @SuppressFBWarnings(
            value = "EI_EXPOSE_REP",
            justification = "The match is intended to be accessed from the game controller to give data "
                    + "to classes that modifies it to play the game correctly.")
    @Getter
    private ChessMatch match;
    @Setter
    private BoardView boardView;
    @Getter
    @Setter
    private Loadout whiteLoadout;
    @Getter
    @Setter
    private Loadout blackLoadout;

    private Point2D lastPointClicked;
    private final Set<Point2D> lastPossibleMoves = new HashSet<>();
    private GameHistoryRecorder historyRecorder;

    /**
     * Constructs a new {@code GameControllerImpl}.
     *
     * @param gameCoordinator The {@link GameCoordinator} that manages the overall application lifecycle.
     * @param pieceCreator the {@link PieceCreator} used to create new pieces.
     * @param loadoutManager the {@link LoadoutManager} used to load the standard loadout.
     */
    public GameControllerImpl(
            final GameCoordinator gameCoordinator,
            final PieceCreator pieceCreator,
            final LoadoutManager loadoutManager
    ) {
        this.pieceCreator = pieceCreator;
        this.whiteLoadout = loadoutManager.load(STANDARD_LOADOUT_ID).get();
        this.blackLoadout = loadoutManager.load(STANDARD_LOADOUT_ID).get().mirrored();
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
        if (this.match == null) {
            throw new IllegalStateException("Board should be initialized before using it");
        }

        // final Set<Point2D> moves = new HashSet<>(match.getBoard().getEntity(pointClicked).get()
        //     .asMoveable().get().getValidMoves(pointClicked, match.getBoard()));
        final Set<Point2D> result = new HashSet<>(match.getTurnHandler().thinking(pointClicked));

        // Added this check to silence the spot bugs error
        // The view is created and injected after the controller instantiation
        // so it needs to be injected by a setter later
        if (boardView != null) {
            if (result.isEmpty()) {
                boardView.hideMovementCells(lastPossibleMoves);
            } else if (result.size() == 1 && pointClicked.equals(lastPointClicked)) {
                boardView.hideMovementCells(lastPossibleMoves);
            } else {
                boardView.hideMovementCells(lastPossibleMoves);
                boardView.showMovementCells(result);
            }

            boardView.hideMovementCells(Set.of(pointClicked));
        }

        lastPointClicked = pointClicked;
        lastPossibleMoves.clear();
        lastPossibleMoves.addAll(result);
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
        if (match == null) {
            throw new IllegalStateException("Chess Match can't be null in this state");
        }
        final Point2D pos = match.getPromotionPos();
        match.getBoard().removeEntity(pos);
        pieceCreator.createNewPiece(pos, match.getBoard(),
                pieceEntry.packId(), pieceEntry.pieceId(),
                RulesUtils.swapColor(match.getCurrentPlayer()));
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Scans the board to find the King of the opponent of the current player.
     * </p>
     *
     */
    @Override
    public Point2D getKingPos(final PlayerColor color) {
        if (this.match == null) {
            throw new IllegalStateException("Match should be initialized before using it");
        }
        return this.match.getBoard().getPosByEntity(RulesUtils
            .getKing(this.match.getBoard(), RulesUtils.swapColor(color)).get());
    }

    @Override
    public void setMatch(final ChessMatch match) {
        if (this.match != null) {
            this.match.getGameTimer().shutdown();
        }
        this.match = match;

        this.historyRecorder = new GameHistoryRecorder(match::getTurnNumber, match::getScoreManager);
        // Record initial state
        this.match.getBoard().getBoard().forEach((pos, entity) -> {
            if (this.historyRecorder == null) {
                throw new IllegalStateException("Replay controller should be instantiated at this time");
            }
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
