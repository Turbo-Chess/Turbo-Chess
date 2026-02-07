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
     * placeholder.
     *
     * @param filesPath placeholder.
     * @param classToLoad placeholder.
     * @return placeholder.
     */
    List<AbstractEntityDefinition> loadEntityFile(Path filesPath, Class<? extends AbstractEntityDefinition> classToLoad);
}
