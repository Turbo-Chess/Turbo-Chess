package it.unibo.turbochess.view.impl;

import javafx.css.PseudoClass;

/**
 * Collection of JavaFX {@link PseudoClass pseudo-classes} used by the chessboard view.
 *
 * <p>
 * These pseudo-classes are toggled on board cells to drive CSS-based highlights (valid moves, captures, checks).
 * </p>
 */
public final class ChessboardViewPseudoClasses {
    /**
     * Pseudo-class applied to cells that represent a valid movement destination.
     */
    public static final PseudoClass VALID_MOVEMENT_CELL = PseudoClass.getPseudoClass("valid-movement");

    /**
     * Pseudo-class applied to the king cell when the king is in check.
     */
    public static final PseudoClass CHECK_KING = PseudoClass.getPseudoClass("check-king");

    /**
     * Pseudo-class applied to the king cell when the king is checkmated.
     */
    public static final PseudoClass CHECKMATE_KING = PseudoClass.getPseudoClass("checkmate-king");

    /**
     * Pseudo-class applied to the origin cell of the last performed move.
     */
    public static final PseudoClass HASMOVED = PseudoClass.getPseudoClass("move-cell");

    /**
     * Pseudo-class applied to the destination cell of the last capture.
     */
    public static final PseudoClass HASEAT = PseudoClass.getPseudoClass("eat-cell");

    /**
     * Pseudo-class applied to the starting cell of a highlighted interaction (e.g., selected piece).
     */
    public static final PseudoClass START = PseudoClass.getPseudoClass("start-cell");

    /**
     * Pseudo-class applied to cells that represent a valid capture destination.
     */
    public static final PseudoClass VALID_CAPTURE_CELL = PseudoClass.getPseudoClass("valid-capture");

    private ChessboardViewPseudoClasses() {
    }
}
