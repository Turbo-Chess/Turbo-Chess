package it.unibo.samplejavafx.mvc.model.entity.entitydefinition;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

        if (builder.getWeight() <= 0) {
           throw new IllegalArgumentException("Weight must be a positive non-0 number");
        }

        if (builder.moveRules == null) {
            throw new IllegalArgumentException("A piece must have at least one move rule");
        }

        this.moveRules = List.copyOf(builder.moveRules);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return String.format("A piece of type %s with value %d", 
            getPieceType(), 
            getWeight());
    }

    /**
     * A concrete builder for creating {@link PieceDefinition} instances.
     * <p>
     * Facilitates the construction of complex piece definitions with validated parameters.
     * </p>
     */
    @JsonPOJOBuilder(withPrefix = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder extends AbstractEntityDefinition.AbstractBuilder<PieceDefinition.Builder> {
        @JsonDeserialize(contentAs = MoveRulesImpl.class)
        private List<MoveRules> moveRules;

        /**
         * Placeholder.
         *
         * @param newMoveRules A list of {@link MoveRules} to assign.
         * @return this builder instance for method chaining.
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
