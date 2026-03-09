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
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PieceDefinition.class, name = "PIECE"),
        @JsonSubTypes.Type(value = PowerUpDefinition.class, name = "POWERUP")
})
public abstract class AbstractEntityDefinition {
    private final String name;
    private final String id;
    private final String imagePath;
    private final PieceType pieceType;
    private final int weight;

    /**
     * Placeholder.
     *
     * @param builder Placeholder.
     * @param <T> Placeholder.
     */
    protected <T extends AbstractBuilder<T>> AbstractEntityDefinition(final AbstractBuilder<T> builder) {
        if (builder.getName() == null || builder.getName().isEmpty()) {
            throw new IllegalArgumentException("Missing required field: name");
        }

        if (builder.getId() == null || builder.getId().isEmpty()) {
            throw new IllegalArgumentException("Missing required field: id");
        }

        if (builder.getImagePath() == null || builder.getImagePath().isEmpty()) {
            throw new IllegalArgumentException("Missing required filed: imagePath");
        } else if (!builder.getImagePath().startsWith(GameProperties.INTERNAL_ASSETS_FOLDER.getPath())
                && !builder.getImagePath().startsWith(GameProperties.EXTERNAL_ASSETS_FOLDER.getPath())
                && !builder.getImagePath().startsWith("file:")
                && !builder.getImagePath().startsWith("classpath:")
                && !builder.getImagePath().contains("/assets/images/")) {
            throw new IllegalArgumentException("Path does not start with the correct base path: " + builder.getImagePath());
        }

        if (builder.getPieceType() == null) {
            throw new IllegalArgumentException("Missing required field: name");
        }

        this.name = builder.getName();
        this.id = builder.getId();
        this.imagePath = String.valueOf(LoadingUtils.getCorrectPath(builder.getImagePath()));
        this.pieceType = builder.getPieceType();
        this.weight = builder.getWeight();
    }

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    public abstract String getDescription();

    /**
     * Placeholder.
     *
     * @param <X> Placeholder.
     */
    @JsonPOJOBuilder(withPrefix = "")
    @Getter
    public abstract static class AbstractBuilder<X extends AbstractBuilder<X>> {
        private String name;
        private String id;
        private String imagePath;
        private PieceType pieceType;
        private int weight;

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
         * @param newWeight Placeholder.
         * @return Placeholder.
         */
        public X weight(final int newWeight) {
            this.weight = newWeight;
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
