package it.unibo.samplejavafx.mvc.model.loading;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;

class NewLoadingTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewLoadingTest.class);

    @Test
    void testNew() throws URISyntaxException {
        final URI uri = Objects.requireNonNull(getClass().getResource("/EntityResources")).toURI();
        LOGGER.info("Uri: {}", uri);
        LOGGER.info("Path: {}", Paths.get(uri));
        LOGGER.info("Scheme: {}", uri.getScheme());
    }
}
