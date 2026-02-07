package it.unibo.samplejavafx.mvc.model.entity.entitydefinition;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@JsonDeserialize(builder = AbstractEntityDefinition.Builder.class)
public class AbstractEntityDefinition {
    private final String name;
    private final String id;
    private final String imagePath;
    private final PieceType pieceType;

    public AbstractEntityDefinition (final Builder builder) {
        this.name = builder.name;
        this.id = builder.id;
        this.imagePath = builder.imagePath;
        this.pieceType = builder.pieceType;
    }

    @JsonPOJOBuilder
    public abstract static class Builder {
        private String name;
        private String id;
        private String imagePath;
        private PieceType pieceType;

        public Builder setName(final String name) {
           this.name = name;
           return this;
        }

        public Builder setId(final String id) {
            this.id = id;
            return this;
        }

        public Builder setImagePath(final String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public Builder setPieceType(final PieceType pieceType) {
            this.pieceType = pieceType;
            return this;
        }

        public abstract AbstractEntityDefinition build();
    }
}
