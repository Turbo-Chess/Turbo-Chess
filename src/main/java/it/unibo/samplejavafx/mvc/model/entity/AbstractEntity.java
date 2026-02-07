package it.unibo.samplejavafx.mvc.model.entity;

import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.AbstractEntityDefinition;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public abstract class AbstractEntity<X extends AbstractEntityDefinition> implements Entity {
    private final X entityDefinition;
    private final int gameId;
    private final PlayerColor playerColor;

    AbstractEntity(final X def, final int gameId, final PlayerColor playerColor) {
        this.entityDefinition = def;
        this.gameId = gameId;
        this.playerColor = playerColor;
    }

    // Package private field to not break encapsulation
    protected X getEntityDefinition() {
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
}
