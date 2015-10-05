package com.bol.game.web.websocket;

import com.bol.game.web.Player;
import com.bol.game.web.WebGame;

import com.codahale.metrics.Meter;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class GameWebSocketServlet extends WebSocketServlet implements WebSocketCreator {

    @SuppressFBWarnings("SE_BAD_FIELD")
    private final BlockingQueue<Player> players;
    @SuppressFBWarnings("SE_BAD_FIELD")
    private final ConcurrentMap<UUID, WebGame> games;
    private final ObjectMapper mapper;
    @SuppressFBWarnings("SE_BAD_FIELD")
    private final Meter requests;

    public GameWebSocketServlet(
            final BlockingQueue<Player> players,
            final ConcurrentMap<UUID, WebGame> games,
            final ObjectMapper mapper,
            final Meter requests) {
        this.players = players;
        this.games = games;
        this.mapper = mapper;
        this.requests = requests;
    }

    @Override
    public void configure(final WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(TimeUnit.MINUTES.toMillis(3));
        factory.register(GameSocket.class);
        factory.setCreator(this);
    }

    @Override
    public Object createWebSocket(final ServletUpgradeRequest req, final ServletUpgradeResponse resp) {
        this.requests.mark();
        return new GameSocket(this.players, this.games, this.mapper);
    }


}
