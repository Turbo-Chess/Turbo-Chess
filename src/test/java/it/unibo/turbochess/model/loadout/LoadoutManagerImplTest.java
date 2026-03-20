package it.unibo.turbochess.model.loadout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import it.unibo.turbochess.model.loadout.api.Loadout;
import it.unibo.turbochess.model.loadout.impl.LoadoutImpl;
import it.unibo.turbochess.model.loadout.impl.LoadoutManagerImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoadoutManagerImplTest {

    private static final String JSON_EXTENSION = ".json";
    private static final String STANDARD_ID = "standard-chess-loadout";
    private static final String CUSTOM_ID = "custom";
    private static final String CUSTOM_NAME = "Custom";
    private static final String INVALID_ID = "invalid";
    private static final String CORRUPT_ID = "corrupt";

    @Test
    void constructorEnsuresStandardLoadoutExists(@TempDir final Path tmp) {
        new LoadoutManagerImpl(tmp);
        assertTrue(Files.exists(tmp.resolve(STANDARD_ID + JSON_EXTENSION)));
    }

    @Test
    void saveLoadGetAllAndDeleteWorkOnTempDir(@TempDir final Path tmp) {
        final var manager = new LoadoutManagerImpl(tmp);

        final var standard = manager.load(STANDARD_ID).orElseThrow();
        final Loadout custom = new LoadoutImpl(
            CUSTOM_ID,
            CUSTOM_NAME,
            Instant.now().toEpochMilli(),
            Instant.now().toEpochMilli(),
            standard.getEntries()
        );

        manager.save(custom);
        assertTrue(Files.exists(tmp.resolve(CUSTOM_ID + JSON_EXTENSION)));

        final var loaded = manager.load(CUSTOM_ID).orElseThrow();
        assertEquals(CUSTOM_ID, loaded.getId());
        assertEquals(standard.getEntries(), loaded.getEntries());

        final var all = manager.getAll();
        assertTrue(all.stream().anyMatch(l -> STANDARD_ID.equals(l.getId())));
        assertTrue(all.stream().anyMatch(l -> CUSTOM_ID.equals(l.getId())));

        manager.delete(CUSTOM_ID);
        assertFalse(Files.exists(tmp.resolve(CUSTOM_ID + JSON_EXTENSION)));
        assertTrue(manager.load(CUSTOM_ID).isEmpty());
    }

    @Test
    void saveDoesNotWriteInvalidLoadout(@TempDir final Path tmp) {
        final var manager = new LoadoutManagerImpl(tmp);
        final Loadout invalid = new LoadoutImpl(
            INVALID_ID,
            "Invalid",
            Instant.now().toEpochMilli(),
            Instant.now().toEpochMilli(),
            List.of()
        );

        manager.save(invalid);
        assertFalse(Files.exists(tmp.resolve(INVALID_ID + JSON_EXTENSION)));
    }

    @Test
    void loadReturnsEmptyOnCorruptedJsonAndGetAllSkipsIt(@TempDir final Path tmp) throws IOException {
        final var manager = new LoadoutManagerImpl(tmp);

        Files.writeString(tmp.resolve(CORRUPT_ID + JSON_EXTENSION), "{not-json");
        assertTrue(manager.load(CORRUPT_ID).isEmpty());
        assertFalse(manager.getAll().stream().anyMatch(l -> CORRUPT_ID.equals(l.getId())));
    }
}
