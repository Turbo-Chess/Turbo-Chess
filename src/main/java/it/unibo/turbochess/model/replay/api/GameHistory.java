package it.unibo.turbochess.model.replay.api;

import it.unibo.turbochess.model.loadout.api.Loadout;

import java.util.List;

/**
 * Stores the history of events in a game for replay purposes.
 *
 * <p>
 * A {@code GameHistory} collects {@link GameEvent}s emitted during gameplay and also stores
 * additional metadata required to restore a match consistently (e.g., loadouts and timers).
 * </p>
 */
public interface GameHistory {
    /**
     * Adds an event to the history.
     *
     * @param event the event to add
     */
    void addEvent(GameEvent event);

    /**
     * Removes the last event from the history if it exists.
     */
    void removeLastEvent();

    /**
     * Returns the last event in the history.
     *
     * @return the last event in the history or {@code null} if the history is empty
     */
    GameEvent getLastEvent();

    /**
     * Returns the list of recorded events.
     *
     * @return an unmodifiable list of events
     */
    List<GameEvent> getEvents();

    /**
     * Replaces the current list of events.
     *
     * <p>
     * This method exists primarily to support Jackson deserialization.
     * </p>
     *
     * @param events the events to set
     */
    void setEvents(List<GameEvent> events);

    /**
     * Returns the loadout used by the white player.
     *
     * @return the white player's loadout
     */
    Loadout getWhiteLoadout();

    /**
     * Sets the loadout used by the white player.
     *
     * @param whiteLoadout the white player's loadout
     */
    void setWhiteLoadout(Loadout whiteLoadout);

    /**
     * Returns the loadout used by the black player.
     *
     * @return the black player's loadout
     */
    Loadout getBlackLoadout();

    /**
     * Sets the loadout used by the black player.
     *
     * @param blackLoadout the black player's loadout
     */
    void setBlackLoadout(Loadout blackLoadout);

    /**
     * Returns the time remaining for the white player (in seconds).
     *
     * @return the remaining time for white player
     */
    long getWhiteTimeRemaining();

    /**
     * Sets the time remaining for the white player (in seconds).
     *
     * @param whiteTimeRemaining the remaining time for white player
     */
    void setWhiteTimeRemaining(long whiteTimeRemaining);

    /**
     * Returns the time remaining for the black player (in seconds).
     *
     * @return the remaining time for black player
     */
    long getBlackTimeRemaining();

    /**
     * Sets the time remaining for the black player (in seconds).
     *
     * @param blackTimeRemaining the remaining time for black player
     */
    void setBlackTimeRemaining(long blackTimeRemaining);
}
