package it.unibo.samplejavafx.mvc.model.loader;

import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameControllerImpl;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * placeholder.
 */
//TODO: Missing javadoc for LUCA and GIACOMO 
public final class LoadingUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadingUtils.class);
    private static final Map<URI, FileSystem> JAR_FILE_SYSTEMS = new ConcurrentHashMap<>();
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

    private static URI toJarFileSystemUri(final URI jarEntryUri) {
        final String raw = jarEntryUri.toString();
        final int sep = raw.indexOf("!/");
        final String jarRoot = sep >= 0 ? raw.substring(0, sep + 2) : raw;
        return URI.create(jarRoot);
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
        final String fileName = color + "_" + id + ".png";
        if (imagePath.startsWith("classpath:")) {
            final String resourcePath = imagePath.replace("classpath:", "") + fileName;
            final var url = GameControllerImpl.class.getResource(resourcePath);
            if (url == null) {
                throw new IllegalStateException("Image resource not found: " + resourcePath);
            }
            return url.toExternalForm();
        }
        final var finalPath = LoadingUtils.getCorrectPath(imagePath).resolve(fileName);
        if (!Files.exists(finalPath)) {
            throw new IllegalStateException("File: " + finalPath + " does not exists.");
        }
        return finalPath.toUri().toString();
    }
}
