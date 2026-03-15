package it.unibo.samplejavafx.mvc.model.chessboard.boardfactory;

import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.AbstractEntityDefinition;

import java.util.List;
import java.util.Map;

/**
 * Interface defining a registry for entity definitions.
 */
public interface DefinitionRegistry {
    /**
     * @return a {@link List} of resource pack identifiers.
     */
    List<String> getResPackIds();

    /**
     * @param packId the identifier of the resource pack.
     * @return a {@link Map} of piece identifiers to their definitions.
     */
    Map<String, AbstractEntityDefinition> getPackData(String packId);

    /**
     * @return a {@link List} of all entity definitions.
     */
    List<AbstractEntityDefinition> getAllDefinitions();

    /**
     * @param packId the identifier of the resource pack.
     * @param pieceId the identifier of the piece.
     * @return the {@link AbstractEntityDefinition} for the specified piece.
     */
    AbstractEntityDefinition getDefinition(String packId, String pieceId);

    /**
     * @return a {@link List} of all piece identifiers.
     */
    List<String> getAllIds();
}
