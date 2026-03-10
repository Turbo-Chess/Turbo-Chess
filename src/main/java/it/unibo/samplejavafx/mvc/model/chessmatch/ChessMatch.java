package it.unibo.samplejavafx.mvc.model.chessmatch;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.handler.GameState;
import it.unibo.samplejavafx.mvc.model.handler.TurnHandler;
import it.unibo.samplejavafx.mvc.model.replay.GameHistory;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.score.ScoreManager;

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
     * Sets the current turn number.
     *
     * @param turnNumber the turn number to set.
     */
    void setTurnNumber(int turnNumber);

    /**
     * Sets the current player color.
     *
     * @param playerColor the player color to set.
     */
    void setPlayerColor(PlayerColor playerColor);

    /**
     * Gets the game history.
     *
     * @return the game history.
     */
    GameHistory getGameHistory();

    /**
     * Gets the score of a player.
     *
     * @param player the player to get the score of.
     * @return the score of the player.
     */
    int getScore(PlayerColor player);

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    Point2D getPromotionPos();
}
