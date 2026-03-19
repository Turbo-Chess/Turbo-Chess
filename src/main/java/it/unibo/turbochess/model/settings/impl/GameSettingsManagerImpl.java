package it.unibo.turbochess.model.settings.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.unibo.turbochess.model.properties.GameProperties;
import it.unibo.turbochess.model.settings.api.GameSettingsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Default implementation of {@link GameSettingsManager} based on JSON serialization.
 */
public final class GameSettingsManagerImpl implements GameSettingsManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSettingsManagerImpl.class);
    private static final String SETTINGS_FILE_NAME = "settings.json";

    private final Path settingsFile;
    private final ObjectMapper mapper;

    /**
     * Creates a manager that uses the default settings file path.
     */
    public GameSettingsManagerImpl() {
        this(defaultSettingsPath());
    }

    /**
     * Creates a manager that reads and writes settings to a specific file.
     *
     * @param settingsFile the file used for persistence
     */
    public GameSettingsManagerImpl(final Path settingsFile) {
        this.settingsFile = settingsFile;
        this.mapper = new ObjectMapper();
        this.mapper.findAndRegisterModules();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /** {@inheritDoc} */
    @Override
    public GameSettings load() {
        if (!Files.exists(settingsFile)) {
            return GameSettings.defaultSettings();
        }
        try (InputStream is = Files.newInputStream(settingsFile)) {
            final GameSettings loaded = mapper.readValue(is, GameSettings.class);
            return new GameSettings(loaded.initialTimeSeconds());
        } catch (final IOException e) {
            LOGGER.error("Error loading settings from file: {}", settingsFile, e);
            return GameSettings.defaultSettings();
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean save(final GameSettings settings) {
        try {
            final Path parent = settingsFile.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (OutputStream os = Files.newOutputStream(settingsFile)) {
                mapper.writeValue(os, settings);
                return true;
            }
        } catch (final IOException e) {
            LOGGER.error("Error saving settings to file: {}", settingsFile, e);
            return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean resetToDefault() {
        return save(GameSettings.defaultSettings());
    }

    /** {@inheritDoc} */
    @Override
    public Path getSettingsFile() {
        return settingsFile;
    }

    private static Path defaultSettingsPath() {
        return Paths.get(GameProperties.ROOT_RESOURCE_FOLDER.getPath(), SETTINGS_FILE_NAME);
    }
}
