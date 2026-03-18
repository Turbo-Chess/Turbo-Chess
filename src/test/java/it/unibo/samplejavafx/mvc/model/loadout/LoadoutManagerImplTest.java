package it.unibo.samplejavafx.mvc.model.loadout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoadoutManagerImplTest {

    private static final String STANDARD_ID = "standard-chess-loadout";

    @Test
    void constructorEnsuresStandardLoadoutExists(@TempDir final Path tmp) {
        new LoadoutManagerImpl(tmp);
        assertTrue(Files.exists(tmp.resolve(STANDARD_ID + ".json")));
    }

    @Test
    void saveLoadGetAllAndDeleteWorkOnTempDir(@TempDir final Path tmp) {
        final var manager = new LoadoutManagerImpl(tmp);

        final var standard = manager.load(STANDARD_ID).orElseThrow();
        final var custom = new Loadout(
            "custom",
            "Custom",
            Instant.now().toEpochMilli(),
            Instant.now().toEpochMilli(),
            standard.getEntries()
        );

        manager.save(custom);
        assertTrue(Files.exists(tmp.resolve("custom.json")));

        final var loaded = manager.load("custom").orElseThrow();
        assertEquals("custom", loaded.getId());
        assertEquals(standard.getEntries(), loaded.getEntries());

        final var all = manager.getAll();
        assertTrue(all.stream().anyMatch(l -> STANDARD_ID.equals(l.getId())));
        assertTrue(all.stream().anyMatch(l -> "custom".equals(l.getId())));

        manager.delete("custom");
        assertFalse(Files.exists(tmp.resolve("custom.json")));
        assertTrue(manager.load("custom").isEmpty());
    }

    @Test
    void saveDoesNotWriteInvalidLoadout(@TempDir final Path tmp) {
        final var manager = new LoadoutManagerImpl(tmp);
        final var invalid = new Loadout(
            "invalid",
            "Invalid",
            Instant.now().toEpochMilli(),
            Instant.now().toEpochMilli(),
            List.of()
        );

        manager.save(invalid);
        assertFalse(Files.exists(tmp.resolve("invalid.json")));
    }

    @Test
    void loadReturnsEmptyOnCorruptedJsonAndGetAllSkipsIt(@TempDir final Path tmp) throws Exception {
        final var manager = new LoadoutManagerImpl(tmp);

        Files.writeString(tmp.resolve("corrupt.json"), "{not-json");
        assertTrue(manager.load("corrupt").isEmpty());
        assertFalse(manager.getAll().stream().anyMatch(l -> "corrupt".equals(l.getId())));
    }
}

