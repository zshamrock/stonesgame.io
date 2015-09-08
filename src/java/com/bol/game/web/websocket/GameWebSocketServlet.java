package com.bol.game.web.websocket;

import com.bol.game.web.Player;
import com.bol.game.web.WebGame;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.websocket.servlet.*;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class GameWebSocketServlet extends WebSocketServlet implements WebSocketCreator {

    private final BlockingQueue<Player> players;
    private final ConcurrentMap<UUID, WebGame> games;
    private final ObjectMapper mapper;

    public GameWebSocketServlet(BlockingQueue<Player> players, ConcurrentMap<UUID, WebGame> games, ObjectMapper mapper) {
        this.players = players;
        this.games = games;
        this.mapper = mapper;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(TimeUnit.MINUTES.toMillis(3));
        factory.register(GameSocket.class);
        factory.setCreator(this);
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        return new GameSocket(this.players, this.games, this.mapper);
    }


}
