package it.unibo.samplejavafx.mvc.model.handler;

import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * Placeholder.
 */
@FunctionalInterface
public interface TurnHandler {

    /**
     *  placeholder.
     *
     * 
     */
    public boolean executeTurn(final MoveRulesImpl.MoveType moveAction, final Point2D target);
}
