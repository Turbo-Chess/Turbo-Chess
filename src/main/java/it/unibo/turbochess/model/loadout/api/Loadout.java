package it.unibo.turbochess.model.loadout.api;

import it.unibo.turbochess.model.entity.definition.PieceDefinition;
import it.unibo.turbochess.model.loadout.impl.LoadoutEntry;

import java.util.List;
import java.util.Map;

/**
 * Immutable description of a player's initial board configuration.
 *
 * <p>
 * A {@code Loadout} contains metadata (id, name, timestamps) and a list of entries describing which pieces should
 * spawn, and where.
 * </p>
 */
public interface Loadout {
    /**
     * @return the unique identifier of the loadout
     */
    String getId();

    /**
     * @return the display name of the loadout
     */
    String getName();

    /**
     * @return the creation timestamp (epoch millis)
     */
    long getCreatedAt();

    /**
     * @return the last update timestamp (epoch millis)
     */
    long getUpdatedAt();

    /**
     * @return an immutable copy of the entries composing this loadout
     */
    List<LoadoutEntry> getEntries();

    /**
     * Returns a copy of this loadout with a different name.
     *
     * @param newName the new loadout name
     * @return a new {@link Loadout} instance with the updated name and refreshed {@code updatedAt}
     */
    Loadout withName(String newName);

    /**
     * Returns a copy of this loadout with different entries.
     *
     * @param newEntries the new entries to use
     * @return a new {@link Loadout} instance with the updated entries and refreshed {@code updatedAt}
     */
    Loadout withEntries(List<LoadoutEntry> newEntries);

    /**
     * Duplicates this loadout into a new one, generating a new id and timestamps.
     *
     * @param newName the name of the duplicated loadout
     * @return a new {@link Loadout} with the same entries as this one
     */
    Loadout duplicate(String newName);

    /**
     * Creates a mirrored version of this loadout for the opposite side of the board.
     *
     * @return a new {@link Loadout} containing mirrored entries
     */
    Loadout mirrored();

    /**
     * Validates the loadout against game rules and a standard configuration.
     *
     * @param definitions the definitions of the pieces
     * @param standardLoadout the standard loadout to compare against
     * @return {@code true} if the loadout is valid with respect to the standard configuration
     */
    boolean isValid(Map<String, PieceDefinition> definitions, Loadout standardLoadout);
}
