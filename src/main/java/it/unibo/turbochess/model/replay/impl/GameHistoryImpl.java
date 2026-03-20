package it.unibo.turbochess.model.replay.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.unibo.turbochess.model.loadout.api.Loadout;
import it.unibo.turbochess.model.loadout.impl.LoadoutImpl;
import it.unibo.turbochess.model.replay.api.GameEvent;
import it.unibo.turbochess.model.replay.api.GameHistory;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Default {@link GameHistory} implementation.
 *
 * <p>
 * Stores the ordered list of {@link GameEvent}s produced during a match and the metadata required to restore it
 * consistently (player loadouts and remaining time).
 * </p>
 */
public final class GameHistoryImpl implements GameHistory {
    private final List<GameEvent> events = new LinkedList<>();

    @Getter
    @Setter
    @JsonDeserialize(as = LoadoutImpl.class)
    private Loadout whiteLoadout;

    @Getter
    @Setter
    @JsonDeserialize(as = LoadoutImpl.class)
    private Loadout blackLoadout;

    @Getter
    @Setter
    private long whiteTimeRemaining;

    @Getter
    @Setter
    private long blackTimeRemaining;

    /** {@inheritDoc} */
    @Override
    public void addEvent(final GameEvent event) {
        events.add(event);
    }

    /** {@inheritDoc} */
    @Override
    public void removeLastEvent() {
        if (!events.isEmpty()) {
            events.remove(events.size() - 1);
        }
    }

    /** {@inheritDoc} */
    @Override
    @JsonIgnore
    public GameEvent getLastEvent() {
        return events.isEmpty() ? null : events.get(events.size() - 1);
    }

    /** {@inheritDoc} */
    @Override
    public List<GameEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }

    /** {@inheritDoc} */
    @Override
    public void setEvents(final List<GameEvent> events) {
        this.events.clear();
        this.events.addAll(events);
    }
}
