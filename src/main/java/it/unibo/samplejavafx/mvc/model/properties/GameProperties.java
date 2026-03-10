package it.unibo.samplejavafx.mvc.model.properties;

import lombok.Getter;

import java.io.File;
import java.nio.file.Paths;
import java.util.Locale;

// TODO: Add javadoc
/**
 * placeholder.
 */
public enum GameProperties {
    ROOT_RESOURCE_FOLDER(getAppDataFolder()),
    EXTERNAL_MOD_FOLDER("file:" + Paths.get(ROOT_RESOURCE_FOLDER.getPath(), "Mods")),
    EXTERNAL_ASSETS_FOLDER("file:" + Paths.get(ROOT_RESOURCE_FOLDER.getPath(), "Assets")),
    SAVES_FOLDER(Paths.get(ROOT_RESOURCE_FOLDER.getPath(), "saves").toString()),
    LOADOUTS_FOLDER(Paths.get(ROOT_RESOURCE_FOLDER.getPath(), "loadouts").toString()),
    INTERNAL_ASSETS_FOLDER("classpath:/assets"),
    INTERNAL_ENTITIES_FOLDER("classpath:/EntityResources");

    @Getter
    private final String path;

    GameProperties(final String path) {
        this.path = path;
    }

    private static String getAppDataFolder() {
        final String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        final String userHome = System.getProperty("user.home");
        if (os.contains("win")) {
            return System.getenv("APPDATA") + File.separator + "TurboChess";
        } else if (os.contains("mac")) {
            return userHome + File.separator + "Library" + File.separator + "Application Support" + File.separator + "TurboChess";
        } else {
            // Linux and others
            final String xdgConfigHome = System.getenv("XDG_CONFIG_HOME");
            if (xdgConfigHome != null && !xdgConfigHome.isEmpty()) {
                return xdgConfigHome + File.separator + "turbochess";
            }
            return userHome + File.separator + ".config" + File.separator + "turbochess";
        }
    }
}
