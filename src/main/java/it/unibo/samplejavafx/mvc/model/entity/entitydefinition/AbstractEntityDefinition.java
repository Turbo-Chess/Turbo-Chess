package it.unibo.samplejavafx.mvc.model.entity.entitydefinition;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Placeholder.
 */
@Getter
@EqualsAndHashCode
@ToString
@JsonDeserialize(builder = AbstractEntityDefinition.AbstractBuilder.class)
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
// The class is abstract because it mustn't be instantiated on its own, even if it hasn't abstract methods.
public abstract class AbstractEntityDefinition {
    private final String name;
    private final String id;
    private final String imagePath;
    private final PieceType pieceType;

    /**
     * Placeholder.
     *
     * @param builder Placeholder.
     * @param <T> Placeholder.
     */
    protected <T extends AbstractBuilder<T>> AbstractEntityDefinition(final AbstractBuilder<T> builder) {
        this.name = builder.name;
        this.id = builder.id;
        this.imagePath = builder.imagePath;
        this.pieceType = builder.pieceType;
    }

    /**
     * Placeholder.
     *
     * @param <X> Placeholder.
     */
    @JsonPOJOBuilder(withPrefix = "")
    public abstract static class AbstractBuilder<X extends AbstractBuilder<X>> {
        private String name;
        private String id;
        private String imagePath;
        private PieceType pieceType;

        /**
         * Placeholder.
         *
         * @param newName Placeholder.
         * @return Placeholder.
         */
        public X name(final String newName) {
           this.name = newName;
           return self();
        }

        /**
         * Placeholder.
         *
         * @param newId Placeholder.
         * @return Placeholder.
         */
        public X id(final String newId) {
            this.id = newId;
            return self();
        }

        /**
         * Placeholder.
         *
         * @param newImagePath Placeholder.
         * @return Placeholder.
         */
        public X imagePath(final String newImagePath) {
            if (newImagePath.startsWith("classpath:")) {
                this.imagePath = newImagePath.replace("classpath:", "");
            } else {
                this.imagePath = "file:" + newImagePath;
            }
            return self();
        }

        /**
         * Placeholder.
         *
         * @param newPieceType Placeholder.
         * @return Placeholder.
         */
        public X pieceType(final PieceType newPieceType) {
            this.pieceType = newPieceType;
            return self();
        }

        /**
         * Placeholder.
         *
         * @return Placeholder.
         */
        protected abstract X self();

        /**
         * Placeholder.
         *
         * @return Placeholder.
         */
        public abstract AbstractEntityDefinition build();
    }
}
