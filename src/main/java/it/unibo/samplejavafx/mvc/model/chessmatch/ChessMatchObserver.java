package it.unibo.samplejavafx.mvc.model.chessmatch;

import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.handler.GameState;

/**
 * The {@code ChessMatchObserver} interface defines a contract for objects that wish to monitor
 * the progression and state changes of a chess match.
 *
 * <p>
 * This observer pattern implementation to react to core game events
 * like turn completion, player switching, and game-over conditions.
 * </p>
 */
public interface ChessMatchObserver {
    /**
     * Called when the turn number of the match is updated.
     *
     * @param turnNumber The new current turn number.
     */
    void onTurnUpdated(int turnNumber);

    /**
     * Called when the active player changes.
     *
     * <p>
     * This event signals that control has passed from one player to the other, indicating whose turn it is to move.
     * </p>
     *
     * @param playerColor The {@link PlayerColor} of the player who is now active.
     */
    void onPlayerUpdated(PlayerColor playerColor);

    /**
     * Called when the overall game state changes.
     *
     * <p>
     * This method is critical for detecting victory conditions (Checkmate), draws (Stalemate), or special states (Check).
     * </p>
     *
     * @param gameState   The new {@link GameState} of the match.
     * @param playerColor The {@link PlayerColor} associated with the state change (e.g., the player in check or the winner).
     */
    void onGameStateUpdated(GameState gameState, PlayerColor playerColor);

    /**
     * Called when the score of a player changes.
     *
     * @param player the player whose score changed.
     * @param newScore the new score of the player.
     */
    void onScoreChanged(PlayerColor player, int newScore);

    /**
     * Called when the timer updates.
     *
     * @param player        the player whose timer updated.
     * @param timeRemaining the time remaining for the player in seconds.
     */
    void onTimerUpdated(PlayerColor player, long timeRemaining);
}
