package it.unibo.samplejavafx.mvc.model.entity;

import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PowerUpDefinition;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * PowerUps are special entities that applies effect on the board that can affect both players.
 * Each power up has a duration.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class PowerUp extends AbstractEntity<PowerUpDefinition> {
    private final int duration;

    /**
     * Constructs a new power up with the builder configuration obtained in input.
     *
     * @param builder The builder to construct the object.
     */
    PowerUp(final Builder builder) {
        super(builder);
        this.duration = builder.duration;
    }

    /**
     * Applies the effect on the board.
     */
    public void applyEffect() {
         //TO DO: implement method
    }

    /**
     * placeholder.
     */
    public static class Builder extends AbstractEntity.AbstractBuilder<PowerUpDefinition, Builder> {
        private int duration;

        /**
         * placeholder.
         *
         * @param newDuration placeholder.
         * @return placeholder.
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
         */
        @Override
        public PowerUp build() {
            return new PowerUp(this);
        }
    }
}
