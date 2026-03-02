package it.unibo.samplejavafx.mvc.controller.gamecontroller;

import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderController;
import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderControllerImpl;
import it.unibo.samplejavafx.mvc.controller.movecontroller.MoveCache;
import it.unibo.samplejavafx.mvc.controller.movecontroller.MoveCacheImpl;
import it.unibo.samplejavafx.mvc.controller.uicontroller.ChessboardViewController;
import it.unibo.samplejavafx.mvc.model.chessboard.boardfactory.BoardFactory;
import it.unibo.samplejavafx.mvc.model.chessboard.boardfactory.BoardFactoryImpl;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutManager;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.properties.GameProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * placeholder.
 */
@NoArgsConstructor(force = true)
public final class GameControllerImpl implements GameController {
    private static final List<String> PATHS = List.of(
            GameProperties.INTERNAL_ENTITIES_FOLDER.getPath(),
            GameProperties.EXTERNAL_MOD_FOLDER.getPath());

    // Will the taken from the selected loadout
    final String loadoutId = "standard-chess-loadout";
    @Getter
    private final LoaderController loaderController = new LoaderControllerImpl(PATHS);
    private final MoveCache moveCache = new MoveCacheImpl();
    @Getter
    private final LoadoutManager loadoutManager = new LoadoutManager();
    @Setter
    private ChessMatch match;
    @Setter
    private ChessboardViewController chessboardViewController;
    @Getter
    final Loadout whiteLoadout = loadoutManager.load(loadoutId).get();
    @Getter
    // Will be replaced with the effective black loadout, (for now is mirrored because the standard is the same
    final Loadout blackLoadout = loadoutManager.load(loadoutId).get().mirrored();
    @Getter
    private final BoardFactory boardFactory = new BoardFactoryImpl(loaderController);

    private Point2D lastPointClicked;
    private final Set<Point2D> lastPossibleMoves = new HashSet<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Point2D> getAvailableCells(final int pieceGameId) {
        return moveCache.getAvailableCells(pieceGameId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cacheAvailableCells(final int pieceGameId, final List<Point2D> moves) {
        moveCache.cacheAvailableCells(pieceGameId, moves);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearCache() {
        moveCache.clearCache();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleClick(final Point2D pointClicked) {
        // TODO: implement using the cache
        // TODO: think of using sets instead of lists

        if (this.match == null) {
            throw new IllegalStateException("Board should be initialized before using it");
        }

        // final Set<Point2D> moves = new HashSet<>(match.getBoard().getEntity(pointClicked).get()
        //     .asMoveable().get().getValidMoves(pointClicked, match.getBoard()));
        final Set<Point2D> result = new HashSet<>(match.getTurnHandler().thinking(pointClicked));

        // Added this check to silence the spot bugs error
        // The view is created and injected after the controller instantiation
        // so it needs to be injected by a setter later
        if (chessboardViewController != null) {
            if (result.isEmpty()) {
                chessboardViewController.hideMovementCells(lastPossibleMoves);
            } else if (result.size() == 1 && pointClicked.equals(lastPointClicked)) {
                chessboardViewController.hideMovementCells(lastPossibleMoves);
            } else {
                chessboardViewController.hideMovementCells(lastPossibleMoves);
                chessboardViewController.showMovementCells(result);
            }

            chessboardViewController.hideMovementCells(Set.of(pointClicked));
        }

        lastPointClicked = pointClicked;
        lastPossibleMoves.clear();
        lastPossibleMoves.addAll(result);
    }

    /**
     * placeholder.
     *
     * @param entity placeholder.
     * @return placeholder.
     */
    @Override
    public String calculateImageColorPath(final Entity entity) {
        final String color = entity.getPlayerColor() == PlayerColor.WHITE ? "white" : "black";
        return "file:" + entity.getImagePath() + "/" + color + "_" + entity.getId() + ".png";
    }

    @Override
    public void surrender() {
        if (this.match != null) {
            match.getTurnHandler().surrender();
        }
    }

    @Override
    public Point2D getKingPos() {
        if (this.match == null) {
            throw new IllegalStateException("Match should be initialized before using it");
        }
        return this.match.getBoard().getPosByEntity(this.match.getBoard().getBoard().inverse().keySet().stream()
                .filter(e -> e.getType() == PieceType.KING)
                // Get the king of the opposite player
                .filter(e -> e.getPlayerColor() != this.match.getCurrentPlayer())
                .findFirst().get()); // Impossible to not have a king of the specified color
    }
}
