package it.unibo.samplejavafx.mvc.controller.movecontroller;

import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

public interface MoveController {
    void move(Point2D start, Point2D end);

    void eat(Point2D start, Point2D end);
}
