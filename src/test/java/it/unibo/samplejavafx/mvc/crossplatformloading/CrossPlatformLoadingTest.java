package it.unibo.samplejavafx.mvc.crossplatformloading;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class CrossPlatformLoadingTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrossPlatformLoadingTest.class);
    private static final String BASEPATH = System.getProperty("user.home");
    private static final Path TCPATH = Paths.get(BASEPATH, "/.TurboChess/Mods");
    @Test
    void testHome() throws IOException {
        LOGGER.info(System.getProperty("user.home"));
        Files.createDirectories(TCPATH);
        LOGGER.info(Paths.get(BASEPATH, "/.TurboChess/Mods").toString());
    }
}
