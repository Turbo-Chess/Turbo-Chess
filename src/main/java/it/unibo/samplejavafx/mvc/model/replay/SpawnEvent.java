package it.unibo.samplejavafx.mvc.model.replay;

import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * Represents an entity appearing on the board.
 *
 * @param turn the turn number.
 * @param entity the entity that spawned.
 * @param position the position where it spawned.
 */
public record SpawnEvent(
    int turn, 
    Entity entity, 
    Point2D position
) implements GameEvent {
    @Override
    public int getTurn() {
        return turn;
    }

    @Override
    public String getEventDescription() {
        return String.format("Spawn | %s | %s", entity.getName(), position);
    }
}
