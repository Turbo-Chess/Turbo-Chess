package it.unibo.samplejavafx.mvc.model.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Optional;

/**
 * The {@code Entity} interface represents any distinct object that can exist on the game board.
 * This abstraction serves as the root for various game elements such as pieces, power-ups, or obstacles.
 * Implementing classes must define core characteristics including identification, visual representation,
 * and ownership information.
 *
 * <p>
 * This interface works in conjunction with the JSON serialization mechanism to allow polymorphic
 * handling of board entities.
 * </p>
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
     * Retrieves the unique identifier of the specific entity instance within the game context.
     * This ID is used to differentiate between instances that may share the same definition.
     *
     * @return a {@link String} representing the unique ID of the entity.
     */
    String getId();

    /**
     * Retrieves the display name of the entity.
     * This name is typically used for user interface elements or logging purposes to identify the type of entity.
     *
     * @return a {@link String} containing the name of the entity.
     */
    String getName();

    /**
     * Retrieves the file path or resource location for the image representing this entity.
     * The path should point to a folder when the images for the specific entity are stored.
     * The image for the specific color will be calculated at runtime.
     *
     * @return a {@link String} representing the image path.
     */
    String getImagePath();

    /**
     * Retrieves the categorical type of the entity.
     * The type classifies the entity into broader categories defined by {@link PieceType},
     * such as PAWN, KING, or POWERUP.
     *
     * @return the {@link PieceType} associated with this entity.
     */
    PieceType getType();

    /**
     * Retrieves the color of the player who owns this entity.
     * The ownership determines which player can control or interact with the entity.
     *
     * @return the {@link PlayerColor} indicating the owner of the entity.
     */
    PlayerColor getPlayerColor();

    /**
     * Attempts to cast this entity to a {@link Moveable} object.
     * In this way is possible to safely check if the entity possesses movement capabilities.
     * In that case some specific properties are present.
     * By default, entities are not moveable.
     *
     * @return an {@link Optional} containing this entity cast to {@link Moveable} if supported,
     *         or {@link Optional#empty()} otherwise.
     */
    default Optional<Moveable> asMoveable() {
        return Optional.empty();
    }
}
