package it.unibo.samplejavafx.mvc.model.entity;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@ToString
public class Piece extends AbstractEntity<PieceDefinition> implements Entity, Moveable {
    private final boolean hasMoved;
    @Deprecated
    private List<Point2D> availableCells = new ArrayList<>();

    public Piece(final PieceDefinition def , final int gameId, final PlayerColor playerColor, final boolean hasMoved) {
        super(def, gameId, playerColor);
        this.hasMoved = hasMoved;
    }

    @Override
    public List<Point2D> getValidMoves(Point2D start, ChessBoard board) {
        this.availableCells.clear();
        final List<Point2D> res = new ArrayList<>();
        for (var movement : super.getEntityDefinition().getMoveRules()) {
            availableCells.addAll(movement.getValidMoves(start, board, super.getPlayerColor()));
        }
        return Collections.unmodifiableList(this.availableCells);
    }

    @Override
    public List<Point2D> getAvailableCells() {
        return Collections.unmodifiableList(this.availableCells);
    }

    @Override
    public Optional<Moveable> asMoveable() {
        return Optional.of(this);
    }

    public boolean hasMoved() {
        return !this.hasMoved;
    }
}
