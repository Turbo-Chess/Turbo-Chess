package it.unibo.samplejavafx.mvc.controller.coordinator;

import ch.qos.logback.core.util.Loader;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameControllerImpl;
import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderController;
import it.unibo.samplejavafx.mvc.controller.uicontroller.ChessboardViewController;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GameCoordinator {
    private final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/GameLayout.fxml"));
    private final Stage stage;
    private ChessMatch match = new ChessMatchImpl();
    private GameController gameController = new GameControllerImpl();

    public GameCoordinator(final Stage stage) {
        this.stage = stage;
    }

    public void loadPieces() {
        gameController.getLoaderController().load();
        System.out.println(gameController.getLoaderController().getEntityCache());
    }

    public void initGame() throws IOException {
        final ChessMatch match = new ChessMatchImpl();
        Parent root = loader.load();
        final ChessboardViewController viewController = loader.getController();
        viewController.setMatch(this.match);
        viewController.setGameController(this.gameController);

        var cssLocation = getClass().getResource("/css/GameLayout.css");
        Scene scene = new Scene(root, 800, 800);
        stage.setTitle("TurboChess - Game");
        stage.setScene(scene);
        scene.getStylesheets().add(cssLocation.toExternalForm());
        stage.show();
        //Only for testing
        final PieceDefinition rook = new PieceDefinition.Builder()
                .name("Rook")
                .id("rook")
                .imagePath("/home/giacomo/Documents/pawn.jpg")
                .weight(5)
                .pieceType(PieceType.TOWER)
                .moveRules(List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING)
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
                .weight(100)
                .pieceType(PieceType.KING)
                .moveRules(List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(1, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(1, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-1, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-1, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING)
                ))
                .build();
        final Piece king = new Piece.Builder()
                .entityDefinition(kingDef)
                .gameId(2)
                .playerColor(PlayerColor.WHITE)
                .build();
        this.match.getBoard().setEntity(new Point2D(1, 1), piece);
        this.match.getBoard().setEntity(new Point2D(6, 7), king);
    }
}
