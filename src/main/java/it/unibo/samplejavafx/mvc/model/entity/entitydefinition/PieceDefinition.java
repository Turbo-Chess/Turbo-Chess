package it.unibo.samplejavafx.mvc.model.entity.entitydefinition;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import it.unibo.samplejavafx.mvc.model.movement.MoveRules;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Placeholder.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@JsonDeserialize(builder = PieceDefinition.Builder.class)
public class PieceDefinition extends AbstractEntityDefinition {
    private final int weight;
    @JsonDeserialize(contentAs = MoveRulesImpl.class)
    private final List<MoveRules> moveRules;

    /**
     * Placeholder.
     *
     * @param builder Placeholder.
     */
    @JsonCreator
   protected PieceDefinition(final Builder builder) {
        super(builder);

        if (builder.weight <= 0) {
           throw new IllegalArgumentException("Weight must be a positive non-0 number");
        }

        if (builder.moveRules == null) {
            throw new IllegalArgumentException("A piece must have at least one move rule");
        }

        this.weight = builder.weight;
        this.moveRules = List.copyOf(builder.moveRules);
    }

    /**
     * Placeholder.
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder extends AbstractEntityDefinition.AbstractBuilder<PieceDefinition.Builder> {
        private int weight;
        @JsonDeserialize(contentAs = MoveRulesImpl.class)
        private List<MoveRules> moveRules;

        /**
         * Placeholder.
         *
         * @param newWeight Placeholder.
         * @return Placeholder.
         */
        public Builder weight(final int newWeight) {
            this.weight = newWeight;
            return this;
        }

        /**
         * Placeholder.
         *
         * @param newMoveRules Placeholder.
         * @return Placeholder.
         */
        public Builder moveRules(final List<MoveRules> newMoveRules) {
            this.moveRules = List.copyOf(newMoveRules);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected PieceDefinition.Builder self() {
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public PieceDefinition build() {
            return new PieceDefinition(this);
        }
    }
}
