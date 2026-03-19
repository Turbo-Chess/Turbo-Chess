package it.unibo.turbochess.controller.uicontroller.api;

import javafx.event.ActionEvent;

/**
 * The {@link LoadoutEditor} interface handles the UI needed to create custom loadouts.
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface") // this interface is not meant to be used ad a functional interface.
public interface LoadoutEditor {

    /**
     * Handles the "Loadout Selector" button action.
     * 
     * @param e the {@link ActionEvent} linked to the button.
     */
    void backToLoadoutSelector(ActionEvent e);
}
