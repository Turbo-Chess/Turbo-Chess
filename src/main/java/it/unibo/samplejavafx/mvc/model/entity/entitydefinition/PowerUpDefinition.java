package it.unibo.samplejavafx.mvc.model.entity.entitydefinition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents the definition for a game power-up, separate from standard pieces.
 *
 * <p>
 * This class defines the attributes of a power-up, specifically its duration in game turns.
 * Power-up definitions are typically instantiated from configuration files and used to generate
 * power-up entities on the board.
 * </p>
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class PowerUpDefinition extends AbstractEntityDefinition {
    private final int duration;

    /**
     * Constructs a new {@code PowerUpDefinition} using the provided builder.
     *
     * <p>
     * Ensures that the duration is a positive or zero number to have a durability.
     * Zero means that the effect will terminate in that turn, while a positive number means that
     * the effect will last for other turns.
     * </p>
     *
     * @param builder The builder containing the initialization parameters.
     * @throws IllegalArgumentException if the duration is negative.
     */
    public PowerUpDefinition(final Builder builder) {
        super(builder);

        if (builder.duration < 0) {
            throw new IllegalArgumentException("Duration must be a positive non-0 value");
        }

        this.duration = builder.duration;
    }

    /**
     * A concrete builder for creating {@link PowerUpDefinition} instances.
     *
     * <p>
     * Allows for setting the duration before building the immutable definition object.
     * </p>
     */
    @JsonPOJOBuilder(withPrefix = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder extends AbstractEntityDefinition.AbstractBuilder<PowerUpDefinition.Builder> {
        private int duration;

        /**
         * Sets the duration (in turns) for the power-up effect.
         *
         * @param newDuration A non-negative integer representing the duration.
         * @return this builder instance for method chaining.
         */
        public Builder duration(final int newDuration) {
           this.duration = newDuration;
           return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected PowerUpDefinition.Builder self() {
            return this;
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * Creates a new immutable {@link PowerUpDefinition} instance.
         * </p>
         */
        @Override
        public PowerUpDefinition build() {
            return new PowerUpDefinition(this);
        }
    }
}
