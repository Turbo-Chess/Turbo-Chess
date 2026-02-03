package it.unibo.samplejavafx.mvc.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.movement.JumpingMovement;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

public class LoaderTest {
    @Test
    void test() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Piece p = new Piece("test", "test-piece", 1, "/home/giacomo/Documents/pawn.jpg", PlayerColor.BLACK,
                3, PieceType.INFERIOR, List.of(
                new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, new JumpingMovement())
        ));

        System.out.println(gson.toJson(p));

    }
}
