package io.stonesgame.web.health;

import io.stonesgame.web.Player;

import com.codahale.metrics.health.HealthCheck;

import java.util.concurrent.BlockingQueue;

public class PlayersQueueCapacityHealthCheck extends HealthCheck {
    private static final double THRESHOLD = 0.2;

    private final int capacity;
    private final BlockingQueue<Player> players;

    public PlayersQueueCapacityHealthCheck(final int capacity, final BlockingQueue<Player> players) {
        this.capacity = capacity;
        this.players = players;
    }

    @Override
    protected Result check() throws Exception {
        final int remainingCapacity = this.players.remainingCapacity();
        final float remainingPercentage = ((float) remainingCapacity) / this.capacity;
        final String status = "%.2f%% [%d out of %d]";
        final Object[] args = new Object[] {remainingPercentage, remainingCapacity, this.capacity};
        if (Math.abs(remainingPercentage - THRESHOLD) <= .00001) {
            return Result.unhealthy("Critical players queue remaining capacity: " + status, args);
        } else {
            return Result.healthy(
                    "Players queue remaining capacity is above critical: " + status + ". Everything is ok.", args);
        }
    }
}
