package it.unibo.samplejavafx.mvc.model.replay;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * Represents a piece disappearing from the board (e.g. via powerup).
 *
 * @param turn the turn number.
 * @param entityName the name of the entity that disappeared.
 * @param position the position where the piece disappeared.
 */
public record DespawnEvent(
    @JsonProperty("turn") int turn, 
    @JsonProperty("entityName") String entityName, 
    @JsonProperty("position") Point2D position
) implements GameEvent {
    @Override
    public int getTurn() {
        return turn;
    }

    @Override
    public String getEventDescription() {
        return String.format("Despawn | %s | %s", entityName, position);
    }
}
