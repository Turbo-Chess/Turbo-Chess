package it.unibo.samplejavafx.mvc.model.timer;

import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Implementation of the game timer.
 */
public class GameTimerImpl implements GameTimer {

    private final ScheduledExecutorService scheduler;
    private long whiteTime;
    private long blackTime;
    private PlayerColor activePlayer;
    private boolean isRunning;
    private final BiConsumer<PlayerColor, Long> onTick;
    private final Consumer<PlayerColor> onTimeOut;

    /**
     * Creates a new game timer with custom times for each player.
     *
     * @param whiteTimeSeconds the initial time in seconds for white player.
     * @param blackTimeSeconds the initial time in seconds for black player.
     * @param onTick           the callback for when the timer updates.
     * @param onTimeOut        the callback for when a player's time runs out.
     */
    public GameTimerImpl(final long whiteTimeSeconds, final long blackTimeSeconds, final BiConsumer<PlayerColor, Long> onTick, final Consumer<PlayerColor> onTimeOut) {
        this.scheduler = Executors.newSingleThreadScheduledExecutor(newDaemonThreadFactory());
        this.whiteTime = whiteTimeSeconds;
        this.blackTime = blackTimeSeconds;
        this.activePlayer = PlayerColor.WHITE;
        this.onTick = onTick;
        this.onTimeOut = onTimeOut;
        this.isRunning = false;
        startScheduler();
    }

    /**
     * Creates a new game timer.
     *
     * @param initialTimeSeconds the initial time in seconds for each player.
     * @param onTick             the callback for when the timer updates.
     * @param onTimeOut          the callback for when a player's time runs out.
     */
    public GameTimerImpl(final long initialTimeSeconds, final BiConsumer<PlayerColor, Long> onTick, final Consumer<PlayerColor> onTimeOut) {
        this(initialTimeSeconds, initialTimeSeconds, onTick, onTimeOut);
    }

    private void startScheduler() {
        scheduler.scheduleAtFixedRate(() -> {
            if (isRunning) {
                decrementTime();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private void decrementTime() {
        if (activePlayer == PlayerColor.WHITE) {
            whiteTime--;
            onTick.accept(PlayerColor.WHITE, whiteTime);
            if (whiteTime <= 0) {
                stop();
                onTimeOut.accept(PlayerColor.WHITE);
            }
        } else {
            blackTime--;
            onTick.accept(PlayerColor.BLACK, blackTime);
            if (blackTime <= 0) {
                stop();
                onTimeOut.accept(PlayerColor.BLACK);
            }
        }
    }

    @Override
    public void start() {
        isRunning = true;
    }

    @Override
    public void stop() {
        isRunning = false;
    }

    @Override
    public void switchTurn() {
        activePlayer = (activePlayer == PlayerColor.WHITE) ? PlayerColor.BLACK : PlayerColor.WHITE;
    }

    @Override
    public long getTimeRemaining(final PlayerColor player) {
        return (player == PlayerColor.WHITE) ? whiteTime : blackTime;
    }

    @Override
    public void setActivePlayer(final PlayerColor player) {
        this.activePlayer = player;
    }

    @Override
    public void shutdown() {
        stop();
        scheduler.shutdownNow();
    }

    private static ThreadFactory newDaemonThreadFactory() {
        return runnable -> {
            final Thread t = new Thread(runnable);
            t.setDaemon(true);
            t.setName("game-timer");
            return t;
        };
    }
}
