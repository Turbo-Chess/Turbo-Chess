package it.unibo.samplejavafx.mvc.controller.loadercontroller;

import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.AbstractEntityDefinition;

import java.util.Map;

/**
 * The {@code LoaderController} interface manages the loading and caching of game resources,
 * specifically entity definitions such as pieces and power-ups.
 * <p>
 * It acts as a central repository for these definitions, allowing other components (like factories)
 * to retrieve entity blueprints by their pack and ID without needing to access the filesystem directly.
 * </p>
 */
public interface LoaderController {
    /**
     * Initiates the resource loading process.
     * <p>
     * Scans configured directories for resource packs, parses entity definition files,
     * and populates the internal cache. This method is typically called during application startup.
     * </p>
     */
    void load();

    /**
     * Retrieves the cache of loaded entity definitions.
     * <p>
     * The structure is a nested Map where the outer key is the resource pack name (e.g., "standard-chess")
     * and the inner key is the specific entity ID (e.g., "pawn") that is associated with the entity definition.
     * </p>
     *
     * @return a {@link Map} providing read access to all loaded {@link AbstractEntityDefinition} objects.
     */
    Map<String, Map<String, AbstractEntityDefinition>> getEntityCache();
}
