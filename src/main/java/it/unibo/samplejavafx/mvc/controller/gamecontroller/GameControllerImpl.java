package it.unibo.samplejavafx.mvc.controller.gamecontroller;

import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderController;
import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderControllerImpl;
import it.unibo.samplejavafx.mvc.controller.movecontroller.MoveCache;
import it.unibo.samplejavafx.mvc.controller.movecontroller.MoveCacheImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import lombok.Getter;

import java.util.List;

/**
 * placeholder.
 */
public final class GameControllerImpl implements GameController {
    private static final List<String> PATHS = List.of("src/main/resources/EntityResources/");
    @Getter
    private final LoaderController loaderController = new LoaderControllerImpl(PATHS);
    private final MoveCache moveCache = new MoveCacheImpl();

    @Override
    public List<Point2D> getAvailableCells(final int pieceGameId) {
        return moveCache.getAvailableCells(pieceGameId);
    }

    @Override
    public void cacheAvailableCells(final int pieceGameId, final List<Point2D> moves) {
        moveCache.cacheAvailableCells(pieceGameId, moves);
    }

    @Override
    public void clearCache() {
        moveCache.clearCache();
    }
}
