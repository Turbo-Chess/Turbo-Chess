package it.unibo.samplejavafx.mvc.model.chessboard.boardfactory;

import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.AbstractEntityDefinition;

import java.util.List;
import java.util.Map;

public interface DefinitionRegistry {
    List<String> getResPackIds();
    Map<String, AbstractEntityDefinition> getPackData(String packId);
    List<AbstractEntityDefinition> getAllDefinitions();
    AbstractEntityDefinition getDefinition(String packId, String pieceId);
    List<String> getAllIds();
}
