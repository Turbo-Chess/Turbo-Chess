package it.unibo.turbochess.model.loadout.api;

import java.util.List;
import java.util.Optional;

/**
 * Loadout manager.
 */
public interface LoadoutManager {

    /**
     * Saves a loadout, validating it against the current piece definitions and the standard loadout.
     *
     * @param loadout the loadout to save
     */
    void save(Loadout loadout);

    /**
     * Loads a loadout by its identifier.
     *
     * @param id the loadout identifier (file name without extension).
     * @return the loaded {@link Loadout}, or {@link Optional#empty()} if not found or unreadable.
     */
    Optional<Loadout> load(String id);

    /**
     * Loads all available loadouts from the configured loadout directory.
     *
     * @return the list of successfully parsed {@link Loadout}s. Returns an empty list if none are found.
     */
    List<Loadout> getAll();

    /**
     * Deletes the loadout with the given identifier from disk.
     *
     * @param id the loadout identifier (file name without extension).
     */
    void delete(String id);
}
