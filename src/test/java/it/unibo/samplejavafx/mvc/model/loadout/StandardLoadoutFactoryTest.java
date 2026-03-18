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

        assertTrue(loadout.getEntries().stream().anyMatch(e -> e.pieceId().equals("king") && e.position().equals(new Point2D(4, 7))));
        assertTrue(loadout.getEntries().stream().anyMatch(e -> e.pieceId().equals("queen") && e.position().equals(new Point2D(3, 7))));
        assertTrue(loadout.getEntries().stream().anyMatch(e -> e.pieceId().equals("rook") && e.position().equals(new Point2D(0, 7))));
        assertTrue(loadout.getEntries().stream().anyMatch(e -> e.pieceId().equals("rook") && e.position().equals(new Point2D(7, 7))));
        assertTrue(loadout.getEntries().stream().anyMatch(e -> e.pieceId().equals("knight") && e.position().equals(new Point2D(1, 7))));
        assertTrue(loadout.getEntries().stream().anyMatch(e -> e.pieceId().equals("knight") && e.position().equals(new Point2D(6, 7))));
        assertTrue(loadout.getEntries().stream().anyMatch(e -> e.pieceId().equals("bishop") && e.position().equals(new Point2D(2, 7))));
        assertTrue(loadout.getEntries().stream().anyMatch(e -> e.pieceId().equals("bishop") && e.position().equals(new Point2D(5, 7))));

        for (int x = 0; x < 8; x++) {
            final var pos = new Point2D(x, 6);
            assertTrue(loadout.getEntries().stream().anyMatch(e -> e.pieceId().equals("pawn") && e.position().equals(pos)));
        }
    }
}

