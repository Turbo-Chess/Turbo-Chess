package it.unibo.samplejavafx.mvc.model.replay;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Represents a generic event in the game (move, spawn, despawn).
 */
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME, 
  include = JsonTypeInfo.As.PROPERTY, 
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = MoveEvent.class, name = "move"),
  @JsonSubTypes.Type(value = SpawnEvent.class, name = "spawn"),
  @JsonSubTypes.Type(value = DespawnEvent.class, name = "despawn")
})
public interface GameEvent {
    /**
     * @return the turn number when this event occurred.
     */
    int getTurn();

    /**
     * Generates a descriptive string for the event.
     * Format: "EventType | EntityName | Details"
     * Examples: "Move | Pawn | (0,0)->(0,1)", "Spawn | Knight | (4,4)", "Despawn | Rook | (5,5)"
     *
     * @return the formatted event description string.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String getEventDescription();
}
