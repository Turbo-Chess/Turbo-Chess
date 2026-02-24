package it.unibo.samplejavafx.mvc.model.loader;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * placeholder.
 */
public final class LoadingUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadingUtils.class);

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

        } else if (basePath.startsWith("file:")) {
            return Path.of(basePath.replace("file:", ""));
        }

        throw new IllegalStateException("Path does not start with the right prefix: " + basePath);
    }
}
