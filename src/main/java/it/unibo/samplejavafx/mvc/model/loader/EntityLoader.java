package it.unibo.samplejavafx.mvc.model.loader;

import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.AbstractEntityDefinition;

import java.nio.file.Path;
import java.util.List;

/**
 * The interface that models the loader class of the various entities.
 */
@FunctionalInterface
public interface EntityLoader {
    /**
     * Loads all JSON files found under {@code filesPath} and deserializes them into entity definitions.
     *
     * @param filesPath the base directory to traverse looking for JSON definition files
     * @param classToLoad the concrete definition class used for deserialization
     * @return a list of loaded {@link AbstractEntityDefinition} instances
     * @throws IllegalStateException if directory traversal or JSON parsing fails
     */
    List<AbstractEntityDefinition> loadEntityFile(Path filesPath, Class<? extends AbstractEntityDefinition> classToLoad);
}
