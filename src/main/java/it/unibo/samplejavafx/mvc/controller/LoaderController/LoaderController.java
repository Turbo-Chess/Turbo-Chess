package it.unibo.samplejavafx.mvc.controller.LoaderController;

import it.unibo.samplejavafx.mvc.model.entity.Entity;

import java.util.Map;

public interface LoaderController {
    void load();
    Map<String, Map<String, Entity>> getEntityCache();
}
