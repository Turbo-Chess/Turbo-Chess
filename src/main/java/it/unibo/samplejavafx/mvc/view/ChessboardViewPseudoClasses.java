package it.unibo.samplejavafx.mvc.view;

import javafx.css.PseudoClass;

/**
 * placeholder.
 */
public final class ChessboardViewPseudoClasses {
    /**
     * placeholder.
     */
    public static final PseudoClass VALID_MOVEMENT_CELL = PseudoClass.getPseudoClass("valid-movement");
    public static final PseudoClass CHECK_KING = PseudoClass.getPseudoClass("check-king");
    public static final PseudoClass CHECKMATE_KING = PseudoClass.getPseudoClass("checkmate-king");
    public static final PseudoClass HASMOVED = PseudoClass.getPseudoClass("move-cell");
    public static final PseudoClass HASEAT = PseudoClass.getPseudoClass("eat-cell");
    public static final PseudoClass START = PseudoClass.getPseudoClass("start-cell");
    public static final PseudoClass VALID_CAPTURE_CELL = PseudoClass.getPseudoClass("valid-capture");

    private ChessboardViewPseudoClasses() {
    }
}
