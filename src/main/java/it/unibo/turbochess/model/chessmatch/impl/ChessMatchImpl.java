package it.unibo.turbochess.model.chessmatch.impl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.turbochess.model.chessboard.board.api.ChessBoard;
import it.unibo.turbochess.model.chessboard.board.impl.ChessBoardImpl;
import it.unibo.turbochess.model.chessmatch.api.ChessMatch;
import it.unibo.turbochess.model.chessmatch.api.ChessMatchObserver;
import it.unibo.turbochess.model.entity.impl.PlayerColor;
import it.unibo.turbochess.model.handler.impl.GameState;
import it.unibo.turbochess.model.handler.api.TurnHandler;
import it.unibo.turbochess.model.handler.impl.TurnHandlerImpl;
import it.unibo.turbochess.model.replay.api.GameHistory;
import it.unibo.turbochess.model.replay.impl.GameHistoryRecorder;
import it.unibo.turbochess.model.replay.impl.SpawnEvent;
import it.unibo.turbochess.model.point2d.Point2D;
import it.unibo.turbochess.model.score.api.ScoreManager;
import it.unibo.turbochess.model.score.impl.ScoreManagerImpl;
import it.unibo.turbochess.model.timer.api.GameTimer;
import it.unibo.turbochess.model.timer.impl.GameTimerImpl;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code ChessMatchImpl} class is the concrete implementation of the {@link ChessMatch} interface.
 *
 * <p>
 * It serves as the primary source of truth for the game, storing data such as the current turn, the current player,
 * the current game state and references to the components used to manage the advancement of the match.
 * </p>
 *
 * <p>
 * This class is designed to be observable, allowing UI components or other systems to react to changes
 * in the game flow (e.g., turn updates, game over scenarios).
 * </p>
 */
@EqualsAndHashCode
@ToString
public final class ChessMatchImpl implements ChessMatch {
    private static final String SUPPRESS_STRING = "EI_EXPOSE_REP";
    private static final long DEFAULT_TIME_SECONDS = 600;
    @Getter
    private GameState gameState;
    @Getter
    private PlayerColor currentPlayer;
    @Getter
    private int turnNumber;
    @Getter
    @SuppressFBWarnings(value = SUPPRESS_STRING, justification = "Methods of turn handler are necessary outside")
    private final TurnHandler turnHandler;
    @Getter
    // The board needs to be modified by other methods during the game.
    @SuppressFBWarnings(value = SUPPRESS_STRING, justification = "The chessboard is meant to be accessed and modified outside")
    private final ChessBoard board = new ChessBoardImpl();
    @Getter
    @SuppressFBWarnings(value = SUPPRESS_STRING, justification = "The game history is meant to be accessed and read outside")
    private final GameHistory gameHistory;
    @Getter
    @SuppressFBWarnings(value = SUPPRESS_STRING, justification = "The score manager is meant to be accessed and read outside")
    private final ScoreManager scoreManager;
    @Getter
    @SuppressFBWarnings(value = SUPPRESS_STRING, justification = "The game timer is meant to be accessed and read outside")
    private final GameTimer gameTimer;
    private final List<ChessMatchObserver> subscribers = new ArrayList<>();

    /**
     * Constructs a new chess match using a provided board instance.
     *
     * <p>
     * This constructor is suitable for loading saved games or custom scenarios where the board
     * is pre-configured. It initializes the turn handler, history recorder, and sets the starting
     * player to WHITE.
     * </p>
     */
    public ChessMatchImpl() {
        this(DEFAULT_TIME_SECONDS, DEFAULT_TIME_SECONDS);
    }

    /**
     * Constructs a new chess match setting the initial time for each player.
     *
     * @param whiteTimeSeconds the initial time in seconds for white player.
     * @param blackTimeSeconds the initial time in seconds for black player.
     */
    public ChessMatchImpl(final long whiteTimeSeconds, final long blackTimeSeconds) {
        this.gameState = GameState.NORMAL;
        this.turnNumber = 1;
        this.currentPlayer = PlayerColor.WHITE;

        this.scoreManager = new ScoreManagerImpl();
        final var historyRecorder = new GameHistoryRecorder(this::getTurnNumber, () -> this.scoreManager);
        this.gameHistory = historyRecorder.getHistory();
        this.turnHandler = new TurnHandlerImpl(this);

        this.board.getBoard().forEach((pos, entity) -> {
            this.scoreManager.onEntityAdded(pos, entity);
            this.gameHistory.addEvent(new SpawnEvent(this.turnNumber, entity, pos,
                this.scoreManager.getScore(PlayerColor.WHITE),
                this.scoreManager.getScore(PlayerColor.BLACK)));
        });
        this.board.addObserver(historyRecorder);
        this.board.addObserver(scoreManager);
        this.scoreManager.addObserver(this::notifyScoreUpdated);
        this.gameTimer = new GameTimerImpl(
            whiteTimeSeconds,
            blackTimeSeconds,
            this::notifyTimerUpdated,
            this::onTimeOut
        );
    }

    /**
     * Sets the current turn number and synchronizes dependent components.
     *
     * <p>
     * This method also updates the {@link TurnHandler} starting turn and notifies observers.
     * </p>
     *
     * @param turnNumber the new turn number
     */
    @Override
    public void setTurnNumber(final int turnNumber) {
        this.turnNumber = turnNumber;
        this.turnHandler.setStartTurn(turnNumber);
        this.notifyTurnUpdated(this.turnNumber);
    }

    /**
     * Sets the current active player and synchronizes dependent components.
     *
     * <p>
     * This method also updates the {@link TurnHandler} starting player, updates the active player of the
     * {@link GameTimer} and notifies observers.
     * </p>
     *
     * @param playerColor the new active player
     */
    @Override
    public void setPlayerColor(final PlayerColor playerColor) {
        this.currentPlayer = playerColor;
        this.turnHandler.setStartPlayerColor(playerColor);
        this.gameTimer.setActivePlayer(this.currentPlayer);
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

    private void notifyScoreUpdated(final PlayerColor playerColor, final int score) {
        subscribers.forEach(sub -> sub.onScoreChanged(playerColor, score));
    }

    /**
     * {@inheritDoc}
     *
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
     *
     * <p>
     * Updates the active player reference and broadcasts the change.
     * </p>
     */
    @Override
    public void updatePlayerColor(final PlayerColor currentColor) {
        this.currentPlayer = currentColor;
        this.gameTimer.setActivePlayer(this.currentPlayer);
        this.notifyPlayerColorUpdated(this.currentPlayer);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Updates the match status (e.g., transition to CHECKMATE) and notifies observers.
     * </p>
     */
    @Override
    public void updateGameState(final GameState state, final PlayerColor playerColor) {
        if (this.gameState != state) {
            this.gameState = state;
            if (state == GameState.CHECKMATE || state == GameState.DRAW || state == GameState.TIMEOUT) {
                this.gameTimer.stop();
            }
            this.notifyGameStateUpdated(this.gameState, playerColor);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point2D getPromotionPos() {
        return turnHandler.getPromotingPawnPos();
    }

    @Override
    public int getScore(final PlayerColor player) {
        return this.scoreManager.getScore(player);
    }

    private void notifyTimerUpdated(final PlayerColor player, final Long timeRemaining) {
        subscribers.forEach(sub -> sub.onTimerUpdated(player, timeRemaining));
    }

    private void onTimeOut(final PlayerColor loser) {
        final PlayerColor winner = (loser == PlayerColor.WHITE) ? PlayerColor.BLACK : PlayerColor.WHITE;
        this.updateGameState(GameState.TIMEOUT, winner);
    }
}
