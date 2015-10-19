package io.stonesgame.web;

import io.stonesgame.core.Game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public class WebGame extends Game {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebGame.class);

    private final UUID id;
    private final Player[] players;

    public WebGame(final UUID id, final Player p1, final Player p2) throws IOException {
        this.id = id;
        this.players = new Player[]{p1, p2};
        this.players[0].setNum(0);
        this.players[1].setNum(1);
    }

    public void start() throws IOException {
        this.players[1].join(this);
        this.players[0].join(this);
        // "idle" on the player must go before calling "go" on another player,
        // as if the "go" player replies too fast (as was in the case of bot),
        // the state of second player will be corrupted, as "pick" from the "go" player will set the state of "idle"
        // player to "go", but then, if the idle went after go, it would overwrite correct "go" state back to "idle"
        this.players[1].idle(this.getPlayers(), false, false);
        this.players[0].go(this.getPlayers(), false, false);
    }

    @Override
    public boolean pick(final int pit) {
        final boolean over = super.pick(pit);
        final Player active = this.players[this.player];
        final Player passive = this.players[(this.player + 1) % 2];
        try {
            final int[][] board = super.getPlayers();
            final int[] score = this.getScore();
            passive.idle(board, over, score[passive.getNum()] > score[active.getNum()]);
            active.go(board, over, score[active.getNum()] > score[passive.getNum()]);
        } catch (final IOException ex) {
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
