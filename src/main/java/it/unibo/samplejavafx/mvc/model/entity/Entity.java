package it.unibo.samplejavafx.mvc.model.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Optional;

/**
 * Entity is the class that defines what can be on the board.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "entityType"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Piece.class, name = "piece")
})
public interface Entity {
    /**
     * placeholder.
     *
     * @return placeholder.
     */
    String getId();

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    String getName();

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    String getImagePath();

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    PieceType getType();

    /**
     *  placeholder.
     *
     * @return placeholder.
     */
    PlayerColor getPlayerColor();

    /**
     * Gets the weight of the entity for scoring.
     *
     * @return the weight.
     */
    int getWeight();

    /**
     * @return placeholder.
     */
    default Optional<Moveable> asMoveable() {
        return Optional.empty();
    }
}
