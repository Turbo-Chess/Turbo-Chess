package it.unibo.samplejavafx.mvc.model.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * Utility class for methods involving checks and condition for pieces on the board.
 */
public final class RulesUtils {
    /**
     * Utility method that checks if a certain piece hasn't moved at all.
     * 
     * @param cb chessboard of the current game.
     * @param pos position of the aimed piece.
     * @return {@code true} if the piece has not moved, {@code false} otherwise.
     */
    public static boolean hasNotMoved(final ChessBoard cb, final Point2D pos) {
        if (cb.getEntity(pos).get().asMoveable().isPresent()) {
            final var piece = (Piece) cb.getEntity(pos).get().asMoveable().get();
            return !piece.hasMoved();
        }
        return false; // cell is empty or piece has moved
    }

    /**
     * Utility method to scan a certain cell and see if it's under attack by any piece owned by the opposing player.
     * 
     * @param cb chessboard of the current game.
     * @param currentColor color of the enemy player.
     * @param target cell we want to check.
     * @return an {@link Optional} containing the attacking piece if there is any, returns an empty {@link Optional} otherwise.
     */
    public static Optional<Piece> underAttack(final ChessBoard cb, final PlayerColor currentColor, final Point2D target) {
        final BiMap<Point2D, Entity> cells = HashBiMap.create(cb.getBoard());
        cells.remove(target);
        final ChessBoard board = new ChessBoardImpl(cells);
        final Set<Optional<Entity>> set = getPiecesOfColor(board, currentColor);

        for (final Optional<Entity> ent : set) {
            //if(cb.getEntity(piece).get() instanceof Moveable moveable) {
            if (ent.get().asMoveable().isPresent()
                    && new HashSet<>(ent.get().asMoveable().get()
                    .getValidMoves(board.getPosByEntity(ent.get()), board)).contains(target)) {
                // if the piece is a moveable we can use its movement methods
                return Optional.of((Piece) ent.get());
            }
        }
        return Optional.empty(); // otherwise
    }

    /**
     * Utility method to calculate all the possible moves for the king.
     * 
     * @param kingCells cells the king could normally move to.
     * @param cb chessboard of the current game.
     * @param currentColor color of the current player
     * @return an unmodifiable List of {@link Point2D} containing all safe king moves.
     */
    public static List<Point2D> kingPossibleMoves(final List<Point2D> kingCells,
            final ChessBoard cb, final PlayerColor currentColor) {
        final List<Point2D> possibleMoves = new LinkedList<>();
        for (final Point2D cell : kingCells) {
            if (underAttack(cb, swapColor(currentColor), cell).isEmpty()) {
                possibleMoves.add(cell);
            }
        }
        return Collections.unmodifiableList(possibleMoves);
    }

    /**
     * Utility method to get all the pieces on the board of a specific color.
     * 
     * @param cb chessboard of the current game.
     * @param currentColor color of the current player.
     * @return an unmodifiable Set containing all pieces of a certain color, encapsulated in Optionals.
     */
    public static Set<Optional<Entity>> getPiecesOfColor(final ChessBoard cb, final PlayerColor currentColor) {
        final Set<Optional<Entity>> container = new HashSet<>();
        final Set<Point2D> set = cb.getBoard().keySet().stream()
                .filter(pos -> !cb.isFree(pos))
                .filter(pos -> {
                    final Optional<Entity> e = cb.getEntity(pos);
                    return e.isPresent() && e.get().getPlayerColor() == currentColor;
                })
                .collect(Collectors.toSet());

        for (final Point2D pos : set) {
            container.add(cb.getEntity(pos));
        }
        return Collections.unmodifiableSet(container);
    }

    /**
     * Utility method that returns the corresponding king of the current player.
     * 
     * @param cb chessboard of the current game.
     * @param color color of the current player.
     * @return an {@link Optional} containing the king of the given color, an empty otherwise.
     */
    public static Optional<Piece> getKing(final ChessBoard cb, final PlayerColor color) {
        return cb.getBoard().values().stream()
                .filter(e -> e.asMoveable().isPresent())
                .map(e -> (Piece) e)
                .filter(p -> p.getPlayerColor() == color && p.getType() == PieceType.KING)
                .findFirst();
    }

    /**
     * Utility method to swap the player color.
     * 
     * @param currentColor color of the current player.
     * @return the other {@link PlayerColor}.
     */
    public static PlayerColor swapColor(final PlayerColor currentColor) {
        return (currentColor == PlayerColor.WHITE) ? PlayerColor.BLACK : PlayerColor.WHITE;
    }
}
