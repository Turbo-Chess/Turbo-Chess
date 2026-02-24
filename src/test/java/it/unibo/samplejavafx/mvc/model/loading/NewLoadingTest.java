package it.unibo.samplejavafx.mvc.model.loading;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;

public class NewLoadingTest {
    @Test
    void testNew() throws URISyntaxException {
        URI uri = Objects.requireNonNull(getClass().getResource("/EntityResources")).toURI();
        System.out.printf("Uri: " + uri);
        System.out.println("Path: " + Paths.get(uri));
        System.out.println("Scheme: " + uri.getScheme());
    }
}
