package it.unibo.samplejavafx.mvc.controller.LoaderController;

import it.unibo.samplejavafx.mvc.model.entity.Entity;

import java.util.Map;

public interface LoaderController {
    Map<String, Map<String, Entity>> getEntityCache();
}
