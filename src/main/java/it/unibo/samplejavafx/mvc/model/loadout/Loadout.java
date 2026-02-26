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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
    public Loadout mirrored() {
        final int boardHeight = 8;
        final List<LoadoutEntry> mirroredEntries = getEntries().stream()
                .map(e -> new LoadoutEntry(e.position(), e.packId(), e.pieceId()))
                .map(e -> new LoadoutEntry(e.position().flipY(boardHeight), e.packId(), e.pieceId()))
                .toList();
        return Loadout.create(this.name + " (Mirrored)", mirroredEntries);
    }

    /**
     * validate the loadout against game rules
     * 
     * @param definitions the definitions of the pieces
     * @param expectedWeight the expected weight of the loadout
     * @param standardLoadout the standard loadout to compare against
     * @return true if the loadout is valid
     */
    @JsonIgnore
    public boolean isValid(Map<String, PieceDefinition> definitions, int expectedWeight, Loadout standardLoadout) {
        ValidationContext context = new ValidationContext(this.entries, definitions, expectedWeight, standardLoadout);
        
        return Stream.<Predicate<ValidationContext>>of(
                Loadout::validateWeight,
                Loadout::validateEntryCount,
                Loadout::validateKingCount,
                Loadout::validateDistinctPositions,
                Loadout::validateAllExist,
                Loadout::validatePositionsMatch,
                Loadout::validateWeightMatch
        ).allMatch(validator -> validator.test(context));
    }

    @JsonIgnore
    public boolean isValid(Map<String, PieceDefinition> definitions, Loadout standardLoadout) {
        int expected = calculateWeight(standardLoadout.getEntries(), definitions);
        return isValid(definitions, expected, standardLoadout);
    }
    private static final class ValidationContext {
        final List<LoadoutEntry> entries;
        final Map<String, PieceDefinition> definitions;
        final int expectedWeight;
        final Loadout standardLoadout;

        ValidationContext(List<LoadoutEntry> entries, Map<String, PieceDefinition> definitions,
                          int expectedWeight, Loadout standardLoadout) {
            this.entries = entries;
            this.definitions = definitions;
            this.expectedWeight = expectedWeight;
            this.standardLoadout = standardLoadout;
        }
    }

    /**
     * validate the weight of the loadout matches the expected weight
     */
    private static boolean validateWeight(ValidationContext ctx) {
        int currentWeight = calculateWeight(ctx.entries, ctx.definitions);
        return currentWeight == ctx.expectedWeight;
    }

    /**
     * validate the number of entries matches the standard loadout
     */
    private static boolean validateEntryCount(ValidationContext ctx) {
        return ctx.entries.size() == ctx.standardLoadout.getEntries().size();
    }

    /**
     * validate there is exactly one king in the loadout
     */
    private static boolean validateKingCount(ValidationContext ctx) {
        long kingCount = ctx.entries.stream()
                .filter(e -> ctx.definitions.containsKey(e.pieceId()))
                .filter(e -> ctx.definitions.get(e.pieceId()).getPieceType().equals(PieceType.KING))
                .count();
        return kingCount == 1;
    }

    /**
     * validate all positions are distinct
     */
    private static boolean validateDistinctPositions(ValidationContext ctx) {
        long distinctPositions = ctx.entries.stream()
                .map(LoadoutEntry::position)
                .distinct()
                .count();
        return distinctPositions == ctx.entries.size();
    }

    /**
     * validate all pieces exist in the definitions
     */
    private static boolean validateAllExist(ValidationContext ctx) {
        return ctx.entries.stream()
                .allMatch(e -> ctx.definitions.containsKey(e.pieceId()));
    }

    /**
     * validate the positions of the entries match the standard loadout
     */
    private static boolean validatePositionsMatch(ValidationContext ctx) {
        List<Point2D> standardPositions = ctx.standardLoadout.getEntries().stream()
                .map(LoadoutEntry::position)
                .toList();
        return ctx.entries.stream()
                .map(LoadoutEntry::position)
                .allMatch(standardPositions::contains);
    }

    /**
     * validate the weights of the entries match the standard loadout
     */
    private static boolean validateWeightMatch(ValidationContext ctx) {
        return ctx.entries.stream()
                .allMatch(entry -> {
                    Point2D pos = entry.position();
                    LoadoutEntry standardEntry = ctx.standardLoadout.getEntries().stream()
                            .filter(e -> e.position().equals(pos))
                            .findFirst().orElseThrow();

                    int entryWeight = ctx.definitions.get(entry.pieceId()).getWeight();
                    int standardWeightVal = ctx.definitions.get(standardEntry.pieceId()).getWeight();

                    return entryWeight == standardWeightVal;
                });
    }

    /**
     * Factory method to create a new Loadout.
     */
    public static Loadout create(String name, List<LoadoutEntry> entries) {
        return new Loadout(UUID.randomUUID().toString(), name, Instant.now().toEpochMilli(), Instant.now().toEpochMilli(), entries);
    }
}
