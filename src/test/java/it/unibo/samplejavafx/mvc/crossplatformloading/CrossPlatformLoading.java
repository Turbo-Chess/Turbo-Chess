package it.unibo.samplejavafx.mvc.crossplatformloading;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CrossPlatformLoading {
    private static final String basePath = System.getProperty("user.home");
    private static final Path tcPath  = Paths.get(basePath, "/.TurboChess/Mods");
    @Test
    void testHome() throws IOException {
        System.out.println(System.getProperty("user.home"));
        Files.createDirectories(tcPath);
        System.out.println(Paths.get(basePath, "/.TurboChess/Mods"));
    }
}
