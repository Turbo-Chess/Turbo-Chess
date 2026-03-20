package it.unibo.turbochess.model.loading;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Test class used to experiment with and verify URI and Path behaviors when loading resources.
 */
class NewLoadingTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewLoadingTest.class);

    /**
     * Tests resource URI retrieval and path construction.
     *
     * <p>
     * Logs the URI and Path of the `/EntityResources` directory to verify correct resource location.
     * </p>
     *
     * @throws URISyntaxException if the resource URL cannot be converted to a URI.
     */
    @Test
    void testNew() throws URISyntaxException {
        final URI uri = Objects.requireNonNull(getClass().getResource("/EntityResources")).toURI();
        LOGGER.info("Uri: {}", uri);
        LOGGER.info("Path: {}", Paths.get(uri));
        LOGGER.info("Scheme: {}", uri.getScheme());
    }
}
