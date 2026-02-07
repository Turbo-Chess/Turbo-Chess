package it.unibo.samplejavafx.mvc.model.replay;

import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * Represents a piece movement.
 *
 * @param turn the turn number.
 * @param entity the entity being moved.
 * @param from source position.
 * @param to destination position.
 */
public record MoveEvent(int turn, Entity entity, Point2D from, Point2D to) implements GameEvent {

    @Override
    public int getTurn() {
        return turn;
    }

    @Override
    public String getEventDescription() {
        return String.format("Move | %s | %s->%s", entity.getName(), from, to);
    }
}
