package it.unibo.samplejavafx.mvc.model.chessboard.boardfactory;

import it.unibo.samplejavafx.mvc.model.chessboard.BoardObserver;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * The {@code BoardFactory} interface defines a factory for creating and populating {@link ChessBoard} instances.
 *
 * <p>
 * It provides methods to generate a fully populated board based on player loadouts, as well as utility methods
 * for creating individual game pieces dynamically during a match (e.g., for pawn promotion).
 * </p>
 */
public interface BoardFactory extends PieceCreator, BoardCreator {

}
