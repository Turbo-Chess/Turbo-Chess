package it.unibo.samplejavafx.mvc.model.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileSystemUtilsTest {

    @Test
    void normalizePathConvertsBackslashes() {
        assertEquals("a/b/c", FileSystemUtils.normalizePath("a\\b\\c"));
    }

    @Test
    void normalizePathReturnsNullForNull() {
        assertEquals(null, FileSystemUtils.normalizePath(null));
    }

    @Test
    void pathContainsWorksAcrossSeparators() {
        assertTrue(FileSystemUtils.pathContains("a\\b\\c", "b/c"));
        assertTrue(FileSystemUtils.pathContains("a/b/c", "b\\c"));
        assertFalse(FileSystemUtils.pathContains(null, "b/c"));
        assertFalse(FileSystemUtils.pathContains("a/b/c", null));
    }

    @Test
    void ensureDirectoryExistsCreatesDirectory(@TempDir final Path tmp) throws IOException {
        final Path dir = tmp.resolve("nested").resolve("dir");
        assertFalse(Files.exists(dir));
        FileSystemUtils.ensureDirectoryExists(dir);
        assertTrue(Files.isDirectory(dir));
    }

    @Test
    void ensureDirectoryExistsThrowsIfPathIsFile(@TempDir final Path tmp) throws Exception {
        final Path file = tmp.resolve("a-file.txt");
        Files.writeString(file, "x");
        assertNotNull(file);
        assertThrows(IOException.class, () -> FileSystemUtils.ensureDirectoryExists(file));
    }
}

