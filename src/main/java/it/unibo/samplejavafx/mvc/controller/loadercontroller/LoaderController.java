package it.unibo.samplejavafx.mvc.controller.loadercontroller;

import it.unibo.samplejavafx.mvc.model.entity.Entity;

import java.util.Map;

/**
 * placeholder.
 */
public interface LoaderController {
    /**
     * placeholder.
     */
    void load();

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    Map<String, Map<String, Entity>> getEntityCache();
}
