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
 * placeholder.
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
     * placeholder.
     *
     * @param direction placeholder.
     * @param restriction placeholder.
     * @param moveStrategy placeholder.
     * @param hasMoved placeholder.
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

    /**
     *  placeholder.
     *
     * @param board  placeholder.
     * @param tempResult placeholder.
     * @return placeholder.
     */
    private List<Point2D> moveOnlyFilter(final ChessBoard board, final List<Point2D> tempResult) {
        return tempResult.stream()
                .filter(board::isFree)
                .toList();
    }

    /**
     *  placeholder.
     *
     * @param board placeholder.
     * @param tempResult placeholder.
     * @param playerColor placeholder.
     * @return placeholder.
     */
    private List<Point2D> eatOnlyFilter(final ChessBoard board, final List<Point2D> tempResult, final PlayerColor playerColor) {
        return tempResult.stream()
                .filter(pos -> !board.isFree(pos))
                .filter(pos -> board.getEntity(pos).get().getPlayerColor() != playerColor)
                .toList();
    }

    /**
     *  placeholder.
     *
     * @param board  placeholder.
     * @param tempResult placeholder.
     * @param playerColor placeholder.
     * @return placeholder.
     */
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
     *  placeholder.
     */
    public enum MoveType {
        MOVE_ONLY,
        EAT_ONLY,
        MOVE_AND_EAT
    }

    /**
     * placeholder.
     */
    @Getter
    public enum MoveStrategy {
        JUMPING(new JumpingMovement()),
        SLIDING(new SlidingMovement()),
        STEPPING(new SteppingMovement());

        private final MovementStrategy strategy;

        MoveStrategy(final MovementStrategy strategy) {
            this.strategy = strategy;
        }
    }
}

/*MOVE_ONLY(ChessBoard::isFree),
 EAT_ONLY((board, pos) -> !board.isFree(pos) && (board.getEntity().)),
 MOVE_AND_EAT((board, pos) -> true);

 private final BiPredicate<ChessBoard, Point2D> filterFunc;

 MoveType(final BiPredicate<ChessBoard, Point2D> filterFunc) {
 this.filterFunc = filterFunc;
 }
}*/
