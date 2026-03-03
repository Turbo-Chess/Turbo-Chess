package it.unibo.samplejavafx.mvc.model.chessmatch;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.handler.GameState;
import it.unibo.samplejavafx.mvc.model.handler.TurnHandler;
import it.unibo.samplejavafx.mvc.model.handler.TurnHandlerImpl;
import it.unibo.samplejavafx.mvc.model.replay.GameHistory;
import it.unibo.samplejavafx.mvc.model.replay.GameHistoryRecorder;
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
    private final TurnHandler turnHandler;
    @Getter
    private final ChessBoard board;
    @Getter
    private final GameHistory gameHistory;
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
        this.gameHistory = new GameHistory();
        // TODO: check turn number passed to turn handler
        this.turnHandler = new TurnHandlerImpl(this.turnNumber, this.board);

        final var historyRecorder = new GameHistoryRecorder(
            this::getTurnNumber,
            this.gameHistory
        );
        this.board.addObserver(historyRecorder);
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

    @Override
    public void updateTurn() {
       this.turnNumber++;
       this.notifyTurnUpdated(this.turnNumber);
    }

    @Override
    public void updatePlayerColor() {
        if (this.currentPlayer == PlayerColor.WHITE) {
            this.currentPlayer = PlayerColor.BLACK;
        } else {
            this.currentPlayer = PlayerColor.WHITE;
        }

        this.notifyPlayerColorUpdated(this.currentPlayer);
    }
    // TODO: aggiungere per il timer

}
