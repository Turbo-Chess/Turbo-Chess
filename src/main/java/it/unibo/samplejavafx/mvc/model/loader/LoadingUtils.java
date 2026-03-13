package it.unibo.samplejavafx.mvc.model.loader;

import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * placeholder.
 */
public final class LoadingUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadingUtils.class);
    private static final Map<URI, FileSystem> JAR_FILE_SYSTEMS = new ConcurrentHashMap<>();

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
            final String resourcePath = basePath.replace("classpath:", "");
            final URL resourceUrl = LoadingUtils.class.getResource(resourcePath);
            if (resourceUrl == null) {
                throw new IllegalStateException("Classpath resource not found: " + basePath);
            }
            try {
                final URI uri = resourceUrl.toURI();
                if ("jar".equalsIgnoreCase(uri.getScheme())) {
                    final URI jarFsUri = toJarFileSystemUri(uri);
                    final FileSystem fs = JAR_FILE_SYSTEMS.computeIfAbsent(jarFsUri, key -> {
                        try {
                            return FileSystems.newFileSystem(key, Collections.emptyMap());
                        } catch (final FileSystemAlreadyExistsException e) {
                            return FileSystems.getFileSystem(key);
                        } catch (final IOException e) {
                            throw new IllegalStateException("Cannot open jar filesystem for: " + key, e);
                        }
                    });
                    return fs.getPath(resourcePath);
                }
                return Path.of(uri);
            } catch (final URISyntaxException e) {
                LOGGER.error(e.getMessage(), e);
            } catch (final FileSystemNotFoundException e) {
                LOGGER.error("Cannot access classpath resource filesystem for {}", basePath, e);
            }

        } else if (basePath.startsWith("file:")) {
            return Path.of(basePath.replace("file:", ""));
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

    private static URI toJarFileSystemUri(final URI jarEntryUri) {
        final String raw = jarEntryUri.toString();
        final int sep = raw.indexOf("!/");
        final String jarRoot = sep >= 0 ? raw.substring(0, sep + 2) : raw;
        return URI.create(jarRoot);
    }
}
