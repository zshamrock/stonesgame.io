package com.bol.game;
public final class Game {
    public static final int TOTAL_PITS = 7;

    public enum Status {
        GAME_OVER,
        NEXT_PLAYER,
        SAME_PLAYER
    }

    public int[][] turn(final int[] player1, final int pit, final int[] player2) {
        // assume all arguments are correct, no validation, specially for pit
        final int stones = player1[pit];
        final int[][] players = new int[2][TOTAL_PITS];
        players[0][pit] = 0;
        for (int i = 1; i <= stones; i++) {
            final int position = (pit + i) % TOTAL_PITS;
            players[0][(position)] = player1[(position)] + 1;
        }
        System.arraycopy(player2, 0, players[1], 0, TOTAL_PITS);
        return players;
    }
}
