package io.stonesgame.bots;

import io.stonesgame.web.Player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Bots implements Runnable, BotLifecycle {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bots.class);

    public static final int INITIAL_DELAY_IN_SECONDS = 30;
    public static final int SCHEDULED_PERIOD_IN_SECONDS = 30;

    private static final int POOL_SIZE = 5;

    private final BlockingQueue<Player> players;

    // simple pool to reduce GC overhead for the used bots as they could be easily reused
    private final Queue<Bot> pool = new ArrayBlockingQueue<>(POOL_SIZE);

    public Bots(final BlockingQueue<Player> players) {
        this.players = players;
        this.initPool();
    }

    private void initPool() {
        for (int i = 0; i < POOL_SIZE; i++) {
            this.pool.add(new Bot(this));
        }
    }

    @Override
    public void run() {
        if (this.pool.isEmpty()) {
            return;
        }
        final Bot bot = this.pool.poll();
        if (bot != null) {
            final boolean added = this.players.offer(bot);
            LOGGER.info(added
                    ? "New bot is added to the players queue."
                    : "Was unable to add bot into the players queue due to the capacity restrictions.");
        }
    }

    @Override
    public void joined(final Bot bot) {
        LOGGER.info("A bot joined a game.");
    }

    @Override
    public void over(final Bot bot) {
        final boolean added = this.pool.offer(bot);
        if (added) {
            LOGGER.info("Offer a bot back to the pool.");
        }
    }
}
