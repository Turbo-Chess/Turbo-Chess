package it.unibo.samplejavafx.mvc.model.loadout;

import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating the standard chess loadout.
 */
public final class StandardLoadoutFactory {

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

        entries.add(new LoadoutEntry(new Point2D(0, backLines), packId, "rook"));
        entries.add(new LoadoutEntry(new Point2D(1, backLines), packId, "knight"));
        entries.add(new LoadoutEntry(new Point2D(2, backLines), packId, "bishop"));
        entries.add(new LoadoutEntry(new Point2D(3, backLines), packId, "queen"));
        entries.add(new LoadoutEntry(new Point2D(4, backLines), packId, "king"));
        entries.add(new LoadoutEntry(new Point2D(5, backLines), packId, "bishop"));
        entries.add(new LoadoutEntry(new Point2D(6, backLines), packId, "knight"));
        entries.add(new LoadoutEntry(new Point2D(7, backLines), packId, "rook"));   

        for (int x = 0; x < 8; x++) {
            entries.add(new LoadoutEntry(new Point2D(x, pawnsLines), packId, "pawn"));
        }

        return Loadout.create("Standard Chess", entries);
    }
}
