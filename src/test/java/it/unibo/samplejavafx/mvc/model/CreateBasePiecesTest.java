package it.unibo.samplejavafx.mvc.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.loader.JsonViews;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreateBasePiecesTest {
    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final String PIECES_PATH = "src/main/resources/EntityResources/StandardChessPieces/pieces/";

    @Test
    void createPawn() throws IOException {
        final Piece pawn = new Piece(
                "pawn",
                "Pawn",
                0,
                "",
                PlayerColor.NONE,
                1,
                PieceType.PAWN,
                List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_ONLY, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-1, 1), MoveRulesImpl.MoveType.EAT_ONLY, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(1, 1), MoveRulesImpl.MoveType.EAT_ONLY, MoveRulesImpl.MoveStrategy.JUMPING)
                )
        );
        mapper.writerWithView(JsonViews.FirstLoading.class).writeValue(new File("src/main/resources/EntityResources/StandardChessPieces/pieces/Pawn.json"), pawn);
    }

    @Test
    void createKnight() throws IOException {
        final Piece knight = new Piece(
                "knight",
                "Knight",
                0,
                "",
                PlayerColor.NONE,
                3,
                PieceType.INFERIOR,
                List.of(
                        new MoveRulesImpl(new Point2D(2, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(2, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-2, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-2, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(1, 2), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(1, -2), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-1, 2), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-1, -2), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING)
                )
        );
        mapper.writerWithView(JsonViews.FirstLoading.class).writeValue(new File("src/main/resources/EntityResources/StandardChessPieces/pieces/Knight.json"), knight);
    }

    @Test
    void createBishop() throws IOException {
        final Piece bishop = new Piece(
                "bishop",
                "Bishop",
                0,
                "",
                PlayerColor.NONE,
                3,
                PieceType.INFERIOR,
                List.of(
                        new MoveRulesImpl(new Point2D(1, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(1, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(-1, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(-1, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING)
                )
        );
        mapper.writerWithView(JsonViews.FirstLoading.class).writeValue(new File("src/main/resources/EntityResources/StandardChessPieces/pieces/Bishop.json"), bishop);
    }

    @Test
    void createRook() throws IOException {
        final Piece rook = new Piece(
                "rook",
                "Rook",
                0,
                "",
                PlayerColor.NONE,
                5,
                PieceType.TOWER,
                List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING)
                )
        );
        mapper.writerWithView(JsonViews.FirstLoading.class).writeValue(new File("src/main/resources/EntityResources/StandardChessPieces/pieces/Rook.json"), rook);
    }

    @Test
    void createQueen() throws IOException {
        final Piece queen = new Piece(
                "queen",
                "Queen",
                0,
                "",
                PlayerColor.NONE,
                9,
                PieceType.SUPERIOR,
                List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(1, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(1, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(-1, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(-1, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING)
                )
        );
        mapper.writerWithView(JsonViews.FirstLoading.class).writeValue(new File("src/main/resources/EntityResources/StandardChessPieces/pieces/Queen.json"), queen);
    }

    @Test
    void createKing() throws IOException {
        final Piece king = new Piece(
                "king",
                "King",
                0,
                "",
                PlayerColor.NONE,
                100,
                PieceType.KING,
                List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(1, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(1, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-1, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-1, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING)
                )
        );
        mapper.writerWithView(JsonViews.FirstLoading.class).writeValue(new File("src/main/resources/EntityResources/StandardChessPieces/pieces/King.json"), king);
    }

    @Test
    void testLoadPawnFromJson() throws IOException {
        final Piece pawn = mapper.readerWithView(JsonViews.FirstLoading.class)
                .readValue(new File(PIECES_PATH + "Pawn.json"), Piece.class);
        assertNotNull(pawn);
        assertEquals("pawn", pawn.getId());
        assertEquals("Pawn", pawn.getName());
        assertEquals(1, pawn.getWeight());
        assertEquals(3, pawn.getMoveRules().size());
    }

    @Test
    void testLoadKnightFromJson() throws IOException {
        final Piece knight = mapper.readerWithView(JsonViews.FirstLoading.class)
                .readValue(new File(PIECES_PATH + "Knight.json"), Piece.class);
        assertNotNull(knight);
        assertEquals("knight", knight.getId());
        assertEquals("Knight", knight.getName());
        assertEquals(3, knight.getWeight());
        assertEquals(8, knight.getMoveRules().size());
    }

    @Test
    void testLoadBishopFromJson() throws IOException {
        final Piece bishop = mapper.readerWithView(JsonViews.FirstLoading.class)
                .readValue(new File(PIECES_PATH + "Bishop.json"), Piece.class);
        assertNotNull(bishop);
        assertEquals("bishop", bishop.getId());
        assertEquals("Bishop", bishop.getName());
        assertEquals(3, bishop.getWeight());
        assertEquals(4, bishop.getMoveRules().size());
    }

    @Test
    void testLoadRookFromJson() throws IOException {
        final Piece rook = mapper.readerWithView(JsonViews.FirstLoading.class)
                .readValue(new File(PIECES_PATH + "Rook.json"), Piece.class);
        assertNotNull(rook);
        assertEquals("rook", rook.getId());
        assertEquals("Rook", rook.getName());
        assertEquals(5, rook.getWeight());
        assertEquals(4, rook.getMoveRules().size());
    }

    @Test
    void testLoadQueenFromJson() throws IOException {
        final Piece queen = mapper.readerWithView(JsonViews.FirstLoading.class)
                .readValue(new File(PIECES_PATH + "Queen.json"), Piece.class);
        assertNotNull(queen);
        assertEquals("queen", queen.getId());
        assertEquals("Queen", queen.getName());
        assertEquals(9, queen.getWeight());
        assertEquals(8, queen.getMoveRules().size());
    }

    @Test
    void testLoadKingFromJson() throws IOException {
        final Piece king = mapper.readerWithView(JsonViews.FirstLoading.class)
                .readValue(new File(PIECES_PATH + "King.json"), Piece.class);
        assertNotNull(king);
        assertEquals("king", king.getId());
        assertEquals("King", king.getName());
        assertEquals(100, king.getWeight());
        assertEquals(8, king.getMoveRules().size());
    }
}
