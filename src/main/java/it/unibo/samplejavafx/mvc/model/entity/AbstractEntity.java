package it.unibo.samplejavafx.mvc.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.AbstractEntityDefinition;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An abstract implementation of the {@link Entity} interface, providing core functionality
 * for all game entities including pieces and power-ups.
 *
 * <p>
 * This class handles the association between an entity and its definition ({@link AbstractEntityDefinition}),
 * as well as managing common properties shared by all entities.
 * Subclasses should extend this to implement specific behaviors for different entity types.
 * </p>
 *
 * @param <T> The type of {@link AbstractEntityDefinition} that defines the structural rules
 *            and attributes for this specific entity class.
 */
@ToString
@EqualsAndHashCode
public abstract class AbstractEntity<T extends AbstractEntityDefinition> implements Entity {
    private final T entityDefinition;
    @Getter
    private final int gameId;
    @Getter
    private final PlayerColor playerColor;

    /**
     * Constructs a new {@link AbstractEntity} using the provided builder.
     * This constructor is protected to enforce object creation exclusively through builder pattern implementation.
     *
     * @param builder The builder containing the necessary initialization parameters.
     * @param <X>     The recursive generic type for the builder, ensuring proper method chaining.
     */
    protected <X extends AbstractEntity.AbstractBuilder<T, X>> AbstractEntity(final AbstractBuilder<T, X> builder) {
        this.entityDefinition = builder.entityDefinition;
        this.gameId = builder.gameId;
        this.playerColor = builder.playerColor;
    }

    /**
     * Retrieves the entity definition associated with this entity instance.
     * Only the reference is stored to not duplicate useless data across all instances.
     *
     * @return the underlying definition of type {@code T}.
     */
    @JsonProperty("entityDefinition")
    public T getEntityDefinition() {
        return this.entityDefinition;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Implementation delegates to the underlying {@link AbstractEntityDefinition}.
     * </p>
     */
    @Override
    @JsonIgnore
    public String getId() {
        return getEntityDefinition().getId();
    }


    /**
     * {@inheritDoc}
     *
     * <p>
     * Implementation delegates to the underlying {@link AbstractEntityDefinition}.
     * </p>
     */
    @Override
    @JsonIgnore
    public String getName() {
        return getEntityDefinition().getName();
    }


    /**
     * {@inheritDoc}
     *
     * <p>
     * Implementation delegates to the underlying {@link AbstractEntityDefinition}.
     * </p>
     */
    @Override
    @JsonIgnore
    public String getImagePath() {
        return this.getEntityDefinition().getImagePath();
    }


    /**
     * {@inheritDoc}
     *
     * <p>
     * Implementation delegates to the underlying {@link AbstractEntityDefinition}.
     * </p>
     */
    @Override
    @JsonIgnore
    public PieceType getType() {
        return getEntityDefinition().getPieceType();
    }

    /**
     * A generic builder for constructing {@link AbstractEntity} instances.
     * This class implements the Builder design pattern to facilitate the creation of
     * entity objects with multiple parameters.
     *
     * @param <T> The type of {@link AbstractEntityDefinition} the entity uses.
     * @param <X> The recursive type of the builder subclass itself.
     */
    @JsonPOJOBuilder(withPrefix = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public abstract static class AbstractBuilder<T extends AbstractEntityDefinition, X extends AbstractBuilder<T, X>> {
        private T entityDefinition;
        private int gameId;
        private PlayerColor playerColor;

        /**
         * Sets the {@link AbstractEntityDefinition} for the entity being built.
         * The definition provides the static configuration for the entity type.
         *
         * @param definition The definition to associate with the entity.
         * @return this builder instance for method chaining.
         */
        public X entityDefinition(final T definition) {
            this.entityDefinition = definition;
            return self();
        }

        /**
         * Sets the unique game identifier for the entity instance.
         * This ID is used to track specific pieces during a match.
         *
         * @param id The integer ID to assign.
         * @return this builder instance for method chaining.
         */
        public X gameId(final int id) {
            this.gameId = id;
            return self();
        }

        /**
         * Sets the player ownership for the entity.
         * This determines which player controls the entity.
         *
         * @param color The {@link PlayerColor} of the owning player (WHITE or BLACK).
         * @return this builder instance for method chaining.
         */
        public X playerColor(final PlayerColor color) {
            this.playerColor = color;
            return self();
        }

        /**
         * Returns the builder instance itself.
         * This abstract method allows subclasses to return their specific type ensuring type safety
         * in method chaining.
         *
         * @return the builder instance of type {@code X}.
         */
        public abstract X self();

        /**
         * Builds the final {@link AbstractEntity} instance.
         *
         * @return a new instance of the concrete entity class.
         */
        public abstract AbstractEntity<T> build();
    }
}
