package it.unibo.samplejavafx.mvc.model.replay;

import it.unibo.samplejavafx.mvc.model.loadout.Loadout;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Stores the history of events in a game for replay purposes.
 */
public class GameHistory {
    private final List<GameEvent> events = new LinkedList<>();
    @Getter
    @Setter
    private Loadout whiteLoadout;
    @Getter
    @Setter
    private Loadout blackLoadout;

    @Getter
    @Setter
    private long whiteTimeRemaining;
    @Getter
    @Setter
    private long blackTimeRemaining;

    /**
     * Adds an event to the history.
     *
     * @param event the event to add.
     */
    public void addEvent(final GameEvent event) {
        events.add(event);
    }

    /**
     * Removes the last event from the history if it exists.
     */
    public void removeLastEvent() {
        if (!events.isEmpty()) {
            events.remove(events.size() - 1);
        }
    }

    /**
     * @return the last event in the history or null if empty.
     */
    @JsonIgnore
    public GameEvent getLastEvent() {
        return events.isEmpty() ? null : events.get(events.size() - 1);
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
