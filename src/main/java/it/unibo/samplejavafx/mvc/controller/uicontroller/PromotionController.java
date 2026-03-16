package it.unibo.samplejavafx.mvc.controller.uicontroller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;

/**
 * The {@link PromotionController} interface handles the promotion UI by exposing the method to initialize it.
 */
@SuppressFBWarnings("PMD.ImplicitFunctionalInterface") // this interface is not meant to be used ad a functional interface.
public interface PromotionController {
    
    /**
     * Initializes the UI to choose the new {@link Piece}.
     * 
     * @param currentColor the {@link PlayerColor} of the player promoting his pawn.
     */
    void init(PlayerColor currentColor);
}
