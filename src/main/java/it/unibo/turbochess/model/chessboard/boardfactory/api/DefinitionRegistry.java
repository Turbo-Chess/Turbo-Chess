package it.unibo.turbochess.model.chessboard.boardfactory.api;

import it.unibo.turbochess.model.entity.definition.AbstractEntityDefinition;

import java.util.List;
import java.util.Map;

/**
 * Interface defining a registry for entity definitions.
 */
public interface DefinitionRegistry {
    /**
     * Retrieves a list of all available resource pack identifiers.
     *
     * @return a {@link List} of strings representing the IDs of the resource packs.
     */
    List<String> getResPackIds();

    /**
     * Retrieves all piece definitions contained within a specific resource pack.
     *
     * @param packId the identifier of the resource pack.
     * @return a {@link Map} where the keys are piece identifiers and the values are their corresponding definitions.
     */
    Map<String, AbstractEntityDefinition> getPackData(String packId);

    /**
     * Retrieves a complete list of all entity definitions across all registered resource packs.
     *
     * @return a {@link List} containing every {@link AbstractEntityDefinition} available in the registry.
     */
    List<AbstractEntityDefinition> getAllDefinitions();

    /**
     * Retrieves the definition for a specific entity based on its pack and piece identifiers.
     *
     * @param packId the identifier of the resource pack containing the entity.
     * @param pieceId the identifier of the piece within the pack.
     * @return the {@link AbstractEntityDefinition} corresponding to the specified IDs.
     */
    AbstractEntityDefinition getDefinition(String packId, String pieceId);

    /**
     * Retrieves a list of all unique piece identifiers available in the registry, across all packs.
     *
     * @return a {@link List} of strings representing the unique IDs of all pieces.
     */
    List<String> getAllIds();
}
