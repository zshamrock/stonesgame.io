package io.stonesgame.web.websocket;

import io.stonesgame.web.Player;
import io.stonesgame.web.WebGame;
import io.stonesgame.web.WebPlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@WebSocket
public class GameSocket {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameSocket.class);
    public static final MapType MESSAGE_TYPE = MapType.construct(
            HashMap.class, SimpleType.construct(String.class), SimpleType.construct(String.class));

    private final BlockingQueue<Player> players;
    private final ConcurrentMap<UUID, WebGame> games;
    private final ObjectMapper mapper;
    private Player player;

    public GameSocket(
            final BlockingQueue<Player> players, final ConcurrentMap<UUID, WebGame> games, final ObjectMapper mapper) {
        this.players = players;
        this.games = games;
        this.mapper = mapper;
    }

    @OnWebSocketConnect
    public void join(final Session session) throws InterruptedException {
        this.player = new WebPlayer(session, this.mapper);
        final boolean added = this.players.offer(this.player, 1, TimeUnit.SECONDS);
        if (added) {
            LOGGER.info("Connect :: Added a player to the players queue.");
        } else {
            LOGGER.warn("Player was not added to the queue due to the capacity limit.");
            session.close();
        }
    }

    @OnWebSocketMessage
    public void turn(final String message) throws IOException {
        LOGGER.info("Message :: Action received {}.", message);
        final Map<String, String> msg = this.mapper.readValue(message, MESSAGE_TYPE);
        final String action = msg.get("action");
        final WebGame game = this.player.getGame();
        switch (action) {
            case "pick":
                final String pit = msg.get("pit");
                final boolean over = game.pick(Integer.parseInt(pit));
                if (over) {
                    this.games.remove(game.getId());
                }
                break;
            default:
                LOGGER.info("Unknown action {} received.", action);
        }
    }

    @OnWebSocketClose
    public void leaving(final int closeCode, final String closeReason) {
        LOGGER.info("Close :: Leaving with {} {}.", closeCode, closeReason);
        this.player.getGame().over();
        this.games.remove(this.player.getGame().getId());
    }
}
