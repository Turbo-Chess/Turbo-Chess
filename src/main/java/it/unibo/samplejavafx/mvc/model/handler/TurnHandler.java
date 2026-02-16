package it.unibo.samplejavafx.mvc.model.handler;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl.MoveType;
import javafx.geometry.Point2D;

/**
 * Placeholder.
 */
public interface TurnHandler {

    /**
     *  placeholder.
     *
     * 
     */
    boolean executeTurn(final MoveType moveAction, final Point2D target);
}
