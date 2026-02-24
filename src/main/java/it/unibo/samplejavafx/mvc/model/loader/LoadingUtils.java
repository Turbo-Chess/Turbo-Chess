package it.unibo.samplejavafx.mvc.model.loader;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class LoadingUtils {
    public static Path getCorrectPath(final String basePath) {
        if (basePath.startsWith("classpath:")) {
            try {
                final URI uri = LoadingUtils.class.getResource(basePath.replace("classpath:", "")).toURI();
                return Path.of(uri);
            } catch (final URISyntaxException e) {
                System.out.println(e.getMessage());
            }

        } else if (basePath.startsWith("file:")) {
            return Path.of(basePath.replace("file:", ""));
        }

        throw new IllegalStateException("Path does not start with the right prefix: " + basePath);
    }
}
