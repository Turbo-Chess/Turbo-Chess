package it.unibo.samplejavafx.mvc.model.loadout;

import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating the standard chess loadout.
 */
public final class StandardLoadoutFactory {

    private static final int ROOK_1_X = 0;
    private static final int KNIGHT_1_X = 1;
    private static final int BISHOP_1_X = 2;
    private static final int QUEEN_X = 3;
    private static final int KING_X = 4;
    private static final int BISHOP_2_X = 5;
    private static final int KNIGHT_2_X = 6;
    private static final int ROOK_2_X = 7;
    private static final int BOARD_SIZE = 8;

    private StandardLoadoutFactory() {
        // Utility class
    }

    /**
     * Creates the standard chess loadout.
     *
     * @return the standard loadout
     */
    public static Loadout createStandard() {
        final List<LoadoutEntry> entries = new ArrayList<>();
        final String packId = "StandardChessPieces";
        final int backLines = 7;
        final int pawnsLines = 6;

        entries.add(new LoadoutEntry(new Point2D(ROOK_1_X, backLines), packId, "rook"));
        entries.add(new LoadoutEntry(new Point2D(KNIGHT_1_X, backLines), packId, "knight"));
        entries.add(new LoadoutEntry(new Point2D(BISHOP_1_X, backLines), packId, "bishop"));
        entries.add(new LoadoutEntry(new Point2D(QUEEN_X, backLines), packId, "queen"));
        entries.add(new LoadoutEntry(new Point2D(KING_X, backLines), packId, "king"));
        entries.add(new LoadoutEntry(new Point2D(BISHOP_2_X, backLines), packId, "bishop"));
        entries.add(new LoadoutEntry(new Point2D(KNIGHT_2_X, backLines), packId, "knight"));
        entries.add(new LoadoutEntry(new Point2D(ROOK_2_X, backLines), packId, "rook"));

        for (int x = 0; x < BOARD_SIZE; x++) {
            entries.add(new LoadoutEntry(new Point2D(x, pawnsLines), packId, "pawn"));
        }

        return Loadout.create("Standard Chess", entries);
    }
}
