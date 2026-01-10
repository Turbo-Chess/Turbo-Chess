package it.unibo.samplejavafx.mvc.model.Entity;

import it.unibo.samplejavafx.mvc.model.Point2D.Point2D;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Piece extends AbstractEntity{
    private final int weight;
    private boolean hasMoved = false;
    private final List<Point2D> avaliableCells = new ArrayList<>();
    private final List<MoveRules> moveRules;

    Piece(final String id, final String name, final Path path, final boolean whiteColor, final int weight, final List<MoveRules> moveRules) {
        super(id, name, path, whiteColor);
        this.moveRules = moveRules;
        this.weight = weight;
    }

    public List<Point2D> getValidMoves() {
        // TODO: implement method
        return this.avaliableCells;
    }

    public int getWeight() {
        return weight;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved() {
        this.hasMoved = true;
    }
}
