package it.unibo.turbochess.model.replay.api;

import it.unibo.turbochess.model.loadout.impl.Loadout;

import java.util.List;

public interface GameHistory {
    void addEvent(GameEvent event);

    void removeLastEvent();

    GameEvent getLastEvent();

    List<GameEvent> getEvents();

    void setEvents(List<GameEvent> events);

    Loadout getWhiteLoadout();

    void setWhiteLoadout(Loadout whiteLoadout);

    Loadout getBlackLoadout();

    void setBlackLoadout(Loadout blackLoadout);

    long getWhiteTimeRemaining();

    void setWhiteTimeRemaining(long whiteTimeRemaining);

    long getBlackTimeRemaining();

    void setBlackTimeRemaining(long blackTimeRemaining);
}
