package it.unibo.turbochess.model.chessboard.boardfactory.api;

import it.unibo.turbochess.model.chessboard.board.api.ChessBoard;

/**
 * The {@code BoardFactory} interface defines a factory for creating and populating {@link ChessBoard} instances.
 *
 * <p>
 * It provides methods to generate a fully populated board based on player loadouts, as well as utility methods
 * for creating individual game pieces dynamically during a match (e.g., for pawn promotion).
 * Those methods are defined in the {@link BoardCreator} and {@link PieceCreator}.
 * </p>
 */
public interface BoardFactory extends PieceCreator, BoardCreator {

}
