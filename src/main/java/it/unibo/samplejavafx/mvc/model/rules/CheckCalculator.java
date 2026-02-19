package it.unibo.samplejavafx.mvc.model.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.Entity;

import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * Utility class to calculate check situations and identify interposing pieces.
 */
public final class CheckCalculator {

    private CheckCalculator() {
        // Utility class
    }

    /**
     * Identifies all friendly pieces that can validly interpose between the king and the attacker(s).
     * 
     * @param cb the current chessboard.
     * @param kingColor the color of the king under check.
     * @return a list of pieces that can block the check, sorted by weight (ascending).
     */
    public static Map<Piece, List<Point2D>> getInterposingPieces(final ChessBoard cb, final PlayerColor kingColor) {
        final List<Piece> attackers = getAttackers(cb, kingColor);

        final Piece attacker = attackers.get(0);
        final Optional<Piece> kingOpt = AdvancedRules.getKing(cb, kingColor);
        if (kingOpt.isEmpty()) {
            return Collections.emptyMap();
        }
        final Piece king = kingOpt.get();
        final Point2D kingPos = cb.getPosByEntity(king);
        final Point2D attackerPos = cb.getPosByEntity(attacker);

        // Calculate path between King and Attacker
        final List<Point2D> path = getLineOfSight(kingPos, attackerPos);

        if (path.isEmpty()) {
            return Collections.emptyMap(); // Check for non-sliding pieces
        }

        final Map<Piece, List<Point2D>> candidates = new HashMap<>();
        final List<Point2D> holder = new LinkedList<>();
        final Set<Optional<Entity>> friends = AdvancedRules.getPiecesOfColor(cb, kingColor);

        for (final Optional<Entity> friendOpt : friends) {
            if (friendOpt.isPresent() && friendOpt.get().asMoveable().isPresent()) {
                final Piece friend = (Piece) friendOpt.get();
                if (friend.getType() == PieceType.KING) {
                    continue; // King cannot block check for itself
                }

                final Point2D startPos = cb.getPosByEntity(friend);
                final List<Point2D> moves = friend.getValidMoves(startPos, cb);

                // Check if piece can move to any cell in the path
                for (final Point2D cell : path) {
                    if (moves.contains(cell) && isMoveSafe(cb, friend, startPos, cell, kingColor)) {
                        holder.add(cell);
                        break;
                    }
                }
                if (!holder.isEmpty()) {
                    candidates.put(friend, List.copyOf(holder));
                }
                holder.clear();
            }
        }
        return candidates;
    }

    /**
     * Utility method to calculate the cells strictly between two points.
     * 
     * @param p1 first point.
     * @param p2 second point.
     * @return a list of all cells between the two points, empty if not adjacent or in a straight line.
     */
    public static List<Point2D> getLineOfSight(final Point2D p1, final Point2D p2) {
        final List<Point2D> path = new ArrayList<>();
        final int dx = p2.x() - p1.x();
        final int dy = p2.y() - p1.y();

        if (dx == 0 && dy == 0) {
            return path;
        }

        // Check if horizontal, vertical, or diagonal
        final boolean horizontal = dy == 0;
        final boolean vertical = dx == 0;
        final boolean diagonal = Math.abs(dx) == Math.abs(dy);

        if (!horizontal && !vertical && !diagonal) {
            return path; // Not a straight line (e.g. Knight)
        }

        final int steps = Math.max(Math.abs(dx), Math.abs(dy));
        final int stepX = Integer.signum(dx);
        final int stepY = Integer.signum(dy);

        // Start from initial cell, stop before last cell
        for (int i = 0; i < steps; i++) {
            path.add(new Point2D(p1.x() + (stepX * i), p1.y() + (stepY * i)));
        }

        return path;
    }

    /**
     * Utility method that gets all enemy pieces attacking the king.
     * 
     * @param cb chessboard of the current game.
     * @param kingColor color associated with the king given in input.
     * @return a List containing all pieces that are attacking the king.
     */
    public static List<Piece> getAttackers(final ChessBoard cb, final PlayerColor kingColor) {
        final List<Piece> attackers = new ArrayList<>();
        final Optional<Piece> kingOpt = AdvancedRules.getKing(cb, kingColor);
        if (kingOpt.isEmpty()) {
            return attackers;
        }
        final Point2D kingPos = cb.getPosByEntity(kingOpt.get());
        final PlayerColor enemyColor = AdvancedRules.swapColor(kingColor);

        final Set<Optional<Entity>> enemies = AdvancedRules.getPiecesOfColor(cb, enemyColor);

        for (final Optional<Entity> enemyOpt : enemies) {
            if (enemyOpt.isPresent() && enemyOpt.get().asMoveable().isPresent()) {
                final Piece enemy = (Piece) enemyOpt.get();
                final Point2D enemyPos = cb.getPosByEntity(enemy);
                if (enemy.getValidMoves(enemyPos, cb).contains(kingPos)) {
                    attackers.add(enemy);
                }
            }
        }
        return attackers;
    }

    /**
     * Utility method that simulates a move, to see if it leaves the king in check.
     * 
     * @param cb chessboard of the current game.
     * @param piece piece whose move is being simulated.
     * @param from the beginning point for the move.
     * @param to the ending point for the move.
     * @param kingColor the color of the king given in input.
     * @return {@code true} if the move doesn't leave the king in check, {@code false} otherwise.
     */
    public static boolean isMoveSafe(final ChessBoard cb, final Piece piece, final Point2D from,
                                    final Point2D to, final PlayerColor kingColor) {

        final ChessBoard tempBoard = new ChessBoardImpl(cb.getBoard());
        if (tempBoard.isFree(to)) {
            tempBoard.move(from, to);
        } else {
            tempBoard.eat(from, to);
        }

        return getAttackers(tempBoard, kingColor).isEmpty();
    }
}
