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
 * Represents the definition for a game piece, extending the general {@link AbstractEntityDefinition}.
 *
 * <p>
 * This class encapsulates the specific rules and attributes that define a piece's behavior on the board.
 * Key properties include its weight (strategic value) and a collection of {@link MoveRules} that dictate
 * legal movement patterns.
 * </p>
 *
 * <p>
 * Instances of this class are immutable and intended to be defined once (e.g., loaded from JSON configuration)
 * and shared across multiple piece instances.
 * </p>
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@JsonDeserialize(builder = PieceDefinition.Builder.class)
public class PieceDefinition extends AbstractEntityDefinition {
    @JsonDeserialize(contentAs = MoveRulesImpl.class)
    private final List<MoveRules> moveRules;

    /**
     * Constructs a new {@code PieceDefinition} using the provided builder.
     *
     * <p>
     * This constructor validates that the piece has a positive weight and at least one defined move rule.
     * </p>
     *
     * @param builder The builder containing the initialization parameters.
     * @throws IllegalArgumentException if the weight is non-positive or no move rules are provided.
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
     *
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
         * Sets the list of movement rules for the piece.
         * These rules collectively define how the piece can move on the board.
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
         *
         * <p>
         * Creates a new immutable {@link PieceDefinition} instance based on the current builder state.
         * </p>
         */
        @Override
        public PieceDefinition build() {
            return new PieceDefinition(this);
        }
    }
}
