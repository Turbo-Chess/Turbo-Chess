package it.unibo.samplejavafx.mvc.model.properties;

import lombok.Getter;

import java.io.File;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * Enum representing global game properties and file paths.
 *
 * <p>
 * This enum defines the standard directory structure used by the application
 * for storing data, configuration, and resources across different operating systems.
 * It handles the resolution of OS-specific application data paths.
 * </p>
 */
public enum GameProperties {
    /**
     * The root directory for the application's data storage.
     *
     * <p>
     * Resolved dynamically based on the current operating system:
     * - Windows: {@code %APPDATA%\TurboChess}
     * - macOS: {@code ~/Library/Application Support/TurboChess}
     * - Linux/Other: {@code $XDG_CONFIG_HOME/turbochess} or {@code ~/.config/turbochess}
     * </p>
     */
    ROOT_RESOURCE_FOLDER(getAppDataFolder()),

    /**
     * Directory for external user-created or downloaded mods.
     * Located within the root resource folder.
     */
    EXTERNAL_MOD_FOLDER("file:" + Paths.get(ROOT_RESOURCE_FOLDER.getPath(), "Mods")),

    /**
     * Directory for external assets.
     * Located within the root resource folder.
     */
    EXTERNAL_ASSETS_FOLDER("file:" + Paths.get(ROOT_RESOURCE_FOLDER.getPath(), "Assets")),

    /**
     * Directory where game save files are stored.
     * Located within the root resource folder.
     */
    SAVES_FOLDER(Paths.get(ROOT_RESOURCE_FOLDER.getPath(), "saves").toString()),

    /**
     * Directory where custom user loadouts are stored.
     * Located within the root resource folder.
     */
    LOADOUTS_FOLDER(Paths.get(ROOT_RESOURCE_FOLDER.getPath(), "loadouts").toString()),

    /**
     * Classpath reference to internal assets bundled with the application.
     */
    INTERNAL_ASSETS_FOLDER("classpath:/assets"),

    /**
     * Classpath reference to internal entity definitions bundled with the application.
     */
    INTERNAL_ENTITIES_FOLDER("classpath:/EntityResources");

    @Getter
    private final String path;

    /**
     * Constructs a GameProperty with the specified path.
     *
     * @param path the string representation of the path.
     */
    GameProperties(final String path) {
        this.path = path;
    }

    /**
     * Determines the operating system-specific application data folder.
     *
     * @return the absolute path to the application data folder.
     */
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
