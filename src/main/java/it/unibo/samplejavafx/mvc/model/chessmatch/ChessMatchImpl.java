package it.unibo.samplejavafx.mvc.model.chessmatch;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.handler.GameState;
import it.unibo.samplejavafx.mvc.model.handler.TurnHandler;
import it.unibo.samplejavafx.mvc.model.handler.TurnHandlerImpl;
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
    private GameState gameState; //NOPMD
    @Getter
    private PlayerColor currentPlayer;
    @Getter
    private int turnNumber;
    @Getter
    private final TurnHandler turnHandler;
    @Getter
    private final ChessBoard board;
    private final List<ChessMatchObserver> subscribers = new ArrayList<>();

    /**
     * placeholder.
     */
    public ChessMatchImpl(final ChessBoard board) {
        gameState = GameState.NORMAL;
        this.board = board;
        this.turnNumber = 1;
        this.currentPlayer = PlayerColor.WHITE;
        // TODO: chech turn number passed to turn handler
        this.turnHandler = new TurnHandlerImpl(this.turnNumber, this.board);
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
