package it.unibo.samplejavafx.mvc.model.replay;

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
@FunctionalInterface
public interface GameEvent {
    /**
     * @return the turn number when this event occurred.
     */
    int getTurn();
}
