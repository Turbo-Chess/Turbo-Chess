package it.unibo.samplejavafx.mvc.controller.uicontroller;

import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;

/**
 * The {@link PromotionController} interface handles the promotion UI by exposing the method to initialize it.
 */
public interface PromotionController {
    
    /**
     * Initializes the UI to choose the new {@link Piece}.
     * 
     * @param currentColor the {@link PlayerColor} of the player promoting his pawn.
     */
    void init(PlayerColor currentColor);
}
