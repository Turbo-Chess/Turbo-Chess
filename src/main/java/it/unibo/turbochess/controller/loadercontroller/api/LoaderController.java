package it.unibo.turbochess.controller.loadercontroller.api;

import it.unibo.turbochess.model.loader.impl.DefinitionCacheEntry;

import java.util.List;

/**
 * The {@code LoaderController} interface manages the loading and caching of game resources,
 * specifically entity definitions such as pieces.
 *
 * <p>
 * It acts as a central loader for these definitions, allowing other components (like factories)
 * to retrieve entity blueprints by their pack and ID without needing to access the filesystem directly.
 * </p>
 */
public interface LoaderController {
    /**
     * Initiates the resource loading process.
     *
     * <p>
     * Scans configured directories for resource packs, parses entity definition files,
     * and populates the internal cache. This method is typically called during application startup.
     * </p>
     */
    void load();

    /**
     * Retrieves the cache of loaded entity definitions entries.
     *
     * <p>
     * The structure is a list of {@link DefinitionCacheEntry}. In that way other classes can get those data
     * and store them as they want.
     * </p>
     *
     * @return a {@link List} providing access to all loaded {@link DefinitionCacheEntry} objects.
     */
    List<DefinitionCacheEntry> getEntityDefinitionCacheEntries();
}
