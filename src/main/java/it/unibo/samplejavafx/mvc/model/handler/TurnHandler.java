package it.unibo.samplejavafx.mvc.model.handler;

import java.util.List;

import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl.MoveType;

/**
 * The {@link TurnHandler} interface expresses all base methods that are needed to handle events and moves during
 * a game of chess, keeping count of the turns and swapping players' turn accordingly. 
 */
public interface TurnHandler {

    /**
     * Handles all the actions of the players during his turn.
     * 
     * @param pos the {@link Point2D} of the clicked cell.
     * @return a list of {@link Point2D} of all possible moves for the View side.
     */
    List<Point2D> thinking(Point2D pos);

    /**
     * Executes the turn, finalizing the chosen move and rechecking all rules.
     *
     * @param moveAction the {@link MoveType} of the chosen move.
     * @param target the {@link Point2D} position of the chosen move.
     * @return   {@code true} if the turn has ended successfully, 
     *           {@code false} if the game ends with {@code CHECKMATE} or {@code DRAW}.
     */
    boolean executeTurn(MoveType moveAction, Point2D target);

    /**
     * Sets the current turn number.
     *
     * @param turn the new turn number.
     */
    void setStartTurn(int turn);

    /**
     * Sets the current player color.
     *
     * @param color the new player color.
     */
    void setStartPlayerColor(PlayerColor color);

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
