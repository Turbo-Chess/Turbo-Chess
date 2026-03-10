package it.unibo.samplejavafx.mvc.model.properties;

import lombok.Getter;

import java.io.File;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * placeholder.
 */
public enum GameProperties {
    USER_HOME_FOLDER(System.getProperty("user.home")),
    ROOT_RESOURCE_FOLDER(getAppDataFolder()),
    EXTERNAL_MOD_FOLDER("file:" + Paths.get(ROOT_RESOURCE_FOLDER.getPath(), "Mods").toString()),
    EXTERNAL_ASSETS_FOLDER("file:" + Paths.get(ROOT_RESOURCE_FOLDER.getPath(), "Assets").toString()),
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
        String path;
        if (os.contains("win")) {
            path = System.getenv("APPDATA") + File.separator + "TurboChess";
        } else if (os.contains("mac")) {
            path = userHome + File.separator + "Library" + File.separator + "Application Support" + File.separator + "TurboChess";
        } else {
            // Linux and others
            final String xdgConfigHome = System.getenv("XDG_CONFIG_HOME");
            if (xdgConfigHome != null && !xdgConfigHome.isEmpty()) {
                path = xdgConfigHome + File.separator + "turbochess";
            } else {
                path = userHome + File.separator + ".config" + File.separator + "turbochess";
            }
        }

        // Fallback check: if the primary path is not writable or cannot be created, use a hidden folder in user home
        final File folder = new File(path);
        try {
            // If folder exists, check if writable. If not, check if parent is writable to create it.
            boolean canUse = false;
            if (folder.exists()) {
                canUse = folder.canWrite();
            } else {
                File parent = folder.getParentFile();
                canUse = parent != null && parent.exists() && parent.canWrite();
            }

            if (canUse) {
                return path;
            }
        } catch (SecurityException e) {
            // Ignore and fallback
        }

        return userHome + File.separator + ".turbochess";
    }
}
