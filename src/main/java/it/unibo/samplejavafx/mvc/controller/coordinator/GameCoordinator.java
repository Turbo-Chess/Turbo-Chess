package it.unibo.samplejavafx.mvc.controller.coordinator;

import java.io.IOException;

public interface GameCoordinator {

    void initMainMenu();

    void initSettings();

    void initLoadout();

    void quit();

    void initGame() throws IOException;
}
