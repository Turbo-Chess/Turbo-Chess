package it.unibo.samplejavafx.mvc.model.handler;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl.MoveType;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.rules.CastleCondition;

/**
 * The {@link TurnHandlerContext} acts as an additional interface meant for implementations of TurnHandlers that
 * TurnStates need to gather information from the TurnHandler, this interface ensures that TurnStates don't
 * call methods they shouldn't have access to by exposing only the needed ones.
 */
public interface TurnHandlerContext {

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
     * Transitions the current {@link TurnState} to a new one.
     * 
     * @param newState the new TurnState.
     */
    void transitionTo(TurnState newState);

    /**
     * Getter for the current color.
     * 
     * @return the current {@link PlayerColor}.
     */
    PlayerColor getCurrentColor();

    /**
     * Getter for the current piece, encapsulated in an {@link Optional}.
     * 
     * @return the an empty {@link Optional} or one containing a {@link Piece}. 
     */
    Optional<Piece> getCurrentPiece();

    /**
     * Getter for the current piece moves.
     * 
     * @return the List containing {@link Point2D}s. 
     */
    List<Point2D> getCurrentMoves();

    /**
     * Getter for the board of the current match.
     * 
     * @return the {@link ChessBoard} of the match.
     */
    ChessBoard getBoard();

    /**
     * Getter for the interposing pieces and their moves.
     * 
     * @return the Map with {@link Piece} as keys and a List of {@link Point2D} as values.
     */
    Map<Piece, List<Point2D>> getInterposing();

    /**
     * Getter for the castling possibilities.
     * 
     * @return a value of the {@link CastleCondition} enum.
     */
    CastleCondition getCastleCon();

    /**
     * Getter for the current turn.
     * 
     * @return the {@code int} of the turn.
     */
    int getTurn();

    /**
     * Setter for the current piece, called by the TurnState implementations.
     * 
     * @param piece the {@link Piece} to set.
     */
    void setCurrentPiece(Piece piece);

    /**
     * Setter for the current piece moves, called by the TurnState implementations.
     * 
     * @param moves the List of {@link Point2D} to set.
     */
    void setPieceMoves(List<Point2D> moves);

    /**
     * Setter for the turn.
     * 
     * @param newTurn the new {@code int} symbolizing the turn.
     */
    void setTurn(int newTurn);

    /**
     * Setter for the promotionHolder of the TurnHandler.
     * 
     * @return the {@link Optional} where we want to save a promoting piece.
     */
    void passOnPromotion(Optional<Piece> pawn);

    /**
     * Unsets the current piece and all related fields.
     */
    void unsetCurrentPiece();
}
