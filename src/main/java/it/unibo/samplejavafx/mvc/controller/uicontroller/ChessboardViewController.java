package it.unibo.samplejavafx.mvc.controller.uicontroller;

import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.Set;

public interface ChessboardViewController {
    public void showMovementCells(final Set<Point2D> cellsToShow);

    public void hideMovementCells(final Set<Point2D> cellsToHide);
}
