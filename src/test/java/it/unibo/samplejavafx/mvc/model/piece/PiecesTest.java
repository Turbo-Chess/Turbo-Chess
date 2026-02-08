package it.unibo.samplejavafx.mvc.model.piece;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class to verify that a piece definition and a full piece can be instantiated and/or initialized
 * from a JSON file.
 */
class PiecesTest {
    @Test
    void testPieceDefinitionInitialization() {
        final PieceDefinition p = new PieceDefinition.Builder()
                .name("test-piece")
                .id("test")
                .imagePath("/home/giacomo/Documents/pawn.jpg")
                .weight(3)
                .pieceType(PieceType.INFERIOR)
                .moveRules(List.of(new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING)))
                .build();

        assertEquals("test", p.getId());
        assertEquals("test-piece", p.getName());
        assertEquals("/home/giacomo/Documents/pawn.jpg", p.getImagePath());
        assertEquals(3, p.getWeight());
        assertEquals(1, p.getMoveRules().size());
    }

    @Test
    void testFullPieceCreation() {
        final PieceDefinition p = new PieceDefinition.Builder()
                .name("test-piece")
                .id("test")
                .imagePath("/home/giacomo/Documents/pawn.jpg")
                .weight(3)
                .pieceType(PieceType.INFERIOR)
                .moveRules(List.of(new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING)))
                .build();
        final Piece piece = new Piece.Builder()
                .entityDefinition(p)
                .gameId(1)
                .playerColor(PlayerColor.BLACK)
                .build();
        assertNotNull(piece);
        assertEquals(p.getId(), piece.getId());
        assertEquals(p.getName(), piece.getName());
    }

    /**
     * Test initialization from resource JSON file.
     *
     * @throws IOException if there is an error reading the JSON file
     */
    @Test
    void testInitializationFromResourceJson() throws IOException {
        final String resourcePath = "/EntityResources/StandardChessPieces/pieces/Pawn.json";
        final ObjectMapper mapper = new ObjectMapper();
        try (var is = getClass().getResourceAsStream(resourcePath)) {
            assertNotNull(is, "Resource not found: " + resourcePath);
            final PieceDefinition def = mapper.readValue(is, PieceDefinition.class);
            assertNotNull(def);
            assertEquals("pawn", def.getId());
            assertEquals("Pawn", def.getName());
            final Piece piece = new Piece.Builder()
                .entityDefinition(def)
                .gameId(1)
                .playerColor(PlayerColor.BLACK)
                .build();
            assertNotNull(piece);
            assertEquals(def.getId(), piece.getId());
            assertEquals(def.getName(), piece.getName());
        }
    }
}
