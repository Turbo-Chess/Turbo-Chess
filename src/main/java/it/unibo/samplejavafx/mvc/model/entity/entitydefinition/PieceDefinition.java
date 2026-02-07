package it.unibo.samplejavafx.mvc.model.entity.entitydefinition;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.movement.MoveRules;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class PieceDefinition extends AbstractEntityDefinition {
    private final int weight;
    @JsonDeserialize(contentAs = MoveRulesImpl.class)
    private final List<MoveRules> moveRules;

    @JsonCreator
    public PieceDefinition(
            @JsonProperty("name") final String name,
            @JsonProperty("id") final String id,
            @JsonProperty("imagePath") final String imagePath,
            @JsonProperty("weight") final int weight,
            @JsonProperty("pieceType") final PieceType pieceType,
            @JsonProperty("moveRules") final List<MoveRules> moveRules
            ) {
        super(name, id, imagePath, pieceType);
        this.weight = weight;
        this.moveRules = List.copyOf(moveRules);
    }
}
