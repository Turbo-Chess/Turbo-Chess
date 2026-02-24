package it.unibo.samplejavafx.mvc.model.entity.entitydefinition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.loader.LoadingUtils;
import it.unibo.samplejavafx.mvc.model.properties.GameProperties;
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
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PieceDefinition.class, name = "PIECE"),
        @JsonSubTypes.Type(value = PowerUpDefinition.class, name = "POWERUP")
})
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
        if (builder.name == null || builder.name.isEmpty()) {
            throw new IllegalArgumentException("Missing required field: name");
        }

        if (builder.id == null || builder.id.isEmpty()) {
            throw new IllegalArgumentException("Missing required field: id");
        }

        if (builder.imagePath == null || builder.imagePath.isEmpty()) {
            throw new IllegalArgumentException("Missing required filed: imagePath");
        } else if (!builder.imagePath.startsWith(GameProperties.INTERNAL_ASSETS_FOLDER.getPath()) && !builder.imagePath.startsWith(GameProperties.EXTERNAL_ASSETS_FOLDER.getPath())) {
            throw new IllegalArgumentException("Path does not start with the correct base path");
        }

        if (builder.pieceType == null) {
            throw new IllegalArgumentException("Missing required field: name");
        }

        this.name = builder.name;
        this.id = builder.id;
        this.imagePath = LoadingUtils.getCorrectPath(builder.imagePath).toString();
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
            if (!newImagePath.startsWith("classpath:")) {
                this.imagePath = "file:" + newImagePath;
            } else {
                this.imagePath = newImagePath;
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
