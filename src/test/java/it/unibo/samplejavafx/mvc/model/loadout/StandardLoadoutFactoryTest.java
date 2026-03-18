package it.unibo.samplejavafx.mvc.model.loadout;

import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StandardLoadoutFactoryTest {

    @Test
    void createStandardHasExpectedPiecesAndPositions() {
        final Loadout loadout = StandardLoadoutFactory.createStandard();

        assertEquals("Standard Chess", loadout.getName());
        assertEquals(16, loadout.getEntries().size());

        final var positions = new HashSet<Point2D>();
        loadout.getEntries().forEach(e -> positions.add(e.position()));
        assertEquals(16, positions.size());

        assertTrue(loadout.getEntries().stream().allMatch(e -> "StandardChessPieces".equals(e.packId())));

        assertTrue(loadout.getEntries().stream().anyMatch(e -> "king".equals(e.pieceId()) && new Point2D(4, 7).equals(e.position())));
        assertTrue(loadout.getEntries().stream().anyMatch(e -> "queen".equals(e.pieceId()) && new Point2D(3, 7).equals(e.position())));
        assertTrue(loadout.getEntries().stream().anyMatch(e -> "rook".equals(e.pieceId()) && new Point2D(0, 7).equals(e.position())));
        assertTrue(loadout.getEntries().stream().anyMatch(e -> "rook".equals(e.pieceId()) && new Point2D(7, 7).equals(e.position())));
        assertTrue(loadout.getEntries().stream().anyMatch(e -> "knight".equals(e.pieceId()) && new Point2D(1, 7).equals(e.position())));
        assertTrue(loadout.getEntries().stream().anyMatch(e -> "knight".equals(e.pieceId()) && new Point2D(6, 7).equals(e.position())));
        assertTrue(loadout.getEntries().stream().anyMatch(e -> "bishop".equals(e.pieceId()) && new Point2D(2, 7).equals(e.position())));
        assertTrue(loadout.getEntries().stream().anyMatch(e -> "bishop".equals(e.pieceId()) && new Point2D(5, 7).equals(e.position())));

        for (int x = 0; x < 8; x++) {
            final var pos = new Point2D(x, 6);
            assertTrue(loadout.getEntries().stream().anyMatch(e -> "pawn".equals(e.pieceId()) && pos.equals(e.position())));
        }
    }
}
