package it.unibo.turbochess.model.handler;

import it.unibo.turbochess.model.chessmatch.impl.ChessMatchImpl;
import it.unibo.turbochess.model.entity.impl.Piece;
import it.unibo.turbochess.model.entity.impl.PieceType;
import it.unibo.turbochess.model.entity.impl.PlayerColor;
import it.unibo.turbochess.model.entity.definition.PieceDefinition;
import it.unibo.turbochess.model.handler.impl.GameState;
import it.unibo.turbochess.model.handler.impl.TurnHandlerImpl;
import it.unibo.turbochess.model.handler.turnstates.impl.DoubleCheckTurnState;
import it.unibo.turbochess.model.movement.api.MoveRules;
import it.unibo.turbochess.model.movement.impl.MoveRulesImpl;
import it.unibo.turbochess.model.point2d.Point2D;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static it.unibo.turbochess.model.movement.impl.MoveRulesImpl.MoveStrategy.JUMPING;
import static it.unibo.turbochess.model.movement.impl.MoveRulesImpl.MoveStrategy.SLIDING;
import static it.unibo.turbochess.model.movement.impl.MoveRulesImpl.MoveStrategy.STEPPING;
import static it.unibo.turbochess.model.movement.impl.MoveRulesImpl.MoveType.MOVE_AND_EAT;
import static it.unibo.turbochess.model.movement.impl.MoveRulesImpl.MoveType.MOVE_ONLY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurnHandlerTest {
    private static final String MKING = "King";
    private static final String KING = "king";
    private static final String MPAWN = "Pawn";
    private static final String PAWN = "pawn";

    private static PieceDefinition def(
        final String name,
        final String id,
        final PieceType type,
        final int weight,
        final List<? extends MoveRules> rules
    ) {
        return new PieceDefinition.Builder()
            .name(name)
            .id(id)
            .imagePath("classpath:/assets/images/")
            .pieceType(type)
            .weight(weight)
            .moveRules(List.copyOf(rules))
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

    @Nested
    class BasicFlow {

        @Test
        void thinkingSelectsOwnedPieceMovesItAndAdvancesTurn() {
            final var match = new ChessMatchImpl();
            try {
                final var handler = match.getTurnHandler();

                final var pawnDef = def(
                    MPAWN,
                    PAWN,
                    PieceType.PAWN,
                    1,
                    List.of(new MoveRulesImpl(new Point2D(0, 1), MOVE_ONLY, STEPPING, false))
                );

                final var whitePawn = piece(pawnDef, PlayerColor.WHITE, 1, false);
                final var blackPawn = piece(pawnDef, PlayerColor.BLACK, 2, false);

                final var whiteFrom = new Point2D(0, 6);
                final var whiteTo = new Point2D(0, 5);
                final var blackPos = new Point2D(7, 1);

                match.getBoard().setEntity(whiteFrom, whitePawn);
                match.getBoard().setEntity(blackPos, blackPawn);

                final var moves = handler.thinking(whiteFrom);
                assertTrue(moves.contains(whiteTo));

                final var selected = handler.thinking(whiteTo);
                assertEquals(List.of(whiteTo), selected);
                assertTrue(match.getBoard().getEntity(whiteTo).isPresent());
                assertTrue(match.getBoard().getEntity(whiteFrom).isEmpty());

                assertEquals(2, match.getTurnNumber());
                assertEquals(PlayerColor.BLACK, match.getCurrentPlayer());

                assertTrue(handler.thinking(whiteTo).isEmpty());
            } finally {
                match.getGameTimer().shutdown();
            }
        }

        @Test
        void thinkingCapturesEnemyPiece() {
            final var match = new ChessMatchImpl();
            try {
                final var handler = match.getTurnHandler();

                final var attackerDef = def(
                    "Superior",
                    "superior",
                    PieceType.SUPERIOR,
                    9,
                    List.of(new MoveRulesImpl(new Point2D(1, 0), MOVE_AND_EAT, STEPPING, false))
                );
                final var pawnDef = def(
                    MPAWN,
                    PAWN,
                    PieceType.PAWN,
                    1,
                    List.of(new MoveRulesImpl(new Point2D(0, 1), MOVE_ONLY, STEPPING, false))
                );

                final var white = piece(attackerDef, PlayerColor.WHITE, 1, false);
                final var black = piece(pawnDef, PlayerColor.BLACK, 2, false);

                final var from = new Point2D(4, 4);
                final var target = new Point2D(5, 4);
                final var blackOther = new Point2D(7, 1);

                match.getBoard().setEntity(from, white);
                match.getBoard().setEntity(target, black);
                match.getBoard().setEntity(blackOther, piece(pawnDef, PlayerColor.BLACK, 3, false));

                final var moves = handler.thinking(from);
                assertTrue(moves.contains(target));

                final var selected = handler.thinking(target);
                assertEquals(List.of(target), selected);

                assertTrue(match.getBoard().getEntity(target).isPresent());
                assertTrue(match.getBoard().getEntity(from).isEmpty());
                assertEquals(PlayerColor.WHITE, match.getBoard().getEntity(target).orElseThrow().getPlayerColor());
            } finally {
                match.getGameTimer().shutdown();
            }
        }

        @Test
        void cannotMoveWhiteAfterLoadIfBlackToPlay() {
            final var match = new ChessMatchImpl();
            try {
                final Piece whitePawn = new Piece.Builder()
                    .moved(false)
                    .entityDefinition(new PieceDefinition.Builder()
                        .name(MPAWN)
                        .id("test-pawn")
                        .imagePath("classpath:/assets/images/")
                        .weight(1)
                        .pieceType(PieceType.PAWN)
                        .moveRules(List.of(
                            new MoveRulesImpl(
                                new Point2D(0, 1),
                                MoveRulesImpl.MoveType.MOVE_ONLY,
                                MoveRulesImpl.MoveStrategy.STEPPING,
                                false
                            )
                        ))
                        .build())
                    .gameId(0)
                    .playerColor(PlayerColor.WHITE)
                    .build();

                final Point2D start = new Point2D(0, 6);
                match.getBoard().setEntity(start, whitePawn);

                match.setPlayerColor(PlayerColor.BLACK);

                final var moves = match.getTurnHandler().thinking(start);
                assertTrue(moves.isEmpty());
            } finally {
                match.getGameTimer().shutdown();
            }
        }
    }

    @Nested
    class DirectExecuteTurn {

        @Test
        void executeTurnReturnsFalseWhenMoveLeavesKingInCheck() {
            final var match = new ChessMatchImpl();
            try {
                final var handler = (TurnHandlerImpl) match.getTurnHandler();

                final var kingDef = def(
                    MKING,
                    KING,
                    PieceType.KING,
                    100,
                    List.of(new MoveRulesImpl(new Point2D(0, 1), MOVE_AND_EAT, STEPPING, false))
                );
                final var rookDef = def(
                    "Rook",
                    "rook",
                    PieceType.TOWER,
                    5,
                    List.of(new MoveRulesImpl(new Point2D(0, 1), MOVE_AND_EAT, SLIDING, false))
                );
                final var blockerDef = def(
                    "Blocker",
                    "blocker",
                    PieceType.SUPERIOR,
                    9,
                    List.of(new MoveRulesImpl(new Point2D(1, 0), MOVE_ONLY, STEPPING, false))
                );

                final var whiteKing = piece(kingDef, PlayerColor.WHITE, 1, false);
                final var blackRook = piece(rookDef, PlayerColor.BLACK, 2, false);
                final var blocker = piece(blockerDef, PlayerColor.WHITE, 3, false);

                final var kingPos = new Point2D(4, 7);
                final var rookPos = new Point2D(4, 0);
                final var blockerPos = new Point2D(4, 6);
                final var unsafeTarget = new Point2D(5, 6);

                match.getBoard().setEntity(kingPos, whiteKing);
                match.getBoard().setEntity(rookPos, blackRook);
                match.getBoard().setEntity(blockerPos, blocker);

                handler.setCurrentPiece(blocker);
                final boolean ok = handler.executeTurn(MOVE_ONLY, unsafeTarget);
                assertFalse(ok);

                assertTrue(match.getBoard().getEntity(blockerPos).isPresent());
                assertTrue(match.getBoard().getEntity(unsafeTarget).isEmpty());
                assertEquals(1, match.getTurnNumber());
                assertEquals(PlayerColor.WHITE, match.getCurrentPlayer());
            } finally {
                match.getGameTimer().shutdown();
            }
        }

        @Test
        void transitionToDoubleCheckStateDisallowsSelectingNonKing() {
            final var match = new ChessMatchImpl();
            try {
                final var handler = (TurnHandlerImpl) match.getTurnHandler();

                final var pawnDef = def(
                    MPAWN,
                    PAWN,
                    PieceType.PAWN,
                    1,
                    List.of(new MoveRulesImpl(new Point2D(0, 1), MOVE_ONLY, STEPPING, false))
                );
                final var whitePawn = piece(pawnDef, PlayerColor.WHITE, 1, false);
                final var pawnPos = new Point2D(0, 6);
                match.getBoard().setEntity(pawnPos, whitePawn);

                handler.transitionTo(new DoubleCheckTurnState(handler));
                assertTrue(handler.thinking(pawnPos).isEmpty());
            } finally {
                match.getGameTimer().shutdown();
            }
        }
    }

    @Nested
    class Integration {

        @Test
        void afterMoveCausingCheckOnlyKingOrInterposingPieceCanActAndCaptureResolvesCheck() {
            final var match = new ChessMatchImpl();
            try {
                final var handler = (TurnHandlerImpl) match.getTurnHandler();

                final var kingDef = def(
                    MKING,
                    KING,
                    PieceType.KING,
                    100,
                    List.of(
                        new MoveRulesImpl(new Point2D(1, 0), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(-1, 0), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(0, 1), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(0, -1), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(1, 1), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(1, -1), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(-1, 1), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(-1, -1), MOVE_AND_EAT, STEPPING, false)
                    )
                );
                final var knightDef = def(
                    "Knight",
                    "knight",
                    PieceType.INFERIOR,
                    3,
                    List.of(
                        new MoveRulesImpl(new Point2D(1, 2), MOVE_AND_EAT, JUMPING, false),
                        new MoveRulesImpl(new Point2D(2, 1), MOVE_AND_EAT, JUMPING, false),
                        new MoveRulesImpl(new Point2D(-1, 2), MOVE_AND_EAT, JUMPING, false),
                        new MoveRulesImpl(new Point2D(-2, 1), MOVE_AND_EAT, JUMPING, false),
                        new MoveRulesImpl(new Point2D(1, -2), MOVE_AND_EAT, JUMPING, false),
                        new MoveRulesImpl(new Point2D(2, -1), MOVE_AND_EAT, JUMPING, false),
                        new MoveRulesImpl(new Point2D(-1, -2), MOVE_AND_EAT, JUMPING, false),
                        new MoveRulesImpl(new Point2D(-2, -1), MOVE_AND_EAT, JUMPING, false)
                    )
                );
                final var pawnDef = def(
                    MPAWN,
                    PAWN,
                    PieceType.PAWN,
                    1,
                    List.of(new MoveRulesImpl(new Point2D(0, 1), MOVE_ONLY, STEPPING, false))
                );

                final var whiteKing = piece(kingDef, PlayerColor.WHITE, 1, false);
                final var blackKing = piece(kingDef, PlayerColor.BLACK, 2, false);
                final var whiteKnight = piece(knightDef, PlayerColor.WHITE, 3, false);
                final var blackKnight = piece(knightDef, PlayerColor.BLACK, 4, false);
                final var blackPawn = piece(pawnDef, PlayerColor.BLACK, 5, false);

                final var whiteKingPos = new Point2D(4, 7);
                final var blackKingPos = new Point2D(4, 0);
                final var whiteKnightFrom = new Point2D(4, 4);
                final var whiteKnightTo = new Point2D(5, 2);
                final var blackKnightPos = new Point2D(6, 0);
                final var blackPawnPos = new Point2D(0, 1);

                match.getBoard().setEntity(whiteKingPos, whiteKing);
                match.getBoard().setEntity(blackKingPos, blackKing);
                match.getBoard().setEntity(whiteKnightFrom, whiteKnight);
                match.getBoard().setEntity(blackKnightPos, blackKnight);
                match.getBoard().setEntity(blackPawnPos, blackPawn);

                assertEquals(PlayerColor.WHITE, match.getCurrentPlayer());
                final var whiteMoves = handler.thinking(whiteKnightFrom);
                assertTrue(whiteMoves.contains(whiteKnightTo));
                assertEquals(List.of(whiteKnightTo), handler.thinking(whiteKnightTo));

                assertEquals(PlayerColor.BLACK, match.getCurrentPlayer());
                assertEquals(GameState.CHECK, match.getGameState());
                assertFalse(handler.getInterposing().isEmpty());

                assertTrue(handler.thinking(blackPawnPos).isEmpty());

                final var captureMoves = handler.thinking(blackKnightPos);
                assertTrue(captureMoves.contains(whiteKnightTo));
                assertEquals(List.of(whiteKnightTo), handler.thinking(whiteKnightTo));

                assertEquals(PlayerColor.WHITE, match.getCurrentPlayer());
                assertEquals(GameState.NORMAL, match.getGameState());
            } finally {
                match.getGameTimer().shutdown();
            }
        }

        @Test
        void duringCheckPawnCanInterposeOnAttackedSquareAndResolveCheck() {
            final var match = new ChessMatchImpl();
            try {
                final var handler = (TurnHandlerImpl) match.getTurnHandler();
                final var kingDef = def(
                    MKING,
                    KING,
                    PieceType.KING,
                    100,
                    List.of(
                        new MoveRulesImpl(new Point2D(1, 0), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(-1, 0), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(0, 1), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(0, -1), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(1, 1), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(1, -1), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(-1, 1), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(-1, -1), MOVE_AND_EAT, STEPPING, false)
                    )
                );
                final var bishopDef = def(
                    "Bishop",
                    "bishop",
                    PieceType.INFERIOR,
                    3,
                    List.of(
                        new MoveRulesImpl(new Point2D(1, 1), MOVE_AND_EAT, SLIDING, false),
                        new MoveRulesImpl(new Point2D(-1, 1), MOVE_AND_EAT, SLIDING, false),
                        new MoveRulesImpl(new Point2D(1, -1), MOVE_AND_EAT, SLIDING, false),
                        new MoveRulesImpl(new Point2D(-1, -1), MOVE_AND_EAT, SLIDING, false)
                    )
                );
                final var pawnDef = def(
                    MPAWN,
                    PAWN,
                    PieceType.PAWN,
                    1,
                    List.of(new MoveRulesImpl(new Point2D(0, 1), MOVE_ONLY, STEPPING, false))
                );

                final var whiteKing = piece(kingDef, PlayerColor.WHITE, 1, false);
                final var blackKing = piece(kingDef, PlayerColor.BLACK, 2, false);
                final var whiteBishop = piece(bishopDef, PlayerColor.WHITE, 3, false);
                final var blackPawn = piece(pawnDef, PlayerColor.BLACK, 4, false);

                final var whiteKingPos = new Point2D(4, 7);
                final var blackKingPos = new Point2D(4, 0);
                final var bishopFrom = new Point2D(2, 4);
                final var bishopTo = new Point2D(1, 3);
                final var blackPawnPos = new Point2D(2, 1);
                final var interposeSquare = new Point2D(2, 2);

                match.getBoard().setEntity(whiteKingPos, whiteKing);
                match.getBoard().setEntity(blackKingPos, blackKing);
                match.getBoard().setEntity(bishopFrom, whiteBishop);
                match.getBoard().setEntity(blackPawnPos, blackPawn);

                assertEquals(PlayerColor.WHITE, match.getCurrentPlayer());
                final var bishopMoves = handler.thinking(bishopFrom);
                assertTrue(bishopMoves.contains(bishopTo));
                assertEquals(List.of(bishopTo), handler.thinking(bishopTo));

                assertEquals(PlayerColor.BLACK, match.getCurrentPlayer());
                assertEquals(GameState.CHECK, match.getGameState());

                final var pawnMoves = handler.thinking(blackPawnPos);
                assertTrue(pawnMoves.contains(interposeSquare));
                assertEquals(List.of(interposeSquare), handler.thinking(interposeSquare));

                assertEquals(PlayerColor.WHITE, match.getCurrentPlayer());
                assertEquals(GameState.NORMAL, match.getGameState());
            } finally {
                match.getGameTimer().shutdown();
            }
        }

        @Test
        void afterMoveCausingDoubleCheckOnlyKingCanBeSelected() {
            final var match = new ChessMatchImpl();
            try {
                final var handler = (TurnHandlerImpl) match.getTurnHandler();

                final var kingDef = def(
                    MKING,
                    KING,
                    PieceType.KING,
                    100,
                    List.of(
                        new MoveRulesImpl(new Point2D(1, 0), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(-1, 0), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(0, 1), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(0, -1), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(1, 1), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(1, -1), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(-1, 1), MOVE_AND_EAT, STEPPING, false),
                        new MoveRulesImpl(new Point2D(-1, -1), MOVE_AND_EAT, STEPPING, false)
                    )
                );
                final var rookDef = def(
                    "Rook",
                    "rook",
                    PieceType.TOWER,
                    5,
                    List.of(
                        new MoveRulesImpl(new Point2D(1, 0), MOVE_AND_EAT, SLIDING, false),
                        new MoveRulesImpl(new Point2D(-1, 0), MOVE_AND_EAT, SLIDING, false),
                        new MoveRulesImpl(new Point2D(0, 1), MOVE_AND_EAT, SLIDING, false),
                        new MoveRulesImpl(new Point2D(0, -1), MOVE_AND_EAT, SLIDING, false)
                    )
                );
                final var bishopDef = def(
                    "Bishop",
                    "bishop",
                    PieceType.INFERIOR,
                    3,
                    List.of(
                        new MoveRulesImpl(new Point2D(1, 1), MOVE_AND_EAT, SLIDING, false),
                        new MoveRulesImpl(new Point2D(-1, 1), MOVE_AND_EAT, SLIDING, false),
                        new MoveRulesImpl(new Point2D(1, -1), MOVE_AND_EAT, SLIDING, false),
                        new MoveRulesImpl(new Point2D(-1, -1), MOVE_AND_EAT, SLIDING, false)
                    )
                );
                final var pawnDef = def(
                    MPAWN,
                    PAWN,
                    PieceType.PAWN,
                    1,
                    List.of(new MoveRulesImpl(new Point2D(0, 1), MOVE_ONLY, STEPPING, false))
                );

                final var whiteKing = piece(kingDef, PlayerColor.WHITE, 1, false);
                final var blackKing = piece(kingDef, PlayerColor.BLACK, 2, false);
                final var whiteRook = piece(rookDef, PlayerColor.WHITE, 3, false);
                final var whiteBishop = piece(bishopDef, PlayerColor.WHITE, 4, false);
                final var blackPawn = piece(pawnDef, PlayerColor.BLACK, 5, false);

                final var whiteKingPos = new Point2D(4, 7);
                final var blackKingPos = new Point2D(4, 0);
                final var rookPos = new Point2D(4, 6);
                final var bishopFrom = new Point2D(4, 2);
                final var bishopTo = new Point2D(3, 1);
                final var blackPawnPos = new Point2D(0, 1);

                match.getBoard().setEntity(whiteKingPos, whiteKing);
                match.getBoard().setEntity(blackKingPos, blackKing);
                match.getBoard().setEntity(rookPos, whiteRook);
                match.getBoard().setEntity(bishopFrom, whiteBishop);
                match.getBoard().setEntity(blackPawnPos, blackPawn);

                assertEquals(PlayerColor.WHITE, match.getCurrentPlayer());
                final var whiteMoves = handler.thinking(bishopFrom);
                assertTrue(whiteMoves.contains(bishopTo));
                assertEquals(List.of(bishopTo), handler.thinking(bishopTo));

                assertEquals(PlayerColor.BLACK, match.getCurrentPlayer());
                assertEquals(GameState.DOUBLE_CHECK, match.getGameState());

                assertTrue(handler.thinking(blackPawnPos).isEmpty());

                final var kingMoves = handler.thinking(blackKingPos);
                assertTrue(kingMoves.contains(new Point2D(3, 0)));
            } finally {
                match.getGameTimer().shutdown();
            }
        }
    }
}
