package it.unibo.samplejavafx.mvc.model.rules;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.handler.GameState;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * COntains all the complex core rules of chess.
 */
public final class AdvancedRules {
    private static final Point2D WKING_POS = new Point2D(4, 7);
    private static final Point2D BKING_POS = new Point2D(4, 0);
    private static final Point2D TOWERS_X = new Point2D(0, 7);
    private static final int CHECK = 1;
    private static final int DOUBLE_CHECK = 2;

    private AdvancedRules() {

    }

    /**
     * Method that checks if the king of the current player is under attack.
     * 
     * @param cb chessboard of the current game.
     * @param currentColor color of the player.
     * @return {@code true} if the king is under attack, {@code false} otherwise.
     */
    public static GameState check(final ChessBoard cb, final PlayerColor currentColor) {
        final int totalAttackers = CheckCalculator.getAttackers(cb, currentColor).size();
        if (totalAttackers == CHECK) {
            return GameState.CHECK;
        } else if (totalAttackers == DOUBLE_CHECK) {
            return GameState.DOUBLE_CHECK;
        } else {
            return GameState.NORMAL;
        }
    }

    /*public static boolean secondaryCheck(final ChessBoard cb, final PlayerColor currentColor, final Piece pastAttacker) {
        final BiMap<Point2D, Entity> cells = HashBiMap.create(cb.getBoard());
        cells.remove(cb.getPosByEntity(pastAttacker));
        final ChessBoard board = new ChessBoardImpl(cells);
        final Optional<Piece> king = getKing(cb, swapColor(currentColor));
        if (!king.isEmpty()) {
            if (!underAttack(board, currentColor, board.getPosByEntity(king.get())).isEmpty()) {
                SEC_ATTACKING_PIECE = underAttack(board, currentColor, board.getPosByEntity(king.get())).get();
                return true;
            }
        }
        return false; // otherwise
    }*/

    /**
     * Method that checks if the king of the current player is under attack and can't defend himself, 
     * therefore ending the game in a checkmate.
     * The method responds in different ways based on the current game-state.
     * 
     * @param cb chessboard of the current game.
     * @param currentColor color of the player.
     * @param state current game-state.
     * @param interposingMoves empty map that will contain the result of getInterposingPieces().
     * @return {@code true} if the king is under attack and can't defend himself, {@code false} otherwise.
     */
    static boolean checkmate(final ChessBoard cb, final PlayerColor currentColor, final GameState state,
                             final Map<Piece, List<Point2D>> interposingMoves) {
        final Optional<Piece> king = getKing(cb, currentColor);
        final List<Point2D> kingCells = king.get().getValidMoves(cb.getPosByEntity(king.get()), cb);
        final List<Point2D> possibleMoves = new LinkedList<>();

        for (final Point2D cell : kingCells) {
            if (underAttack(cb, swapColor(currentColor), cell).isEmpty()) {
                possibleMoves.add(cell);
            }
        }
        if (!king.isEmpty()) {
            switch (state) {
                case CHECK:
                    if (possibleMoves.isEmpty()) {
                        return CheckCalculator.getInterposingPieces(cb, currentColor).isEmpty();
                        // ADD Map<Piece, List<Point2D>> to get moves
                    }
                    break;
                case DOUBLE_CHECK:
                    if (possibleMoves.isEmpty()) {
                        return true;
                    }
                    break;
                default:
                    return false;
            }
        }
        return false;
    }

    /**
     * Method that checks if the current player has any legal move left, 
     * therefore ending the game in a draw.
     * 
     * @param cb chessboard of the current game.
     * @param currentColor color of the player.
     * @return {@code true} if the current player has no legal moves left, {@code false} otherwise.
     */
    public static boolean draw(final ChessBoard cb, final PlayerColor currentColor) {
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
     * Method that returns which castling options are possible to enact.
     * 
     * @param cb chessboard of the current game.
     * @param currentColor color of the player.
     * @return a value of the {@code CastleCondition} enum, describing which castles are possible.
     */
    public static CastleCondition castle(final ChessBoard cb, final PlayerColor currentColor) {
        final Point2D kingPos;
        if (currentColor == PlayerColor.WHITE) {
            kingPos = WKING_POS;
        } else {
            kingPos = BKING_POS;
        }

        if (cb.getEntity(kingPos).get().asMoveable().isPresent()) {
            final var piece = (Piece) cb.getEntity(kingPos).get().asMoveable().get();
            if (piece.getType() == PieceType.KING && !piece.hasMoved()) {
                if (hasNotMoved(cb, new Point2D(TOWERS_X.x(), kingPos.y())) 
                        && hasNotMoved(cb, new Point2D(TOWERS_X.y(), kingPos.y()))) {
                    return CastleCondition.CASTLE_BOTH;
                } else if (hasNotMoved(cb, new Point2D(TOWERS_X.x(), kingPos.y()))) {
                    return CastleCondition.CASTLE_LEFT;
                } else if (hasNotMoved(cb, new Point2D(TOWERS_X.y(), kingPos.y()))) {
                    return CastleCondition.CASTLE_RIGHT;
                } else {
                    return CastleCondition.NO_CASTLE;
                }
            }
        }
        return CastleCondition.NO_CASTLE; // cell is empty or king has moved
    }

    /**
     * Utility method that checks if a certain piece hasn't moved at all.
     * 
     * @param cb chessboard of the current game.
     * @param pos position of the aimed piece.
     * @return {@code true} if the piece has not moved, {@code false} otherwise.
     */
    private static boolean hasNotMoved(final ChessBoard cb, final Point2D pos) {
        if (cb.getEntity(pos).get().asMoveable().isPresent()) {
            final var piece = (Piece) cb.getEntity(pos).get().asMoveable().get();
            if (!piece.hasMoved()) {
                return true;
            }
        }
        return false; // cell is empty or piece has moved
    }

    /**
     * An enum containing all the possible scenarios regarding the ability to castle during a game of chess.
     */
    enum CastleCondition {
        NO_CASTLE,
        CASTLE_LEFT,
        CASTLE_RIGHT,
        CASTLE_BOTH
    }

    /**
     * Utility method to scan a certain cell and see if it's under attack by any piece owned by the opposing player.
     * 
     * @param cb chessboard of the current game.
     * @param currentColor color of the current player.
     * @param target the cell we want to check.
     * @return an {@code Optional} containing the attacking piece if there is any, returns an empty Optional otherwise.
     */
    private static Optional<Piece> underAttack(final ChessBoard cb, final PlayerColor currentColor, final Point2D target) {
        final BiMap<Point2D, Entity> cells = HashBiMap.create(cb.getBoard());
        cells.remove(target);
        final ChessBoard board = new ChessBoardImpl(cells);
        final Set<Optional<Entity>> set = getPiecesOfColor(board, currentColor);

        for (final Optional<Entity> ent : set) {
            //if(cb.getEntity(piece).get() instanceof Moveable moveable) {
            if (ent.get().asMoveable().isPresent() 
                && ent.get().asMoveable().get().getValidMoves(board.getPosByEntity(ent.get()), board).stream()
                        .collect(Collectors.toSet()).contains(target)) { 
                // if the piece is a moveable we can use its movement methods
                return Optional.of((Piece) ent.get());
            }
        }
        return Optional.empty(); // otherwise
    }

    /**
     * Utility method to get all the pieces on the board of a specific color.
     * 
     * @param cb chessboard of the current game.
     * @param currentColor color of the player.
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
     * @return an {@code Optional} containing the king of the given color, an empty otherwise.
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
     * @return the other PlayerColor.
     */
    public static PlayerColor swapColor(final PlayerColor currentColor) {
        return (currentColor == PlayerColor.WHITE) ? PlayerColor.BLACK : PlayerColor.WHITE;
    }
}
