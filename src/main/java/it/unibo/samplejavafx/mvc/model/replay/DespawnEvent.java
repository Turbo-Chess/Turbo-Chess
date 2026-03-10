package it.unibo.samplejavafx.mvc.model.replay;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * Represents a piece disappearing from the board (e.g. via powerup).
 *
 * @param turn the turn number.
 * @param entity the entity that disappeared.
 * @param position the position where the piece disappeared.
 */
public record DespawnEvent(
    @JsonProperty("turn") int turn, 
    @JsonProperty("entity") Entity entity, 
    @JsonProperty("position") Point2D position,
    @JsonProperty("whiteScore") int whiteScore,
    @JsonProperty("blackScore") int blackScore
) implements GameEvent {
    @Override
    public int getTurn() {
        return turn;
    }

    @Override
    public int getWhiteScore() {
        return whiteScore;
    }

    @Override
    public int getBlackScore() {
        return blackScore;
    }

    @Override
    public String getEventDescription() {
        return String.format("Despawn | %s | %s", entity.getName(), position);
    }
}
