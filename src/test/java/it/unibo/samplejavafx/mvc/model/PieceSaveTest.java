package it.unibo.samplejavafx.mvc.model;

import com.google.gson.*;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.movement.JumpingMovement;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PieceSaveTest {
    @Test
    void test() throws IOException {
        var jp = new JumpingMovement();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        Piece p = new Piece("test", "test-piece", 1, "/home/giacomo/Documents/pawn.jpg", PlayerColor.BLACK,
                3, List.of(
                new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING)
        ));

        try (FileWriter writer = new FileWriter("/home/giacomo/Documents/Universita/SecondoAnno/PrimoSemestre/ProgrammazioneOggetti/Turbo-Chess/src/main/resources/EntityResources/StandardChessPieces/Pawn.json")) {
            gson.toJson(p, writer);
        }
    }
}
