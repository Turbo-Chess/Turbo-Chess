package it.unibo.turbochess.model.settings;

// CHECKSTYLE: MagicNumber OFF

import it.unibo.turbochess.model.settings.api.GameSettingsManager;
import it.unibo.turbochess.model.settings.impl.GameSettings;
import it.unibo.turbochess.model.settings.impl.GameSettingsManagerImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class GameSettingsManagerTest {
    private static final String SETTINGS_JSON = "settings.json";

    @Test
    void loadReturnsDefaultWhenMissing(@TempDir final Path tempDir) {
        final Path settingsFile = tempDir.resolve(SETTINGS_JSON);
        final GameSettingsManager manager = new GameSettingsManagerImpl(settingsFile);

        final GameSettings settings = manager.load();

        assertEquals(GameSettings.DEFAULT_INITIAL_TIME_SECONDS, settings.initialTimeSeconds());
    }

    @Test
    void saveAndLoadRoundTrip(@TempDir final Path tempDir) {
        final Path settingsFile = tempDir.resolve(SETTINGS_JSON);
        final GameSettingsManager manager = new GameSettingsManagerImpl(settingsFile);

        final boolean saved = manager.save(new GameSettings(300));
        final GameSettings loaded = manager.load();

        assertTrue(saved);
        assertEquals(300, loaded.initialTimeSeconds());
    }

    @Test
    void loadSanitizesInvalidValue(@TempDir final Path tempDir) throws IOException {
        final Path settingsFile = tempDir.resolve(SETTINGS_JSON);
        Files.writeString(settingsFile, "{\"initialTimeSeconds\":-5}");

        final GameSettingsManager manager = new GameSettingsManagerImpl(settingsFile);
        final GameSettings loaded = manager.load();

        assertEquals(GameSettings.DEFAULT_INITIAL_TIME_SECONDS, loaded.initialTimeSeconds());
    }
}

// CHECKSTYLE: MagicNumber ON
