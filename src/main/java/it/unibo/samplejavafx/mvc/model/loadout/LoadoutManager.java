package it.unibo.samplejavafx.mvc.model.loadout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.AbstractEntityDefinition;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.loader.EntityLoaderImpl;
import it.unibo.samplejavafx.mvc.model.loader.LoadingUtils;
import it.unibo.samplejavafx.mvc.model.properties.GameProperties;
import it.unibo.samplejavafx.mvc.model.utils.FileSystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Loadout manager.
 */
public final class LoadoutManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadoutManager.class);
    private static final String JSON_EXTENSION = ".json";
    private static final String STANDARD_LOADOUT_ID = "standard-chess-loadout";

    private final Path loadoutDir;
    private final ObjectMapper mapper;

    /**
     * Creates a new LoadoutManager.
     */
    public LoadoutManager() {
        this.loadoutDir = Paths.get(GameProperties.LOADOUTS_FOLDER.getPath());
        this.mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        createDirIfNotExists();
        ensureStandardLoadoutExists();
    }

    private void ensureStandardLoadoutExists() {
        final Path file = loadoutDir.resolve(STANDARD_LOADOUT_ID + JSON_EXTENSION);

        if (Files.exists(file)) {
            return;
        }

        try {
            final Loadout generated = StandardLoadoutFactory.createStandard();

            final Loadout standard = new Loadout(
                STANDARD_LOADOUT_ID,
                generated.getName(),
                generated.getCreatedAt(),
                generated.getUpdatedAt(),
                generated.getEntries()
            );
            save(standard);
            LOGGER.info("Ensured default Standard Chess loadout exists and is up-to-date");
        } catch (final IllegalStateException e) {
            LOGGER.error("Failed to ensure standard loadout exists", e);
        }
    }

    private void createDirIfNotExists() {
        try {
            FileSystemUtils.ensureDirectoryExists(loadoutDir);
        } catch (final IOException e) {
            // Already logged by FileSystemUtils, but we might want to suppress app crash
            LOGGER.error("Could not ensure loadout directory exists: {}", loadoutDir);
        }
    }

    /**
     * placeholder.
     *
     * @param loadout placeholder.
     */
    public void save(final Loadout loadout) {
        if (!STANDARD_LOADOUT_ID.equals(loadout.getId()) && !isValid(loadout)) {
            return;
        }
        createDirIfNotExists();
        final Path file = loadoutDir.resolve(loadout.getId() + JSON_EXTENSION);
        try {
            mapper.writeValue(file.toFile(), loadout);
            LOGGER.info("Saved loadout: {}", loadout.getName());
        } catch (final IOException e) {
            LOGGER.error("Failed to save loadout: {}", loadout.getId(), e);
        }
    }

    private boolean isValid(final Loadout loadout) {
        final Optional<Loadout> standardOpt = load(STANDARD_LOADOUT_ID);
        if (standardOpt.isEmpty()) {
            return false;
        }
        final Loadout standard = standardOpt.get();
        final Map<String, PieceDefinition> definitions = loadPieceDefinitions();
        return loadout.isValid(definitions, standard);
    }

    public void saveValid(final Loadout loadout, final Map<String, PieceDefinition> definitions) {
        final Optional<Loadout> standardOpt = load(STANDARD_LOADOUT_ID);
        if (standardOpt.isEmpty()) {
            return;
        }
        final Loadout standard = standardOpt.get();
        if (!loadout.isValid(definitions, standard)) {
            return;
        }
        save(loadout);
    }

    private Map<String, PieceDefinition> loadPieceDefinitions() {
        final var entityLoader = new EntityLoaderImpl();
        final Map<String, PieceDefinition> definitions = new HashMap<>();
        final List<String> roots = List.of(
                GameProperties.INTERNAL_ENTITIES_FOLDER.getPath(),
                GameProperties.EXTERNAL_MOD_FOLDER.getPath()
        );
        for (final String basePathString : roots) {
            final Path basePath;
            try {
                basePath = LoadingUtils.getCorrectPath(basePathString);
            } catch (final Exception e) {
                LOGGER.warn("Failed to resolve entity root path: {}", basePathString, e);
                continue;
            }
            try {
                FileSystemUtils.ensureDirectoryExists(basePath);
            } catch (final IOException e) {
                LOGGER.warn("Cannot access entity root path: {}", basePath, e);
                continue;
            }
            if (!Files.isDirectory(basePath)) {
                continue;
            }
            try (Stream<Path> packs = Files.list(basePath)) {
                packs.filter(Files::isDirectory).forEach(packPath -> {
                    try {
                        final List<AbstractEntityDefinition> loaded =
                                entityLoader.loadEntityFile(packPath, AbstractEntityDefinition.class);
                        loaded.stream()
                                .filter(def -> def instanceof PieceDefinition)
                                .map(def -> (PieceDefinition) def)
                                .forEach(def -> definitions.putIfAbsent(def.getId(), def));
                    } catch (final RuntimeException ex) {
                        LOGGER.warn("Failed to load entity definitions from {}", packPath, ex);
                    }
                });
            } catch (final IOException e) {
                LOGGER.warn("Failed to list entity packs in {}", basePath, e);
            }
        }
        return Collections.unmodifiableMap(definitions);
    }

    /**
     * placeholder.
     *
     * @param id placeholder.
     * @return placeholder.
     */
    public Optional<Loadout> load(final String id) {
        final Path file = loadoutDir.resolve(id + JSON_EXTENSION);
        if (!Files.exists(file)) {
            return Optional.empty();
        }
        try {
            final Loadout loadout = mapper.readValue(file.toFile(), Loadout.class);
            return Optional.of(loadout);
        } catch (final IOException e) {
            LOGGER.error("Failed to load loadout: {}", id, e);
            return Optional.empty();
        }
    }

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    public List<Loadout> getAll() {
        if (!Files.exists(loadoutDir)) {
            return Collections.emptyList();
        }
        try (Stream<Path> files = Files.list(loadoutDir)) {
            return files
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(JSON_EXTENSION))
                    .map(p -> {
                        try {
                            return mapper.readValue(p.toFile(), Loadout.class);
                        } catch (final IOException e) {
                            LOGGER.warn("Failed to parse loadout file: {}", p, e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (final IOException e) {
            LOGGER.error("Failed to list loadouts", e);
            return Collections.emptyList();
        }
    }

    /**
     * placeholder.
     *
     * @param id placeholder.
     */
    public void delete(final String id) {
        final Path file = loadoutDir.resolve(id + JSON_EXTENSION);
        try {
            Files.deleteIfExists(file);
            LOGGER.info("Deleted loadout: {}", id);
        } catch (final IOException e) {
            LOGGER.error("Failed to delete loadout: {}", id, e);
        }
    }
}
