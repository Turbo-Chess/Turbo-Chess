package it.unibo.samplejavafx.mvc.model.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Optional;

/**
 * Entity is the class that defines what can be on the board.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Piece.class, name = "piece"),
    @JsonSubTypes.Type(value = PowerUp.class, name = "powerUp")
})
public interface Entity {
    /**
     *  placeholder.
     *
     * @return placeholder.
     */
    PlayerColor getPlayerColor();

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    String getId();

    /**
     * Returns the name of the entity.
     *
     * @return the name.
     */
    String getName();

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    default Optional<Moveable> asMoveable() {
        return Optional.empty();
    }
}
