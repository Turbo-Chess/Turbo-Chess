package it.unibo.samplejavafx.mvc.model.Loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.samplejavafx.mvc.model.entity.Entity;

import java.io.File;
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
        try  {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readerWithView(JsonViews.FirstLoading.class).readValue(new File(filePath.toString()), classToLoad);
        } catch (final Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return null;
        }
    }
}
