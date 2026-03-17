package it.unibo.samplejavafx.mvc.model.settings;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class GameSettingsManagerTest {

    @Test
    void loadReturnsDefaultWhenMissing(@TempDir final Path tempDir) {
        final Path settingsFile = tempDir.resolve("settings.json");
        final GameSettingsManager manager = new GameSettingsManager(settingsFile);

        final GameSettings settings = manager.load();

        assertEquals(GameSettings.DEFAULT_INITIAL_TIME_SECONDS, settings.initialTimeSeconds());
    }

    @Test
    void saveAndLoadRoundTrip(@TempDir final Path tempDir) {
        final Path settingsFile = tempDir.resolve("settings.json");
        final GameSettingsManager manager = new GameSettingsManager(settingsFile);

        final boolean saved = manager.save(new GameSettings(300));
        final GameSettings loaded = manager.load();

        assertTrue(saved);
        assertEquals(300, loaded.initialTimeSeconds());
    }

    @Test
    void loadSanitizesInvalidValue(@TempDir final Path tempDir) throws IOException {
        final Path settingsFile = tempDir.resolve("settings.json");
        Files.writeString(settingsFile, "{\"initialTimeSeconds\":-5}");

        final GameSettingsManager manager = new GameSettingsManager(settingsFile);
        final GameSettings loaded = manager.load();

        assertEquals(GameSettings.DEFAULT_INITIAL_TIME_SECONDS, loaded.initialTimeSeconds());
    }
}
