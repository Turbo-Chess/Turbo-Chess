package it.unibo.samplejavafx.mvc.model.handler;

import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.properties.GameProperties;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TurnHandlerTest {

    @Test
    void testCannotMoveWhiteAfterLoadIfBlackToPlay() {
        final var match = new ChessMatchImpl();

        final Piece whitePawn = new Piece.Builder()
            .moved(false)
            .entityDefinition(new PieceDefinition.Builder()
                .name("Pawn")
                .id("test-pawn")
                .imagePath(GameProperties.EXTERNAL_ASSETS_FOLDER.getPath())
                .weight(1)
                .pieceType(PieceType.PAWN)
                .moveRules(List.of(
                    new MoveRulesImpl(
                        new Point2D(0, 1),
                        MoveRulesImpl.MoveType.MOVE_ONLY,
                        MoveRulesImpl.MoveStrategy.STEPPING,
                        false
                    )
                ))
                .build())
            .gameId(0)
            .playerColor(PlayerColor.WHITE)
            .build();

        final Point2D start = new Point2D(0, 6);
        match.getBoard().setEntity(start, whitePawn);

        match.setPlayerColor(PlayerColor.BLACK);

        final var moves = match.getTurnHandler().thinking(start);
        assertTrue(moves.isEmpty(), "White must not be able to select/move when it is Black's turn");
    }

}
