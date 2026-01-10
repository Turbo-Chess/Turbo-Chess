package it.unibo.samplejavafx.mvc.model;

import it.unibo.samplejavafx.mvc.model.Entity.Entity;
import it.unibo.samplejavafx.mvc.model.Point2D.Point2D;

import java.util.Optional;

public class CellImpl implements Cell {
    private final Point2D pos;
    private final Optional<Entity> entity;

    public CellImpl(final Point2D pos) {
        entity = Optional.empty();
        this.pos = pos;
    }

    public Point2D getPos() {
        return this.pos;
    }

    public Optional<Entity> getEntity() {
        return this.entity;
    }

    @Override
    public boolean isFree() {
        return entity.isEmpty();
    }
}
