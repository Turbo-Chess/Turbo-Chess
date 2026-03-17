package it.unibo.samplejavafx.mvc.model.settings;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class GameSettingsTest {

    @Test
    void constructorClampsTooLow() {
        final GameSettings settings = new GameSettings(1);
        assertEquals(GameSettings.MIN_INITIAL_TIME_SECONDS, settings.initialTimeSeconds());
    }

    @Test
    void constructorClampsTooHigh() {
        final GameSettings settings = new GameSettings(GameSettings.MAX_INITIAL_TIME_SECONDS + 1);
        assertEquals(GameSettings.MAX_INITIAL_TIME_SECONDS, settings.initialTimeSeconds());
    }
}
