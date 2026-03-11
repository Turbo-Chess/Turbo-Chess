package it.unibo.samplejavafx.mvc.controller.replay;

import it.unibo.samplejavafx.mvc.model.replay.GameEvent;
import it.unibo.samplejavafx.mvc.model.replay.GameHistory;

import java.util.Optional;

/**
 * Controller for managing game replay playback.
 */
public interface ReplayController {

    /**
     * Loads a game history into the controller.
     * Resets the playback to the beginning.
     *
     * @param history the game history to load.
     */
    void loadHistory(GameHistory history);

    /**
     * Advances the replay by one step.
     *
     * @return an Optional containing the event that was applied, or empty if at the end.
     */
    Optional<GameEvent> next();

    /**
     * Reverts the replay by one step.
     *
     * @return an Optional containing the event that was reverted, or empty if at the start.
     */
    Optional<GameEvent> prev();

    /**
     * Jumps to the start of the replay.
     */
    void jumpToStart();

    /**
     * Jumps to the end of the replay.
     */
    void jumpToEnd();

    /**
     * @return the current turn index (0-based index of the next event to apply).
     */
    int getCurrentIndex();

    /**
     * @return the total number of events in the history.
     */
    int getTotalEvents();

    /**
     * Sets the minimum index for replay navigation.
     * Prevents going back before this index.
     *
     * @param minIndex the minimum index (must be >= 0).
     */
    void setMinIndex(int minIndex);
}
