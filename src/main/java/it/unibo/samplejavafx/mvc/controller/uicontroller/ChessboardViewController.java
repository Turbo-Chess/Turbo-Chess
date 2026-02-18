package it.unibo.samplejavafx.mvc.controller.uicontroller;

import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.Set;

/**
 * placeholder.
 */
public interface ChessboardViewController {
    /**
     * placeholder.
     *
     * @param cellsToShow placeholder.
     */
    void showMovementCells(Set<Point2D> cellsToShow);

    /**
     * placeholder.
     *
     * @param cellsToHide placeholder.
     */
    void hideMovementCells(Set<Point2D> cellsToHide);
}
