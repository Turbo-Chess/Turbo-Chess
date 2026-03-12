package it.unibo.samplejavafx.mvc.view;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderController;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutManager;

public interface ViewFactory {
    void showMainMenu(GameCoordinator gameCoordinator);
    void showSettings(final GameCoordinator gameCoordinator);
    void showLoadout(final GameController gameController, final GameCoordinator gameCoordinator, final LoadoutManager loadoutManager);
    void showLoadoutEditor(final GameCoordinator gameCoordinator, final LoaderController loaderController, final LoadoutManager loadoutManager);
    void initPromotion(final GameController gameController, final LoaderController loaderController);
    void initLoadGame(GameCoordinator gameCoordinator);
    void initGameUI(final GameController gameController, final GameCoordinator gameCoordinator);
    void showGame();
    void resetGame();
    void quit();
    boolean isGameBeingShown();
}
