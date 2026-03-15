package it.unibo.samplejavafx.mvc.controller.uicontroller;

import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.Set;

/**
 * Interface defining the view for the chessboard.
 */
public interface BoardView {

    /**
     * Shows the cells where a piece can verify a move.
     *
     * @param cellsToShow the {@link Set} of cells to highlight.
     */
    void showMovementCells(Set<Point2D> cellsToShow);

   /**
    * Hides the cells where a piece can verify a move.
    *
    * @param cellsToHide the {@link Set} of cells to hide.
    */
   void hideMovementCells(Set<Point2D> cellsToHide);
}
