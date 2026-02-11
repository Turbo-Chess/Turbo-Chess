package it.unibo.samplejavafx.mvc.model.chessmatch;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.handler.GameState;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class ChessMatchImpl implements ChessMatch {
    @Getter
    private GameState gameState;
    private final ChessBoard board;

    public ChessMatchImpl() {
        gameState = GameState.NORMAL;
        this.board = new ChessBoardImpl();
    }
}
