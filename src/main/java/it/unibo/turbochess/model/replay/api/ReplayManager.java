package it.unibo.turbochess.model.replay.api;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Manages saving and loading of game replays.
 */
public interface ReplayManager {
    /**
     * Saves the game history to a file.
     *
     * @param history the history to save.
     * @param path the destination file path.
     * @return true if successful.
     * @throws IOException if an I/O error occurs.
     */
    boolean saveGame(GameHistory history, Path path) throws IOException;

    /**
     * Loads a game history from a file.
     *
     * @param path the source file path.
     * @return the loaded history.
     */
    GameHistory loadGame(Path path);
}
