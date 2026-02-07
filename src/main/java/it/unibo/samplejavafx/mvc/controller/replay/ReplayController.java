package it.unibo.samplejavafx.mvc.controller.replay;

import it.unibo.samplejavafx.mvc.model.replay.GameHistory;

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
     * @return true if advanced, false if at the end.
     */
    boolean next();

    /**
     * Reverts the replay by one step.
     *
     * @return true if reverted, false if at the start.
     */
    boolean prev();

    /**
     * Jumps to the start of the replay.
     */
    void jumpToStart();

    /**
     * Jumps to the end of the replay.
     */
    void jumpToEnd();

    /**
     * @return the current turn index (0-based index of the last applied event).
     */
    int getCurrentIndex();

    /**
     * @return the total number of events in the history.
     */
    int getTotalEvents();
}
