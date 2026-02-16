package it.unibo.samplejavafx.mvc.model.chessmatch;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.handler.GameState;

/**
 * placeholder.
 */
public interface ChessMatch {
    /**
     * placeholder.
     *
     * @return placeholder.
     */
    GameState getGameState();

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    PlayerColor getCurrentPlayer();

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    int getTurnNumber();

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    ChessBoard getBoard();
}
