package it.unibo.samplejavafx.mvc.model.replay;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import it.unibo.samplejavafx.mvc.model.entity.Entity;

/**
 * Represents a piece movement.
 * Optimized to store only entity Name instead of full entity object.
 *
 * @param turn the turn number.
 * @param entityName the name of the entity being moved.
 * @param from source position.
 * @param to destination position.
 * @param capturedEntity the entity being captured, if any.
 */
public record MoveEvent(
    @JsonProperty("turn") int turn, 
    @JsonProperty("entityName") String entityName, 
    @JsonProperty("from") Point2D from, 
    @JsonProperty("to") Point2D to,
    @JsonProperty("capturedEntity") Entity capturedEntity
) implements GameEvent {

    @Override
    public int getTurn() {
        return turn;
    }

    @Override
    public String getEventDescription() {
        return String.format("Move | %s | %s->%s | Capture: %s", 
            entityName, from, to, capturedEntity != null ? capturedEntity.getName() : "None");
    }
}
