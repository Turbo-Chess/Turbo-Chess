package it.unibo.samplejavafx.mvc.model.chessboard;

import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.Optional;

import com.google.common.collect.BiMap;

/**
 * The {@code ChessBoard} interface represents the fundamental game space where the chess match occurs.
 * <p>
 * It defines the contract for board interactions, including placing, retrieving, and moving entities (pieces),
 * as well as checking board boundaries and state. The board acts as the central data structure maintaining
 * the locations of all game entities.
 * </p>
 */
public interface ChessBoard {

    /**
     * Checks if a specific position on the board is currently unoccupied.
     *
     * @param pos The {@link Point2D} coordinate to check.
     * @return {@code true} if no entity occupies the position, {@code false} otherwise.
     */
    boolean isFree(Point2D pos);

    /**
     * Places or updates an entity at a specific position on the board.
     * <p>
     * If the position is already occupied, the existing entity is typically replaced or removed
     * depending on game rules.
     * </p>
     *
     * @param pos       The target {@link Point2D} coordinate.
     * @param newEntity The {@link Entity} to place at the location.
     */
    void setEntity(Point2D pos, Entity newEntity);

    /**
     * Removes any entity present at the specified position.
     * <p>
     * If the position is already empty, this operation has no effect.
     * </p>
     *
     * @param pos The {@link Point2D} coordinate from which to remove the entity.
     */
    void removeEntity(Point2D pos);

    /**
     * Retrieves the entity located at a specific position, if one exists.
     *
     * @param pos The {@link Point2D} coordinate to query.
     * @return an {@link Optional} containing the {@link Entity} if found, or {@link Optional#empty()} if the cell is free.
     */
    Optional<Entity> getEntity(Point2D pos);

    /**
     * Finds the current position of a specific entity on the board.
     * <p>
     * This reverse-lookup is useful for determining where a known piece is currently located.
     * </p>
     *
     * @param entity The {@link Entity} instance to locate.
     * @return the {@link Point2D} coordinate of the entity, or {@code null} if the entity is not on the board.
     */
    Point2D getPosByEntity(Entity entity);

    /**
     * Verifies if a given coordinate falls within the valid boundaries of the game board.
     *
     * @param pos The {@link Point2D} coordinate to validate.
     * @return {@code true} if the position is within the board's dimensions, {@code false} otherwise.
     */
    boolean checkBounds(Point2D pos);

    /**
     * Retrieves a complete mapping of all occupied positions and their corresponding entities.
     * <p>
     * The returned {@link BiMap} allows for bidirectional lookup between positions and entities.
     * </p>
     *
     * @return a {@link BiMap} containing the current state of the board.
     */
    BiMap<Point2D, Entity> getBoard();

    /**
     * Registers an observer to receive notifications about state changes on the board.
     *
     * @param observer The {@link BoardObserver} implementation to attach.
     */
    void addObserver(BoardObserver observer);

    /**
     * Unregisters a previously added observer, stopping it from receiving further notifications.
     *
     * @param observer The {@link BoardObserver} to remove.
     */
    void removeObserver(BoardObserver observer);

    /**
     * Executes a move of an entity from one position to another.
     * <p>
     * This method handles the logical update of the board state corresponding to a piece's movement.
     * </p>
     *
     * @param start The starting {@link Point2D} coordinate.
     * @param end   The destination {@link Point2D} coordinate.
     */
    void move(Point2D start, Point2D end);

    /**
     * Executes a capture move (eating) where a piece moves to a destination occupied by another entity,
     * replacing it.
     *
     * @param start The starting {@link Point2D} coordinate of the attacking piece.
     * @param end   The {@link Point2D} coordinate of the piece being captured.
     */
    void eat(Point2D start, Point2D end);
}
