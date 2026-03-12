package it.unibo.samplejavafx.mvc.model.handler;

import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl.MoveType;

/**
 * Placeholder.
 */
public interface TurnHandler {

    /**
     * placeholder.
     *
     * @param moveAction placeholder.
     * @param target placeholder.
     * @return placeholder.
     */
    boolean executeTurn(MoveType moveAction, Point2D target);

    /**
     * Sets the current turn number.
     *
     * @param turn the new turn number.
     */
    void setTurn(int turn);

    /**
     * Sets the current player color.
     *
     * @param color the new player color.
     */
    void setPlayerColor(PlayerColor color);

    /**
     * Sets the game state to checkmate.
     */
    void surrender();

    /**
     * Getter for the pawn's position that is undergoing promotion.
     * 
     * @return the {@link Point2D} position.
     */
    Point2D getPromotingPawnPos();
}
