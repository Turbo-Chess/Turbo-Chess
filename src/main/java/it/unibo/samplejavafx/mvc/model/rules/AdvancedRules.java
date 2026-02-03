package it.unibo.samplejavafx.mvc.model.rules;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * Placeholder.
 */
public interface AdvancedRules {

    /*static boolean check() {

    }*/

    /*
    static boolean checkmate() {

    }*/

    /**
     * placeholder.
     * 
     * @param cb chessboard of the current game
     * @param currentColor color of the player
     * @return placeholder.
     */
    static boolean draw(final ChessBoard cb, final PlayerColor currentColor) {
        final Set<Optional<Entity>> set = getPiecesOfColor(cb, currentColor);
        final Set<Point2D> container = new HashSet<>();

        for (final Optional<Entity> piece : set) {
            if (piece.get().asMoveable().isPresent()) { // if the piece is a moveable we can use its movement methods
                container.addAll(piece.get().asMoveable().get().getValidMoves(cb.getPosByEntity(piece.get()), cb).stream()
                        .collect(Collectors.toSet()));
            }
        }

        return container.isEmpty();
    }

    /**
     * placeholder.
     * 
     * @param cb chessboard of the current game
     * @param currentColor color of the player
     * @return a value of the CastleCondition enum, describing which castles are possible.
     */
    static CastleCondition castle(final ChessBoard cb, final PlayerColor currentColor) {
        final Point2D kingPos;
        if(currentColor == PlayerColor.WHITE) {
            kingPos = new Point2D(4, 7);
        } else {
            kingPos = new Point2D(4, 0);
        }

        if (cb.getEntity(kingPos).get().asMoveable().isPresent()) {
            var piece = (Piece) cb.getEntity(kingPos).get().asMoveable().get();
            if (piece.getType() == PieceType.KING && !piece.hasMoved()) {
                if (hasNotMoved(cb, new Point2D(1, kingPos.y())) && hasNotMoved(cb, new Point2D(7, kingPos.y()))) {
                    return CastleCondition.CASTLE_BOTH;
                } else if (hasNotMoved(cb, new Point2D(1, kingPos.y()))) {
                    return CastleCondition.CASTLE_LEFT;
                } else if (hasNotMoved(cb, new Point2D(7, kingPos.y()))) {
                    return CastleCondition.CASTLE_RIGHT;
                } else {
                    return CastleCondition.NO_CASTLE;
                }
            }
        }
        return CastleCondition.NO_CASTLE; // cell is empty or king has moved
    }

    /**
     * placeholder.
     * 
     * @param cb
     * @param pos
     * @return placeholder.
     */
    private static boolean hasNotMoved(final ChessBoard cb, final Point2D pos) {
        if(cb.getEntity(pos).get().asMoveable().isPresent()) {
            var piece = (Piece) cb.getEntity(pos).get().asMoveable().get();
            if(!piece.hasMoved()) {
                return true;
            }
        }
        return false; // cell is empty or piece has moved
    }

    /**
     * placeholder.
     */
    public enum CastleCondition {
        NO_CASTLE,
        CASTLE_LEFT,
        CASTLE_RIGHT,
        CASTLE_BOTH
    }

    /**
     * placeholder.
     * 
     * @param cb
     * @param currentColor
     * @param target
     * @return placeholder.
     */
    private static boolean scan(final ChessBoard cb, final PlayerColor currentColor, final Point2D target) {
        final Set<Optional<Entity>> set = getPiecesOfColor(cb, currentColor);

        for (Optional<Entity> ent : set) {
            //if(cb.getEntity(piece).get() instanceof Moveable moveable) {
            if (ent.get().asMoveable().isPresent()) { // if the piece is a moveable we can use its movement methods
                return ent.get().asMoveable().get().getValidMoves(cb.getPosByEntity(ent.get()), cb).stream()
                        .collect(Collectors.toSet()).contains(target);
            }
        }
        return false; // otherwise
    }

    /**
     * placeholder.
     * 
     * @param cb chessboard of the current game
     * @param currentColor color of the player
     * @return a set containing all pieces of a certain color encapsulated in Optionals
     */
    private static Set<Optional<Entity>> getPiecesOfColor(final ChessBoard cb, final PlayerColor currentColor) {
        Set<Optional<Entity>> container = new HashSet<>();
        Set<Point2D> set = cb.getCells().stream()
                .filter(pos -> !cb.isFree(pos))
                .filter(pos -> !(cb.getEntity(pos).get().getPlayerColor() == currentColor))
                .collect(Collectors.toSet());
        for (Point2D pos : set) {
            container.add(cb.getEntity(pos));
        }
        return container;
    }
}

