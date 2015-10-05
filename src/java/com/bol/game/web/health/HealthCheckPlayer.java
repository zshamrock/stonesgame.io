package com.bol.game.web.health;

import com.bol.game.web.Player;
import com.bol.game.web.WebGame;

import com.codahale.metrics.health.HealthCheck;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class HealthCheckPlayer extends HealthCheck implements Player {
    protected static final int ACCEPTABLE_HEALTH_DURATION_SECONDS = 5;

    private final BlockingQueue<Player> players;
    private final CountDownLatch latch = new CountDownLatch(1);

    public HealthCheckPlayer(final BlockingQueue<Player> players) {
        this.players = players;
    }

    @Override
    protected Result check() throws Exception {
        // add 2 players, so if there is no one waiting for the game to join,
        // we sure that at least one (effectively first of these 2 players) will join the game
        final boolean added = this.players.offer(this, 1, TimeUnit.SECONDS)
                && this.players.offer(this, 1, TimeUnit.SECONDS);
        if (!added) {
            return Result.unhealthy("Unable to add a new player to the players queue. Remaining queue capacity is %d.",
                    this.players.remainingCapacity());
        }
        final boolean timedOut = !this.latch.await(ACCEPTABLE_HEALTH_DURATION_SECONDS, TimeUnit.SECONDS);
        if (timedOut) {
            return Result.unhealthy("Unable to join a game in the acceptable period of time [%d seconds]",
                    ACCEPTABLE_HEALTH_DURATION_SECONDS);
        }
        return Result.healthy("Successfully \"joined\" a game in %d seconds", ACCEPTABLE_HEALTH_DURATION_SECONDS);
    }

    @Override
    public boolean isReady() {
        this.latch.countDown();
        return false;
    }

    @Override
    public void setNum(final int num) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNum() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void join(final WebGame game) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void go(final int[][] board, final boolean over, final boolean won) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void idle(final int[][] board, final boolean over, final boolean won) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebGame getGame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void over() {
        throw new UnsupportedOperationException();
    }
}
