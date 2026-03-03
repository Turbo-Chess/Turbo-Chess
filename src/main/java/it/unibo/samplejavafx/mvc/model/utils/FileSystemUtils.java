package it.unibo.samplejavafx.mvc.model.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class for file system operations.
 */
public final class FileSystemUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemUtils.class);

    private FileSystemUtils() {
        // utility class
    }

    /**
     * Ensures that the specified directory exists.
     * If it does not exist, it attempts to create it.
     *
     * @param path the directory path.
     * @throws IOException if an I/O error occurs or the directory cannot be created.
     */
    public static void ensureDirectoryExists(final Path path) throws IOException {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                LOGGER.info("Created directory: {}", path);
            } catch (final FileSystemException e) {
                if (e.getMessage() != null && e.getMessage().contains("Operation not permitted")) {
                    LOGGER.error("PERMISSION DENIED: Cannot create directory at {}. Please grant Full Disk Access to the terminal or application.", path);
                }
                // Try to create it anyway with File.mkdirs() as a fallback for some edge cases
                if (!path.toFile().exists() && !path.toFile().mkdirs()) {
                    // Rethrow original exception if fallback fails
                     throw e;
                }
            } catch (final IOException e) {
                LOGGER.error("Failed to create directory at {}: {}", path, e.getMessage());
                throw e;
            }
        } else if (!Files.isDirectory(path)) {
            throw new IOException("Path exists but is not a directory: " + path);
        } else if (!Files.isWritable(path)) {
            LOGGER.warn("Directory exists but is not writable: {}", path);
        }
    }
}
