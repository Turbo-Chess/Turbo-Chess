package it.unibo.samplejavafx.mvc.model.movement;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;


import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class MoveRulesImpl implements MoveRules {
    private final Point2D direction;
    private final MoveType restriction;
    private final MovementStrategy moveStrategy;

    public MoveRulesImpl(final Point2D direction, final MoveType restriction, final MovementStrategy moveStrategy) {
        this.direction = direction;
        this.restriction = restriction;
        this.moveStrategy = moveStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Point2D> getValidMoves(final Point2D start, final ChessBoard board, final boolean isWhite) {
        List<Point2D> tempResult = moveStrategy.calculateMoves(start, this.direction, board);
        return tempResult.stream()
                .filter(pos -> this.restriction.filterFunc.test(board, pos))
                .toList();
    }


    public Point2D getDirection() {
        return direction;
    }

    public MoveType getRestriction() {
        return restriction;
    }

    public MovementStrategy getMoveStrategy() {
        return moveStrategy;
    }

    public enum MoveType {
        MOVE_ONLY(ChessBoard::isFree),
        EAT_ONLY((board, pos) -> !board.isFree(pos)),
        MOVE_AND_EAT((board, pos) -> true);

        private final BiPredicate<ChessBoard, Point2D> filterFunc;

        MoveType(final BiPredicate<ChessBoard, Point2D> filterFunc) {
            this.filterFunc = filterFunc;
        }
    }
}
