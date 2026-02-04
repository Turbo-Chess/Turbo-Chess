package it.unibo.samplejavafx.mvc.model.replay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Manages saving and loading of game replays.
 */
public class ReplayManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReplayManager.class);  
    private final ObjectMapper mapper;

    /**
     * Default constructor.
     */
    public ReplayManager() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Saves the game history to a file.
     *
     * @param history the history to save.
     * @param path the destination file path.
     * @throws IOException if an error occurs.
     */
    public void saveGame(final GameHistory history, final Path path) {
        try (OutputStream os = Files.newOutputStream(path)) {
            mapper.writeValue(os, history);
        } catch (final IOException e) {
            LOGGER.error("Error saving game history to file: {}", path, e); 
        }
    }

    /**
     * Loads a game history from a file.
     *
     * @param path the source file path.
     * @return the loaded history.
     * @throws IOException if an error occurs.
     */
    public GameHistory loadGame(final Path path) {
        try (InputStream is = Files.newInputStream(path)) {
            return mapper.readValue(is, GameHistory.class);
        } catch (final IOException e) {
            LOGGER.error("Error loading game history from file: {}", path, e);
            return null;
        }
    }
}
