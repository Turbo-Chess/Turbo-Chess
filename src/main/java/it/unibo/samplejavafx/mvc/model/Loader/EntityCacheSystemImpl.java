package it.unibo.samplejavafx.mvc.model.Loader;

import it.unibo.samplejavafx.mvc.model.entity.Entity;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class EntityCacheSystemImpl implements EntityCacheSystem {
    private final Map<String, Entity> entityCache = new HashMap<>();

    @Override
    public void addEntity(final Entity entity, final String id) {
       entityCache.put(id, entity);
    }
}
