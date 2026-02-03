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
    private final List<String> entityPath = new ArrayList<>();
    private final Map<String, Class<? extends Entity>> associations = Map.of(
            "pieces", Piece.class
    );
    private final Map<String, Entity> entityCache = new HashMap<>();
    private final EntityLoader entityLoader = new EntityLoaderImpl();

    public LoaderControllerImpl(final List<String> paths) {
        entityPath.addAll(paths);
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
                                entities.forEach(entity -> entityCache.put(entity.getId(),entity));

                            });
                } catch (Exception e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
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
    public Map<String, Entity> getEntityCache() {
        return Collections.unmodifiableMap(entityCache);
    }

}
