package it.unibo.turbochess.model.loadout.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.unibo.turbochess.model.entity.definition.AbstractEntityDefinition;
import it.unibo.turbochess.model.entity.definition.PieceDefinition;
import it.unibo.turbochess.model.loader.impl.EntityLoaderImpl;
import it.unibo.turbochess.model.utils.LoadingUtils;
import it.unibo.turbochess.model.loadout.api.Loadout;
import it.unibo.turbochess.model.loadout.api.LoadoutManager;
import it.unibo.turbochess.model.properties.GameProperties;
import it.unibo.turbochess.model.utils.FileSystemUtils;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Default implementation of {@link LoadoutManager} based on JSON serialization.
 */
public final class LoadoutManagerImpl implements LoadoutManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadoutManagerImpl.class);
    private static final String JSON_EXTENSION = ".json";
    private static final String STANDARD_LOADOUT_ID = "standard-chess-loadout";

    private final Path loadoutDir;
    private final ObjectMapper mapper;

    /**
     * Creates a manager using the default loadout directory defined in {@link GameProperties}.
     */
    public LoadoutManagerImpl() {
        this(Paths.get(GameProperties.LOADOUTS_FOLDER.getPath()));
    }

    /**
     * Creates a manager using the specified loadout directory.
     *
     * @param loadoutDir the loadout directory
     */
    public LoadoutManagerImpl(final Path loadoutDir) {
        this.loadoutDir = loadoutDir;
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

            final Loadout standard = new LoadoutImpl(
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

    /** {@inheritDoc} */
    @Override
    public void save(final Loadout loadout) {
        if (STANDARD_LOADOUT_ID.equals(loadout.getId())) {
            write(loadout);
            return;
        }
        load(STANDARD_LOADOUT_ID)
                .filter(standard -> loadout.isValid(loadPieceDefinitions(), standard))
                .ifPresent(ignored -> write(loadout));
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Loadout> load(final String id) {
        final Path file = loadoutDir.resolve(id + JSON_EXTENSION);
        if (!Files.exists(file)) {
            return Optional.empty();
        }
        try {
            final Loadout loadout = mapper.readValue(file.toFile(), LoadoutImpl.class);
            return Optional.of(loadout);
        } catch (final IOException e) {
            LOGGER.error("Failed to load loadout: {}", id, e);
            return Optional.empty();
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<Loadout> getAll() {
        if (!Files.exists(loadoutDir)) {
            return Collections.emptyList();
        }
        try (Stream<Path> files = Files.list(loadoutDir)) {
            return files
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(JSON_EXTENSION))
                    .map(this::parseLoadoutFile)
                    .flatMap(Optional::stream)
                    .collect(Collectors.toList());
        } catch (final IOException e) {
            LOGGER.error("Failed to list loadouts", e);
            return Collections.emptyList();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void delete(final String id) {
        final Path file = loadoutDir.resolve(id + JSON_EXTENSION);
        try {
            Files.deleteIfExists(file);
            LOGGER.info("Deleted loadout: {}", id);
        } catch (final IOException e) {
            LOGGER.error("Failed to delete loadout: {}", id, e);
        }
    }

    private void write(final Loadout loadout) {
        createDirIfNotExists();
        final Path file = loadoutDir.resolve(loadout.getId() + JSON_EXTENSION);
        try {
            mapper.writeValue(file.toFile(), loadout);
            LOGGER.info("Saved loadout: {}", loadout.getName());
        } catch (final IOException e) {
            LOGGER.error("Failed to save loadout: {}", loadout.getId(), e);
        }
    }

    private Optional<Loadout> parseLoadoutFile(final Path file) {
        try {
            return Optional.of(mapper.readValue(file.toFile(), LoadoutImpl.class));
        } catch (final IOException e) {
            LOGGER.warn("Failed to parse loadout file: {}", file, e);
            return Optional.empty();
        }
    }

    private Map<String, PieceDefinition> loadPieceDefinitions() {
        final var entityLoader = new EntityLoaderImpl();
        final Map<String, PieceDefinition> definitions = new HashMap<>();
        final List<String> roots = List.of(
                GameProperties.INTERNAL_ENTITIES_FOLDER.getPath(),
                GameProperties.EXTERNAL_MOD_FOLDER.getPath()
        );
        roots.stream()
                .map(this::resolveEntityRoot)
                .flatMap(Optional::stream)
                .forEach(basePath -> loadPieceDefinitionsFromRoot(basePath, entityLoader, definitions));
        return Collections.unmodifiableMap(definitions);
    }

    private Optional<Path> resolveEntityRoot(final String basePathString) {
        final Path basePath;
        try {
            basePath = LoadingUtils.getCorrectPath(basePathString);
        } catch (final IllegalStateException e) {
            LOGGER.warn("Failed to resolve entity root path: {}", basePathString, e);
            return Optional.empty();
        }
        try {
            FileSystemUtils.ensureDirectoryExists(basePath);
        } catch (final IOException e) {
            LOGGER.warn("Cannot access entity root path: {}", basePath, e);
            return Optional.empty();
        }
        if (!Files.isDirectory(basePath)) {
            return Optional.empty();
        }
        return Optional.of(basePath);
    }

    private void loadPieceDefinitionsFromRoot(
            final Path basePath,
            final EntityLoaderImpl entityLoader,
            final Map<String, PieceDefinition> definitions
    ) {
        try (Stream<Path> packs = Files.list(basePath)) {
            packs.filter(Files::isDirectory)
                    .forEach(packPath -> loadPieceDefinitionsFromPack(packPath, entityLoader, definitions));
        } catch (final IOException e) {
            LOGGER.warn("Failed to list entity packs in {}", basePath, e);
        }
    }

    private void loadPieceDefinitionsFromPack(
            final Path packPath,
            final EntityLoaderImpl entityLoader,
            final Map<String, PieceDefinition> definitions
    ) {
        try {
            final List<AbstractEntityDefinition> loaded =
                    entityLoader.loadEntityFile(packPath, AbstractEntityDefinition.class);
            loaded.stream()
                    .filter(PieceDefinition.class::isInstance)
                    .map(PieceDefinition.class::cast)
                    .forEach(def -> definitions.putIfAbsent(def.getId(), def));
        } catch (final IllegalStateException ex) {
            LOGGER.warn("Failed to load entity definitions from {}", packPath, ex);
        }
    }

    private void createDirIfNotExists() {
        try {
            FileSystemUtils.ensureDirectoryExists(loadoutDir);
        } catch (final IOException e) {
            LOGGER.error("Could not ensure loadout directory exists: {}", loadoutDir);
        }
    }
}
