package it.unibo.samplejavafx.mvc.model.entity.entitydefinition;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class PowerUpDefinition extends AbstractEntityDefinition {
    private final int duration;

    @JsonCreator
    public PowerUpDefinition(
            @JsonProperty("name") final String name,
            @JsonProperty("id") final String id,
            @JsonProperty("imagePath") final String imagePath,
            @JsonProperty("pieceType") final PieceType pieceType,
            @JsonProperty("duration") final int duration
    ) {
        super(name, id, imagePath, pieceType);
        this.duration = duration;
    }
}
