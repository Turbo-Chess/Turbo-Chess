package it.unibo.samplejavafx.mvc.controller.uicontroller;

import javafx.event.ActionEvent;

/**
 * The {@link LoadoutEditor} interface handles the UI needed to create custom loadouts.
 */
public interface LoadoutEditor {

    /**
     * Handles the "Loadout Selector" button action.
     * 
     * @param e the {@link ActionEvent} linked to the button.
     */
    void backToLoadoutSelector(final ActionEvent e);
}
