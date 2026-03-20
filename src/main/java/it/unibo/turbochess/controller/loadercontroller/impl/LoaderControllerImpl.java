package it.unibo.turbochess.controller.loadercontroller.impl;

import it.unibo.turbochess.controller.loadercontroller.api.LoaderController;
import it.unibo.turbochess.model.loader.impl.DefinitionCacheEntry;
import it.unibo.turbochess.model.entity.definition.AbstractEntityDefinition;
import it.unibo.turbochess.model.loader.api.EntityLoader;
import it.unibo.turbochess.model.loader.impl.EntityLoaderImpl;
import it.unibo.turbochess.model.utils.LoadingUtils;
import it.unibo.turbochess.model.properties.GameProperties;
import it.unibo.turbochess.model.utils.FileSystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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
public final class LoaderControllerImpl implements LoaderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoaderControllerImpl.class);

    private static final List<String> PATHS = List.of(
            GameProperties.INTERNAL_ENTITIES_FOLDER.getPath(),
            GameProperties.EXTERNAL_MOD_FOLDER.getPath());

    private final List<DefinitionCacheEntry> entityDefinitionCache = new ArrayList<>();
    private final EntityLoader entityLoader = new EntityLoaderImpl();

    /**
     * Constructs a new {@code LoaderControllerImpl}.
     */
    public LoaderControllerImpl() {
        // Default constructor
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
        try {
            final Path assetsPath = LoadingUtils.getCorrectPath(GameProperties.EXTERNAL_ASSETS_FOLDER.getPath());
            FileSystemUtils.ensureDirectoryExists(assetsPath);
        } catch (final IOException | IllegalStateException e) {
            LOGGER.error("Cannot ensure Assets directory exists", e);
        }
        // Get a path from URI
        for (final String basePathString : PATHS) {
            final Path unifiedBasePath = LoadingUtils.getCorrectPath(basePathString);
            if (basePathString.startsWith("file:")) {
                try {
                    FileSystemUtils.ensureDirectoryExists(unifiedBasePath);
                } catch (final IOException e) {
                    LOGGER.error("Cannot ensure directory exists: " + unifiedBasePath);
                }
            }
            if (Files.isDirectory(unifiedBasePath)) {
                String resPackDirStr = "";
                try {
                    for (final var resPackDir : getDirs(unifiedBasePath)) {
                        resPackDirStr = resPackDir.toString();
                        loadResourcePack(unifiedBasePath, resPackDir);
                    }
                } catch (final IllegalStateException e) {
                    LOGGER.warn("Skipping loading from {}: {}", unifiedBasePath, e.getMessage());
                    throw new IllegalStateException("Error while reading: " + unifiedBasePath + resPackDirStr, e);
                }
            } else {
                LOGGER.warn("Skipping non-existent or inaccessible directory: {}", unifiedBasePath);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DefinitionCacheEntry> getEntityDefinitionCacheEntries() {
        return Collections.unmodifiableList(entityDefinitionCache);
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
        try {
            final List<AbstractEntityDefinition> loadedEntities =
                    entityLoader.loadEntityFile(resPackPath, AbstractEntityDefinition.class);
            loadIntoCache(loadedEntities, resPackDir.toString());
        } catch (final IllegalArgumentException | IllegalStateException e) {
            LOGGER.error("Failed to load resource pack: {}", resPackDir, e);
            throw new IllegalStateException("Fatal error in json configuration", e);
        }
    }

    private void loadIntoCache(final List<AbstractEntityDefinition> entitiesToLoad, final String packName) {
        for (final var entity : entitiesToLoad) {
            entityDefinitionCache.add(new DefinitionCacheEntry(packName, entity.getId(), entity));
        }
    }
}
