package it.unibo.turbochess.controller.uicontroller.api;

import javafx.event.ActionEvent;

/**
 * The {@link LoadoutSelector} interface handles the UI to select and use loadouts.
 */
public interface LoadoutSelector {

    /**
     * Handles the "Back" button action.
     *
     * @param e the {@link ActionEvent} linked to the button.
     */
    void backToMenu(ActionEvent e);

    /**
     * Handles the "Loadout Editor" button action.
     *
     * @param e the {@link ActionEvent} linked to the button.
     */
    void backToLoadoutEditor(ActionEvent e);
}
