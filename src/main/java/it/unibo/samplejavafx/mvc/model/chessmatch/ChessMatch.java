package it.unibo.samplejavafx.mvc.model.chessmatch;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.handler.GameState;
import it.unibo.samplejavafx.mvc.model.handler.TurnHandler;

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

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    TurnHandler getTurnHandler();

    /**
     * placeholder.
     *
     * @param observer placeholder.
     */
    void addObserver(ChessMatchObserver observer);

    /**
     * placeholder.
     */
    void updateTurn();

    /**
     * placeholder.
     */
    void updatePlayerColor();
}
