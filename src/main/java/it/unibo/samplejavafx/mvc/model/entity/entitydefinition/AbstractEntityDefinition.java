package it.unibo.samplejavafx.mvc.model.entity.entitydefinition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.loader.LoadingUtils;
import it.unibo.samplejavafx.mvc.model.properties.GameProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Defines the fundamental properties and structure for all entity types in the game.
 *
 * <p>
 * This abstract class serves as a blueprint for entity definitions, encapsulating shared attributes
 * such as the entity's unique identifier, display name, visual asset path, and its categorical type
 * (as defined by {@link PieceType}).
 * This allows to not duplicate this data across all instances of the same entity in the game.
 * </p>
 *
 * <p>
 * It utilizes the Builder pattern for object construction and includes JSON annotations
 * provided by the Jackson library to support polymorphic deserialization of different entity definition types.
 * </p>
 */
@Getter
@EqualsAndHashCode
@ToString
@JsonDeserialize(builder = AbstractEntityDefinition.AbstractBuilder.class)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PieceDefinition.class, name = "PIECE"),
        @JsonSubTypes.Type(value = PowerUpDefinition.class, name = "POWERUP")
})
public abstract class AbstractEntityDefinition {
    private final String name;
    private final String id;
    private final String imagePath;
    private final PieceType pieceType;
    private final int weight;

    /**
     * Constructs a new {@code AbstractEntityDefinition} from the provided builder.
     * This constructor performs validation on the required fields to ensure the definition is complete and valid
     * throwing an exception to ensure that all data is correctly loaded
     *
     * @param builder The builder instance containing the initialization parameters.
     * @param <T>     The specific type of the builder subclass.
     * @throws IllegalArgumentException if any required field (name, id, imagePath, pieceType) is missing or invalid.
     */
    protected <T extends AbstractBuilder<T>> AbstractEntityDefinition(final AbstractBuilder<T> builder) {
        if (builder.getName() == null || builder.getName().isEmpty()) {
            throw new IllegalArgumentException("Missing required field: name");
        }

        if (builder.getId() == null || builder.getId().isEmpty()) {
            throw new IllegalArgumentException("Missing required field: id");
        }

        if (builder.getImagePath() == null || builder.getImagePath().isEmpty()) {
            throw new IllegalArgumentException("Missing required filed: imagePath");
        } else if (!builder.getImagePath().startsWith(GameProperties.INTERNAL_ASSETS_FOLDER.getPath())
                && !builder.getImagePath().startsWith(GameProperties.EXTERNAL_ASSETS_FOLDER.getPath())
                && !builder.getImagePath().startsWith("file:")
                && !builder.getImagePath().startsWith("classpath:")
                && !builder.getImagePath().contains("/assets/images/")) {
            throw new IllegalArgumentException("Path does not start with the correct base path: " + builder.getImagePath());
        }

        if (builder.getPieceType() == null) {
            throw new IllegalArgumentException("Missing required field: name");
        }

        this.name = builder.getName();
        this.id = builder.getId();
        this.imagePath = String.valueOf(LoadingUtils.getCorrectPath(builder.getImagePath()));
        this.pieceType = builder.getPieceType();
        this.weight = builder.getWeight();
    }

    /**
     * Retrieves a text description of the entity.
     *
     * @return a String description.
     */
    public abstract String getDescription();

    /**
     * A generic abstract builder for constructing {@link AbstractEntityDefinition} instances.
     *
     * <p>
     * This class uses recursive generics to allow method chaining in subclasses and to maintain type safety.
     * </p>
     *
     * @param <X> The recursive type of the builder subclass.
     */
    @JsonPOJOBuilder(withPrefix = "")
    @Getter
    public abstract static class AbstractBuilder<X extends AbstractBuilder<X>> {
        private String name;
        private String id;
        private String imagePath;
        private PieceType pieceType;
        private int weight;

        /**
         * Sets the display name of the entity definition.
         *
         * @param newName The name to assign.
         * @return this builder concrete instance for method chaining.
         */
        public X name(final String newName) {
           this.name = newName;
           return self();
        }

        /**
         * Sets the unique identifier for the entity definition.
         *
         * @param newId The unique string ID.
         * @return this builder concrete instance for method chaining.
         */
        public X id(final String newId) {
            this.id = newId;
            return self();
        }

        /**
         * Sets the path to the image asset representing the entity.
         * The path must be valid and verifiable against the game's asset loading mechanism.
         *
         * @param newImagePath The string path to the image resource.
         * @return this builder concrete instance for method chaining.
         */
        public X imagePath(final String newImagePath) {
            this.imagePath = newImagePath;
            return self();
        }

        /**
         * Sets the categorical type of the entity (e.g., KING, PAWN, POWERUP).
         *
         * @param newPieceType The {@link PieceType} value.
         * @return this builder concrete instance for method chaining.
         */
        public X pieceType(final PieceType newPieceType) {
            this.pieceType = newPieceType;
            return self();
        }

        /**
         * Returns the builder instance itself.
         *
         * <p>
         * Designed to be implemented by subclasses to return {@code this}, ensuring the correct return type
         * for fluent method chaining (otherwise the abstract type would be returned and method chaining will break).
         * </p>
         *
         * @param newWeight Placeholder.
         * @return Placeholder.
         */
        public X weight(final int newWeight) {
            this.weight = newWeight;
            return self();
        }

        /**
         * Placeholder.
         *
         * @return the builder instance of type {@code X}.
         */
        protected abstract X self();

        /**
         * Builds the final {@link AbstractEntityDefinition} instance.
         *
         * <p>
         * Subclasses must implement this to return their specific concrete type.
         * </p>
         *
         * @return a new instance of an {@link AbstractEntityDefinition} concrete subclass.
         */
        public abstract AbstractEntityDefinition build();
    }
}
