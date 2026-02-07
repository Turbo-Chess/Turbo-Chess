package it.unibo.samplejavafx.mvc.model.entity.entitydefinition;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.unibo.samplejavafx.mvc.model.movement.MoveRules;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@JsonDeserialize(builder = PieceDefinition.Builder.class)
public class PieceDefinition extends AbstractEntityDefinition {
    private final int weight;
    @JsonDeserialize(contentAs = MoveRulesImpl.class)
    private final List<MoveRules> moveRules;

    @JsonCreator
    public PieceDefinition(final Builder builder) {
        super(builder);
        this.weight = builder.weight;
        this.moveRules = List.copyOf(builder.moveRules);
    }

    public static class Builder extends AbstractEntityDefinition.Builder<PieceDefinition.Builder> {
        private int weight;
        private List<MoveRules> moveRules;

        @JsonProperty("weight")
        public Builder setWeight(final int weight) {
            this.weight = weight;
            return this;
        }

        public Builder setMoveRules(final List<MoveRules> moveRules) {
            this.moveRules = moveRules;
            return this;
        }

        @Override
        protected PieceDefinition.Builder self() {
            return this;
        }

        @Override
        public PieceDefinition build() {
            return new PieceDefinition(this);
        }
    }
}
