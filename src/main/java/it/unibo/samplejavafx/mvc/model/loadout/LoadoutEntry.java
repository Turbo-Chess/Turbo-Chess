package it.unibo.samplejavafx.mvc.model.loadout;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * placeholder.
 *
 * @param position placeholder.
 * @param packId placeholder.
 * @param pieceId placeholder.
 */
public record LoadoutEntry(
    @JsonProperty("position") Point2D position,
    @JsonProperty("packId") String packId,
    @JsonProperty("pieceId") String pieceId
) { }
