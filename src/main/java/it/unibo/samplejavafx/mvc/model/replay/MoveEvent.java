package it.unibo.samplejavafx.mvc.model.replay;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import it.unibo.samplejavafx.mvc.model.entity.Entity;

/**
 * Represents a piece movement.
 * Optimized to store only entity Name instead of full entity object.
 *
 * @param turn the turn number.
 * @param entityName the name of the entity being moved.
 * @param entityColor the color of the entity being moved.
 * @param from source position.
 * @param to destination position.
 * @param capturedEntity the entity being captured, if any.
 * @param promotedEntity the entity that the piece is promoted to, if any.
 * @param originalEntity the entity that was moved, if any (used for promotion revert).
 */
public record MoveEvent(
    @JsonProperty("turn") int turn, 
    @JsonProperty("entityName") String entityName, 
    @JsonProperty("entityColor") PlayerColor entityColor,
    @JsonProperty("from") Point2D from, 
    @JsonProperty("to") Point2D to,
    @JsonProperty("capturedEntity") Entity capturedEntity,
    @JsonProperty("promotedEntity") Entity promotedEntity,
    @JsonProperty("originalEntity") Entity originalEntity
) implements GameEvent {

    public MoveEvent(int turn, String entityName, PlayerColor entityColor, Point2D from, Point2D to, Entity capturedEntity) {
        this(turn, entityName, entityColor, from, to, capturedEntity, null, null);
    }

    @Override
    public int getTurn() {
        return turn;
    }

    @Override
    public String getEventDescription() {
        return String.format("Move | %s %s | %s->%s | Capture: %s | Promotion: %s", 
            entityColor,
            entityName, 
            from,
            to,
            capturedEntity != null ? capturedEntity.getName() : "None",
            promotedEntity != null ? promotedEntity.getName() : "None"
        );
    }
}
