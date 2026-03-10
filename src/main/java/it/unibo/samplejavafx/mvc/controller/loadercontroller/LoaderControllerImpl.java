package it.unibo.samplejavafx.mvc.controller.loadercontroller;

import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.AbstractEntityDefinition;
import it.unibo.samplejavafx.mvc.model.loader.EntityLoader;
import it.unibo.samplejavafx.mvc.model.loader.EntityLoaderImpl;
import it.unibo.samplejavafx.mvc.model.loader.LoadingUtils;
import it.unibo.samplejavafx.mvc.model.properties.GameProperties;
import it.unibo.samplejavafx.mvc.model.utils.FileSystemUtils;
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
 * A concrete implementation of the {@link LoaderController} interface.
 *
 * <p>
 * This class handles the filesystem operations required to traverse resource directories,
 * utilizing {@link EntityLoader} to parse individual files. It maintains an in-memory cache
 * of all successfully loaded definitions for efficient runtime access.
 * </p>
 *
 * <p>
 * It is robust against missing directories or malformed files, logging errors rather than crashing the application.
 * </p>
 */
public class LoaderControllerImpl implements LoaderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoaderControllerImpl.class);
    // TODO: maybe move outside
    private static final List<String> PATHS = List.of(
            GameProperties.INTERNAL_ENTITIES_FOLDER.getPath(),
            GameProperties.EXTERNAL_MOD_FOLDER.getPath());

    private final Map<String, Map<String, AbstractEntityDefinition>> entityCache = new HashMap<>();
    private final EntityLoader entityLoader = new EntityLoaderImpl();

    /**
     * Constructs a new {@code LoaderControllerImpl}.
     */
    public LoaderControllerImpl() {
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Iterates over all configured root paths. It attempts to access each path as a directory
     * and load all subordinate resource packs into the cache.
     * </p>
     */
    @Override
    public void load() {
        // Get a path from URI
        for (final String basePathString : PATHS) {
            final Path unifiedBasePath = LoadingUtils.getCorrectPath(basePathString);
            try {
                FileSystemUtils.ensureDirectoryExists(unifiedBasePath);
            } catch (final IOException e) {
                LOGGER.error("Cannot ensure directory exists: " + unifiedBasePath);
                // Continue even if directory creation fails, it might be read-only or handled elsewhere
            }
            if (Files.isDirectory(unifiedBasePath)) {
                try {
                    getDirs(unifiedBasePath).forEach(resPackDir -> loadResourcePack(unifiedBasePath, resPackDir));
                } catch (final IllegalStateException e) {
                    LOGGER.warn("Skipping loading from {}: {}", unifiedBasePath, e.getMessage());
                }
            } else {
                LOGGER.warn("Skipping non-existent or inaccessible directory: {}", unifiedBasePath);
            }
        }
    }

    private List<Path> getDirs(final Path rootResDir) {
        final List<Path> res = new ArrayList<>();
        try (Stream<Path> paths = Files.list(rootResDir)) {
            res.addAll(paths.filter(Files::isDirectory).map(rootResDir::relativize).toList());
        } catch (final IOException e) {
            LOGGER.error("Cannot get directories: {}", rootResDir, e);
            throw new IllegalStateException("Cannot get directories: " + rootResDir, e);
        }

        return res;
    }

    private void loadResourcePack(final Path basePath, final Path resPackDir) {
        final Path resPackPath = basePath.resolve(resPackDir);
        entityCache.computeIfAbsent(resPackDir.toString(), map -> new HashMap<>());
        try {
            final List<AbstractEntityDefinition> loadedEntities =
                    entityLoader.loadEntityFile(resPackPath, AbstractEntityDefinition.class);
            loadIntoCache(loadedEntities, resPackDir.toString());
        } catch (final IllegalArgumentException | IllegalStateException e) {
            LOGGER.error("Failed to load resource pack: {}", resPackDir, e);
            throw new RuntimeException("Fatal error in json configuration", e);
        }
    }

    private void loadIntoCache(final List<AbstractEntityDefinition> entitiesToLoad, final String packName) {
        for (final var entity : entitiesToLoad) {
            entityCache.get(packName).put(entity.getId(), entity);
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Returns an unmodifiable view of the internal cache to prevent external modification.
     * </p>
     */
    @Override
    public Map<String, Map<String, AbstractEntityDefinition>> getEntityCache() {
        return Collections.unmodifiableMap(entityCache);
    }
}
