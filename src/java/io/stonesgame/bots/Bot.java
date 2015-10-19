package io.stonesgame.bots;

import io.stonesgame.web.BasicPlayer;
import io.stonesgame.web.Player;
import io.stonesgame.web.WebGame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Bot extends BasicPlayer implements Player {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    private static final int MAX_THINKING_TIME_IN_SECONDS = 5;

    private final BotLifecycle lifecycle;
    private final Random random;
    private volatile WebGame game;

    public Bot(final BotLifecycle lifecycle) {
        this.lifecycle = lifecycle;
        this.random = new Random();
    }

    @Override
    public void join(final WebGame game) throws IOException {
        this.game = game;
        // seed random so the bot will behave differently for every game it joins
        this.random.setSeed(System.currentTimeMillis());
        this.lifecycle.joined(this);
    }

    @Override
    public void go(final int[][] board, final boolean over, final boolean won) throws IOException {
        try {
            // sleep to simulate real user behaviour "thinking" time
            TimeUnit.SECONDS.sleep(this.random.nextInt(MAX_THINKING_TIME_IN_SECONDS) + 1L);
        } catch (final InterruptedException ex) {
            LOGGER.warn("Bot failed to sleep.", ex);
        }
        final int pit = this.selectRandomNonEmptyPit(board[this.num]);
        this.game.pick(pit);
    }

    private int selectRandomNonEmptyPit(final int[] pits) {
        final int activePits = pits.length - 1; // excluding score pit
        int pit = this.random.nextInt(activePits);
        while (pits[pit] == 0) {
            pit = (pit + 1) % activePits;
        }
        return pit;
    }

    @Override
    public void idle(final int[][] board, final boolean over, final boolean won) throws IOException {
        // do nothing
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public WebGame getGame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void over() {
        this.game = null;
        this.lifecycle.over(this);
    }

    @Override
    public boolean isBot() {
        return true;
    }
}
