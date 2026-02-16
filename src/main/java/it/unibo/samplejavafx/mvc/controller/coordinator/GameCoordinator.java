package it.unibo.samplejavafx.mvc.controller.coordinator;

import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameControllerImpl;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.controller.uicontroller.ChessboardViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * placeholder.
 */
public final class GameCoordinator {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;
    private static final int ROOK_WEIGHT = 5;
    private static final int KING_WEIGHT = 100;
    private static final int KING_POS_X = 6;
    private static final int KING_POS_Y = 7;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameCoordinator.class);

    private final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/GameLayout.fxml"));
    private final Stage stage;
    private final ChessMatch match = new ChessMatchImpl();
    private final GameController gameController = new GameControllerImpl();

    /**
     * placeholder.
     *
     * @param stage placeholder.
     */
    public GameCoordinator(final Stage stage) {
        this.stage = stage;
    }

    /**
ì    * placeholder.
     */
    public void loadPieces() {
        gameController.getLoaderController().load();
        LOGGER.debug(gameController.getLoaderController().getEntityCache().toString());
    }

    /**
     * placeholder.
     *
     * @throws IOException placeholder.
     */
    public void initGame() throws IOException {
        final Parent root = loader.load();
        final ChessboardViewController viewController = loader.getController();
        viewController.setMatch(this.match);
        viewController.setGameController(this.gameController);

        final var cssLocation = getClass().getResource("/css/GameLayout.css");
        final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("TurboChess - Game");
        stage.setScene(scene);
        scene.getStylesheets().add(cssLocation.toExternalForm());
        stage.show();
        //Only for testing
        final PieceDefinition rook = new PieceDefinition.Builder()
                .name("Rook")
                .id("rook")
                .imagePath("/home/giacomo/Documents/pawn.jpg")
                .weight(ROOK_WEIGHT)
                .pieceType(PieceType.TOWER)
                .moveRules(List.of(
                        new MoveRulesImpl(new Point2D(0, 1),
                                MoveRulesImpl.MoveType.MOVE_AND_EAT,
                                MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(0, -1),
                                MoveRulesImpl.MoveType.MOVE_AND_EAT,
                                MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(1, 0),
                                MoveRulesImpl.MoveType.MOVE_AND_EAT,
                                MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(-1, 0),
                                MoveRulesImpl.MoveType.MOVE_AND_EAT,
                                MoveRulesImpl.MoveStrategy.SLIDING)
                ))
                .build();
        final Piece piece = new Piece.Builder()
                .entityDefinition(rook)
                .gameId(1)
                .playerColor(PlayerColor.BLACK)
                .build();

        final PieceDefinition kingDef = new PieceDefinition.Builder()
                .name("King")
                .id("king")
                .imagePath("/home/giacomo/Documents/king.jpg")
                .weight(KING_WEIGHT)
                .pieceType(PieceType.KING)
                .moveRules(List.of(
                        new MoveRulesImpl(new Point2D(0, 1),
                                MoveRulesImpl.MoveType.MOVE_AND_EAT,
                                MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(0, -1),
                                MoveRulesImpl.MoveType.MOVE_AND_EAT,
                                MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(1, 0),
                                MoveRulesImpl.MoveType.MOVE_AND_EAT,
                                MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-1, 0),
                                MoveRulesImpl.MoveType.MOVE_AND_EAT,
                                MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(1, 1),
                                MoveRulesImpl.MoveType.MOVE_AND_EAT,
                                MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(1, -1),
                                MoveRulesImpl.MoveType.MOVE_AND_EAT,
                                MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-1, 1),
                                MoveRulesImpl.MoveType.MOVE_AND_EAT,
                                MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-1, -1),
                                MoveRulesImpl.MoveType.MOVE_AND_EAT,
                                MoveRulesImpl.MoveStrategy.JUMPING)
                ))
                .build();
        final Piece king = new Piece.Builder()
                .entityDefinition(kingDef)
                .gameId(2)
                .playerColor(PlayerColor.WHITE)
                .build();

        final PieceDefinition bishopDef = new PieceDefinition.Builder()
                .name("Bishop")
                .id("bishop")
                .imagePath("/home/giacomo/Documents/bishop.jpg")
                .weight(3)
                .pieceType(PieceType.INFERIOR)
                .moveRules(List.of(
                        new MoveRulesImpl(new Point2D(-1, -1),
                                MoveRulesImpl.MoveType.MOVE_AND_EAT,
                                MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(-1, 1),
                                MoveRulesImpl.MoveType.MOVE_AND_EAT,
                                MoveRulesImpl.MoveStrategy.SLIDING)

                ))
                .build();
        final Piece bishop = new Piece.Builder()
                .entityDefinition(bishopDef)
                .gameId(3)
                .playerColor(PlayerColor.WHITE)
                .build();
        this.match.getBoard().setEntity(new Point2D(1, 1), piece);
        this.match.getBoard().setEntity(new Point2D(KING_POS_X, KING_POS_Y), king);
        this.match.getBoard().setEntity(new Point2D(4, 4), bishop);

    }
}
