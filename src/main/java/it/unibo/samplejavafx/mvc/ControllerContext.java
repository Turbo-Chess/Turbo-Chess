package it.unibo.samplejavafx.mvc;

import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderController;
import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderControllerImpl;
import it.unibo.samplejavafx.mvc.model.chessboard.boardfactory.BoardFactory;
import it.unibo.samplejavafx.mvc.model.chessboard.boardfactory.BoardFactoryImpl;
import it.unibo.samplejavafx.mvc.model.chessboard.boardfactory.DefinitionCacheEntry;
import it.unibo.samplejavafx.mvc.model.chessboard.boardfactory.DefinitionRegistry;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutManager;

import java.util.List;

/**
 * Record acting as a dependency injection container for shared controller and model instances.
 *
 * <p>
 * This context is passed to the {@link it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController}
 * to provide access to singleton-like services such as the {@link LoaderController} and {@link LoadoutManager}.
 * </p>
 *
 * @param loadoutManager the manager for player loadouts.
 * @param boardFactory the factory for creating board entities.
 */
public record ControllerContext(
        LoadoutManager loadoutManager,
        BoardFactory boardFactory
) {
    /**
     * Creates a default {@link ControllerContext} with standard implementations.
     *
     * @return a new instance of {@link ControllerContext} initialized with default controllers.
     */
    public static ControllerContext createDefaultContext(final List<DefinitionCacheEntry> entityCache) {
        final LoaderController loaderController = new LoaderControllerImpl();
        final BoardFactory boardFactory = new BoardFactoryImpl(entityCache);

        return new ControllerContext(new LoadoutManager(), boardFactory);
    }
}
