package io.stonesgame.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class WebPlayer implements Player {
    private volatile WebGame game;
    private volatile int num;
    private final Session session;
    private final ObjectMapper mapper;

    public WebPlayer(final Session session, final ObjectMapper mapper) {
        this.session = session;
        this.mapper = mapper;
    }

    @Override
    public void setNum(final int num) {
        this.num = num;
    }

    @Override
    public int getNum() {
        return this.num;
    }

    @Override
    public void join(final WebGame game) throws IOException {
        this.game = game;
        this.session.getRemote().sendString(this.mapper.writeValueAsString(ImmutableMap.of(
                "action", "join",
                "board", game.getPlayers(),
                "player", this.num,
                "id", game.getId())));
    }

    @Override
    public void go(final int[][] board, final boolean over, final boolean won) throws IOException {
        this.send("go", board, over, won);
    }

    @Override
    public void idle(final int[][] board, final boolean over, final boolean won) throws IOException {
        this.send("wait", board, over, won);
    }

    private void send(final String action, final int[][] board, final boolean over, final boolean won)
            throws IOException {
        this.session.getRemote().sendString(this.mapper.writeValueAsString(ImmutableMap.builder()
                .put("action", action)
                .put("board", board)
                .put("over", over)
                .put("won", won)
                .put("player", this.num)
                .put("id", this.game.getId())
                .build()));
    }

    @Override
    public boolean isReady() {
        return this.session.isOpen();
    }

    @Override
    public WebGame getGame() {
        return this.game;
    }

    @Override
    public void over() {
        if (this.session.isOpen()) {
            this.session.close();
        }
    }
}
