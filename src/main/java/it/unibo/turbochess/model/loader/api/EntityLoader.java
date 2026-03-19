package it.unibo.turbochess.model.loader.api;

import it.unibo.turbochess.model.entity.definition.AbstractEntityDefinition;

import java.nio.file.Path;
import java.util.List;

/**
 * The interface that models the loader class' contract for the entity definitions.
 */
@FunctionalInterface
public interface EntityLoader {
    /**
     * Loads entity definitions from a specified file path.
     *
     * <p>
     * This method reads and parses configuration files (e.g., JSON) to create runtime instances
     * of {@link AbstractEntityDefinition}. It supports polymorphic loading based on the
     * provided class type.
     * </p>
     *
     * @param filesPath   The {@link Path} to the directory or file containing entity definitions.
     * @param classToLoad The concrete subclass of {@link AbstractEntityDefinition} to instantiate.
     * @return a {@link List} containing the loaded entity definitions.
     */
    List<AbstractEntityDefinition> loadEntityFile(Path filesPath, Class<? extends AbstractEntityDefinition> classToLoad);
}
