package it.unibo.samplejavafx.mvc.model.rules;

import java.util.Optional;
import java.util.Set;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.handler.GameState;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.utils.RulesUtils;

/**
 * Contains all the complex core rules of chess.
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
     * @return  {@link GameState} {@code CHECK} if the king is under attack,
     *          {@code DOUBLE_CHECK} if the king is under attack by two pieces,
     *          {@code NORMAL} otherwise.
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

    /**
     * Method that checks if the king of the current player is under attack and can't defend himself,
     * therefore ending the game in a checkmate.
     * The method responds in different ways based on the current game-state.
     *
     * @param cb chessboard of the current game.
     * @param currentColor color of the player.
     * @param state current game-state.
     * @param interposingPieces empty map that will contain the result of getInterposingPieces().
     * @return  {@link GameState} {@code CHECKMATE} if the king is under attack and can't defend himself,
     *          otherwise returns the same state taken as imput.
     */
    public static GameState checkmate(final ChessBoard cb, final PlayerColor currentColor, final GameState state,
                                    final Map<Piece, List<Point2D>> interposingPieces) {
        final Optional<Piece> king = RulesUtils.getKing(cb, currentColor);
        final List<Point2D> kingCells = king.get().getValidMoves(cb.getPosByEntity(king.get()), cb);
        final List<Point2D> possibleMoves = RulesUtils.kingPossibleMoves(kingCells, cb, currentColor);

        if (!king.isEmpty()) {
            switch (state) {
                case CHECK:
                    if (possibleMoves.isEmpty()) {
                        interposingPieces.putAll(CheckCalculator.getInterposingPieces(cb, currentColor));
                        if (interposingPieces.isEmpty()) {
                            return GameState.CHECKMATE;
                        }
                    }
                    break;
                case DOUBLE_CHECK:
                    if (possibleMoves.isEmpty()) {
                        return GameState.CHECKMATE;
                    }
                default:
                    return state;
            }
        }
        return state;
    }

    /**
     * Method that checks if the current player has any legal move left, 
     * therefore ending the game in a draw.
     * 
     * @param cb chessboard of the current game.
     * @param currentColor color of the player.
     * @param state current state of the ChessMatch.
     * @return  {@link GameState} {@code NORMAL} if the current player has no legal moves left,
     *          otherwise returns the same state taken as imput.
     */
    public static GameState draw(final ChessBoard cb, final PlayerColor currentColor, final GameState state) {
        final Set<Optional<Entity>> set = RulesUtils.getPiecesOfColor(cb, currentColor);
        final List<Point2D> container = new LinkedList<>();

        for (final Optional<Entity> piece : set) {
            if (piece.get().asMoveable().isPresent()) {
                if (piece.get().getType() == PieceType.KING) {
                    container.addAll(RulesUtils.kingPossibleMoves(piece.get().asMoveable().get()
                            .getValidMoves(cb.getPosByEntity(piece.get()), cb), cb, currentColor));
                } else {
                    container.addAll(new HashSet<>(piece.get().asMoveable().get()
                            .getValidMoves(cb.getPosByEntity(piece.get()), cb)));
                }
            }
        }
        if (container.isEmpty()) {
            return GameState.DRAW;
        }
        final List<Entity> holder = cb.getBoard().inverse().keySet().stream()
                .filter(e -> e.getType() != PieceType.POWERUP)
                .toList();
        if (holder.size() == 2) {
            return GameState.DRAW;
        }
        if (holder.size() == 3) {
            final List<Entity> list = holder.stream()
                .filter(e -> e.getType() != PieceType.KING)
                .toList();
            if (list.size() == 1 && list.getFirst().getType() == PieceType.INFERIOR) {
                return GameState.DRAW;
            }
        }
        return state;
    }

    /**
     * Method that returns which castling options are possible to enact.
     * 
     * @param cb chessboard of the current game.
     * @param currentColor color of the player.
     * @return a value of the {@link CastleCondition} enum, describing which castles are possible.
     */
    public static CastleCondition castle(final ChessBoard cb, final PlayerColor currentColor) {
        final Point2D kingPos;
        if (currentColor == PlayerColor.WHITE) {
            kingPos = WKING_POS;
        } else {
            kingPos = BKING_POS;
        }
        if (cb.getEntity(kingPos).isEmpty()) {
            return CastleCondition.NO_CASTLE;
        }

        if (cb.getEntity(kingPos).get().asMoveable().isPresent()) {
            final var piece = (Piece) cb.getEntity(kingPos).get().asMoveable().get();
            if (piece.getType() == PieceType.KING && !piece.hasMoved()) {
                if (cb.getEntity(new Point2D(TOWERS_X.x(), kingPos.y())).isPresent()
                        && cb.getEntity(new Point2D(TOWERS_X.y(), kingPos.y())).isPresent() 
                        && RulesUtils.hasNotMoved(cb, new Point2D(TOWERS_X.x(), kingPos.y())) 
                        && RulesUtils.hasNotMoved(cb, new Point2D(TOWERS_X.y(), kingPos.y()))
                        && castleLeft(cb, kingPos, currentColor)
                        && castleRight(cb, kingPos, currentColor)) {
                    return CastleCondition.CASTLE_BOTH;
                } 
                if (cb.getEntity(new Point2D(TOWERS_X.x(), kingPos.y())).isPresent()
                        && RulesUtils.hasNotMoved(cb, new Point2D(TOWERS_X.x(), kingPos.y()))
                        && castleLeft(cb, kingPos, currentColor)) {
                    return CastleCondition.CASTLE_LEFT;
                }
                if (cb.getEntity(new Point2D(TOWERS_X.y(), kingPos.y())).isPresent()
                        && RulesUtils.hasNotMoved(cb, new Point2D(TOWERS_X.y(), kingPos.y()))
                        && castleRight(cb, kingPos, currentColor)) {
                    return CastleCondition.CASTLE_RIGHT;
                }
                return CastleCondition.NO_CASTLE;
            }
        }
        return CastleCondition.NO_CASTLE; // cell is empty or king has moved
    }

    /**
     * Utility method that checks if castling is possible on the left.
     * 
     * @param cb chessboard of the current game
     * @param kingPos position of the king.
     * @param currentColor color of the current player.
     * @return {@code true} if the castle is possible, {@code false} otherwise.
     */
    private static boolean castleLeft(final ChessBoard cb, final Point2D kingPos, final PlayerColor currentColor) {
        return cb.isFree(new Point2D(kingPos.x() - 1, kingPos.y()))
                && cb.isFree(new Point2D(kingPos.x() - 2, kingPos.y()))
                && cb.isFree(new Point2D(kingPos.x() - 3, kingPos.y()))
                && RulesUtils.underAttack(cb, RulesUtils.swapColor(currentColor), new Point2D(kingPos.x() - 1, kingPos.y())).isEmpty()
                && RulesUtils.underAttack(cb, RulesUtils.swapColor(currentColor), new Point2D(kingPos.x() - 2, kingPos.y())).isEmpty();
    }

    /**
     * Utility method that checks if castling is possible on the right.
     * 
     * @param cb chessboard of the current game
     * @param kingPos position of the king.
     * @param currentColor color of the current player.
     * @return {@code true} if the castle is possible, {@code false} otherwise.
     */
    private static boolean castleRight(final ChessBoard cb, final Point2D kingPos, final PlayerColor currentColor) {
        return cb.isFree(new Point2D(kingPos.x() + 1, kingPos.y()))
                && cb.isFree(new Point2D(kingPos.x() + 2, kingPos.y()))
                && RulesUtils.underAttack(cb, RulesUtils.swapColor(currentColor), new Point2D(kingPos.x() + 1, kingPos.y())).isEmpty()
                && RulesUtils.underAttack(cb, RulesUtils.swapColor(currentColor), new Point2D(kingPos.x() + 2, kingPos.y())).isEmpty();
    }

    
}
