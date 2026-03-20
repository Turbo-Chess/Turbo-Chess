package it.unibo.samplejavafx.mvc.model.utils;

import it.unibo.turbochess.model.chessboard.board.impl.ChessBoardImpl;
import it.unibo.turbochess.model.entity.definition.PieceDefinition;
import it.unibo.turbochess.model.entity.impl.Piece;
import it.unibo.turbochess.model.entity.impl.PieceType;
import it.unibo.turbochess.model.entity.impl.PlayerColor;
import it.unibo.turbochess.model.movement.impl.MoveRulesImpl;
import it.unibo.turbochess.model.point2d.Point2D;
import it.unibo.turbochess.model.utils.RulesUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RulesUtilsTest {

    private static final String PAWN_NAME = "Pawn";
    private static final String PAWN_ID = "pawn";
    private static final String KING_NAME = "King";
    private static final String KING_ID = "king";
    private static final int BACK_RANK = 7;

    private static PieceDefinition def(final String name, final String id, final PieceType type, final int weight) {
        return new PieceDefinition.Builder()
            .name(name)
            .id(id)
            .imagePath("classpath:/assets/images/")
            .pieceType(type)
            .weight(weight)
            .moveRules(List.of(new MoveRulesImpl(
                new Point2D(0, 1),
                MoveRulesImpl.MoveType.MOVE_AND_EAT,
                MoveRulesImpl.MoveStrategy.JUMPING,
                false
            )))
            .build();
    }

    private static Piece piece(final PieceDefinition def, final PlayerColor color, final int gameId, final boolean moved) {
        return new Piece.Builder()
            .entityDefinition(def)
            .playerColor(color)
            .gameId(gameId)
            .moved(moved)
            .build();
    }

    @Test
    void swapColorReturnsOpposite() {
        assertEquals(PlayerColor.BLACK, RulesUtils.swapColor(PlayerColor.WHITE));
        assertEquals(PlayerColor.WHITE, RulesUtils.swapColor(PlayerColor.BLACK));
    }

    @Test
    void kingFindsCorrectKing() {
        final var board = new ChessBoardImpl();
        final var whiteKing = piece(def(KING_NAME, KING_ID, PieceType.KING, 100), PlayerColor.WHITE, 1, false);
        final var blackPawn = piece(def(PAWN_NAME, PAWN_ID, PieceType.PAWN, 1), PlayerColor.BLACK, 2, false);

        board.setEntity(new Point2D(4, BACK_RANK), whiteKing);
        board.setEntity(new Point2D(0, 1), blackPawn);

        final var king = RulesUtils.getKing(board, PlayerColor.WHITE).orElseThrow();
        assertEquals(whiteKing, king);
        assertTrue(RulesUtils.getKing(board, PlayerColor.BLACK).isEmpty());
    }

    @Test
    void piecesOfColorReturnsOnlyThatColor() {
        final var board = new ChessBoardImpl();
        final var whitePawn1 = piece(def(PAWN_NAME, PAWN_ID, PieceType.PAWN, 1), PlayerColor.WHITE, 1, false);
        final var whitePawn2 = piece(def(PAWN_NAME, PAWN_ID, PieceType.PAWN, 1), PlayerColor.WHITE, 2, false);
        final var blackKing = piece(def(KING_NAME, KING_ID, PieceType.KING, 100), PlayerColor.BLACK, 3, false);
        board.setEntity(new Point2D(0, 0), whitePawn1);
        board.setEntity(new Point2D(1, 0), whitePawn2);
        board.setEntity(new Point2D(0, BACK_RANK), blackKing);

        assertEquals(2, RulesUtils.getPiecesOfColor(board, PlayerColor.WHITE).size());
        assertEquals(1, RulesUtils.getPiecesOfColor(board, PlayerColor.BLACK).size());
    }

    @Test
    void hasNotMovedTracksMovementState() {
        final var board = new ChessBoardImpl();
        final var pawn = piece(def(PAWN_NAME, PAWN_ID, PieceType.PAWN, 1), PlayerColor.WHITE, 1, false);
        final var from = new Point2D(0, 6);
        final var to = new Point2D(0, 5);

        board.setEntity(from, pawn);
        assertTrue(RulesUtils.hasNotMoved(board, from));

        board.move(from, to);
        assertFalse(RulesUtils.hasNotMoved(board, to));
    }
}
