package it.unibo.samplejavafx.mvc.controller.LoaderController;

import it.unibo.samplejavafx.mvc.model.Loader.EntityLoader;
import it.unibo.samplejavafx.mvc.model.Loader.EntityLoaderImpl;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.Piece;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class LoaderControllerImpl implements LoaderController {
    private final List<String> entityResRootPath = new ArrayList<>();
    private final Map<String, Class<? extends Entity>> associations = Map.of(
            "pieces", Piece.class
    );
    private final Map<String, Map<String, Entity>> entityCache = new HashMap<>();
    private final EntityLoader entityLoader = new EntityLoaderImpl();

    public LoaderControllerImpl(final List<String> paths) {
        entityResRootPath.addAll(paths);
    }

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
                        List<Entity> loadedEntities = entityLoader.loadEntityFile(path, associations.get(entityType));
                        loadIntoCache(loadedEntities, resPackDir.toString());

                    });

        } catch (final Exception e) {

        }
    }

    private void loadIntoCache(final List<Entity> entitiesToLoad, final String packName) {
        for (var entity : entitiesToLoad) {
            entityCache.get(packName).put(entity.getId(), entity);
        }
    }

    private List<Path> getDirs(final Path rootResDir) {
        List<Path> res = new ArrayList<>();
        try (Stream<Path> paths = Files.list(rootResDir)) {
            res = paths.filter(Files::isDirectory).map(Path::getFileName).toList();
        } catch (final Exception e) {

        }

        return res;
    }

    @Override
    public Map<String, Map<String, Entity>> getEntityCache() {
        return Collections.unmodifiableMap(entityCache);
    }

}
