package it.unibo.samplejavafx.mvc.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.loader.JsonViews;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * New test class made to test all the possible movements of the real chess pieces
 * loaded from the new JSON loading system.
 */
class ChessPieceMovementTest {
    private static final String PIECES_PATH = "src/main/resources/EntityResources/StandardChessPieces/pieces/";
    private static final String PAWN_JSON = "Pawn.json";
    private static final String KNIGHT_JSON = "Knight.json";
    private static final String BISHOP_JSON = "Bishop.json";
    private static final String ROOK_JSON = "Rook.json";
    private static final String QUEEN_JSON = "Queen.json";
    private static final String KING_JSON = "King.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private ChessBoard board;
    private int gameIdCounter;

    @BeforeEach
    void setUp() {
        board = new ChessBoardImpl();
        gameIdCounter = 0;
    }

    private Piece loadPieceFromJson(final String filename, final PlayerColor color) throws IOException {
        gameIdCounter++;
       final Piece piece = mapper.readerWithView(JsonViews.FirstLoading.class)
                .readValue(new File(PIECES_PATH + filename), Piece.class);
        return new Piece(
                piece.getId(),
                piece.getName(),
                gameIdCounter,
                piece.getImagePath(),
                color,
                piece.getWeight(),
                piece.getType(),
                piece.getMoveRules()
        );
    }

    @Test
    void testPawnMovementAndCapture() throws IOException {
        final Piece whitePawn = loadPieceFromJson(PAWN_JSON, PlayerColor.WHITE);
        final Piece blackPawn = loadPieceFromJson(PAWN_JSON, PlayerColor.BLACK);

        // Test: Forward movement for both colors, diagonal captures, blocking, can't capture friendly
        board.setEntity(new Point2D(4, 6), whitePawn);
        board.setEntity(new Point2D(4, 1), blackPawn);
        assertTrue(whitePawn.getValidMoves(new Point2D(4, 6), board).contains(new Point2D(4, 5)), "White pawn forward");
        assertTrue(blackPawn.getValidMoves(new Point2D(4, 1), board).contains(new Point2D(4, 2)), "Black pawn forward");
        board.removeEntity(new Point2D(4, 6));
        board.removeEntity(new Point2D(4, 1));

        // White pawn with captures, blocking
        final Piece whitePawn2 = loadPieceFromJson(PAWN_JSON, PlayerColor.WHITE);
        final Piece blackEnemy1 = loadPieceFromJson(PAWN_JSON, PlayerColor.BLACK);
        final Piece blackEnemy2 = loadPieceFromJson(PAWN_JSON, PlayerColor.BLACK);
        final Piece blocker = loadPieceFromJson(PAWN_JSON, PlayerColor.BLACK);

        board.setEntity(new Point2D(4, 4), whitePawn);
        board.setEntity(new Point2D(3, 3), blackEnemy1);
        board.setEntity(new Point2D(5, 3), blackEnemy2);
        board.setEntity(new Point2D(2, 3), whitePawn2);

        Set<Point2D> expectedMoves = Set.of(new Point2D(4, 3), new Point2D(3, 3), new Point2D(5, 3));
        assertEquals(expectedMoves, new HashSet<>(whitePawn.getValidMoves(new Point2D(4, 4), board)));

        // Test blocking
        board.setEntity(new Point2D(4, 3), blocker);
        List<Point2D> moves = whitePawn.getValidMoves(new Point2D(4, 4), board);
        assertFalse(moves.contains(new Point2D(4, 3)), "Blocked forward");

        // Test can't capture friendly
        board.removeEntity(new Point2D(3, 3));
        board.removeEntity(new Point2D(2, 3));
        board.setEntity(new Point2D(3, 3), whitePawn2);
        moves = whitePawn.getValidMoves(new Point2D(4, 3), board);
        assertFalse(moves.contains(new Point2D(3, 3)), "Can't capture friendly");

        // Test x edge - only forward available
        board = new ChessBoardImpl();
        board.setEntity(new Point2D(0, 4), whitePawn);
        expectedMoves = Set.of(new Point2D(0, 3));
        assertEquals(expectedMoves, new HashSet<>(whitePawn.getValidMoves(new Point2D(0, 4), board)), "Edge position");

        // Test y edge - no moves available
        board.removeEntity(new Point2D(0, 4));
        board.setEntity(new Point2D(3, 0), whitePawn);
        assertFalse(whitePawn.getAvailableCells().contains(new Point2D(3,  -1)), "No moves at board edge");

    }

    @Test
    void testKnightMovement() throws IOException {
        final Piece whiteKnight = loadPieceFromJson(KNIGHT_JSON, PlayerColor.WHITE);
        final Piece whitePawn = loadPieceFromJson(PAWN_JSON, PlayerColor.WHITE);
        final Piece whitePawn2 = loadPieceFromJson(PAWN_JSON, PlayerColor.WHITE);
        final Piece blackPawn = loadPieceFromJson(PAWN_JSON, PlayerColor.BLACK);

        // All L-shaped moves, jumping, capturing, blocking
        board.setEntity(new Point2D(4, 4), whiteKnight);
        board.setEntity(new Point2D(4, 5), whitePawn);    // Jumps over
        board.setEntity(new Point2D(6, 5), blackPawn);    // Can capture
        board.setEntity(new Point2D(2, 3), whitePawn2);    // Can't land on friendly

        // Test jumping, capturing and can't land on friendly
        List<Point2D> moves = whiteKnight.getValidMoves(new Point2D(4, 4), board);
        assertTrue(moves.contains(new Point2D(6, 5)), "Can capture enemy");
        assertFalse(moves.contains(new Point2D(2, 3)), "Can't land on friendly");
        board.removeEntity(new Point2D(2, 3));

        // Test L-shaped moves in all directions
        moves = whiteKnight.getValidMoves(new Point2D(4, 4), board);
        assertEquals(
            Set.of(
                new Point2D(2, 5), new Point2D(2, 3), new Point2D(3, 6), new Point2D(3, 2),
                new Point2D(5, 6), new Point2D(5, 2), new Point2D(6, 5), new Point2D(6, 3)
            ),
            new HashSet<>(moves),
            "8 valid L-shaped moves to correct positions"
        );

        // Test boundaries
        board = new ChessBoardImpl();
        board.setEntity(new Point2D(0, 0), whiteKnight);
        Set<Point2D> expectedMoves = Set.of(new Point2D(2, 1), new Point2D(1, 2));
        assertEquals(expectedMoves, new HashSet<>(whiteKnight.getValidMoves(new Point2D(0, 0), board)), "Corner: 2 moves");

        board.removeEntity(new Point2D(0, 0));
        board.setEntity(new Point2D(7, 4), whiteKnight);
        expectedMoves = Set.of(new Point2D(5, 5), new Point2D(5, 3), new Point2D(6, 6), new Point2D(6, 2));
        assertEquals(expectedMoves, new HashSet<>(whiteKnight.getValidMoves(new Point2D(7, 4), board)), "Edge: 4 moves");
    }

    @Test
    void testBishopDiagonalMovement() throws IOException {
        final Piece whiteBishop = loadPieceFromJson(BISHOP_JSON, PlayerColor.WHITE);
        final Piece whitePawn = loadPieceFromJson(PAWN_JSON, PlayerColor.WHITE);
        final Piece blackPawn = loadPieceFromJson(PAWN_JSON, PlayerColor.BLACK);

        // Diagonal sliding, blocking, capturing
        board.setEntity(new Point2D(4, 4), whiteBishop);
        board.setEntity(new Point2D(6, 6), whitePawn);   // Blocks
        board.setEntity(new Point2D(2, 2), blackPawn);   // Can capture

        final List<Point2D> moves = whiteBishop.getValidMoves(new Point2D(4, 4), board);
        Set<Point2D> expectedMoves = Set.of(
                new Point2D(5, 5),                                    // Up-right (blocked at 6,6)
                new Point2D(3, 3), new Point2D(2, 2),                // Down-left (capture at 2,2)
                new Point2D(3, 5), new Point2D(2, 6), new Point2D(1, 7),  // Up-left
                new Point2D(5, 3), new Point2D(6, 2), new Point2D(7, 1)   // Down-right
        );

        assertEquals(expectedMoves, new HashSet<>(moves));
        assertTrue(moves.contains(new Point2D(2, 2)), "Can capture enemy");
        assertFalse(moves.contains(new Point2D(6, 6)), "Blocked by friendly");
        assertFalse(moves.contains(new Point2D(1, 1)), "Can't go beyond capture");

        // Test corner
        board = new ChessBoardImpl();
        board.setEntity(new Point2D(0, 0), whiteBishop);
        expectedMoves = Set.of(
                new Point2D(1, 1), new Point2D(2, 2), new Point2D(3, 3),
                new Point2D(4, 4), new Point2D(5, 5), new Point2D(6, 6), new Point2D(7, 7)
        );
        assertEquals(expectedMoves, new HashSet<>(whiteBishop.getValidMoves(new Point2D(0, 0), board)), "Corner: 7 diagonal moves");
    }

    @Test
    void testRookStraightMovement() throws IOException {
        final Piece whiteRook = loadPieceFromJson(ROOK_JSON, PlayerColor.WHITE);
        final Piece whitePawn = loadPieceFromJson(PAWN_JSON, PlayerColor.WHITE);
        final Piece whitePawn2 = loadPieceFromJson(PAWN_JSON, PlayerColor.WHITE);
        final Piece blackPawn = loadPieceFromJson(PAWN_JSON, PlayerColor.BLACK);
        final Piece blackPawn2 = loadPieceFromJson(PAWN_JSON, PlayerColor.BLACK);

        // Straight sliding, blocking, capturing in 4 directions
        board.setEntity(new Point2D(4, 4), whiteRook);
        board.setEntity(new Point2D(4, 6), whitePawn);   // Blocks up
        board.setEntity(new Point2D(6, 4), whitePawn2);   // Blocks right
        board.setEntity(new Point2D(4, 2), blackPawn);   // Can capture down
        board.setEntity(new Point2D(2, 4), blackPawn2);   // Can capture left

        final List<Point2D> moves = whiteRook.getValidMoves(new Point2D(4, 4), board);
        final Set<Point2D> expectedMoves = Set.of(
                new Point2D(4, 5),                       // Up (blocked at 6)
                new Point2D(4, 3), new Point2D(4, 2),    // Down (capture at 2)
                new Point2D(5, 4),                       // Right (blocked at 6)
                new Point2D(3, 4), new Point2D(2, 4)     // Left (capture at 2)
        );

        assertEquals(expectedMoves, new HashSet<>(moves));
        assertTrue(moves.contains(new Point2D(4, 2)), "Can capture");
        assertFalse(moves.contains(new Point2D(4, 6)), "Blocked by friendly");

        // Test corner
        board = new ChessBoardImpl();
        board.setEntity(new Point2D(0, 0), whiteRook);
        assertEquals(
            Set.of(
                new Point2D(0, 1), new Point2D(0, 2), new Point2D(0, 3), new Point2D(0, 4), new Point2D(0, 5), new Point2D(0, 6), new Point2D(0, 7), // right
                new Point2D(1, 0), new Point2D(2, 0), new Point2D(3, 0), new Point2D(4, 0), new Point2D(5, 0), new Point2D(6, 0), new Point2D(7, 0)  // up
            ),
            new HashSet<>(whiteRook.getValidMoves(new Point2D(0, 0), board)),
            "Corner: 14 straight moves"
        );
    }

    @Test
    void testQueenCombinedMovement() throws IOException {
        final Piece whiteQueen = loadPieceFromJson(QUEEN_JSON, PlayerColor.WHITE);
        final Piece whitePawn1 = loadPieceFromJson(PAWN_JSON, PlayerColor.WHITE);
        final Piece whitePawn2 = loadPieceFromJson(PAWN_JSON, PlayerColor.WHITE);
        final Piece blackPawn1 = loadPieceFromJson(PAWN_JSON, PlayerColor.BLACK);
        final Piece blackPawn2 = loadPieceFromJson(PAWN_JSON, PlayerColor.BLACK);

        // Combines bishop + rook moves (8 directions)
        board.setEntity(new Point2D(4, 4), whiteQueen);
        List<Point2D> moves = whiteQueen.getValidMoves(new Point2D(4, 4), board);
        assertEquals(
            Set.of(
                // Diagonal moves
                new Point2D(5, 5), new Point2D(6, 6), new Point2D(7, 7),
                new Point2D(3, 3), new Point2D(2, 2), new Point2D(1, 1), new Point2D(0, 0),
                new Point2D(5, 3), new Point2D(6, 2), new Point2D(7, 1),
                new Point2D(3, 5), new Point2D(2, 6), new Point2D(1, 7),
                // Straight moves
                new Point2D(4, 5), new Point2D(4, 6), new Point2D(4, 7),
                new Point2D(4, 3), new Point2D(4, 2), new Point2D(4, 1), new Point2D(4, 0),
                new Point2D(5, 4), new Point2D(6, 4), new Point2D(7, 4),
                new Point2D(3, 4), new Point2D(2, 4), new Point2D(1, 4), new Point2D(0, 4)
            ),
            new HashSet<>(moves),
            "Queen from center: 27 moves (13 diagonal + 14 straight)"
        );

        // Test blocking and capturing
        board.setEntity(new Point2D(5, 5), whitePawn1);   // Blocks diagonal
        board.setEntity(new Point2D(4, 6), whitePawn2);   // Blocks vertical
        board.setEntity(new Point2D(6, 6), blackPawn1);   // Enemy diagonal
        board.setEntity(new Point2D(7, 4), blackPawn2);   // Enemy horizontal

        moves = whiteQueen.getValidMoves(new Point2D(4, 4), board);
        assertFalse(moves.contains(new Point2D(5, 5)), "Blocked by friendly diagonal");
        assertFalse(moves.contains(new Point2D(4, 6)), "Blocked by friendly vertical");
        assertFalse(moves.contains(new Point2D(6, 6)), "Block by friendly diagonal");
        assertTrue(moves.contains(new Point2D(7, 4)), "Can capture horizontal");

        // Test edge
        board = new ChessBoardImpl();
        board.setEntity(new Point2D(0, 4), whiteQueen);
        assertEquals(
            Set.of(
                new Point2D(0, 5), new Point2D(0, 6), new Point2D(0, 7), // bottom
                new Point2D(0, 3), new Point2D(0, 2), new Point2D(0, 1), new Point2D(0, 0), // up
                new Point2D(1, 4), new Point2D(2, 4), new Point2D(3, 4), new Point2D(4, 4), new Point2D(5, 4), new Point2D(6, 4), new Point2D(7, 4), // right
                new Point2D(1, 5), new Point2D(2, 6), new Point2D(3, 7), // bottom-left diagonal
                new Point2D(1, 3), new Point2D(2, 2), new Point2D(3, 1), new Point2D(4, 0) // up-right diagonal
            ),
            new HashSet<>(whiteQueen.getValidMoves(new Point2D(0, 4), board)),
            "Edge: 21 moves"
        );
    }

    @Test
    void testKingMovement() throws IOException {
        final Piece whiteKing = loadPieceFromJson(KING_JSON, PlayerColor.WHITE);
        final Piece whitePawn = loadPieceFromJson(PAWN_JSON, PlayerColor.WHITE);
        final Piece blackPawn = loadPieceFromJson(PAWN_JSON, PlayerColor.BLACK);

        // All 8 adjacent squares, capturing, blocking
        board.setEntity(new Point2D(4, 4), whiteKing);
        Set<Point2D> expectedMoves = Set.of(
                new Point2D(4, 5), new Point2D(4, 3),
                new Point2D(5, 4), new Point2D(3, 4),
                new Point2D(5, 5), new Point2D(5, 3),
                new Point2D(3, 5), new Point2D(3, 3)
        );
        assertEquals(expectedMoves, new HashSet<>(whiteKing.getValidMoves(new Point2D(4, 4), board)), "Center: 8 moves");

        // Test blocking and capturing
        board.setEntity(new Point2D(5, 5), whitePawn);   // Can't land on friendly
        board.setEntity(new Point2D(3, 3), blackPawn);   // Can capture enemy

        final List<Point2D> moves = whiteKing.getValidMoves(new Point2D(4, 4), board);
        assertFalse(moves.contains(new Point2D(5, 5)), "Can't land on friendly");
        assertTrue(moves.contains(new Point2D(3, 3)), "Can capture enemy");
        assertEquals(
            Set.of(
                new Point2D(4, 3), new Point2D(4, 5),
                new Point2D(5, 3), new Point2D(5, 4),
                new Point2D(3, 3), new Point2D(3, 4), new Point2D(3, 5)
            ),
            new HashSet<>(moves),
            "7 moves with one blocked"
        );

        // Test boundaries
        board = new ChessBoardImpl();
        board.setEntity(new Point2D(0, 0), whiteKing);
        expectedMoves = Set.of(new Point2D(1, 0), new Point2D(0, 1), new Point2D(1, 1));
        assertEquals(expectedMoves, new HashSet<>(whiteKing.getValidMoves(new Point2D(0, 0), board)), "Corner: 3 moves");

        board.removeEntity(new Point2D(0, 0));
        board.setEntity(new Point2D(4, 0), whiteKing);
        expectedMoves = Set.of(
                new Point2D(3, 0), new Point2D(5, 0),
                new Point2D(3, 1), new Point2D(4, 1), new Point2D(5, 1)
        );
        assertEquals(expectedMoves, new HashSet<>(whiteKing.getValidMoves(new Point2D(4, 0), board)), "Edge: 5 moves");
    }

    @Test
    void testMultiplePiecesInteraction() throws IOException {
        final Piece whiteKnight = loadPieceFromJson(KNIGHT_JSON, PlayerColor.WHITE);
        final Piece whiteRook = loadPieceFromJson(ROOK_JSON, PlayerColor.WHITE);
        final Piece blackBishop = loadPieceFromJson(BISHOP_JSON, PlayerColor.BLACK);

        board.setEntity(new Point2D(4, 4), whiteKnight);
        board.setEntity(new Point2D(0, 4), whiteRook);
        board.setEntity(new Point2D(6, 6), blackBishop);

        // Rook blocked by knight (can't reach 4,4 or beyond)
        final List<Point2D> rookMoves = whiteRook.getValidMoves(new Point2D(0, 4), board);
        assertFalse(rookMoves.contains(new Point2D(4, 4)), "Rook blocked by friendly knight");
        assertTrue(rookMoves.contains(new Point2D(3, 4)), "Rook can move up to blocking piece");

        // Bishop can move freely in its diagonals
        final List<Point2D> bishopMoves = blackBishop.getValidMoves(new Point2D(6, 6), board);
        assertTrue(bishopMoves.contains(new Point2D(5, 5)), "Bishop diagonal movement");
        assertTrue(bishopMoves.contains(new Point2D(7, 7)), "Bishop diagonal movement");
        assertTrue(bishopMoves.contains(new Point2D(4, 4)), "Bishop can capture knight");
    }
}
