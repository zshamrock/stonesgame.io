package com.bol.game.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public class GameJoiner implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameJoiner.class);

    private final BlockingQueue<Player> players;
    private final ConcurrentMap<UUID, WebGame> games;

    public GameJoiner(BlockingQueue<Player> players, ConcurrentMap<UUID, WebGame> games) {
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
                }
            } catch (InterruptedException ex) {
                LOGGER.warn("Leaving game joiner, no more players can join the game.", ex);
                break;
            } catch (Exception ex) {
                LOGGER.warn("Failed to join.", ex);
            }
        }
    }
}
