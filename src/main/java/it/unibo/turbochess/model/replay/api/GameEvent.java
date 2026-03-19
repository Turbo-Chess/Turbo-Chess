package it.unibo.turbochess.model.replay.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.unibo.turbochess.model.replay.impl.DespawnEvent;
import it.unibo.turbochess.model.replay.impl.MoveEvent;
import it.unibo.turbochess.model.replay.impl.SpawnEvent;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public interface GameEvent {
    /**
     * @return the turn number when this event occurred.
     */
    int getTurn();

    /**
     * @return the formatted event description string.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String getEventDescription();

    /**
     * @return the white player's score at the time of this event.
     */
    int getWhiteScore();

    /**
     * @return the black player's score at the time of this event.
     */
    int getBlackScore();
}
