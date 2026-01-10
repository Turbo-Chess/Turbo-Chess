package it.unibo.samplejavafx.mvcexample.model;

import java.nio.file.Path;

public class AbstractEntity implements Entity {
    private final String id;
    private final String name;
    private final Path imagePath;
    private final boolean isWhite;

    AbstractEntity(final String id, final String name, final Path path, final boolean whiteColor) {
        this.id = id;
        this.name = name;
        this.imagePath = path;
        this.isWhite = whiteColor;
    }

    public String getId() {
        return this.id;
    }

    public String name() {
        return this.name;
    }

    public boolean isWhite() {
        return this.isWhite;
    }

    public Path getImagePath() {
        return this.imagePath;
    }
}
