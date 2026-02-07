package it.unibo.samplejavafx.mvc.model.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.AbstractEntityDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * placeholder.
 */
public class EntityLoaderImpl implements EntityLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityLoaderImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AbstractEntityDefinition> loadEntityFile(final Path filesPath, final Class<? extends AbstractEntityDefinition> classToLoad) {
        try (Stream<Path> paths = Files.walk(filesPath)) {
           return paths.filter(Files::isRegularFile)
                   .filter(file -> file.toString().endsWith(".json"))
                   .map(file -> this.parseEntityFile(file, classToLoad))
                   .filter(java.util.Objects::nonNull)
                   .toList();
        } catch (final IOException e) {
            LOGGER.error("Cannot get files inside: {}", filesPath, e);
            return List.of();
        }

    }

    private AbstractEntityDefinition parseEntityFile(final Path filePath, final Class<? extends AbstractEntityDefinition> classToLoad) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            //return mapper.readerWithView(JsonViews.FirstLoading.class).readValue(new File(filePath.toString()), classToLoad);
            return mapper.readValue(new File(filePath.toString()), classToLoad);
        } catch (final IOException e) {
            LOGGER.error("Cannot read file: {}", filePath, e);
            return null;
        }
    }
}
