package it.unibo.samplejavafx.mvc.controller.gamecontroller;

import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderController;
import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderControllerImpl;
import it.unibo.samplejavafx.mvc.controller.movecontroller.MoveCache;
import it.unibo.samplejavafx.mvc.controller.movecontroller.MoveCacheImpl;
import it.unibo.samplejavafx.mvc.controller.uicontroller.ChessboardViewController;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * placeholder.
 */
public final class GameControllerImpl implements GameController {
    private static final List<String> PATHS = List.of("src/main/resources/EntityResources/");
    @Getter
    private final LoaderController loaderController = new LoaderControllerImpl(PATHS);
    private final MoveCache moveCache = new MoveCacheImpl();
    @Setter
    private ChessMatch match;
    @Setter
    private ChessboardViewController chessboardViewController;

    private Point2D lastPointClicked;
    private final Set<Point2D> lastPossibleMoves = new HashSet<>();

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

    @Override
    public void handleClick(final Point2D pointClicked) {
        // TODO: implement using the cache
        // TODO: think of using sets instead of lists

        // final Set<Point2D> moves = new HashSet<>(match.getBoard().getEntity(pointClicked).get()
        //     .asMoveable().get().getValidMoves(pointClicked, match.getBoard()));
        final Set<Point2D> result = new HashSet<>(match.getTurnHandler().thinking(pointClicked));

        if (result.isEmpty()) {
            chessboardViewController.hideMovementCells(lastPossibleMoves);
        } else if (result.size() == 1 && pointClicked.equals(lastPointClicked)) {
            chessboardViewController.hideMovementCells(lastPossibleMoves);
        } else {
            chessboardViewController.hideMovementCells(lastPossibleMoves);
            chessboardViewController.showMovementCells(result);
        }

        lastPointClicked = pointClicked;
        lastPossibleMoves.clear();
        lastPossibleMoves.addAll(result);
    }
}
