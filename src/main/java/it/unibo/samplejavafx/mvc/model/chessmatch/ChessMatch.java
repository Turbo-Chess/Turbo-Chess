package it.unibo.samplejavafx.mvc.model.chessmatch;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.handler.GameState;
import it.unibo.samplejavafx.mvc.model.handler.TurnHandler;
import it.unibo.samplejavafx.mvc.model.replay.GameHistory;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.score.ScoreManager;
import it.unibo.samplejavafx.mvc.model.timer.GameTimer;

/**
 * The {@code ChessMatch} interface represents the core session of a chess game.
 *
 * <p>
 * It acts as the central source of truth for the game state, maintaining the flow of turns, player progression,
 * and the overall status of the match (e.g., normal, checkmate, draw).
 * It aggregates key components such as the {@link ChessBoard}, {@link TurnHandler}, and {@link GameHistory}.
 * </p>
 */
public interface ChessMatch {
    /**
     * Retrieves the current state of the game (e.g., NORMAL, CHECK, CHECKMATE).
     *
     * @return the current {@link GameState}.
     */
    GameState getGameState();

    /**
     * Identifies the player whose turn it currently is.
     *
     * @return the {@link PlayerColor} of the active player.
     */
    PlayerColor getCurrentPlayer();

    /**
     * Retrieves the current turn number of the match.
     * The turn number typically increments after each player have completed a move.
     *
     * @return an integer representing the turn number.
     */
    int getTurnNumber();

    /**
     * Accesses the game board associated with this match.
     *
     * @return the {@link ChessBoard} instance where the game is being played.
     */
    ChessBoard getBoard();

    /**
     * Accesses the handler responsible for managing turn logic and rule enforcement.
     *
     * @return the {@link TurnHandler} for this match.
     */
    TurnHandler getTurnHandler();

    /**
     * Accesses the score manager for this match.
     *
     * @return the {@link ScoreManager} instance tracking the game score.
     */
    ScoreManager getScoreManager();

    /**
     * Registers an observer to receive outcomes and state updates from the match.
     *
     * @param observer The {@link ChessMatchObserver} to subscribe.
     */
    void addObserver(ChessMatchObserver observer);

    /**
     * Updates the turn counter to a specific value and notifies observers.
     *
     * @param turn The new turn number to set.
     */
    void updateTurn(int turn);

    /**
     * Updates the active player color and notifies observers of the change.
     *
     * @param currentColor The {@link PlayerColor} of the player who is now active.
     */
    void updatePlayerColor(PlayerColor currentColor);

    /**
     * Updates the overall game state (e.g., declaring checkmate) and notifies observers.
     *
     * @param state       The new {@link GameState}.
     * @param playerColor The {@link PlayerColor} relevant to the state change (e.g., the winner).
     */
    void updateGameState(GameState state, PlayerColor playerColor);

    /**
     * placeholder.
     *
     * @param turnNumber placeholder.
     */
    void setTurnNumber(int turnNumber);

    /**
     * placeholder.
     *
     * @param playerColor placeholder.
     */
    void setPlayerColor(PlayerColor playerColor);

    /**
     * placeholder.
     *
     * @return placeholder.
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
     * Accesses the game timer.
     *
     * @return the {@link GameTimer} instance.
     */
    GameTimer getGameTimer();

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    Point2D getPromotionPos();
}
