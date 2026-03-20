package it.unibo.turbochess.model.settings.api;

import it.unibo.turbochess.model.settings.impl.GameSettings;

import java.nio.file.Path;

/**
 * Manages persistence of {@link GameSettings}.
 */
public interface GameSettingsManager {
    /**
     * Loads persisted settings.
     *
     * @return the loaded settings, or {@link GameSettings#defaultSettings()} if missing or invalid
     */
    GameSettings load();

    /**
     * Saves settings to the configured destination.
     *
     * @param settings the settings to persist
     * @return {@code true} if the save completes successfully, {@code false} otherwise
     */
    boolean save(GameSettings settings);

    /**
     * Resets persisted settings to {@link GameSettings#defaultSettings()}.
     *
     * @return {@code true} if the reset completes successfully, {@code false} otherwise
     */
    boolean resetToDefault();

    /**
     * @return the backing file used to persist settings
     */
    Path getSettingsFile();
}
