package it.unibo.turbochess.model.loadout.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.unibo.turbochess.model.chessboard.board.impl.ChessBoardImpl;
import it.unibo.turbochess.model.entity.impl.PieceType;
import it.unibo.turbochess.model.loadout.api.Loadout;
import it.unibo.turbochess.model.entity.definition.PieceDefinition;
import it.unibo.turbochess.model.point2d.Point2D;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Immutable description of a player's initial board configuration.
 *
 * <p>
 * A {@code Loadout} contains metadata (id, name, timestamps) and a list of {@link LoadoutEntry entries}
 * describing which pieces should spawn, and where.
 * </p>
 */
@Getter
@ToString
public class LoadoutImpl implements Loadout {
    private final String id;
    private final String name;
    private final long createdAt;
    private final long updatedAt;
    private final List<LoadoutEntry> entries;

    /**
     * Creates a loadout instance.
     *
     * <p>
     * This constructor is primarily used by JSON deserialization. The entries are defensively copied to
     * preserve immutability.
     * </p>
     *
     * @param id the unique identifier of the loadout
     * @param name the display name of the loadout
     * @param createdAt the creation timestamp (epoch millis)
     * @param updatedAt the last update timestamp (epoch millis)
     * @param entries the list of entries composing the loadout
     * @throws NullPointerException if {@code entries} is {@code null}
     */
    @JsonCreator
    public LoadoutImpl(
            @JsonProperty("id") final String id,
            @JsonProperty("name") final String name,
            @JsonProperty("createdAt") final long createdAt,
            @JsonProperty("updatedAt") final long updatedAt,
            @JsonProperty("entries") final List<LoadoutEntry> entries) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.entries = List.copyOf(entries);
    }

    /** {@inheritDoc} */
    @Override
    public LoadoutImpl withName(final String newName) {
        return new LoadoutImpl(
            this.id,
            newName,
            this.createdAt,
            Instant.now().toEpochMilli(),
            this.entries
        );
    }

    /** {@inheritDoc} */
    @Override
    public LoadoutImpl withEntries(final List<LoadoutEntry> newEntries) {
        return new LoadoutImpl(
            this.id,
            this.name,
            this.createdAt,
            Instant.now().toEpochMilli(),
            newEntries
        );
    }

    /** {@inheritDoc} */
    @Override
    public LoadoutImpl duplicate(final String newName) {
        return new LoadoutImpl(
            UUID.randomUUID().toString(),
            newName,
            Instant.now().toEpochMilli(),
            Instant.now().toEpochMilli(),
            this.entries
        );
    }

    /** {@inheritDoc} */
    @Override
    public LoadoutImpl mirrored() {
        final List<LoadoutEntry> mirroredEntries = getEntries().stream()
                .map(e -> new LoadoutEntry(e.position().flipY(ChessBoardImpl.CHESSBOARD_SIZE), e.packId(), e.pieceId()))
                .toList();
        return create(this.name + " (Mirrored)", mirroredEntries);
    }

    /** {@inheritDoc} */
    @Override
    public List<LoadoutEntry> getEntries() {
        return List.copyOf(this.entries);
    }

    /** {@inheritDoc} */
    @Override
    @JsonIgnore
    public boolean isValid(
        final Map<String, PieceDefinition> definitions,
        final Loadout standardLoadout
    ) {
        final int expected = calculateWeight(standardLoadout.getEntries(), definitions);
        return isValid(definitions, expected, standardLoadout);
    }

    private boolean isValid(
        final Map<String, PieceDefinition> definitions,
        final int expectedWeight,
        final Loadout standardLoadout
    ) {
        final ValidationContext context = new ValidationContext(this.entries, definitions, expectedWeight, standardLoadout);
        return Stream.<Predicate<ValidationContext>>of(
                LoadoutImpl::validateWeight,
                LoadoutImpl::validateEntryCount,
                LoadoutImpl::validateKingCount,
                LoadoutImpl::validateDistinctPositions,
                LoadoutImpl::validateAllExist,
                LoadoutImpl::validatePositionsMatch,
                LoadoutImpl::validateWeightMatch
        ).allMatch(validation -> validation.test(context));
    }

    /**
     * Factory method that creates a new loadout with a generated id and timestamps.
     *
     * @param name the name of the loadout
     * @param entries the entries composing the loadout
     * @return a new {@link Loadout} instance
     */
    public static LoadoutImpl create(final String name, final List<LoadoutEntry> entries) {
        return new LoadoutImpl(
            UUID.randomUUID().toString(),
            name,
            Instant.now().toEpochMilli(),
            Instant.now().toEpochMilli(),
            entries
        );
    }

    /**
     * Computes the total weight of the given entries using the provided piece definitions.
     *
     * <p>
     * Entries whose piece id is not present in {@code definitions} are ignored.
     * </p>
     *
     * @param entries the entries whose weight should be computed
     * @param definitions the available piece definitions keyed by piece id
     * @return the total weight of all entries that are present in {@code definitions}
     */
    private static int calculateWeight(final List<LoadoutEntry> entries, final Map<String, PieceDefinition> definitions) {
        return entries.stream()
                .map(entry -> definitions.get(entry.pieceId()))
                .filter(Objects::nonNull)
                .mapToInt(PieceDefinition::getWeight)
                .sum();
    }

    /**
     * validate the weight of the loadout matches the expected weight.
     *
     * @param ctx the validation context
     * @return {@code true} if the loadout total weight equals the expected one
     */
    private static boolean validateWeight(final ValidationContext ctx) {
        final int currentWeight = calculateWeight(ctx.getEntries(), ctx.getDefinitions());
        return currentWeight == ctx.getExpectedWeight();
    }

    /**
     * validate the number of entries matches the standard loadout.
     *
     * @param ctx the validation context
     * @return {@code true} if the number of entries matches the standard loadout
     */
    private static boolean validateEntryCount(final ValidationContext ctx) {
        return ctx.getEntries().size() == ctx.getStandardLoadout().getEntries().size();
    }

    /**
     * validate there is exactly one king in the loadout.
     *
     * @param ctx the validation context
     * @return {@code true} if exactly one king is present among the defined pieces
     */
    private static boolean validateKingCount(final ValidationContext ctx) {
        final long kingCount = ctx.getEntries().stream()
                .map(entry -> ctx.getDefinitions().get(entry.pieceId()))
                .filter(Objects::nonNull)
                .filter(definition -> definition.getPieceType().equals(PieceType.KING))
                .count();
        return kingCount == 1;
    }

    /**
     * validate all positions are distinct.
     *
     * @param ctx the validation context
     * @return {@code true} if no two entries share the same position
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
     * @param ctx the validation context
     * @return {@code true} if each entry refers to an existing definition
     */
    private static boolean validateAllExist(final ValidationContext ctx) {
        return ctx.getEntries().stream()
                .map(entry -> ctx.getDefinitions().get(entry.pieceId()))
                .allMatch(Objects::nonNull);
    }

    /**
     * validate the positions of the entries match the standard loadout.
     *
     * @param ctx the validation context
     * @return {@code true} if every entry position is present in the standard loadout
     */
    private static boolean validatePositionsMatch(final ValidationContext ctx) {
        final Set<Point2D> standardPositions = ctx.getStandardLoadout().getEntries().stream()
                .map(LoadoutEntry::position)
                .collect(Collectors.toUnmodifiableSet());
        return ctx.getEntries().stream()
                .map(LoadoutEntry::position)
                .allMatch(standardPositions::contains);
    }

    /**
     * validate the weights of the entries match the standard loadout.
     *
     * @param ctx the validation context
     * @return {@code true} if each entry at a given position has the same weight as the standard one
     */
    private static boolean validateWeightMatch(final ValidationContext ctx) {
        final Map<Point2D, LoadoutEntry> standardByPosition = ctx.getStandardLoadout().getEntries().stream()
                .collect(Collectors.toUnmodifiableMap(LoadoutEntry::position, Function.identity()));
        return ctx.getEntries().stream()
                .allMatch(entry -> {
                    final Point2D pos = entry.position();
                    final LoadoutEntry standardEntry = standardByPosition.get(pos);
                    if (standardEntry == null) {
                        return false;
                    }

                    final PieceDefinition entryDefinition = ctx.getDefinitions().get(entry.pieceId());
                    final PieceDefinition standardDefinition = ctx.getDefinitions().get(standardEntry.pieceId());
                    if (entryDefinition == null || standardDefinition == null) {
                        return false;
                    }

                    final int entryWeight = entryDefinition.getWeight();
                    final int standardWeightVal = standardDefinition.getWeight();

                    return entryWeight == standardWeightVal;
                });
    }

    @Getter
    private static final class ValidationContext {
        private final List<LoadoutEntry> entries;
        private final Map<String, PieceDefinition> definitions;
        private final int expectedWeight;
        private final Loadout standardLoadout;

        /**
         * Creates a validation context for a loadout validation run.
         *
         * @param entries the loadout entries being validated
         * @param definitions the piece definitions used during validation
         * @param expectedWeight the expected total weight of the loadout
         * @param standardLoadout the standard loadout used as reference
         */
        ValidationContext(
            final List<LoadoutEntry> entries,
            final Map<String, PieceDefinition> definitions,
            final int expectedWeight,
            final Loadout standardLoadout
        ) {
            this.entries = entries;
            this.definitions = definitions;
            this.expectedWeight = expectedWeight;
            this.standardLoadout = standardLoadout;
        }
    }
}
