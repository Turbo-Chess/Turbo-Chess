package it.unibo.samplejavafx.mvc.model.Loader;

import it.unibo.samplejavafx.mvc.model.entity.Entity;

/**
 * Interface that models the cache in which the entities are load at the startup of the game.
 */
public interface EntityCacheSystem {
    void addEntity(Entity entity, String id);

    Entity getEntity(String id);
}
