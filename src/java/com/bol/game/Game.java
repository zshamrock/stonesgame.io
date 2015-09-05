package com.bol.game;
public final class Game {

    public enum Status {
        GAME_OVER,
        NEXT_PLAYER,
        SAME_PLAYER
    }

    public void turn(final Player p1, final int pit, final Player p2) {
        // assume all arguments are correct, no validation, specially for pit
        p1.pick(pit);
    }
}
