package it.unibo.turbochess.model.replay;

import it.unibo.turbochess.model.entity.impl.Piece;
import it.unibo.turbochess.model.entity.impl.PieceType;
import it.unibo.turbochess.model.entity.impl.PlayerColor;
import it.unibo.turbochess.model.entity.definition.PieceDefinition;
import it.unibo.turbochess.model.loadout.impl.LoadoutImpl;
import it.unibo.turbochess.model.movement.impl.MoveRulesImpl;
import it.unibo.turbochess.model.point2d.Point2D;
import it.unibo.turbochess.model.replay.api.ReplayManager;
import it.unibo.turbochess.model.replay.api.GameHistory;
import it.unibo.turbochess.model.replay.impl.GameHistoryImpl;
import it.unibo.turbochess.model.replay.impl.MoveEvent;
import it.unibo.turbochess.model.replay.impl.ReplayManagerImpl;
import it.unibo.turbochess.model.replay.impl.SpawnEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReplayManagerTest {

    @Test
    void testSaveGame(@TempDir final Path tempDir) throws IOException {
        final GameHistory history = new GameHistoryImpl();

        final PieceDefinition pawnDef = new PieceDefinition.Builder()
                .name("Pawn")
                .id("pawn")
                .imagePath("classpath:/assets/images/")
                .weight(1)
                .pieceType(PieceType.PAWN)
                .moveRules(List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_ONLY,
                                MoveRulesImpl.MoveStrategy.JUMPING, false)
                ))
                .build();

        final Piece pawn = new Piece.Builder()
                .entityDefinition(pawnDef)
                .gameId(1)
                .playerColor(PlayerColor.WHITE)
                .build();

        history.addEvent(new SpawnEvent(1, pawn, new Point2D(0, 1), 0, 0));
        history.addEvent(new MoveEvent(2, "Pawn", PlayerColor.WHITE, new Point2D(0, 1), new Point2D(0, 2), null, 0, 0));

        history.setWhiteLoadout(LoadoutImpl.create("White", List.of()));
        history.setBlackLoadout(LoadoutImpl.create("Black", List.of()));

        final ReplayManager manager = new ReplayManagerImpl();
        final Path saveFile = tempDir.resolve("save_test.json");

        final boolean result = manager.saveGame(history, saveFile);
        assertTrue(result, "Save should succeed");
    }
}
