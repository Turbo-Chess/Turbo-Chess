package it.unibo.samplejavafx.mvc.model.timer;

import it.unibo.turbochess.model.entity.impl.PlayerColor;
import it.unibo.turbochess.model.timer.impl.GameTimerImpl;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTimerImplTest {
    private static final long DEFAULT_TIME_SECONDS = 2;
    private static final long SHORT_TIME_SECONDS = 1;
    private static final long AWAIT_TIMEOUT_MS = 2_500;
    private static final long SLEEP_MS = 1_200;

    @Test
    void timerDoesNotTickBeforeStart() throws InterruptedException {
        final var ticks = new AtomicInteger(0);
        final var timer = new GameTimerImpl(
            DEFAULT_TIME_SECONDS,
            (p, t) -> ticks.incrementAndGet(),
            loser -> { }
        );
        try {
            Thread.sleep(SLEEP_MS);
            assertEquals(0, ticks.get());
        } finally {
            timer.shutdown();
        }
    }

    @Test
    void timerDecrementsActivePlayerAfterStart() throws InterruptedException {
        final var remaining = new AtomicLong(-1);
        final var latch = new CountDownLatch(1);
        final var timer = new GameTimerImpl(
            DEFAULT_TIME_SECONDS,
            (p, t) -> {
                if (p == PlayerColor.WHITE) {
                    remaining.set(t);
                    latch.countDown();
                }
            },
            loser -> { }
        );
        try {
            timer.start();
            assertTrue(latch.await(AWAIT_TIMEOUT_MS, TimeUnit.MILLISECONDS));
            assertEquals(1, remaining.get());
            assertEquals(1, timer.getTimeRemaining(PlayerColor.WHITE));
            assertEquals(DEFAULT_TIME_SECONDS, timer.getTimeRemaining(PlayerColor.BLACK));
        } finally {
            timer.shutdown();
        }
    }

    @Test
    void switchTurnChangesWhichClockIsDecremented() throws InterruptedException {
        final var lastWhite = new AtomicLong(DEFAULT_TIME_SECONDS);
        final var lastBlack = new AtomicLong(DEFAULT_TIME_SECONDS);
        final var whiteTick = new CountDownLatch(1);
        final var blackTick = new CountDownLatch(1);

        final var timer = new GameTimerImpl(
            DEFAULT_TIME_SECONDS,
            (p, t) -> {
                if (p == PlayerColor.WHITE) {
                    lastWhite.set(t);
                    whiteTick.countDown();
                } else {
                    lastBlack.set(t);
                    blackTick.countDown();
                }
            },
            loser -> { }
        );

        try {
            timer.start();
            assertTrue(whiteTick.await(AWAIT_TIMEOUT_MS, TimeUnit.MILLISECONDS));
            timer.switchTurn();
            assertTrue(blackTick.await(AWAIT_TIMEOUT_MS, TimeUnit.MILLISECONDS));

            assertEquals(1, lastWhite.get());
            assertEquals(1, lastBlack.get());
        } finally {
            timer.shutdown();
        }
    }

    @Test
    void timeoutTriggersCallbackAndStopsFurtherDecrements() throws InterruptedException {
        final var timedOut = new AtomicReference<PlayerColor>();
        final var timeoutLatch = new CountDownLatch(1);
        final var lastTime = new AtomicLong(99);

        final var timer = new GameTimerImpl(
            SHORT_TIME_SECONDS,
            (p, t) -> {
                if (p == PlayerColor.WHITE) {
                    lastTime.set(t);
                }
            },
            loser -> {
                timedOut.set(loser);
                timeoutLatch.countDown();
            }
        );

        try {
            timer.start();
            assertTrue(timeoutLatch.await(AWAIT_TIMEOUT_MS, TimeUnit.MILLISECONDS));
            assertEquals(PlayerColor.WHITE, timedOut.get());
            assertEquals(0, timer.getTimeRemaining(PlayerColor.WHITE));

            Thread.sleep(SLEEP_MS);
            assertEquals(0, timer.getTimeRemaining(PlayerColor.WHITE));
        } finally {
            timer.shutdown();
        }
    }
}
