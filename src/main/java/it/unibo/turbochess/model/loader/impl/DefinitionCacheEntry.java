package it.unibo.turbochess.model.loader.impl;

import it.unibo.turbochess.model.chessboard.boardfactory.api.BoardFactory;
import it.unibo.turbochess.model.entity.definition.AbstractEntityDefinition;

/**
 * Represents a cached entry for an entity definition.
 * This is a pure DTO passed to the {@link BoardFactory} that manages the cached entity definitions.
 *
 * @param packId the identifier of the resource pack.
 * @param pieceId the identifier of the piece.
 * @param abstractEntityDefinition the definition of the entity.
 */
public record DefinitionCacheEntry(String packId, String pieceId, AbstractEntityDefinition abstractEntityDefinition) {
}
