package it.unibo.samplejavafx.mvc.controller.loadercontroller;

import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.AbstractEntityDefinition;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.loader.EntityLoader;
import it.unibo.samplejavafx.mvc.model.loader.EntityLoaderImpl;
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
import java.util.Objects;
import java.util.stream.Stream;

/**
 * placeholder.
 */
public class LoaderControllerImpl implements LoaderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoaderControllerImpl.class);

    private final List<String> entityResRootPath = new ArrayList<>();
    private final Map<String, Class<? extends AbstractEntityDefinition>> associations = Map.of(
            "pieces", PieceDefinition.class
    );
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
        for (final String basePathString : entityResRootPath) {
            final Path basePath = Path.of(basePathString);
            getDirs(basePath).forEach(resPackDir -> loadResourcePack(basePath, resPackDir));
        }
    }

    private void loadResourcePack(final Path basePath, final Path resPackDir) {
        final Path resPackPath = basePath.resolve(resPackDir);
        entityCache.computeIfAbsent(resPackDir.toString(), map -> new HashMap<>());

        try (Stream<Path> entityTypeDirs = Files.list(resPackPath)) {
            entityTypeDirs
                    .filter(Files::isDirectory)
                    .filter(Objects::nonNull)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(associations::containsKey)
                    .forEach(entityType -> {
                        final Path fullPath = resPackPath.resolve(entityType);
                        final List<AbstractEntityDefinition> loadedEntities = entityLoader.loadEntityFile(
                                fullPath,
                                associations.get(entityType)
                        );
                        loadIntoCache(loadedEntities, resPackDir.toString());
                    });

        } catch (final IOException e) {
            LOGGER.error("Could not read files from the specified folder: {}", resPackPath, e);
        }
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
