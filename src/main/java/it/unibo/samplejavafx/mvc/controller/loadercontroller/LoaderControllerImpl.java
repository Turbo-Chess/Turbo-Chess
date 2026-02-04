package it.unibo.samplejavafx.mvc.controller.loadercontroller;

import it.unibo.samplejavafx.mvc.model.loader.EntityLoader;
import it.unibo.samplejavafx.mvc.model.loader.EntityLoaderImpl;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.Piece;

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
    private final List<String> entityResRootPath = new ArrayList<>();
    private final Map<String, Class<? extends Entity>> associations = Map.of(
            "pieces", Piece.class
    );
    private final Map<String, Map<String, Entity>> entityCache = new HashMap<>();
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
                    .filter(path -> associations.containsKey(path.getFileName().toString()))
                    .forEach(path -> {
                        final String entityType = path.getFileName().toString();
                        final List<Entity> loadedEntities = entityLoader.loadEntityFile(path, associations.get(entityType));
                        loadIntoCache(loadedEntities, resPackDir.toString());

                    });

        } catch (final Exception e) {

        }
    }

    private void loadIntoCache(final List<Entity> entitiesToLoad, final String packName) {
        for (final var entity : entitiesToLoad) {
            entityCache.get(packName).put(entity.getId(), entity);
        }
    }

    private List<Path> getDirs(final Path rootResDir) {
        final List<Path> res = new ArrayList<>();
        try (Stream<Path> paths = Files.list(rootResDir)) {
            res.addAll(paths.filter(Files::isDirectory).map(Path::getFileName).toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return res;
    }

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    @Override
    public Map<String, Map<String, Entity>> getEntityCache() {
        return Collections.unmodifiableMap(entityCache);
    }

}
