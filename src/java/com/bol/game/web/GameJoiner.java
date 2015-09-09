package com.bol.game.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public class GameJoiner implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameJoiner.class);

    private final BlockingQueue<Player> players;
    private final ConcurrentMap<UUID, WebGame> games;

    public GameJoiner(final BlockingQueue<Player> players, final ConcurrentMap<UUID, WebGame> games) {
        this.players = players;
        this.games = games;
    }

    @Override
    public void run() {
        LOGGER.info("Game joiner is running.");
        while (true) {
            try {
                final Player p1 = this.players.take();
                final Player p2 = this.players.take();
                if (p1.isReady() && p2.isReady()) {
                    final UUID id = UUID.randomUUID();
                    final WebGame game = new WebGame(id, p1, p2);
                    game.start();
                    this.games.put(id, game);
                    LOGGER.info("New {} game has just started.", id);
                } else {
                    // put active player back to the queue, it might be that only one of the players disconnected,
                    // while another one is still waiting to join, otherwise he is being removed from the queue
                    // will never join a game ever, unless he reconnects with a new connection/session
                    Arrays.stream(new Player[]{p1, p2}).filter(Player::isReady).forEach(this.players::offer);
                }
            } catch (final InterruptedException ex) {
                LOGGER.warn("Leaving game joiner, no more players can join the game.", ex);
                break;
            } catch (final Exception ex) {
                LOGGER.warn("Failed to join.", ex);
            }
        }
    }
}
