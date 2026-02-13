package it.unibo.samplejavafx.mvc.controller.gamecontroller;

import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderController;
import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderControllerImpl;
import it.unibo.samplejavafx.mvc.controller.movecontroller.MoveCache;
import it.unibo.samplejavafx.mvc.controller.movecontroller.MoveCacheImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameControllerImpl implements GameController {
    private static final List<String> paths = List.of("src/main/resources/EntityResources/");
    @Getter
    private final LoaderController loaderController = new LoaderControllerImpl(paths);
    private final MoveCache moveCache = new MoveCacheImpl();



}
