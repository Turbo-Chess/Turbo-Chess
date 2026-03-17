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
     * placeholder.
     *
     * @param loadout placeholder.
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
     * placeholder.
     *
     * @param id placeholder.
     * @return placeholder.
     */
    Optional<Loadout> load(String id);

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    List<Loadout> getAll();

    /**
     * placeholder.
     *
     * @param id placeholder.
     */
    void delete(String id);
}
