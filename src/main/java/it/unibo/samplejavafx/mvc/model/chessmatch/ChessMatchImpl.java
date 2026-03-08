package it.unibo.samplejavafx.mvc.model.chessmatch;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.handler.GameState;
import it.unibo.samplejavafx.mvc.model.handler.TurnHandler;
import it.unibo.samplejavafx.mvc.model.handler.TurnHandlerImpl;
import it.unibo.samplejavafx.mvc.model.replay.GameHistory;
import it.unibo.samplejavafx.mvc.model.replay.GameHistoryRecorder;
import it.unibo.samplejavafx.mvc.model.replay.SpawnEvent;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code ChessMatchImpl} class is the concrete implementation of the {@link ChessMatch} interface.
 * <p>
 * It server as the primary source ot truth of the game, storing data such as the current turn, the current player
 * the current game state and a reference to all the components used to manage the advancement of the game.
 * </p>
 * <p>
 * This class is designed to be observable, allowing UI components or other systems to react to changes
 * in the game flow (e.g., turn updates, game over scenarios).
 * </p>
 */
@EqualsAndHashCode
@ToString
public final class ChessMatchImpl implements ChessMatch {
    @Getter
    private GameState gameState;
    @Getter
    private PlayerColor currentPlayer;
    @Getter
    private int turnNumber;
    @Getter
    @SuppressFBWarnings("EI_EXPOSE_REP")
    private final TurnHandler turnHandler;
    @Getter
    // The board needs to be modified by other methods during the game.
    @SuppressFBWarnings("EI_EXPOSE_REP")
    private final ChessBoard board;
    @Getter
    @SuppressFBWarnings("EI_EXPOSE_REP")
    private final GameHistory gameHistory;
    private final List<ChessMatchObserver> subscribers = new ArrayList<>();

    /**
     * Constructs a new standard chess match.
     * <p>
     * Initializes a new empty {@link ChessBoardImpl} by default.
     * </p>
     */
    // TODO: check removal of this never user constructor
    public ChessMatchImpl() {
        this(new ChessBoardImpl());
    }

    /**
     * Constructs a new chess match using a provided board instance.
     * <p>
     * This constructor is suitable for loading saved games or custom scenarios where the board
     * is pre-configured. It initializes the turn handler, history recorder, and sets the starting
     * player to WHITE.
     * </p>
     *
     * @param board The {@link ChessBoard} instance to be used for this match.
     */
    public ChessMatchImpl(final ChessBoard board) {
        this.gameState = GameState.NORMAL;
        this.board = board;
        this.turnNumber = 1;
        this.currentPlayer = PlayerColor.WHITE;

        final var historyRecorder = new GameHistoryRecorder(this::getTurnNumber);
        this.gameHistory = historyRecorder.getHistory();
        this.turnHandler = new TurnHandlerImpl(this);

        this.board.getBoard().forEach((pos, entity) -> {
            this.gameHistory.addEvent(new SpawnEvent(this.turnNumber, entity, pos));
        });

        this.board.addObserver(historyRecorder);
    }

    /**
     * placeholder.
     *
     * @param turnNumber placeholder.
     */
    @Override
    public void setTurnNumber(final int turnNumber) {
        this.turnNumber = turnNumber;
        this.turnHandler.setTurn(turnNumber);
        this.notifyTurnUpdated(this.turnNumber);
    }

    /**
     * placeholder.
     *
     * @param playerColor placeholder.
     */
    @Override
    public void setPlayerColor(final PlayerColor playerColor) {
        this.currentPlayer = playerColor;
        this.turnHandler.setPlayerColor(playerColor);
        this.notifyPlayerColorUpdated(this.currentPlayer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addObserver(final ChessMatchObserver observer) {
        subscribers.add(observer);
    }

    private void notifyTurnUpdated(final int turn) {
        subscribers.forEach(sub -> sub.onTurnUpdated(turn));
    }

    private void notifyPlayerColorUpdated(final PlayerColor playerColor) {
        subscribers.forEach(sub -> sub.onPlayerUpdated(playerColor));
    }

    private void notifyGameStateUpdated(final GameState state, final PlayerColor playerColor) {
        subscribers.forEach(sub -> sub.onGameStateUpdated(state, playerColor));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Updates the internal turn counter and broadcasts the change.
     * </p>
     */
    @Override
    public void updateTurn(final int turn) {
       this.turnNumber = turn;
       this.notifyTurnUpdated(this.turnNumber);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Updates the active player reference and broadcasts the change.
     * </p>
     */
    @Override
    public void updatePlayerColor(final PlayerColor currentColor) {
        this.currentPlayer = currentColor;
        this.notifyPlayerColorUpdated(this.currentPlayer);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Updates the match status (e.g., transition to CHECKMATE) and notifies observers.
     * </p>
     */
    @Override
    public void updateGameState(final GameState state, final PlayerColor playerColor) {
        this.gameState = state;
        this.notifyGameStateUpdated(this.gameState, this.currentPlayer);
    }

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    public Point2D getPromotionPos() {
        return turnHandler.getCurrentPiecePos();
    }
    // TODO: aggiungere per il timer

}
