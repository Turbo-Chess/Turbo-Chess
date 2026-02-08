package it.unibo.samplejavafx.mvc.model.entity.entitydefinition;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Placeholder.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class PowerUpDefinition extends AbstractEntityDefinition {
    private final int duration;

    /**
     * Placeholder.
     *
     * @param builder Placeholder.
     */
    public PowerUpDefinition(final Builder builder) {
        super(builder);
        this.duration = builder.duration;
    }

    /**
     * Placeholder.
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder extends AbstractEntityDefinition.AbstractBuilder<PowerUpDefinition.Builder> {
        private int duration;

        /**
         * Placeholder.
         *
         * @param newDuration Placeholder.
         * @return Placeholder.
         */
        public Builder duration(final int newDuration) {
           this.duration = newDuration;
           return this;
        }

        /**
         * Placeholder.
         *
         * @return Placeholder.
         */
        @Override
        protected PowerUpDefinition.Builder self() {
            return this;
        }

        /**
         * Placeholder.
         *
         * @return Placeholder.
         */
        @Override
        public PowerUpDefinition build() {
            return new PowerUpDefinition(this);
        }
    }
}
