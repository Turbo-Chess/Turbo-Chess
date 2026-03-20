package it.unibo.turbochess.model.piece;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.turbochess.model.entity.impl.Piece;
import it.unibo.turbochess.model.entity.impl.PieceType;
import it.unibo.turbochess.model.entity.impl.PlayerColor;
import it.unibo.turbochess.model.entity.definition.PieceDefinition;
import it.unibo.turbochess.model.movement.impl.MoveRulesImpl;
import it.unibo.turbochess.model.point2d.Point2D;
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
    private static final String PIECE_NAME = "test-piece";
    private static final String PIECE_ID = "test";

    /**
     * Tests the manual initialization of a {@link PieceDefinition}.
     *
     * <p>
     * Verifies that building a definition with valid data (name, ID, image path, weight, rules)
     * results in a correctly populated object.
     * </p>
     */
    @Test
    void testPieceDefinitionInitialization() {
        final PieceDefinition p = new PieceDefinition.Builder()
                .name(PIECE_NAME)
                .id(PIECE_ID)
                .imagePath("classpath:/assets/images/white_pawn.png")
                .weight(3)
                .pieceType(PieceType.INFERIOR)
                .moveRules(List.of(new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT,
                        MoveRulesImpl.MoveStrategy.JUMPING, false)))
                .build();

        assertEquals(PIECE_ID, p.getId());
        assertEquals(PIECE_NAME, p.getName());
        assertEquals(3, p.getWeight());
        assertEquals(1, p.getMoveRules().size());
    }

    /**
     * Tests the creation of a full {@link Piece} entity from a {@link PieceDefinition}.
     *
     * <p>
     * Verifies that a {@link Piece} can be built using a definition and game-specific data (gameId, color),
     * and that it inherits the definition's properties correctly.
     * </p>
     */
    @Test
    void testFullPieceCreation() {
        final PieceDefinition p = new PieceDefinition.Builder()
                .name(PIECE_NAME)
                .id(PIECE_ID)
                .imagePath("classpath:/assets/images/white_pawn.png")
                .weight(3)
                .pieceType(PieceType.INFERIOR)
                .moveRules(List.of(new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT,
                        MoveRulesImpl.MoveStrategy.JUMPING, false)))
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
     * <p>
     * Verifies that a {@link PieceDefinition} can be correctly deserialized from a standard JSON resource file,
     * and then used to create a valid {@link Piece} instance.
     * </p>
     *
     * @throws IOException if there is an error reading the JSON file.
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
