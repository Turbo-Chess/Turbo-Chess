package it.unibo.turbochess.model.loadout.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.unibo.turbochess.model.point2d.Point2D;

/**
 * Single entry of a {@link Loadout}, describing which piece should appear at a given position.
 *
 * @param position the board coordinate where the piece is placed
 * @param packId the identifier of the resource pack providing the piece definition
 * @param pieceId the identifier of the piece definition within the resource pack
 */
public record LoadoutEntry(
    @JsonProperty("position") Point2D position,
    @JsonProperty("packId") String packId,
    @JsonProperty("pieceId") String pieceId
) { }
