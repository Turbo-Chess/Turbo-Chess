package it.unibo.samplejavafx.mvc.model.Entity;

import java.nio.file.Path;

public class PowerUp extends AbstractEntity {
    private final int duration;

    PowerUp(final String id, final String name, final Path path, final boolean whiteColor, final int duration) {
        super(id, name, path, whiteColor);
        this.duration = duration;
    }

    public void applyEffect() {
        // TODO: implement method
    }
}
