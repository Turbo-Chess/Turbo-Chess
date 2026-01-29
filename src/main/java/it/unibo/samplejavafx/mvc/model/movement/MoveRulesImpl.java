package it.unibo.samplejavafx.mvc.model.movement;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * placeholder.
 */
public class MoveRulesImpl implements MoveRules {
    private final Point2D direction;
    private final MoveType restriction;
    private final MovementStrategy moveStrategy;

    /**
     * placeholder.
     *
     * @param direction placeholder.
     * @param restriction placeholder.
     * @param moveStrategy placeholder.
     */
    public MoveRulesImpl(final Point2D direction, final MoveType restriction, final MovementStrategy moveStrategy) {
        this.direction = direction;
        this.restriction = restriction;
        this.moveStrategy = moveStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Point2D> getValidMoves(final Point2D start, final ChessBoard board, final PlayerColor playerColor) {
        final List<Point2D> tempResult = moveStrategy.calculateMoves(
                start, playerColor == PlayerColor.WHITE ? direction.invertY() : direction, board
        );
        return switch (restriction) {
            case MOVE_ONLY -> moveOnlyFilter(board, tempResult);
            case EAT_ONLY -> eatOnlyFilter(board, tempResult, playerColor);
            case MOVE_AND_EAT -> moveAndEatFilter(board, tempResult, playerColor);
        };

        // This can't be reached because all rules MUST be one of the 3 in the MoveType enum
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
}

/*MOVE_ONLY(ChessBoard::isFree),
 EAT_ONLY((board, pos) -> !board.isFree(pos) && (board.getEntity().)),
 MOVE_AND_EAT((board, pos) -> true);

 private final BiPredicate<ChessBoard, Point2D> filterFunc;

 MoveType(final BiPredicate<ChessBoard, Point2D> filterFunc) {
 this.filterFunc = filterFunc;
 }
}*/
