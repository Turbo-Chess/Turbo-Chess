package it.unibo.samplejavafx.mvc;

import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderController;
import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderControllerImpl;
import it.unibo.samplejavafx.mvc.model.chessboard.boardfactory.BoardFactory;
import it.unibo.samplejavafx.mvc.model.chessboard.boardfactory.BoardFactoryImpl;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutManager;

public record ControllerContext(
        LoaderController loaderController,
        LoadoutManager loadoutManager,
        BoardFactory boardFactory
) {
    public static ControllerContext createDefaultContext() {
        final LoaderController loaderController = new LoaderControllerImpl();
        final BoardFactory boardFactory = new BoardFactoryImpl(loaderController);

        return new ControllerContext(loaderController, new LoadoutManager(), boardFactory);
    }
}
