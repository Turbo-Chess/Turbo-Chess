package it.unibo.samplejavafx.mvc.model.replay;

import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReplayManagerTest {

    @Test
    void testSaveGame(@TempDir final Path tempDir) throws IOException {
        final GameHistory history = new GameHistory();
        
        final PieceDefinition pawnDef = new PieceDefinition.Builder()
                .name("Pawn")
                .id("pawn")
                .imagePath("classpath:/assets/images/white_pawn.png")
                .weight(1)
                .pieceType(PieceType.PAWN)
                .moveRules(List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_ONLY, MoveRulesImpl.MoveStrategy.JUMPING, false)
                ))
                .build();

        final Piece pawn = new Piece.Builder()
                .entityDefinition(pawnDef)
                .gameId(1)
                .playerColor(PlayerColor.WHITE)
                .build();


        history.addEvent(new SpawnEvent(1, pawn, new Point2D(0, 1)));
        history.addEvent(new MoveEvent(2, "Pawn", new Point2D(0, 1), new Point2D(0, 2)));

        history.setWhiteLoadout(it.unibo.samplejavafx.mvc.model.loadout.Loadout.create("White", List.of()));
        history.setBlackLoadout(it.unibo.samplejavafx.mvc.model.loadout.Loadout.create("Black", List.of()));

        final ReplayManager manager = new ReplayManager();
        final Path saveFile = tempDir.resolve("save_test.json");
        
        final boolean result = manager.saveGame(history, saveFile);
        assertTrue(result, "Save should succeed");
    }
}
