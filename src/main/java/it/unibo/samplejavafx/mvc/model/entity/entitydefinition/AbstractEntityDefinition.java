package it.unibo.samplejavafx.mvc.model.entity.entitydefinition;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class AbstractEntityDefinition {
    private final String name;
    private final String id;
    private final String imagePath;
    private final PieceType pieceType;

    @JsonCreator
    public AbstractEntityDefinition (
            @JsonProperty("name") final String name,
            @JsonProperty("id") final String id,
            @JsonProperty("imagePath") final String imagePath,
            @JsonProperty("pieceType") final PieceType pieceType
    ) {
        this.name = name;
        this.id = id;
        this.imagePath = imagePath;
        this.pieceType = pieceType;
    }
}
