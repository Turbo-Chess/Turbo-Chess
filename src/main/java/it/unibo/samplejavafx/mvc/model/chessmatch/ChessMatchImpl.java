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
import it.unibo.samplejavafx.mvc.model.score.ScoreManager;
import it.unibo.samplejavafx.mvc.model.score.ScoreManagerImpl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * placeholder.
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
    private final ScoreManager scoreManager;
    private final List<ChessMatchObserver> subscribers = new ArrayList<>();

    /**
     * placeholder.
     */
    public ChessMatchImpl() {
        this(new ChessBoardImpl());
    }

    /**
     * placeholder.
     *
     * @param board placeholder.
     */
    public ChessMatchImpl(final ChessBoard board) {
        this.gameState = GameState.NORMAL;
        this.board = board;
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
    }

    @Override
    public void setTurnNumber(final int turnNumber) {
        this.turnNumber = turnNumber;
        this.turnHandler.setTurn(turnNumber);
        this.notifyTurnUpdated(this.turnNumber);
    }

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

    private void notifyScoreUpdated(final PlayerColor playerColor, final int score) {
        subscribers.forEach(sub -> sub.onScoreChanged(playerColor, score));
    }

    @Override
    public void updateTurn(final int turn) {
       this.turnNumber = turn;
       this.notifyTurnUpdated(this.turnNumber);
    }

    @Override
    public void updatePlayerColor(final PlayerColor currentColor) {
        this.currentPlayer = currentColor;
        this.notifyPlayerColorUpdated(this.currentPlayer);
    }

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
    @Override
    public Point2D getPromotionPos() {
        return turnHandler.getCurrentPiecePos();
    }

    @Override
    public int getScore(final PlayerColor player) {
        return this.scoreManager.getScore(player);
    }

}
