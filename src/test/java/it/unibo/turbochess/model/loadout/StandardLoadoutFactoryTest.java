// CHECKSTYLE: MagicNumber: OFF

package it.unibo.turbochess.model.loadout;

import it.unibo.turbochess.model.loadout.api.Loadout;
import it.unibo.turbochess.model.loadout.impl.StandardLoadoutFactory;
import it.unibo.turbochess.model.point2d.Point2D;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StandardLoadoutFactoryTest {
    private static final String STANDARD_PACK_ID = "StandardChessPieces";
    private static final String KING_ID = "king";
    private static final String QUEEN_ID = "queen";
    private static final String ROOK_ID = "rook";
    private static final String KNIGHT_ID = "knight";
    private static final String BISHOP_ID = "bishop";
    private static final String PAWN_ID = "pawn";
    private static final int EXPECTED_ENTRIES = 16;
    private static final int BOARD_SIZE = 8;
    private static final int BACK_RANK = 7;
    private static final int PAWN_RANK = 6;
    private static final int ROOK_1_X = 0;
    private static final int KNIGHT_1_X = 1;
    private static final int BISHOP_1_X = 2;
    private static final int QUEEN_X = 3;
    private static final int KING_X = 4;
    private static final int BISHOP_2_X = 5;
    private static final int KNIGHT_2_X = 6;
    private static final int ROOK_2_X = 7;

    @Test
    void createStandardHasExpectedPiecesAndPositions() {
        final Loadout loadout = StandardLoadoutFactory.createStandard();

        assertEquals("Standard Chess", loadout.getName());
        assertEquals(EXPECTED_ENTRIES, loadout.getEntries().size());

        final var positions = new HashSet<Point2D>();
        loadout.getEntries().forEach(e -> positions.add(e.position()));
        assertEquals(EXPECTED_ENTRIES, positions.size());

        assertTrue(loadout.getEntries().stream().allMatch(e -> STANDARD_PACK_ID.equals(e.packId())));

        assertTrue(loadout.getEntries().stream().anyMatch(e ->
            KING_ID.equals(e.pieceId()) && new Point2D(KING_X, BACK_RANK).equals(e.position())
        ));
        assertTrue(loadout.getEntries().stream().anyMatch(e ->
            QUEEN_ID.equals(e.pieceId()) && new Point2D(QUEEN_X, BACK_RANK).equals(e.position())
        ));
        assertTrue(loadout.getEntries().stream().anyMatch(e ->
            ROOK_ID.equals(e.pieceId()) && new Point2D(ROOK_1_X, BACK_RANK).equals(e.position())
        ));
        assertTrue(loadout.getEntries().stream().anyMatch(e ->
            ROOK_ID.equals(e.pieceId()) && new Point2D(ROOK_2_X, BACK_RANK).equals(e.position())
        ));
        assertTrue(loadout.getEntries().stream().anyMatch(e ->
            KNIGHT_ID.equals(e.pieceId()) && new Point2D(KNIGHT_1_X, BACK_RANK).equals(e.position())
        ));
        assertTrue(loadout.getEntries().stream().anyMatch(e ->
            KNIGHT_ID.equals(e.pieceId()) && new Point2D(KNIGHT_2_X, BACK_RANK).equals(e.position())
        ));
        assertTrue(loadout.getEntries().stream().anyMatch(e ->
            BISHOP_ID.equals(e.pieceId()) && new Point2D(BISHOP_1_X, BACK_RANK).equals(e.position())
        ));
        assertTrue(loadout.getEntries().stream().anyMatch(e ->
            BISHOP_ID.equals(e.pieceId()) && new Point2D(BISHOP_2_X, BACK_RANK).equals(e.position())
        ));

        for (int x = 0; x < BOARD_SIZE; x++) {
            final var pos = new Point2D(x, PAWN_RANK);
            assertTrue(loadout.getEntries().stream().anyMatch(e -> PAWN_ID.equals(e.pieceId()) && pos.equals(e.position())));
        }
    }
}
// CHECKSTYLE: MagicNumber: ON
