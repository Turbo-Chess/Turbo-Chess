package it.unibo.samplejavafx.mvc.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PowerUpDefinition;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents a PowerUp entity on the game board.
 * Unlike standard pieces, PowerUps are special entities that, when interacted with or activated,
 * apply specific effects to the game state. These effects can influence either the active player,
 * the opponent, or the board itself.
 * <p>
 * Each PowerUp has a defined duration, indicating how long its effect persists.
 * </p>
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@JsonDeserialize(builder = PowerUp.Builder.class)
public class PowerUp extends AbstractEntity<PowerUpDefinition> {
    private final int duration;

    /**
     * Constructs a new {@code PowerUp} instance using the provided builder configuration.
     * This constructor initializes the power-up with properties defined in the builder.
     *
     * @param builder The builder containing the initialization parameters including duration.
     */
    PowerUp(final Builder builder) {
        super(builder);
        this.duration = builder.duration;
    }

    /**
     * Triggers and applies the specific effect associated with this PowerUp on the game board.
     * <p>
     * Note: The implementation of specific effects is pending (Currently a placeholder).
     * Future implementations should utilize the duration and definition properties to alter game state.
     * </p>
     */
    public void applyEffect() {
         //TO DO: implement method
    }

    /**
     * A builder class for creating instances of {@link PowerUp}.
     * facilitating the construction of immutable PowerUp objects with specific durations.
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder extends AbstractEntity.AbstractBuilder<PowerUpDefinition, Builder> {
        private int duration;

        /**
         * Sets the duration for the PowerUp's effect.
         * The duration typically represents the number of turns the effect remains active.
         *
         * @param newDuration An integer representing the duration in turns.
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
        public Builder self() {
            return this;
        }

        /**
         * {@inheritDoc}
         * <p>
         * Creates a new immutable {@link PowerUp} object based on the current builder state.
         * </p>
         */
        @Override
        public PowerUp build() {
            return new PowerUp(this);
        }
    }
}
