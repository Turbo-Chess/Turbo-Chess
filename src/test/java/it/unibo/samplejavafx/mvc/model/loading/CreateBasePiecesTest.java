package it.unibo.samplejavafx.mvc.model.loading;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class made to be able to create the JSON files for the basic chess pieces implementation
 * and test that the loading for that is also correct.
 */
class CreateBasePiecesTest {
    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final String PIECES_PATH = "src/main/resources/EntityResources/StandardChessPieces/pieces/";

    /*@Test
    void createPawn() throws IOException {
        final PieceDefinition pawn = new PieceDefinition.Builder()
                .name("Pawn")
                .id("pawn")
                .imagePath(IMAGES_PATH + "white_pawn.png")
                .weight(1)
                .pieceType(PieceType.PAWN)
                .moveRules(List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_ONLY, MoveRulesImpl.MoveStrategy.JUMPING, false),
                        new MoveRulesImpl(new Point2D(-1, 1), MoveRulesImpl.MoveType.EAT_ONLY, MoveRulesImpl.MoveStrategy.JUMPING, false),
                        new MoveRulesImpl(new Point2D(1, 1), MoveRulesImpl.MoveType.EAT_ONLY, MoveRulesImpl.MoveStrategy.JUMPING, false)
                ))
                .build();
        mapper.writeValue(new File(PIECES_PATH + "Pawn.json"), pawn);
    }

    @Test
    void createKnight() throws IOException {
        final PieceDefinition knight = new PieceDefinition.Builder()
                .name("Knight")
                .id("knight")
                .imagePath(IMAGES_PATH + "white_knight.png")
                .weight(3)
                .pieceType(PieceType.INFERIOR)
                .moveRules(List.of(
                        new MoveRulesImpl(new Point2D(2, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING, false),
                        new MoveRulesImpl(new Point2D(2, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING, false),
                        new MoveRulesImpl(new Point2D(-2, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING, false),
                        new MoveRulesImpl(new Point2D(-2, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING, false),
                        new MoveRulesImpl(new Point2D(1, 2), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING, false),
                        new MoveRulesImpl(new Point2D(1, -2), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING, false),
                        new MoveRulesImpl(new Point2D(-1, 2), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING, false),
                        new MoveRulesImpl(new Point2D(-1, -2), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING, false)
                ))
                .build();
        mapper.writeValue(new File(PIECES_PATH + "Knight.json"), knight);
    }

    @Test
    void createBishop() throws IOException {
        final PieceDefinition bishop = new PieceDefinition.Builder()
                .name("Bishop")
                .id("bishop")
                .imagePath(IMAGES_PATH + "white_bishop.png")
                .weight(3)
                .pieceType(PieceType.INFERIOR)
                .moveRules(List.of(
                        new MoveRulesImpl(new Point2D(1, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING, false),
                        new MoveRulesImpl(new Point2D(1, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING, false),
                        new MoveRulesImpl(new Point2D(-1, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING, false),
                        new MoveRulesImpl(new Point2D(-1, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING, false)
                ))
                .build();
        mapper.writeValue(new File(PIECES_PATH + "Bishop.json"), bishop);
    }

    @Test
    void createRook() throws IOException {
        final PieceDefinition rook = new PieceDefinition.Builder()
                .name("Rook")
                .id("rook")
                .imagePath(IMAGES_PATH + "black_rook.png")
                .weight(5)
                .pieceType(PieceType.TOWER)
                .moveRules(List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING, false),
                        new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING, false),
                        new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING, false),
                        new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING, false)
                ))
                .build();
        mapper.writeValue(new File(PIECES_PATH + "Rook.json"), rook);
    }

    @Test
    void createQueen() throws IOException {
        final PieceDefinition queen = new PieceDefinition.Builder()
                .name("Queen")
                .id("queen")
                .imagePath(IMAGES_PATH + "white_queen.png")
                .weight(9)
                .pieceType(PieceType.SUPERIOR)
                .moveRules(List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING, false),
                        new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING, false),
                        new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING, false),
                        new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING, false),
                        new MoveRulesImpl(new Point2D(1, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING, false),
                        new MoveRulesImpl(new Point2D(1, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING, false),
                        new MoveRulesImpl(new Point2D(-1, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING, false),
                        new MoveRulesImpl(new Point2D(-1, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING, false)
                ))
                .build();
        mapper.writeValue(new File(PIECES_PATH + "Queen.json"), queen);
    }

    @Test
    void createKing() throws IOException {
        final PieceDefinition king = new PieceDefinition.Builder()
                .name("King")
                .id("king")
                .imagePath(IMAGES_PATH + "white_king.png")
                .weight(100)
                .pieceType(PieceType.KING)
                .moveRules(List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING, false),
                        new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING, false),
                        new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING, false),
                        new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING, false),
                        new MoveRulesImpl(new Point2D(1, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING, false),
                        new MoveRulesImpl(new Point2D(1, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING, false),
                        new MoveRulesImpl(new Point2D(-1, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING, false),
                        new MoveRulesImpl(new Point2D(-1, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING, false)
                ))
                .build();
        mapper.writeValue(new File(PIECES_PATH + "King.json"), king);
    }
*/
    @Test
    void testLoadPawnFromJson() throws IOException {
        final PieceDefinition pawn = mapper.readValue(new File(PIECES_PATH + "Pawn.json"), PieceDefinition.class);
        assertNotNull(pawn);
        assertEquals("pawn", pawn.getId());
        assertEquals("Pawn", pawn.getName());
        assertEquals(1, pawn.getWeight());
        assertEquals(4, pawn.getMoveRules().size());
    }

    @Test
    void testLoadKnightFromJson() throws IOException {
        final PieceDefinition knight = mapper.readValue(new File(PIECES_PATH + "Knight.json"), PieceDefinition.class);
        assertNotNull(knight);
        assertEquals("knight", knight.getId());
        assertEquals("Knight", knight.getName());
        assertEquals(3, knight.getWeight());
        assertEquals(8, knight.getMoveRules().size());
    }

    @Test
    void testLoadBishopFromJson() throws IOException {
        final PieceDefinition bishop = mapper.readValue(new File(PIECES_PATH + "Bishop.json"), PieceDefinition.class);
        assertNotNull(bishop);
        assertEquals("bishop", bishop.getId());
        assertEquals("Bishop", bishop.getName());
        assertEquals(3, bishop.getWeight());
        assertEquals(4, bishop.getMoveRules().size());
    }

    @Test
    void testLoadRookFromJson() throws IOException {
        final PieceDefinition rook = mapper.readValue(new File(PIECES_PATH + "Rook.json"), PieceDefinition.class);
        assertNotNull(rook);
        assertEquals("rook", rook.getId());
        assertEquals("Rook", rook.getName());
        assertEquals(5, rook.getWeight());
        assertEquals(4, rook.getMoveRules().size());
    }

    @Test
    void testLoadQueenFromJson() throws IOException {
        final PieceDefinition queen = mapper.readValue(new File(PIECES_PATH + "Queen.json"), PieceDefinition.class);
        assertNotNull(queen);
        assertEquals("queen", queen.getId());
        assertEquals("Queen", queen.getName());
        assertEquals(9, queen.getWeight());
        assertEquals(8, queen.getMoveRules().size());
    }

    @Test
    void testLoadKingFromJson() throws IOException {
        final PieceDefinition king = mapper.readValue(new File(PIECES_PATH + "King.json"), PieceDefinition.class);
        assertNotNull(king);
        assertEquals("king", king.getId());
        assertEquals("King", king.getName());
        assertEquals(100, king.getWeight());
        assertEquals(8, king.getMoveRules().size());
    }
}
