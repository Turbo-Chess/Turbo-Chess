package it.unibo.samplejavafx.mvc.model.replay;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.unibo.samplejavafx.mvc.model.entity.AbstractEntity;
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
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    AbstractEntity entity, 
    Point2D position
) implements GameEvent {
    @Override
    public int getTurn() {
        return turn;
    }
}
