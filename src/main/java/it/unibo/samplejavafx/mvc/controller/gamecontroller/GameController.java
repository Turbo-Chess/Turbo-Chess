package it.unibo.samplejavafx.mvc.controller.gamecontroller;

import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderController;
import it.unibo.samplejavafx.mvc.controller.uicontroller.ChessboardViewController;
import it.unibo.samplejavafx.mvc.model.chessboard.boardfactory.BoardFactory;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutManager;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.List;

/**
 * placeholder.
 */
public interface GameController {
    /**
     * placeholder.
     *
     * @return placeholder.
     */
    LoaderController getLoaderController();

    /**
     * placeholder.
     *
     * @param match placeholder.
     */
    void setMatch(ChessMatch match);

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    LoadoutManager getLoadoutManager();

    /**
     * placeholder.
     *
     * @param pieceGameId placeholder.
     * @return placeholder.
     */
    List<Point2D> getAvailableCells(int pieceGameId);

    /**
     * placeholder.
     *
     * @param pieceGameId placeholder.
     * @param moves placeholder.
     */
    void cacheAvailableCells(int pieceGameId, List<Point2D> moves);

    /**
     * placeholder.
     */
    void clearCache();

    /**
     * placeholder.
     *
     * @param pointClicked placeholder.
     */
    void handleClick(Point2D pointClicked);

    /**
     * placeholder.
     *
     * @param chessboardViewController placeholder.
     */
    void setChessboardViewController(ChessboardViewController chessboardViewController);

    /**
     * placeholder.
     *
     * @param entity placeholder.
     * @return placeholder.
     */
    String calculateImageColorPath(Entity entity);

    /**
     * Sets the game state to checkmate.
     */
    void surrender();

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    Point2D getKingPos();

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    Loadout getWhiteLoadout();

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    Loadout getBlackLoadout();

    /**
     *
     * @return
     */
    BoardFactory getBoardFactory();
}
