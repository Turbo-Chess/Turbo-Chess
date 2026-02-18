package it.unibo.samplejavafx.mvc.model.handler;

import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl.MoveType;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.List;

/**
 * Placeholder.
 */
public interface TurnHandler {

    /**
     *  placeholder.
     *
     * @param moveAction placeholder.
     * @param target placeholder.
     * @return placeholder.
     */
    boolean executeTurn(MoveType moveAction, Point2D target);

    /**
     * placeholder.
     *
     * @param pos placeholder.
     * @return placeholder.
     */
    List<Point2D> thinking(Point2D pos);
}
