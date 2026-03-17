package it.unibo.samplejavafx;

/**
 * Alternative entry point used to launch the JavaFX {@link App}.
 *
 * <p>
 * Some build tools and IDEs prefer a plain {@code public static void main} method in a dedicated class.
 * </p>
 *
 */
public final class Main {

    private Main() {
    }

    /**
     * Delegates to {@link App#main(String[])}.
     *
     * @param args command-line arguments
     */
    public static void main(final String[] args) {
        App.main(args);
    }
}
