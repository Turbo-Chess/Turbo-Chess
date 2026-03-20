package it.unibo.turbochess;

/**
 * Application entry point wrapper.
 *
 * <p>
 * This class serves as the launcher for the application, delegating execution to
 * the JavaFX {@link App} class. This structure is required to correctly load
 * JavaFX runtime components in certain packaging configurations to avoid module path issues.
 */
public final class Main {

    private Main() {
    }

    /**
     * Main program entry point.
     *
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        App.main(args);
    }
}
