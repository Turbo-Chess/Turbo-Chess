package it.unibo.turbochess.view.api;

import it.unibo.turbochess.controller.coordinator.api.GameCoordinator;
import it.unibo.turbochess.controller.gamecontroller.api.GameController;
import it.unibo.turbochess.model.chessboard.boardfactory.api.DefinitionRegistry;
import it.unibo.turbochess.model.loadout.api.LoadoutManager;

/**
 * Interface defining the contract for the View Factory.
 * The view factory is responsible for creating and managing the switch between scenes.
 * This interface could be implemented by different UI toolkits.
 */
public interface ViewFactory {

    /**
     * Shows the Main Menu scene.
     *
     * @param gameCoordinator the game coordinator instance
     */
    void showMainMenu(GameCoordinator gameCoordinator);

    /**
     * Shows the Settings scene.
     *
     * @param gameCoordinator the game coordinator instance
     */
    void showSettings(GameCoordinator gameCoordinator);

    /**
     * Shows the Loadout Selector scene.
     *
     * @param gameController    the game controller instance
     * @param gameCoordinator   the game coordinator instance
     * @param loadoutManager    the loadout manager instance
     */
    void showLoadout(GameController gameController, GameCoordinator gameCoordinator, LoadoutManager loadoutManager);

    /**
     * Shows the Loadout Editor scene.
     *
     * @param gameCoordinator   the game coordinator instance
     * @param entityCache       the entity definition registry
     * @param loadoutManager    the loadout manager instance
     */
    void showLoadoutEditor(GameCoordinator gameCoordinator, DefinitionRegistry entityCache, LoadoutManager loadoutManager);

    /**
     * Initializes and shows the Promotion dialog/scene.
     *
     * @param gameController    the game controller instance
     * @param entityCache       the entity definition registry
     * @param gameCoordinator   the game coordinator
     */
    void initPromotion(GameCoordinator gameCoordinator, GameController gameController, DefinitionRegistry entityCache);

    /**
     * Initializes and shows the Load Game scene.
     *
     * @param gameCoordinator the game coordinator instance
     */
    void initLoadGame(GameCoordinator gameCoordinator);

    /**
     * Initializes the Game UI (Chessboard and controls).
     *
     * @param gameController    the game controller instance
     * @param gameCoordinator   the game coordinator instance
     */
    void initGameUI(GameController gameController, GameCoordinator gameCoordinator);

    /**
     * Shows the Game scene (if initialized).
     */
    void showGame();

    /**
     * Resets the game view state (e.g. clears the cached game root).
     */
    void resetGame();

    /**
     * Quits the application.
     */
    void quit();

    /**
     * Checks if the game scene is currently initialized/shown.
     *
     * @return true if game view is active/initialized, false otherwise
     */
    boolean isGameBeingShown();
}
