package com.bol.game.web;

import com.bol.game.core.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public class WebGame extends Game {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebGame.class);

    private final UUID id;
    private final Player[] players;

    public WebGame(UUID id, Player p1, Player p2) throws IOException {
        this.id = id;
        this.players = new Player[]{p1, p2};
        this.players[0].setNum(0);
        this.players[1].setNum(1);
    }

    public void start() throws IOException {
        this.players[0].join(this);
        this.players[1].join(this);
        this.players[0].go(this.getPlayers(), false, false);
        this.players[1].idle(this.getPlayers(), false, false);
    }

    @Override
    public boolean pick(int pit) {
        final boolean over = super.pick(pit);
        Player current = this.players[this.player];
        Player opponent = this.players[(this.player + 1) % 2];
        try {
            final int[][] board = super.getPlayers();
            final int[] score = this.getScore();
            current.go(board, over, score[current.getNum()] > score[opponent.getNum()]);
            opponent.idle(board, over, score[opponent.getNum()] > score[current.getNum()]);
        } catch (IOException ex) {
            LOGGER.error("Pick a pit {} for game {} failed.", pit, this.id, ex);
            LOGGER.info("Closing a game {}.", this.id);
            return true;
        }
        return over;
    }

    public UUID getId() {
        return this.id;
    }

    public void over() {
        this.players[0].over();
        this.players[1].over();
    }
}
