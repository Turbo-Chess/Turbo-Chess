package it.unibo.samplejavafx.mvc.model.settings;

public record GameSettings(long initialTimeSeconds) {
    public static final long DEFAULT_INITIAL_TIME_SECONDS = 600;
    public static final long MIN_INITIAL_TIME_SECONDS = 10;
    public static final long MAX_INITIAL_TIME_SECONDS = 7200;

    public GameSettings {
        initialTimeSeconds = sanitizeInitialTimeSeconds(initialTimeSeconds);
    }

    public static GameSettings defaultSettings() {
        return new GameSettings(DEFAULT_INITIAL_TIME_SECONDS);
    }

    public static long sanitizeInitialTimeSeconds(final long seconds) {
        if (seconds <= 0) {
            return DEFAULT_INITIAL_TIME_SECONDS;
        }
        if (seconds < MIN_INITIAL_TIME_SECONDS) {
            return MIN_INITIAL_TIME_SECONDS;
        }
        if (seconds > MAX_INITIAL_TIME_SECONDS) {
            return MAX_INITIAL_TIME_SECONDS;
        }
        return seconds;
    }
}
