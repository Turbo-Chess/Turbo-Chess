package it.unibo.turbochess.controller.uicontroller.api;

import it.unibo.turbochess.model.point2d.Point2D;

import java.util.Set;

/**
 * Interface defining the view for the chessboard.
 * Useful to decouple only the minimal subset of functions to gave access to other objects.
 */
public interface BoardView {

    /**
     * Highlights a set of cells on the board to indicate valid movement destinations or other special zones.
     *
     * @param cellsToShow A {@link Set} of {@link Point2D} coordinates to highlight.
     */
    void showMovementCells(Set<Point2D> cellsToShow);

    /**
     * Removes the highlight from a set of cells, typically called when deselecting a piece or completing a move.
     *
     * @param cellsToHide A {@link Set} of {@link Point2D} coordinates to un-highlight.
     */
    void hideMovementCells(Set<Point2D> cellsToHide);
}
