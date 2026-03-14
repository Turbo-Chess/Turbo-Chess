package it.unibo.samplejavafx.mvc.model.chessboard.boardfactory;

import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.AbstractEntityDefinition;

public record DefinitionCacheEntry(String packId, String pieceId, AbstractEntityDefinition abstractEntityDefinition) {
}
