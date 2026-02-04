package it.unibo.samplejavafx.mvc.model.replay;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Stores the history of events in a game for replay purposes.
 */
public class GameHistory {
    private final List<GameEvent> events = new LinkedList<>();

    /**
     * Adds an event to the history.
     *
     * @param event the event to add.
     */
    public void addEvent(final GameEvent event) {
        events.add(event);
    }

    /**
     * @return an unmodifiable list of events.
     */
    public List<GameEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }

    /**
     * Setter for Jackson deserialization.
     *
     * @param events list of events.
     */
    public void setEvents(final List<GameEvent> events) {
        this.events.clear();
        this.events.addAll(events);
    }
}
