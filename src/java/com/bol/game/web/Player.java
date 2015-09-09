package com.bol.game.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Player {
    private volatile WebGame game;
    private volatile int num;
    private final Session session;
    private final ObjectMapper mapper;

    public Player(final Session session, final ObjectMapper mapper) {
        this.session = session;
        this.mapper = mapper;
    }

    public void setNum(final int num) {
        this.num = num;
    }

    public int getNum() {
        return this.num;
    }

    public void join(final WebGame game) throws IOException {
        this.game = game;
        this.session.getRemote().sendString(this.mapper.writeValueAsString(ImmutableMap.of(
                "action", "join",
                "board", game.getPlayers(),
                "player", this.num,
                "id", game.getId())));
    }

    public void go(final int[][] board, final boolean over, final boolean won) throws IOException {
        this.send("go", board, over, won);
    }

    public void idle(final int[][] board, final boolean over, final boolean won) throws IOException {
        this.send("wait", board, over, won);
    }

    private void send(final String action, final int[][] board, final boolean over, final boolean won) throws IOException {
        this.session.getRemote().sendString(this.mapper.writeValueAsString(ImmutableMap.builder()
                .put("action", action)
                .put("board", board)
                .put("over", over)
                .put("won", won)
                .put("player", this.num)
                .put("id", this.game.getId())
                .build()));
    }

    public boolean isReady() {
        return this.session.isOpen();
    }

    public WebGame getGame() {
        return this.game;
    }

    public void over() {
        if (this.session.isOpen()) {
            this.session.close();
        }
    }
}
