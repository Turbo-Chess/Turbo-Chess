package it.unibo.turbochess.controller.uicontroller.api;

import it.unibo.turbochess.model.entity.impl.Piece;
import it.unibo.turbochess.model.entity.impl.PlayerColor;

/**
 * The {@link PromotionController} interface handles the promotion UI by exposing the method to initialize it.
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface") // this interface is not meant to be used ad a functional interface.
public interface PromotionController {

    /**
     * Initializes the UI to choose the new {@link Piece}.
     * 
     * @param currentColor the {@link PlayerColor} of the player promoting his pawn.
     */
    void init(PlayerColor currentColor);
}
