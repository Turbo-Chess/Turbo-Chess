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
    private final List<Entity> loadedEntities = new ArrayList<>();

    @Override
    public List<Entity> loadEntityFile(final Path filesPath, final Class<? extends Entity> classToLoad) {
        loadedEntities.clear();
        try (Stream<Path> paths = Files.walk(filesPath)) {
           paths.filter(Files::isRegularFile)
                   .filter(file -> file.endsWith(".json"))
                   .forEach(file -> this.parseEntityFile(file, classToLoad));
        } catch (final Exception e) {

        }

        return List.copyOf(loadedEntities);
    }

    @Override
    public void parseEntityFile(final Path filePath, final Class<? extends Entity> classToLoad) {
        try (final var reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            loadedEntities.add(gson.fromJson(reader, classToLoad));
        } catch (final Exception e) {

        }
    }
}
