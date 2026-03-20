package it.unibo.turbochess.model.replay.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.unibo.turbochess.model.loadout.impl.Loadout;
import it.unibo.turbochess.model.replay.api.GameEvent;
import it.unibo.turbochess.model.replay.api.GameHistory;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class GameHistoryImpl implements GameHistory {
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

    @Override
    public void addEvent(final GameEvent event) {
        events.add(event);
    }

    @Override
    public void removeLastEvent() {
        if (!events.isEmpty()) {
            events.remove(events.size() - 1);
        }
    }

    @Override
    @JsonIgnore
    public GameEvent getLastEvent() {
        return events.isEmpty() ? null : events.get(events.size() - 1);
    }

    @Override
    public List<GameEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }

    @Override
    public void setEvents(final List<GameEvent> events) {
        this.events.clear();
        this.events.addAll(events);
    }
}
