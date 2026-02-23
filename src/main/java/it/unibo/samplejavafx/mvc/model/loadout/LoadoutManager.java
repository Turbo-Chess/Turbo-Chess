package it.unibo.samplejavafx.mvc.model.loadout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Loadout manager.
 */
public class LoadoutManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadoutManager.class); 
    private static final String DEFAULT_APP_DIR = ".turbochess";
    private static final String LOADOUTS_DIR = "loadouts";

    private final Path loadoutDir;
    private final ObjectMapper mapper;

    /**
     * Creates a new LoadoutManager.
     */
    public LoadoutManager() {
        this.loadoutDir = Paths.get(System.getProperty("user.home"), DEFAULT_APP_DIR, LOADOUTS_DIR);
        this.mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        createDirIfNotExists();
    }

    private void createDirIfNotExists() {
        if (!Files.exists(loadoutDir)) {
            try {
                Files.createDirectories(loadoutDir);
            } catch (IOException e) {
                LOGGER.error("Could not create loadout directory: {}", loadoutDir, e);
            }
        }
    }

    public void save(final Loadout loadout) {
        createDirIfNotExists();
        final Path file = loadoutDir.resolve(loadout.getId() + ".json");
        try {
            mapper.writeValue(file.toFile(), loadout);
            LOGGER.info("Saved loadout: {}", loadout.getName());
        } catch (IOException e) {
            LOGGER.error("Failed to save loadout: {}", loadout.getId(), e);
            throw new RuntimeException("Failed to save loadout", e);
        }
    }

    public Optional<Loadout> load(final String id) {
        final Path file = loadoutDir.resolve(id + ".json");
        if (!Files.exists(file)) {
            return Optional.empty();
        }
        try {
            final Loadout loadout = mapper.readValue(file.toFile(), Loadout.class);
            return Optional.of(loadout);
        } catch (IOException e) {
            LOGGER.error("Failed to load loadout: {}", id, e);
            return Optional.empty();
        }
    }

    public List<Loadout> getAll() {
        if (!Files.exists(loadoutDir)) {
            return Collections.emptyList();
        }
        try (Stream<Path> files = Files.list(loadoutDir)) {
            return files
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".json"))
                    .map(p -> {
                        try {
                            return mapper.readValue(p.toFile(), Loadout.class);
                        } catch (IOException e) {
                            LOGGER.warn("Failed to parse loadout file: {}", p, e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.error("Failed to list loadouts", e);
            return Collections.emptyList();
        }
    }

    public void delete(final String id) {
        final Path file = loadoutDir.resolve(id + ".json");
        try {
            Files.deleteIfExists(file);
            LOGGER.info("Deleted loadout: {}", id);
        } catch (IOException e) {
            LOGGER.error("Failed to delete loadout: {}", id, e);
        }
    }
}
