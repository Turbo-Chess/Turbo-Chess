package it.unibo.samplejavafx.mvc.controller.LoaderController;

import it.unibo.samplejavafx.mvc.model.Loader.EntityCacheSystem;
import it.unibo.samplejavafx.mvc.model.Loader.EntityCacheSystemImpl;
import it.unibo.samplejavafx.mvc.model.Loader.EntityLoader;
import it.unibo.samplejavafx.mvc.model.Loader.EntityLoaderImpl;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import lombok.Getter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class LoaderControllerImpl implements LoaderController {
    private static final String INTERNAL_ENTITIES_PATH = "src/main/resources/EntityResources";
    private final List<String> entityPath = List.of(INTERNAL_ENTITIES_PATH);
    // Needs to be generalized for all OSes
    private static final String EXTERNAL_ENTITIES_PATH = "";

    private final Map<String, Class<? extends Entity>> associations = Map.of(
            "pieces", Piece.class
    );

    private final EntityLoader entityLoader = new EntityLoaderImpl();
    private final EntityCacheSystem entityCache = new EntityCacheSystemImpl();

    public LoaderControllerImpl() {

    }

    public void load() {
        // Get all the resource folders
        for (final var stringPath : entityPath) {
            for (final var packDir : getPackDirs(Path.of(stringPath))) {
                try (Stream<Path> entityFileDir = Files.list(Path.of(stringPath).resolve(packDir))) {
                    entityFileDir.filter(Files::isDirectory)
                            .map(Path::getFileName)
                            .map(Path::toString)
                            .filter(associations::containsKey)
                            .forEach(folderName -> {
                                //entityFileDir + folderName
                                final List<Entity> entities = entityLoader.loadEntityFile(Path.of(stringPath).resolve(packDir).resolve(folderName), associations.get(folderName));
                                entities.forEach(entity -> entityCache.addEntity(entity, entity.getId()));
                            });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    private List<Path> getPackDirs(final Path rootResDir) {
        List<Path> res = new ArrayList<>();
        try (Stream<Path> paths = Files.list(rootResDir)) {
             res = paths.filter(Files::isDirectory).map(Path::getFileName).toList();
        } catch (final Exception e) {

        }

        return res;
    }

    @Override
    public EntityCacheSystem getEntityCache() {
        // TODO: make this return an immutable object
        return entityCache;
    }

}
