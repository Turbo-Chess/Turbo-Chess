package it.unibo.samplejavafx.mvc.model.properties;

import lombok.Getter;

import java.nio.file.Paths;

public enum GameProperties {
    USER_HOME_FOLDER(System.getProperty("user.home")),
    ROOT_RESOURCE_FOLDER(Paths.get(USER_HOME_FOLDER.getPath(), "/.TurboChess").toString()),
    MOD_FOLDER(Paths.get(ROOT_RESOURCE_FOLDER.getPath(), "/Mods").toString()),
    ASSETS_FOLDER(Paths.get(ROOT_RESOURCE_FOLDER.getPath() + "/Assets").toString());

    @Getter
    private final String path;

    GameProperties(final String path) {
        this.path = path;
    }
}
