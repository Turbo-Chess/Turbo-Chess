package it.unibo.samplejavafx.mvc.model.replay;

import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * Represents a piece disappearing from the board (e.g. via powerup).
 *
 * @param turn the turn number.
 * @param entity the entity that disappeared.
 * @param position the position where the piece disappeared.
 */
public record DespawnEvent(int turn, Entity entity, Point2D position) implements GameEvent {
    @Override
    public int getTurn() {
        return turn;
    }
}
