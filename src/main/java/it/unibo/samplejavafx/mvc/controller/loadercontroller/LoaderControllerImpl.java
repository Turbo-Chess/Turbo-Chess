package it.unibo.samplejavafx.mvc.controller.loadercontroller;

import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.AbstractEntityDefinition;
import it.unibo.samplejavafx.mvc.model.loader.EntityLoader;
import it.unibo.samplejavafx.mvc.model.loader.EntityLoaderImpl;
import it.unibo.samplejavafx.mvc.model.loader.LoadingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * placeholder.
 */
public class LoaderControllerImpl implements LoaderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoaderControllerImpl.class);

    private final List<String> entityResRootPath = new ArrayList<>();
    private final Map<String, Map<String, AbstractEntityDefinition>> entityCache = new HashMap<>();
    private final EntityLoader entityLoader = new EntityLoaderImpl();

    /**
     * placeholder.
     *
     * @param paths placeholder.
     */
    public LoaderControllerImpl(final List<String> paths) {
        entityResRootPath.addAll(paths);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void load() {
        // Get a path from URI
        for (final String basePathString : entityResRootPath) {
            final Path unifiedBasePath = LoadingUtils.getCorrectPath(basePathString);
            try {
                Files.createDirectories(unifiedBasePath);
            } catch (final IOException e) {
                LOGGER.error("Cannot create the directory " + unifiedBasePath);
                throw new IllegalStateException("Cannot create the directory " + unifiedBasePath, e);
            }
            getDirs(unifiedBasePath).forEach(resPackDir -> loadResourcePack(unifiedBasePath, resPackDir));
        }
    }

    private void loadResourcePack(final Path basePath, final Path resPackDir) {
        final Path resPackPath = basePath.resolve(resPackDir);
        entityCache.computeIfAbsent(resPackDir.toString(), map -> new HashMap<>());
        final List<AbstractEntityDefinition> loadedEntities =
                entityLoader.loadEntityFile(resPackPath, AbstractEntityDefinition.class);
        loadIntoCache(loadedEntities, resPackDir.toString());
   }

    private void loadIntoCache(final List<AbstractEntityDefinition> entitiesToLoad, final String packName) {
        for (final var entity : entitiesToLoad) {
            entityCache.get(packName).put(entity.getId(), entity);
        }
    }

    private List<Path> getDirs(final Path rootResDir) {
        final List<Path> res = new ArrayList<>();
        try (Stream<Path> paths = Files.list(rootResDir)) {
            res.addAll(paths.filter(Files::isDirectory).map(Path::getFileName).toList());
        } catch (final IOException e) {
            LOGGER.error("Cannot get directories: {}", rootResDir, e);
            throw new IllegalStateException("Cannot get directories: " + rootResDir, e);
        }

        return res;
    }

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    @Override
    public Map<String, Map<String, AbstractEntityDefinition>> getEntityCache() {
        return Collections.unmodifiableMap(entityCache);
    }

}
