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
 * placeHolder.
 */
@Getter
@ToString
public class Loadout {
    private final String id;
    private final String name;
    private final long createdAt;
    private final long updatedAt;
    private final List<LoadoutEntry> entries;

    /**
     * placeholder.
     *
     * @param id placeholder.
     * @param name placeholder.
     * @param createdAt placeholder.
     * @param updatedAt placeholder.
     * @param entries placeholder.
     */
    @JsonCreator
    public Loadout(
            @JsonProperty("id") final String id,
            @JsonProperty("name") final String name,
            @JsonProperty("createdAt") final long createdAt,
            @JsonProperty("updatedAt") final long updatedAt,
            @JsonProperty("entries") final List<LoadoutEntry> entries) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.entries = entries;
    }

    /**
     * rename the loadout.
     *
     * @param newName placeholder.
     * @return placeholder.
     */
    public Loadout withName(final String newName) {
        return new Loadout(this.id, newName, this.createdAt, Instant.now().toEpochMilli(), this.entries);
    }

    /**
     * update the loadout entries.
     *
     * @param newEntries placeholder.
     * @return placeholder.
     */
    public Loadout withEntries(final List<LoadoutEntry> newEntries) {
        return new Loadout(this.id, this.name, this.createdAt, Instant.now().toEpochMilli(), newEntries);
    }

    /**
     * duplicate the loadout with a new name.
     *
     * @param newName placeholder.
     * @return placeholder.
     */
    public Loadout duplicate(final String newName) {
        return new Loadout(UUID.randomUUID().toString(), newName, Instant.now().toEpochMilli(),
                Instant.now().toEpochMilli(), this.entries);
    }

    private static int calculateWeight(final List<LoadoutEntry> entries, final Map<String, PieceDefinition> definitions) {
        return entries.stream()
                .filter(e -> definitions.containsKey(e.pieceId()))
                // filter for pieces not defined i.e. mod does not install
                .mapToInt(e -> definitions.get(e.pieceId()).getWeight())
                .sum();
    }

    /**
     * create the loadout version for the black player.
     *
     * @return placeholder.
     */
    public Loadout mirrored() {
        final int boardHeight = 8;
        final List<LoadoutEntry> mirroredEntries = getEntries().stream()
                .map(e -> new LoadoutEntry(e.position(), e.packId(), e.pieceId()))
                .map(e -> new LoadoutEntry(e.position().flipY(boardHeight), e.packId(), e.pieceId()))
                .toList();
        return create(this.name + " (Mirrored)", mirroredEntries);
    }

    /**
     * validate the loadout against game rules.
     *
     * @param definitions the definitions of the pieces
     * @param expectedWeight the expected weight of the loadout
     * @param standardLoadout the standard loadout to compare against
     * @return true if the loadout is valid
     */
    @JsonIgnore
    public boolean isValid(final Map<String, PieceDefinition> definitions, final int expectedWeight,
                           final Loadout standardLoadout) {
        final ValidationContext context = new ValidationContext(this.entries, definitions, expectedWeight, standardLoadout);

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

    /**
     * validate the loadout against game rules.
     *
     * @param definitions placeholder.
     * @param standardLoadout placeholder.
     * @return placeholder.
     */
    @JsonIgnore
    public boolean isValid(final Map<String, PieceDefinition> definitions, final Loadout standardLoadout) {
        final int expected = calculateWeight(standardLoadout.getEntries(), definitions);
        return isValid(definitions, expected, standardLoadout);
    }

    /**
     * Factory method to create a new Loadout.
     *
     * @param name placeholder.
     * @param entries placeholder.
     * @return placeholder.
     */
    public static Loadout create(final String name, final List<LoadoutEntry> entries) {
        return new Loadout(UUID.randomUUID().toString(), name, Instant.now().toEpochMilli(),
                Instant.now().toEpochMilli(), entries);
    }

    /**
     * validate the weight of the loadout matches the expected weight.
     *
     * @param ctx placeholder.
     * @return placeholder.
     */
    private static boolean validateWeight(final ValidationContext ctx) {
        final int currentWeight = calculateWeight(ctx.getEntries(), ctx.getDefinitions());
        return currentWeight == ctx.getExpectedWeight();
    }

    /**
     * validate the number of entries matches the standard loadout.
     *
     * @param ctx placeholder.
     * @return placeholder.
     */
    private static boolean validateEntryCount(final ValidationContext ctx) {
        return ctx.getEntries().size() == ctx.getStandardLoadout().getEntries().size();
    }

    /**
     * validate there is exactly one king in the loadout.
     *
     * @param ctx placeholder.
     * @return placeholder.
     */
    private static boolean validateKingCount(final ValidationContext ctx) {
        final long kingCount = ctx.getEntries().stream()
                .filter(e -> ctx.getDefinitions().containsKey(e.pieceId()))
                .filter(e -> ctx.getDefinitions().get(e.pieceId()).getPieceType().equals(PieceType.KING))
                .count();
        return kingCount == 1;
    }

    /**
     * validate all positions are distinct.
     *
     * @param ctx placeholder.
     * @return placeholder.
     */
    private static boolean validateDistinctPositions(final ValidationContext ctx) {
        final long distinctPositions = ctx.getEntries().stream()
                .map(LoadoutEntry::position)
                .distinct()
                .count();
        return distinctPositions == ctx.getEntries().size();
    }

    /**
     * validate all pieces exist in the definitions.
     *
     * @param ctx placeholder.
     * @return placeholder.
     */
    private static boolean validateAllExist(final ValidationContext ctx) {
        return ctx.getEntries().stream()
                .allMatch(e -> ctx.getDefinitions().containsKey(e.pieceId()));
    }

    /**
     * validate the positions of the entries match the standard loadout.
     *
     * @param ctx placeholder.
     * @return placeholder.
     */
    private static boolean validatePositionsMatch(final ValidationContext ctx) {
        final List<Point2D> standardPositions = ctx.getStandardLoadout().getEntries().stream()
                .map(LoadoutEntry::position)
                .toList();
        return ctx.getEntries().stream()
                .map(LoadoutEntry::position)
                .allMatch(standardPositions::contains);
    }

    /**
     * validate the weights of the entries match the standard loadout.
     *
     * @param ctx placeholder.
     * @return placeholder.
     */
    private static boolean validateWeightMatch(final ValidationContext ctx) {
        return ctx.getEntries().stream()
                .allMatch(entry -> {
                    final Point2D pos = entry.position();
                    final LoadoutEntry standardEntry = ctx.getStandardLoadout().getEntries().stream()
                            .filter(e -> e.position().equals(pos))
                            .findFirst().orElseThrow();

                    final int entryWeight = ctx.getDefinitions().get(entry.pieceId()).getWeight();
                    final int standardWeightVal = ctx.getDefinitions().get(standardEntry.pieceId()).getWeight();

                    return entryWeight == standardWeightVal;
                });
    }

    @Getter
    private static final class ValidationContext {
        private final List<LoadoutEntry> entries;
        private final Map<String, PieceDefinition> definitions;
        private final int expectedWeight;
        private final Loadout standardLoadout;

        ValidationContext(final List<LoadoutEntry> entries, final Map<String, PieceDefinition> definitions,
                          final int expectedWeight, final Loadout standardLoadout) {
            this.entries = entries;
            this.definitions = definitions;
            this.expectedWeight = expectedWeight;
            this.standardLoadout = standardLoadout;
        }
    }
}
