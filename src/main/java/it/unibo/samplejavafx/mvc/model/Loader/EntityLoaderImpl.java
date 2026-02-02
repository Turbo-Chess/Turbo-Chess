package it.unibo.samplejavafx.mvc.model.Loader;

import it.unibo.samplejavafx.mvc.model.entity.Entity;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class EntityLoaderImpl implements EntityLoader {

    @Override
    public List<Entity> loadEntityFile(final Path filesPath, final Class<? extends Entity> classToLoad) {
        try (Stream<Path> paths = Files.walk(filesPath)) {
           return paths.filter(Files::isRegularFile)
                   .filter(file -> file.toString().endsWith(".json"))
                   .map(file -> this.parseEntityFile(file, classToLoad))
                   .filter(java.util.Objects::nonNull)
                   .toList();
        } catch (final Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return List.of();
        }

    }

    private Entity parseEntityFile(final Path filePath, final Class<? extends Entity> classToLoad) {
        try (final var reader = Files.newBufferedReader(filePath)) {
            return null;
        } catch (final Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            //TODO: check this
            return null;
        }
    }
}
