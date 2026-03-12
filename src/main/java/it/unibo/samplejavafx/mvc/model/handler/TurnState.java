package it.unibo.samplejavafx.mvc.model.handler;

import java.util.List;
import java.util.Optional;

import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

public interface TurnState {

    List<Point2D> thinking(Point2D pos);

    void passOnStats(Optional<Piece> promotion);
}
