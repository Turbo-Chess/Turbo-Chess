package it.unibo.turbochess.model.loader.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.unibo.turbochess.model.entity.definition.AbstractEntityDefinition;
import it.unibo.turbochess.model.loader.api.EntityLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A concrete implementation of the {@link EntityLoader} interface.
 *
 * <p>
 * This class uses the Jackson library to deserialize JSON files into entity definition objects.
 * It traverses a directory structure to find and load all matching configuration files.
 * </p>
 */
public class EntityLoaderImpl implements EntityLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityLoaderImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AbstractEntityDefinition> loadEntityFile(
            final Path filesPath,
            final Class<? extends AbstractEntityDefinition> classToLoad) {
        try (Stream<Path> paths = Files.walk(filesPath)) {
           return paths.filter(Files::isRegularFile)
                   .filter(file -> file.toString().endsWith(".json"))
                   .map(file -> this.parseEntityFile(file, classToLoad))
                   .filter(Objects::nonNull)
                   .toList();
        } catch (final IOException e) {
            LOGGER.error("Cannot get files inside: {}", filesPath, e);
            throw new IllegalStateException("Cannot access files in: " + filesPath, e);
        }

    }

    private AbstractEntityDefinition parseEntityFile(
            final Path filePath,
            final Class<? extends AbstractEntityDefinition> classToLoad) {
        try (InputStream input = Files.newInputStream(filePath)) {
            final ObjectMapper mapper = new ObjectMapper();
            //return mapper.readerWithView(JsonViews.FirstLoading.class).readValue(new File(filePath.toString()), classToLoad);
            return mapper.readValue(input, classToLoad);
        } catch (final IOException e) {
            LOGGER.error("Cannot read file: {}", filePath, e);
            throw new IllegalStateException("Fatal error loading json configuration of: " + filePath, e);
        }
    }
}
