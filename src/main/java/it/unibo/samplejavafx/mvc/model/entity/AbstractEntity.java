package it.unibo.samplejavafx.mvc.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.AbstractEntityDefinition;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public abstract class AbstractEntity<T extends AbstractEntityDefinition> implements Entity {
    private final T entityDefinition;
    private final int gameId;
    private final PlayerColor playerColor;

    protected <X extends AbstractEntity.Builder<T, X>> AbstractEntity(final Builder<T, X> builder) {
        this.entityDefinition = builder.entityDefinition;
        this.gameId = builder.gameId;
        this.playerColor = builder.playerColor;
    }

    // Package private field to not break encapsulation
    protected T getEntityDefinition() {
        return this.entityDefinition;
    };

    @Override
    public String getId() {
        return getEntityDefinition().getId();
    }

    @Override
    public String getName() {
        return getEntityDefinition().getName();
    }

    @Override
    public PieceType getType() {
        return getEntityDefinition().getPieceType();
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static abstract class Builder<T extends AbstractEntityDefinition, X extends Builder<T, X>> {
        private T entityDefinition;
        private int gameId;
        private PlayerColor playerColor;

        public X setEntityDefinition(final T entityDefinition) {
            this.entityDefinition = entityDefinition;
            return self();
        }

        public X setGameId(final int gameId) {
            this.gameId = gameId;
            return self();
        }

        public X setPlayerColor(PlayerColor playerColor) {
            this.playerColor = playerColor;
            return self();
        }

        protected abstract X self();

        public abstract AbstractEntity<T> build();
    }
}