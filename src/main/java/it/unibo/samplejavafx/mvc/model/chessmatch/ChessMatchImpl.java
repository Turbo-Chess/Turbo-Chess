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

/**
 * placeholder.
 */
@EqualsAndHashCode
@ToString
public class ChessMatchImpl implements ChessMatch {
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

    /**
     * placeholder.
     */
    public ChessMatchImpl() {
        gameState = GameState.NORMAL;
        this.board = new ChessBoardImpl();
        this.turnNumber = 1;
        // TODO: chech turn number passed to turn handler
        this.turnHandler = new TurnHandlerImpl(this.turnNumber, this.board);
    }
}
