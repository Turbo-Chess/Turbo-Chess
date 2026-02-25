package it.unibo.samplejavafx.mvc.model.loadout;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * placeHolder
 */
@Getter
@ToString
public class Loadout {
    private final String id;
    private final String name;
    private final long createdAt;
    private final long updatedAt;
    private final List<LoadoutEntry> entries;

    @JsonCreator
    public Loadout(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("createdAt") long createdAt,
            @JsonProperty("updatedAt") long updatedAt,
            @JsonProperty("entries") List<LoadoutEntry> entries) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.entries = entries;
    }

    /**
     * rename the loadout   
     */
    public Loadout withName(String newName) {
        return new Loadout(this.id, newName, this.createdAt, Instant.now().toEpochMilli(), this.entries);
    }

    /**
     * update the loadout entries
     */
    public Loadout withEntries(List<LoadoutEntry> newEntries) {
        return new Loadout(this.id, this.name, this.createdAt, Instant.now().toEpochMilli(), newEntries);
    }

    /**
     * duplicate the loadout with a new name
     */
    public Loadout duplicate(String newName) {
        return new Loadout(UUID.randomUUID().toString(), newName, Instant.now().toEpochMilli(), Instant.now().toEpochMilli(), this.entries);
    }

    private static int calculateWeight(List<LoadoutEntry> entries, Map<String, PieceDefinition> definitions) {
        return entries.stream()
                .filter(e -> definitions.containsKey(e.pieceId())) // filter for pieces not defined i.e. mod does not install
                .mapToInt(e -> definitions.get(e.pieceId()).getWeight())
                .sum();
    }

    /**
     * create the loadout version for the black player
     */
    public List<LoadoutEntry> mirrored() {
        final int boardHeight = 8;
        return getEntries().stream()
                .map(e -> new LoadoutEntry(e.position(), e.packId(), e.pieceId()))
                .map(e -> new LoadoutEntry(e.position().flipY(boardHeight), e.packId(), e.pieceId()))
                .toList();
    }

    /**
     * Validates the loadout against game rules.
     *
     */
    @JsonIgnore
    public boolean isValid(Map<String, PieceDefinition> definitions, int expectedWeight, Loadout standardLoadout) {
        int currentWeight = calculateWeight(this.entries, definitions);
        if (currentWeight != expectedWeight) {
            return false;
        }

        if (this.entries.size() != standardLoadout.getEntries().size()) {
            return false;
        }

        long kingCount = entries.stream()
                .filter(e -> definitions.containsKey(e.pieceId()))
                .filter(e -> definitions.get(e.pieceId()).getPieceType().equals(PieceType.KING))
                .count();

        if (kingCount != 1) {
            return false;
        }

        long distinctPositions = entries.stream()
                .map(LoadoutEntry::position)
                .distinct()
                .count();

        if (distinctPositions != entries.size()) {
            return false;
        }

        boolean allExist = entries.stream()
                .allMatch(e -> definitions.containsKey(e.pieceId()));

        if (!allExist) {
            return false;
        }

        List<Point2D> standardPositions = standardLoadout.getEntries().stream()
                .map(LoadoutEntry::position)
                .toList();

        boolean positionsMatch = entries.stream()
                .map(LoadoutEntry::position)
                .allMatch(standardPositions::contains);

        if (!positionsMatch) {
            return false;
        }

        for (LoadoutEntry entry : entries) {
            Point2D pos = entry.position();
            LoadoutEntry standardEntry = standardLoadout.getEntries().stream()
                    .filter(e -> e.position().equals(pos))
                    .findFirst().get();

            int entryWeight = definitions.get(entry.pieceId()).getWeight();
            int standardWeightVal = definitions.get(standardEntry.pieceId()).getWeight();

            if (entryWeight != standardWeightVal) {
                return false;
            }
        }

        return true;
    }

    /**
     * Factory method to create a new Loadout.
     */
    public static Loadout create(String name, List<LoadoutEntry> entries) {
        return new Loadout(UUID.randomUUID().toString(), name, Instant.now().toEpochMilli(), Instant.now().toEpochMilli(), entries);
    }
}
