package it.unibo.samplejavafx.mvc.model.replay;

import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * Represents a piece disappearing from the board (e.g. via powerup).
 *
 * @param turn the turn number.
 * @param position the position where the piece disappeared.
 */
public record DespawnEvent(int turn, Point2D position) implements GameEvent {
    @Override
    public int getTurn() {
        return turn;
    }
}
