package it.unibo.turbochess.model.settings.impl;

/**
 * Immutable configuration of game settings.
 *
 * @param initialTimeSeconds the initial time (in seconds) allocated to each player
 */
public record GameSettings(long initialTimeSeconds) {
    public static final long DEFAULT_INITIAL_TIME_SECONDS = 600;
    public static final long MIN_INITIAL_TIME_SECONDS = 10;
    public static final long MAX_INITIAL_TIME_SECONDS = 7200;

    /**
     * Canonical constructor.
     *
     * @param initialTimeSeconds the initial time (in seconds) allocated to each player
     */
    public GameSettings {
        initialTimeSeconds = sanitizeInitialTimeSeconds(initialTimeSeconds);
    }

    /**
     * @return a {@link GameSettings} instance with default values
     */
    public static GameSettings defaultSettings() {
        return new GameSettings(DEFAULT_INITIAL_TIME_SECONDS);
    }

    /**
     * Sanitizes an initial time value according to {@link #MIN_INITIAL_TIME_SECONDS} and
     * {@link #MAX_INITIAL_TIME_SECONDS}.
     *
     * @param seconds the raw initial time, in seconds
     * @return a sanitized value, in seconds
     */
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
