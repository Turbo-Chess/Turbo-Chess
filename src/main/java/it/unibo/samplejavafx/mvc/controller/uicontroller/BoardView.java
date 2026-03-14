package it.unibo.samplejavafx.mvc.controller.uicontroller;

import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.Set;

public interface BoardView {
   void showMovementCells(Set<Point2D> cellsToShow);
   void hideMovementCells(Set<Point2D> cellsToHide);
}
