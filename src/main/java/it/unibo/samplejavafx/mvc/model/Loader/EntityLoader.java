package it.unibo.samplejavafx.mvc.model.Loader;

import it.unibo.samplejavafx.mvc.model.entity.Entity;

import java.nio.file.Path;
import java.util.List;

/**
 * The interface that models the loader class of the various entities.
 */
public interface EntityLoader {

    public List<Entity> loadEntityFile(final Path filesPath, final Class<? extends Entity> classToLoad);

    public void parseEntityFile(final Path path, final Class<? extends Entity> classToLoad);
}
