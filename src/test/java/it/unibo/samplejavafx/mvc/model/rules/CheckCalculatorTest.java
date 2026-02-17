package it.unibo.samplejavafx.mvc.model.rules;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckCalculatorTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String PIECES_PATH = "src/main/resources/EntityResources/StandardChessPieces/pieces/";
    private ChessBoard board;
    private int idCount;

    @BeforeEach
    void setUp() {
        board = new ChessBoardImpl();
        idCount = 0;
    }

    @Test
    void testHorizontalCheckInterposition() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(0, 0), createKing(PlayerColor.WHITE));
        board.setEntity(new Point2D(7, 0), createRook(PlayerColor.BLACK));
        board.setEntity(new Point2D(4, 4), createRook(PlayerColor.WHITE));

        final List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        assertEquals(1, blockers.size());
        assertEquals(new Point2D(4, 4), board.getPosByEntity(blockers.get(0)));
    }

    @Test
    void testVerticalCheckInterposition() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(4, 0), createKing(PlayerColor.WHITE));
        board.setEntity(new Point2D(4, 7), createQueen(PlayerColor.BLACK));
        board.setEntity(new Point2D(2, 2), createBishop(PlayerColor.WHITE));

        final List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        assertEquals(1, blockers.size());
        assertEquals(new Point2D(2, 2), board.getPosByEntity(blockers.get(0)));
    }

    @Test
    void testDiagonalCheckInterposition() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(0, 0), createKing(PlayerColor.WHITE));
        board.setEntity(new Point2D(7, 7), createBishop(PlayerColor.BLACK));
        board.setEntity(new Point2D(0, 5), createRook(PlayerColor.WHITE));

        final List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        assertEquals(1, blockers.size());
        assertEquals(new Point2D(0, 5), board.getPosByEntity(blockers.get(0)));
    }

    @Test
    void testKnightCheckCannotBlock() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(4, 4), createKing(PlayerColor.WHITE));
        board.setEntity(new Point2D(6, 5), createKnight(PlayerColor.BLACK));
        board.setEntity(new Point2D(4, 5), createRook(PlayerColor.WHITE));

        final List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        assertTrue(blockers.isEmpty());
    }

    @Test
    void testAdjacentCheckCannotBlock() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(0, 0), createKing(PlayerColor.WHITE));
        board.setEntity(new Point2D(0, 1), createRook(PlayerColor.BLACK));
        board.setEntity(new Point2D(5, 5), createRook(PlayerColor.WHITE));

        final List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        assertTrue(blockers.isEmpty());
    }

    @Test
    void testPinnedPieceCannotBlock() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(0, 0), createKing(PlayerColor.WHITE));
        board.setEntity(new Point2D(0, 7), createRook(PlayerColor.BLACK));
        board.setEntity(new Point2D(7, 0), createRook(PlayerColor.BLACK));
        board.setEntity(new Point2D(0, 3), createRook(PlayerColor.WHITE));
        board.setEntity(new Point2D(0, 7), createRook(PlayerColor.BLACK)); // Pins the white rook
        
        board = new ChessBoardImpl(); // Reset
        board.setEntity(new Point2D(0, 0), createKing(PlayerColor.WHITE));
        board.setEntity(new Point2D(7, 0), createRook(PlayerColor.BLACK));
        board.setEntity(new Point2D(7, 7), createBishop(PlayerColor.BLACK));
        board.setEntity(new Point2D(3, 3), createQueen(PlayerColor.WHITE));
        
        final List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        // The queen at 3,3 cannot move to 3,0 because it would expose the king to the bishop.
        assertTrue(blockers.isEmpty());
    }

    /*  
    void testPrioritization() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(0, 0), createKing(PlayerColor.WHITE));
        board.setEntity(new Point2D(0, 7), createRook(PlayerColor.BLACK));
        board.setEntity(new Point2D(2, 3), createKnight(PlayerColor.WHITE));
        board.setEntity(new Point2D(5, 4), createRook(PlayerColor.WHITE));
        board.setEntity(new Point2D(2, 5), createQueen(PlayerColor.WHITE));

        List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        
        assertEquals(3, blockers.size());
        
        assertEquals(PieceType.INFERIOR, blockers.stream()
                                                .filter(piece -> piece.getType() == PieceType.INFERIOR).findFirst().get().getType());
        assertEquals(PieceType.TOWER, blockers.stream()
                                                .filter(piece -> piece.getType() == PieceType.TOWER).findFirst().get().getType());
        assertEquals(PieceType.SUPERIOR, blockers.stream()
                                                .filter(piece -> piece.getType() == PieceType.SUPERIOR).findFirst().get().getType());
    }
    */

    private Piece createPiece(final PlayerColor color, final int id, final PieceDefinition p) throws StreamReadException, DatabindException, IOException {
        idCount += 1;
        return new Piece.Builder().entityDefinition(p).gameId(id).playerColor(color).build();
    }

    private Piece createKing(final PlayerColor color) throws StreamReadException, DatabindException, IOException {
        final PieceDefinition king = mapper.readValue(new File(PIECES_PATH + "King.json"), PieceDefinition.class);
        return createPiece(color, idCount, king);
    }

    private Piece createRook(final PlayerColor color) throws StreamReadException, DatabindException, IOException {
        final PieceDefinition rook = mapper.readValue(new File(PIECES_PATH + "Rook.json"), PieceDefinition.class);
        return createPiece(color, idCount, rook);
    }

    private Piece createBishop(final PlayerColor color) throws StreamReadException, DatabindException, IOException {
        final PieceDefinition bishop = mapper.readValue(new File(PIECES_PATH + "Bishop.json"), PieceDefinition.class);
        return createPiece(color, idCount, bishop);
    }

    private Piece createQueen(final PlayerColor color) throws StreamReadException, DatabindException, IOException {
        final PieceDefinition queen = mapper.readValue(new File(PIECES_PATH + "Queen.json"), PieceDefinition.class);
        return createPiece(color, idCount, queen);
    }

    private Piece createKnight(final PlayerColor color) throws StreamReadException, DatabindException, IOException {
        final PieceDefinition knight = mapper.readValue(new File(PIECES_PATH + "Knight.json"), PieceDefinition.class);
        return createPiece(color, idCount, knight);
    }
}
