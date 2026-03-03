package it.unibo.samplejavafx.mvc.model.chessmatch;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.handler.GameState;
import it.unibo.samplejavafx.mvc.model.handler.TurnHandler;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

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
     *
     * @param turn placeholder.
     */
    void updateTurn(int turn);

    /**
     * placeholder.
     *
     * @param currentColor placeholder.
     */
    void updatePlayerColor(PlayerColor currentColor);

    /**
     * placeholder.
     *
     * @param state placeholder.
     * @param playerColor placeholder.
     */
    void updateGameState(GameState state, PlayerColor playerColor);

    /**
     * placeholder.
     * 
     * @return placeholder.
     */
    Point2D getPromotionPos();
}
