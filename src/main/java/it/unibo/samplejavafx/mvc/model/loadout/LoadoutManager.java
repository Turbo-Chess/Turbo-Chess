package it.unibo.samplejavafx.mvc.model.loadout;

import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Loadout manager.
 */
public interface LoadoutManager {
    /**
     * Persists a loadout to disk.
     *
     * <p>
     * For non-standard loadouts, the loadout is validated before being saved. Invalid loadouts are silently
     * ignored.
     * </p>
     *
     * @param loadout the loadout to save.
     */
    void save(Loadout loadout);

    /**
     * Saves a loadout if valid.
     *
     * @param loadout     the loadout to save
     * @param definitions the piece definitions to validate against
     */
    void saveValid(Loadout loadout, Map<String, PieceDefinition> definitions);

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
