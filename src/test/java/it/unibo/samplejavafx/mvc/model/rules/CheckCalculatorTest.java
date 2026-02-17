package it.unibo.samplejavafx.mvc.model.rules;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.movement.JumpingMovement;
import it.unibo.samplejavafx.mvc.model.movement.MoveRules;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.movement.SlidingMovement;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckCalculatorTest {
    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final String PIECES_PATH = "src/main/resources/EntityResources/StandardChessPieces/pieces/";
    private ChessBoard board;

    @BeforeEach
    void setUp() {
        board = new ChessBoardImpl();
    }

    @Test
    void testHorizontalCheckInterposition() throws StreamReadException, DatabindException, IOException {
        // White King at 0,0
        board.setEntity(new Point2D(0, 0), createKing(PlayerColor.WHITE));
        // Black Rook at 0,7 (checking vertically? No, 0,7 is same X, different Y. That's Vertical)
        // Let's do Horizontal: King at 0,0, Rook at 7,0
        board.setEntity(new Point2D(7, 0), createRook(PlayerColor.BLACK));
        
        // White Rook at 4,4 (can move to 4,0 to block)
        board.setEntity(new Point2D(4, 4), createRook(PlayerColor.BLACK));

        List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        assertEquals(1, blockers.size());
        assertEquals(new Point2D(4, 4), board.getPosByEntity(blockers.get(0)));
    }

    @Test
    void testVerticalCheckInterposition() throws StreamReadException, DatabindException, IOException {
        // White King at 4,0
        createKing(PlayerColor.WHITE, new Point2D(4, 0));
        // Black Queen at 4,7
        createQueen(PlayerColor.BLACK, new Point2D(4, 7));
        
        // White Bishop at 2,2 (can move to 4,4? No, Bishop moves diagonally)
        // Bishop at 2,2. Target path is (4,1) to (4,6).
        // (4,4) is on diagonal from (2,2)? |4-2| = 2, |4-2| = 2. Yes.
        createBishop(PlayerColor.WHITE, new Point2D(2, 2));

        List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        assertEquals(1, blockers.size());
        assertEquals(new Point2D(2, 2), board.getPosByEntity(blockers.get(0)));
    }

    @Test
    void testDiagonalCheckInterposition() throws StreamReadException, DatabindException, IOException {
        // White King at 0,0
        createKing(PlayerColor.WHITE, new Point2D(0, 0));
        // Black Bishop at 7,7
        createBishop(PlayerColor.BLACK, new Point2D(7, 7));
        
        // White Rook at 0,5 (can move to 5,5? Yes)
        // Path: 1,1; 2,2; 3,3; 4,4; 5,5; 6,6
        createRook(PlayerColor.WHITE, new Point2D(0, 5));

        List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        assertEquals(1, blockers.size());
        assertEquals(new Point2D(0, 5), board.getPosByEntity(blockers.get(0)));
    }

    @Test
    void testKnightCheckCannotBlock() throws StreamReadException, DatabindException, IOException {
        createKing(PlayerColor.WHITE, new Point2D(4, 4));
        // Knight at 6,5 (attacks 4,4)
        createKnight(PlayerColor.BLACK, new Point2D(6, 5));
        
        // Friendly Rook that could theoretically go to "intermediate" square if it existed
        createRook(PlayerColor.WHITE, new Point2D(4, 5)); // Adjacent

        List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        assertTrue(blockers.isEmpty());
    }

    @Test
    void testAdjacentCheckCannotBlock() throws StreamReadException, DatabindException, IOException {
        createKing(PlayerColor.WHITE, new Point2D(0, 0));
        createRook(PlayerColor.BLACK, new Point2D(0, 1));
        
        // Friendly piece
        createRook(PlayerColor.WHITE, new Point2D(5, 5));

        List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        assertTrue(blockers.isEmpty());
    }

    @Test
    void testPinnedPieceCannotBlock() throws StreamReadException, DatabindException, IOException {
        // White King at 0,0
        createKing(PlayerColor.WHITE, new Point2D(0, 0));
        
        // Attacker 1 (Rook) at 0,7 (Vertical check)
        createRook(PlayerColor.BLACK, new Point2D(0, 7));
        
        // Friendly Rook at 0,3 (already blocking? No, wait. 
        // If Rook is at 0,3, it IS blocking. So King is NOT in check from 0,7.
        // Let's set up a different scenario.
        
        // Check from Horizontal (Rook at 7,0).
        createRook(PlayerColor.BLACK, new Point2D(7, 0));
        
        // Friendly Rook at 0,3.
        // And ANOTHER Enemy Rook at 0,7.
        // The Friendly Rook at 0,3 is PINNED by the Enemy Rook at 0,7.
        // If Friendly Rook moves to 3,0 to block the horizontal check, 
        // the King (0,0) is exposed to the Vertical check from 0,7.
        createRook(PlayerColor.WHITE, new Point2D(0, 3));
        createRook(PlayerColor.BLACK, new Point2D(0, 7)); // Pins the white rook

        // Now, King is under attack by Rook at 7,0.
        // Can White Rook at 0,3 move to say 3,0 to block?
        // Valid moves for White Rook include (0,0)-(0,7) vertical and (0,3)-(7,3) horizontal.
        // Moving to (something, 0) is impossible for a Rook at (0,3) unless it captures?
        // Wait, Rook at (0,3) can move to (0,X) or (X,3).
        // To block check on row 0 (from 7,0), it needs to move to (X,0).
        // It cannot move to (X,0) from (0,3) in one move! 
        // A Rook moves straight. 
        // So this scenario doesn't test pinning.
        
        // Let's use a Queen or Bishop for the pinned piece.
        // King at 0,0.
        // Check from Rook at 7,0.
        // Pinned piece: White Queen at 3,3.
        // Pinning piece: Black Bishop at 7,7. (Diagonal pin).
        // Queen can move to 3,0 to block the Rook?
        // Queen at 3,3 can move to 3,0.
        // But moving to 3,0 exposes King to Bishop at 7,7?
        // 0,0 -> 3,3 -> 7,7 is a diagonal. Yes.
        
        board = new ChessBoardImpl(); // Reset
        createKing(PlayerColor.WHITE, new Point2D(0, 0));
        createRook(PlayerColor.BLACK, new Point2D(7, 0)); // Checking horizontally
        createBishop(PlayerColor.BLACK, new Point2D(7, 7)); // Pinning diagonally
        createQueen(PlayerColor.WHITE, new Point2D(3, 3)); // Pinned piece
        
        // Verify Queen valid moves includes 3,0 (it should).
        // But CheckCalculator should exclude it.
        
        List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        // The Queen at 3,3 cannot move to 3,0 because it would expose King to Bishop.
        // Are there other blockers? No.
        assertTrue(blockers.isEmpty());
    }

    @Test
    void testPrioritization() throws StreamReadException, DatabindException, IOException {
        createKing(PlayerColor.WHITE, new Point2D(0, 0));
        createRook(PlayerColor.BLACK, new Point2D(0, 7)); // Vertical check
        
        // Path: 0,1 to 0,6.
        
        // 1. Knight at 2,3 (jumps to 0,2) - Weight 3
        createKnight(PlayerColor.WHITE, new Point2D(2, 3));
        
        // 2. Rook at 5,4 (moves to 0,4) - Weight 5
        // Path: 4,4; 3,4; 2,4; 1,4; 0,4. All free.
        createRook(PlayerColor.WHITE, new Point2D(5, 4));
        
        // 3. Queen at 2,5 (moves to 0,5) - Weight 9
        // Path: 1,5; 0,5. All free.
        createQueen(PlayerColor.WHITE, new Point2D(2, 5));

        List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        
        assertEquals(3, blockers.size());
        
        assertEquals(PieceType.INFERIOR, blockers.get(0).getType()); // Knight
        assertEquals(PieceType.TOWER, blockers.get(1).getType()); // Rook
        assertEquals(PieceType.SUPERIOR, blockers.get(2).getType()); // Queen
    }
    
    // --- Helper Methods ---

    private Piece createPiece(final PlayerColor color, final int id, final PieceDefinition p) throws StreamReadException, DatabindException, IOException {
        return new Piece.Builder().entityDefinition(p).gameId(id).playerColor(color).build();
    } // modificare in modo che ritorna l'oggetto stesso, board.setEntity nel test stesso

    private Piece createKing(PlayerColor color) throws StreamReadException, DatabindException, IOException {
        final PieceDefinition king = mapper.readValue(new File(PIECES_PATH + "King.json"), PieceDefinition.class);
        return createPiece(color, 1, king);
    }

    private Piece createRook(PlayerColor color) throws StreamReadException, DatabindException, IOException {
        final PieceDefinition rook = mapper.readValue(new File(PIECES_PATH + "Rook.json"), PieceDefinition.class);
        return createPiece(color, 2, rook);
    }

    private Piece createBishop(PlayerColor color) throws StreamReadException, DatabindException, IOException {
        final PieceDefinition bishop = mapper.readValue(new File(PIECES_PATH + "Bishop.json"), PieceDefinition.class);
        return createPiece(color, 3, bishop); // Bishop weight 3
    }

    private Piece createQueen(PlayerColor color) throws StreamReadException, DatabindException, IOException {
        final PieceDefinition queen = mapper.readValue(new File(PIECES_PATH + "Queen.json"), PieceDefinition.class);
        return createPiece(color, 4, queen);
    }

    private Piece createKnight(PlayerColor color) throws StreamReadException, DatabindException, IOException {
        final PieceDefinition knight = mapper.readValue(new File(PIECES_PATH + "Knight.json"), PieceDefinition.class);
        return createPiece(color, 5, knight); // Knight weight 3
    }
}
