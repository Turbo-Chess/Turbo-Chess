package it.unibo.samplejavafx.mvc.model.Loader;

import com.google.gson.Gson;
import it.unibo.samplejavafx.mvc.model.entity.AbstractEntity;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.Piece;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class EntityLoaderImpl implements EntityLoader {
    private final Gson gson = new Gson();

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
            return gson.fromJson(reader, classToLoad);
        } catch (final Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            //TODO: check this
            return null;
        }
    }
}
