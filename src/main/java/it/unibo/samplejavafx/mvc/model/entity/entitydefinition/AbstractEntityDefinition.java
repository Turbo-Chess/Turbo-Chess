package it.unibo.samplejavafx.mvc.model.entity.entitydefinition;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
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

    public <T extends Builder<T>> AbstractEntityDefinition (final Builder<T> builder) {
        this.name = builder.name;
        this.id = builder.id;
        this.imagePath = builder.imagePath;
        this.pieceType = builder.pieceType;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public abstract static class Builder<X extends Builder<X>> {
        private String name;
        private String id;
        private String imagePath;
        private PieceType pieceType;

        public X setName(final String name) {
           this.name = name;
           return self();
        }

        public X setId(final String id) {
            this.id = id;
            return self();
        }

        public X setImagePath(final String imagePath) {
            this.imagePath = imagePath;
            return self();
        }

        public X setPieceType(final PieceType pieceType) {
            this.pieceType = pieceType;
            return self();
        }

        protected abstract X self();

        public abstract AbstractEntityDefinition build();
    }
}
