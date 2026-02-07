package it.unibo.samplejavafx.mvc.model.entity.entitydefinition;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class PowerUpDefinition extends AbstractEntityDefinition {
    private final int duration;

    public PowerUpDefinition(final Builder builder) {
        super(builder);
        this.duration = builder.duration;
    }

    public static abstract class Builder extends AbstractEntityDefinition.Builder {
        private int duration;

        public Builder setDuration(final int duration) {
           this.duration = duration;
           return this;
        }

        public PowerUpDefinition build() {
            return new PowerUpDefinition(this);
        }
    }
}
