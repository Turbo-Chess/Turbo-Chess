package it.unibo.samplejavafx.mvc.model.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.unibo.samplejavafx.mvc.model.properties.GameProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class GameSettingsManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSettingsManager.class);
    private static final String SETTINGS_FILE_NAME = "settings.json";

    private final Path settingsFile;
    private final ObjectMapper mapper;

    public GameSettingsManager() {
        this(defaultSettingsPath());
    }

    public GameSettingsManager(final Path settingsFile) {
        this.settingsFile = settingsFile;
        this.mapper = new ObjectMapper();
        this.mapper.findAndRegisterModules();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

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

    public boolean resetToDefault() {
        return save(GameSettings.defaultSettings());
    }

    public Path getSettingsFile() {
        return settingsFile;
    }

    private static Path defaultSettingsPath() {
        return Paths.get(GameProperties.ROOT_RESOURCE_FOLDER.getPath(), SETTINGS_FILE_NAME);
    }
}
