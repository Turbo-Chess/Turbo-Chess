package it.unibo.samplejavafx.mvc.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.AbstractEntityDefinition;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Placeholder.
 *
 * @param <T> Placeholder.
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
     * Placeholder.
     *
     * @param builder Placeholder.
     * @param <X> Placeholder.
     */
    protected <X extends AbstractEntity.AbstractBuilder<T, X>> AbstractEntity(final AbstractBuilder<T, X> builder) {
        this.entityDefinition = builder.entityDefinition;
        this.gameId = builder.gameId;
        this.playerColor = builder.playerColor;
    }

    // Package private field to not break encapsulation
    /**
     * Placeholder.
     *
     * @return Placeholder.
     */
    @JsonProperty("entityDefinition")
    protected T getEntityDefinition() {
        return this.entityDefinition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return getEntityDefinition().getId();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return getEntityDefinition().getName();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getImagePath() {
        return this.getEntityDefinition().getImagePath();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PieceType getType() {
        return getEntityDefinition().getPieceType();
    }

    /**
     * Placeholder.
     *
     * @param <T> Placeholder.
     * @param <X> Placeholder.
     */
    @JsonPOJOBuilder(withPrefix = "")
    public abstract static class AbstractBuilder<T extends AbstractEntityDefinition, X extends AbstractBuilder<T, X>> {
        private T entityDefinition;
        private int gameId;
        private PlayerColor playerColor;

        /**
         * Placeholder.
         *
         * @param newEntityDefinition Placeholder.
         * @return Placeholder.
         */
        public X entityDefinition(final T newEntityDefinition) {
            this.entityDefinition = newEntityDefinition;
            return self();
        }

        /**
         * Placeholder.
         *
         * @param newGameId Placeholder.
         * @return Placeholder.
         */
        public X gameId(final int newGameId) {
            this.gameId = newGameId;
            return self();
        }

        /**
         * Placeholder.
         *
         * @param newPlayerColor Placeholder.
         * @return Placeholder.
         */
        public X playerColor(final PlayerColor newPlayerColor) {
            this.playerColor = newPlayerColor;
            return self();
        }

        /**
         * Placeholder.
         *
         * @return Placeholder.
         */
        protected abstract X self();

        /**
         * Placeholder.
         *
         * @return Placeholder.
         */
        public abstract AbstractEntity<T> build();
    }
}
