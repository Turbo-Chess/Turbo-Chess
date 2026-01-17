package it.unibo.samplejavafx.mvc.model.movement;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import lombok.Getter;


import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@Getter
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
        List<Point2D> tempResult = moveStrategy.calculateMoves(start, isWhite ? direction.invertY() : direction, board);
        return switch (restriction) {
            case MOVE_ONLY -> moveOnlyFilter(board, tempResult);
            case EAT_ONLY -> eatOnlyFilter(board, tempResult, isWhite);
            case MOVE_AND_EAT -> moveAndEatFilter(board, tempResult, isWhite);
        };

        // This can't be reached because all rules MUST be one of the 3 in the MoveType enum
    }

    private List<Point2D> moveOnlyFilter(ChessBoard board, final List<Point2D> tempResult) {
        return tempResult.stream()
                .filter(board::isFree)
                .toList();
    }

    private List<Point2D> eatOnlyFilter(ChessBoard board, final List<Point2D> tempResult, boolean isWhite) {
        return tempResult.stream()
                .filter(pos -> !board.isFree(pos))
                .filter(pos -> !board.getEntity(pos).get().isWhite() == isWhite)
                .toList();
    }

    private List<Point2D> moveAndEatFilter(ChessBoard board, final List<Point2D> tempResult, boolean isWhite) {
        for (var pos : tempResult) {
            final Optional<Entity> pieceInNewPos = board.getEntity(pos);
            if (pieceInNewPos.isPresent() && pieceInNewPos.get().isWhite() == isWhite) {
                tempResult.remove(pos);
            }
        }

        return tempResult;
    }


    public enum MoveType {
        MOVE_ONLY,
        EAT_ONLY,
        MOVE_AND_EAT;
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