package it.unibo.samplejavafx.mvc.model.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.samplejavafx.mvc.model.entity.Entity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * placeholder.
 */
public class EntityLoaderImpl implements EntityLoader {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Entity> loadEntityFile(final Path filesPath, final Class<? extends Entity> classToLoad) {
        try (Stream<Path> paths = Files.walk(filesPath)) {
           return paths.filter(Files::isRegularFile)
                   .filter(file -> file.toString().endsWith(".json"))
                   .map(file -> this.parseEntityFile(file, classToLoad))
                   .filter(java.util.Objects::nonNull)
                   .toList();
        } catch (final IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return List.of();
        }

    }

    private Entity parseEntityFile(final Path filePath, final Class<? extends Entity> classToLoad) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readerWithView(JsonViews.FirstLoading.class).readValue(new File(filePath.toString()), classToLoad);
        } catch (final IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
