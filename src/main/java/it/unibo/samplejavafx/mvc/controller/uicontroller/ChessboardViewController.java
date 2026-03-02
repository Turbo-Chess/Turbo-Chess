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

    /**
     * placeholder.
     *
     * @param start placeholder.
     * @param end placeholder.
     */
    void highlightMovement(Point2D start, Point2D end);

    /**
     * placeholder.
     *
     * @param start placeholder.
     * @param end placeholder.
     */
    void highlightEat(Point2D start, Point2D end);
}
