package it.unibo.turbochess.model.replay.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.unibo.turbochess.model.replay.api.GameHistory;
import it.unibo.turbochess.model.replay.api.ReplayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Default implementation of {@link ReplayManager} based on JSON serialization.
 */
public final class ReplayManagerImpl implements ReplayManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReplayManagerImpl.class);
    private final ObjectMapper mapper;

    /**
     * Creates a replay manager configured for pretty-printed JSON output.
     */
    public ReplayManagerImpl() {
        this.mapper = new ObjectMapper();
        this.mapper.findAndRegisterModules();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /** {@inheritDoc} */
    @Override
    public boolean saveGame(final GameHistory history, final Path path) throws IOException {
        final Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (OutputStream os = Files.newOutputStream(path)) {
            mapper.writeValue(os, history);
            return true;
        }
    }

    /** {@inheritDoc} */
    @Override
    public GameHistory loadGame(final Path path) {
        try (InputStream is = Files.newInputStream(path)) {
            return mapper.readValue(is, GameHistoryImpl.class);
        } catch (final IOException e) {
            LOGGER.error("Error loading game history from file: {}", path, e);
            return null;
        }
    }
}
