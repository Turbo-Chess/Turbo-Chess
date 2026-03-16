package it.unibo.samplejavafx.mvc.model.loader;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * placeholder.
 */
public final class LoadingUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadingUtils.class);
    private static final String FILE_PROTOCOL = "file:";

    private LoadingUtils() {
        // utility class
    }

    /**
     * placeholder.
     *
     * @param basePath placeholder.
     * @return placeholder.
     */
    public static Path getCorrectPath(final String basePath) {
        if (basePath.startsWith("classpath:")) {
            try {
                final URI uri = LoadingUtils.class.getResource(basePath.replace("classpath:", "")).toURI();
                return Path.of(uri);
            } catch (final URISyntaxException e) {
                LOGGER.error(e.getMessage(), e);
            }

        } else if (basePath.startsWith(FILE_PROTOCOL)) {
            return Path.of(basePath.replace(FILE_PROTOCOL, ""));
        } else {
            try {
                final Path p = Path.of(basePath);
                if (p.isAbsolute()) {
                    return p;
                }
            } catch (final InvalidPathException e) {
                // Fall through exception
                LOGGER.info("Caught and ignored InvalidPathException");
            }
        }

        throw new IllegalStateException("Path does not start with the right prefix: " + basePath);
    }

    /**
     * Generates the correct file path for a piece's image based on its base path, player color, and ID.
     *
     * @param imagePath   The base directory or path for the image.
     * @param playerColor The color of the player owning the piece (affects the image variant).
     * @param id          The specific ID of the piece type.
     * @return a {@link String} representing the full path to the image resource.
     */
    public static String calculateImageColorPath(final String imagePath, final PlayerColor playerColor, final String id) {
        final String color = playerColor == PlayerColor.WHITE ? "white" : "black";
        return FILE_PROTOCOL + getCorrectPath(imagePath) + "/" + color + "_" + id + ".png";
    }
}
