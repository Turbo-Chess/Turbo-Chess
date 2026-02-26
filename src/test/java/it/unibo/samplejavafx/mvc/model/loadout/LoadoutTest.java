package it.unibo.samplejavafx.mvc.model.loadout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderControllerImpl;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * Tests for Loadout and LoadoutEntry.
 */
class LoadoutTest {

    private static final String ENTITY_RES_PATH = "file:src/main/resources/EntityResources/";
    private static final String STANDARD_LOADOUT_PATH = "src/main/resources/Loadouts/Standard.json";
    private static Map<String, PieceDefinition> standardDefinitions;
    private static Loadout standardLoadout;

    @BeforeAll
    static void setUp() throws IOException {
        final var loaderController = new LoaderControllerImpl(List.of(ENTITY_RES_PATH));
        loaderController.load();

        standardDefinitions = loaderController.getEntityCache().get("StandardChessPieces").entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        e -> (PieceDefinition) e.getValue()
                ));

        final ObjectMapper mapper = new ObjectMapper();
        final String json = Files.readString(Path.of(STANDARD_LOADOUT_PATH));
        standardLoadout = mapper.readValue(json, Loadout.class);
    }

    @Test
    void testInvalidWhenSameTotalWeightButDifferentIndividualWeights() {
        final Point2D rookPos = new Point2D(0, 7);
        final Point2D pawnPos = new Point2D(0, 6);
        
        final List<LoadoutEntry> modifiedEntries = standardLoadout.getEntries().stream()
                .map(e -> {
                    if (e.position().equals(rookPos)) {
                        return new LoadoutEntry(e.position(), e.packId(), "knight");
                    } else if (e.position().equals(pawnPos)) {
                        return new LoadoutEntry(e.position(), e.packId(), "bishop");
                    } else {
                        return e;
                    }
                })
                .toList();
        
        final Loadout modified = standardLoadout.withEntries(modifiedEntries);
        assertFalse(modified.isValid(standardDefinitions, standardLoadout));
    }

    @Test
    void testValidSwapWithSameWeight() {        
        final Point2D knightPos = new Point2D(1, 7);
        
        final List<LoadoutEntry> modifiedEntries = standardLoadout.getEntries().stream()
                .map(e -> e.position().equals(knightPos)
                        ? new LoadoutEntry(e.position(), e.packId(), "bishop")
                        : e)
                .toList();
        
        final Loadout modified = standardLoadout.withEntries(modifiedEntries);
        assertTrue(modified.isValid(standardDefinitions, standardLoadout));
    }

    @Test
    void testStandardLoadoutIsValid() {
        assertTrue(standardLoadout.isValid(standardDefinitions, standardLoadout));
    }

    @Test
    void testInvalidWhenWeightDifferent() {
        final List<LoadoutEntry> modifiedEntries = standardLoadout.getEntries().stream()
                .map(e -> e.position().equals(new Point2D(0, 6))
                        ? new LoadoutEntry(e.position(), e.packId(), "queen") 
                        : e)
                .toList();
        final Loadout modified = standardLoadout.withEntries(modifiedEntries);
        assertFalse(modified.isValid(standardDefinitions, standardLoadout));
    }

    @Test
    void testInvalidWhenMissingKing() {
        final List<LoadoutEntry> modifiedEntries = standardLoadout.getEntries().stream()
                .filter(e -> !"king".equals(e.pieceId()))
                .toList();
        final Loadout modified = standardLoadout.withEntries(modifiedEntries);
        assertFalse(modified.isValid(standardDefinitions, standardLoadout));
    }

    @Test
    void testInvalidWhenOverlappingPositions() {
        final List<LoadoutEntry> modifiedEntries = standardLoadout.getEntries().stream()
                .map(e -> "pawn".equals(e.pieceId())
                        ? new LoadoutEntry(new Point2D(0, 7), e.packId(), e.pieceId())
                        : e)
                .toList();
        final Loadout modified = standardLoadout.withEntries(modifiedEntries);
        assertFalse(modified.isValid(standardDefinitions, standardLoadout));
    }

    @Test
    void testMirroredLoadout() {
        final Loadout mirrored = standardLoadout.mirrored();            
        final List<LoadoutEntry> mirroredEntries = mirrored.getEntries();
        
        assertEquals(standardLoadout.getEntries().size(), mirroredEntries.size());
        
        // Check if positions are correctly flipped
        // Example: White Rook at (0, 7) should become Black Rook at (0, 0)
        final Point2D whiteRookPos = new Point2D(0, 7);
        final Point2D blackRookPos = new Point2D(0, 0);
        
        final boolean hasWhiteRook = standardLoadout.getEntries().stream()
                .anyMatch(e -> e.position().equals(whiteRookPos) && "rook".equals(e.pieceId()));
        
        final boolean hasBlackRook = mirroredEntries.stream()
                .anyMatch(e -> e.position().equals(blackRookPos) && "rook".equals(e.pieceId()));
                
        assertTrue(hasWhiteRook);
        assertTrue(hasBlackRook);
    }

}
