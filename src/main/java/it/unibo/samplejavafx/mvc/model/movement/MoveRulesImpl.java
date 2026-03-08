package it.unibo.samplejavafx.mvc.model.movement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link MoveRules} that defines how a piece moves.
 * <p>
 * A {@code MoveRulesImpl} encapsulates:
 * - A direction vector.
 * - A movement restriction (e.g., move-only, capture-only).
 * - A strategy (sliding, stepping, or jumping).
 * - A conditional flag checking if the piece has already moved.
 * </p>
 */
@EqualsAndHashCode
@Getter
public class MoveRulesImpl implements MoveRules {
    private final Point2D direction;
    private final MoveType restriction;
    private final MoveStrategy moveStrategy;
    // False: condition not checked
    // True: if the piece has already moved, the rule cannot be applied another time
    private final boolean hasMoved;

    /**
     * Creates a new movement rule.
     *
     * @param direction    the vector defining the direction of movement.
     * @param restriction  the type of move allowed (e.g., capture only, move only).
     * @param moveStrategy the strategy used to calculate the path (e.g., sliding vs stepping).
     * @param hasMoved     if {@code true}, this rule is valid only if the piece has not moved yet
     *                     (e.g., for a Pawn's initial double step).
     */
    @JsonCreator
    public MoveRulesImpl(
            @JsonProperty("direction") final Point2D direction,
            @JsonProperty("restriction") final MoveType restriction,
            @JsonProperty("moveStrategy") final MoveStrategy moveStrategy,
            @JsonProperty("hasMoved") final boolean hasMoved
    ) {
        this.direction = direction;
        this.restriction = restriction;
        this.moveStrategy = moveStrategy;
        this.hasMoved = hasMoved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Point2D> getValidMoves(final Point2D start, final ChessBoard board, final PlayerColor playerColor) {
        // If getValidMoves is called the piece exists on the board
        if (this.hasMoved && board.getEntity(start).get().asMoveable().get().hasMoved()) {
            return List.of();
        }
        final List<Point2D> tempResult = moveStrategy.getStrategy().calculateMoves(
                start, playerColor == PlayerColor.WHITE ? direction.invertY() : direction, board
        );
        return switch (restriction) {
            case MOVE_ONLY -> moveOnlyFilter(board, tempResult);
            case EAT_ONLY -> eatOnlyFilter(board, tempResult, playerColor);
            case MOVE_AND_EAT -> moveAndEatFilter(board, tempResult, playerColor);
        };
    }

    private List<Point2D> moveOnlyFilter(final ChessBoard board, final List<Point2D> tempResult) {
        return tempResult.stream()
                .filter(board::isFree)
                .toList();
    }

    private List<Point2D> eatOnlyFilter(final ChessBoard board, final List<Point2D> tempResult, final PlayerColor playerColor) {
        return tempResult.stream()
                .filter(pos -> !board.isFree(pos))
                .filter(pos -> board.getEntity(pos).get().getPlayerColor() != playerColor)
                .toList();
    }

    private List<Point2D> moveAndEatFilter(
            final ChessBoard board, final List<Point2D> tempResult, final PlayerColor playerColor
    ) {
        final List<Point2D> res = new ArrayList<>(tempResult);
        for (final var pos : tempResult) {
            final Optional<Entity> pieceInNewPos = board.getEntity(pos);
            if (pieceInNewPos.isPresent() && pieceInNewPos.get().getPlayerColor() == playerColor) {
                res.remove(pos);
            }
        }

        return res;
    }

    /**
     * Defines restrictions on the move type.
     */
    public enum MoveType {
        /**
         * The piece can only move to an empty cell (cannot capture).
         */
        MOVE_ONLY,
        /**
         * The piece can only move if it captures an enemy piece.
         */
        EAT_ONLY,
        /**
         * The piece can move to an empty cell or capture an enemy piece.
         */
        MOVE_AND_EAT
    }

    /**
     * Defines the algorithmic strategy for movement generation.
     */
    @Getter
    public enum MoveStrategy {
        /**
         * Jumps directly to the target square (like a Knight).
         */
        JUMPING(new JumpingMovement()),
        /**
         * Slides along a line until blocked (like a Rook or Bishop).
         */
        SLIDING(new SlidingMovement()),
        /**
         * Moves a single step in the direction (like a King).
         */
        STEPPING(new SteppingMovement());

        private final MovementStrategy strategy;

        MoveStrategy(final MovementStrategy strategy) {
            this.strategy = strategy;
        }
    }
}
