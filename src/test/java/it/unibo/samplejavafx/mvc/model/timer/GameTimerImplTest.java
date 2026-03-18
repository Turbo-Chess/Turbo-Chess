package it.unibo.samplejavafx.mvc.model.timer;

import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTimerImplTest {

    @Test
    void timerDoesNotTickBeforeStart() throws Exception {
        final var ticks = new AtomicInteger(0);
        final var timer = new GameTimerImpl(
            2,
            (p, t) -> ticks.incrementAndGet(),
            loser -> { }
        );
        try {
            Thread.sleep(1200);
            assertEquals(0, ticks.get());
        } finally {
            timer.shutdown();
        }
    }

    @Test
    void timerDecrementsActivePlayerAfterStart() throws Exception {
        final var remaining = new AtomicLong(-1);
        final var latch = new CountDownLatch(1);
        final var timer = new GameTimerImpl(
            2,
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
            assertTrue(latch.await(2500, TimeUnit.MILLISECONDS));
            assertEquals(1, remaining.get());
            assertEquals(1, timer.getTimeRemaining(PlayerColor.WHITE));
            assertEquals(2, timer.getTimeRemaining(PlayerColor.BLACK));
        } finally {
            timer.shutdown();
        }
    }

    @Test
    void switchTurnChangesWhichClockIsDecremented() throws Exception {
        final var lastWhite = new AtomicLong(2);
        final var lastBlack = new AtomicLong(2);
        final var whiteTick = new CountDownLatch(1);
        final var blackTick = new CountDownLatch(1);

        final var timer = new GameTimerImpl(
            2,
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
            assertTrue(whiteTick.await(2500, TimeUnit.MILLISECONDS));
            timer.switchTurn();
            assertTrue(blackTick.await(2500, TimeUnit.MILLISECONDS));

            assertEquals(1, lastWhite.get());
            assertEquals(1, lastBlack.get());
        } finally {
            timer.shutdown();
        }
    }

    @Test
    void timeoutTriggersCallbackAndStopsFurtherDecrements() throws Exception {
        final var timedOut = new AtomicReference<PlayerColor>();
        final var timeoutLatch = new CountDownLatch(1);
        final var lastTime = new AtomicLong(99);

        final var timer = new GameTimerImpl(
            1,
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
            assertTrue(timeoutLatch.await(2500, TimeUnit.MILLISECONDS));
            assertEquals(PlayerColor.WHITE, timedOut.get());
            assertEquals(0, timer.getTimeRemaining(PlayerColor.WHITE));

            Thread.sleep(1200);
            assertEquals(0, timer.getTimeRemaining(PlayerColor.WHITE));
        } finally {
            timer.shutdown();
        }
    }
}

