package it.unibo.samplejavafx.mvc.controller.gamecontroller;

import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderController;
import it.unibo.samplejavafx.mvc.controller.uicontroller.ChessboardViewController;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
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
     * @return placeholder.
     */
    public List<Point2D> handleClick(Point2D pointClicked);

    /**
     * placeholder.
     *
     * @param match placeholder.
     */
    public void setMatch(ChessMatch match);

    /**
     * placeholder.
     *
     * @param chessboardViewController placeholder.
     */
    public void setChessboardViewController(ChessboardViewController chessboardViewController);
}
