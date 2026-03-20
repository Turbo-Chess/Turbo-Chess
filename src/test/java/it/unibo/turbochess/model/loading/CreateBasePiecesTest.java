// CHECKSTYLE: MagicNumber OFF

package it.unibo.turbochess.model.loading;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.unibo.turbochess.model.entity.definition.PieceDefinition;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class made to be able to create the JSON files for the basic chess pieces implementation
 * and test that the loading for that is also correct.
 */
class CreateBasePiecesTest {
    private static final String PIECES_PATH = "src/main/resources/EntityResources/StandardChessPieces/pieces/";
    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    /**
     * Tests the loading of the Pawn piece definition from JSON.
     *
     * <p>
     * Verifies that the loaded object is not null and that its properties 
     * (ID, name, weight, move rules) match selected expected values.
     * </p>
     *
     * @throws IOException if the file cannot be read.
     */
    @Test
    void testLoadPawnFromJson() throws IOException {
        final PieceDefinition pawn = mapper.readValue(
                new File(PIECES_PATH + "Pawn.json"), 
                PieceDefinition.class);
        assertNotNull(pawn);
        assertEquals("pawn", pawn.getId());
        assertEquals("Pawn", pawn.getName());
        assertEquals(1, pawn.getWeight());
        assertEquals(4, pawn.getMoveRules().size());
    }

    /**
     * Tests the loading of the Knight piece definition from JSON.
     *
     * <p>
     * Verifies that the loaded object is not null and that its properties (ID,
     * name, weight, move rules) match selected expected values.
     * </p>
     *
     * @throws IOException if the file cannot be read.
     */
    @Test
    void testLoadKnightFromJson() throws IOException {
        final PieceDefinition knight = mapper.readValue(
                new File(PIECES_PATH + "Knight.json"),
                PieceDefinition.class);
        assertNotNull(knight);
        assertEquals("knight", knight.getId());
        assertEquals("Knight", knight.getName());
        assertEquals(3, knight.getWeight());
        assertEquals(8, knight.getMoveRules().size());
    }

    /**
     * Tests the loading of the Bishop piece definition from JSON.
     *
     * <p>
     * Verifies that the loaded object is not null and that its properties (ID,
     * name, weight, move rules) match selected expected values.
     * </p>
     *
     * @throws IOException if the file cannot be read.
     */
    @Test
    void testLoadBishopFromJson() throws IOException {
        final PieceDefinition bishop = mapper.readValue(
                new File(PIECES_PATH + "Bishop.json"), 
                PieceDefinition.class);
        assertNotNull(bishop);
        assertEquals("bishop", bishop.getId());
        assertEquals("Bishop", bishop.getName());
        assertEquals(3, bishop.getWeight());
        assertEquals(4, bishop.getMoveRules().size());
    }

    /**
     * Tests the loading of the Rook piece definition from JSON.
     *
     * <p>
     * Verifies that the loaded object is not null and that its properties (ID,
     * name, weight, move rules) match selected expected values.
     * </p>
     *
     * @throws IOException if the file cannot be read.
     */
    @Test
    void testLoadRookFromJson() throws IOException {
        final PieceDefinition rook = mapper.readValue(
                new File(PIECES_PATH + "Rook.json"), 
                PieceDefinition.class);
        assertNotNull(rook);
        assertEquals("rook", rook.getId());
        assertEquals("Rook", rook.getName());
        assertEquals(5, rook.getWeight());
        assertEquals(4, rook.getMoveRules().size());
    }

    /**
     * Tests the loading of the Queen piece definition from JSON.
     *
     * <p>
     * Verifies that the loaded object is not null and that its properties (ID,
     * name, weight, move rules) match selected expected values.
     * </p>
     *
     * @throws IOException if the file cannot be read.
     */
    @Test
    void testLoadQueenFromJson() throws IOException {
        final PieceDefinition queen = mapper.readValue(
                new File(PIECES_PATH + "Queen.json"),
                PieceDefinition.class);
        assertNotNull(queen);
        assertEquals("queen", queen.getId());
        assertEquals("Queen", queen.getName());
        assertEquals(9, queen.getWeight());
        assertEquals(8, queen.getMoveRules().size());
    }

    /**
     * Tests the loading of the King piece definition from JSON.
     *
     * <p>
     * Verifies that the loaded object is not null and that its properties
     * (ID, name, weight, move rules) match selected expected values.
     * </p>
     *
     * @throws IOException if the file cannot be read.
     */
    @Test
    void testLoadKingFromJson() throws IOException {
        final PieceDefinition king = mapper.readValue(new File(
                    PIECES_PATH + "King.json"),
                PieceDefinition.class);
        assertNotNull(king);
        assertEquals("king", king.getId());
        assertEquals("King", king.getName());
        assertEquals(100, king.getWeight());
        assertEquals(8, king.getMoveRules().size());
    }
}

// CHECKSTYLE: MagicNumber ON
