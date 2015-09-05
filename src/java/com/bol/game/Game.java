package com.bol.game;

import java.util.Arrays;

public final class Game {

    private static final int[] INITIAL_PITS = new int[]{6, 6, 6, 6, 6, 6, 0};
    private static final int TOTAL_PITS = INITIAL_PITS.length;
    private static final int SCORE_PIT = TOTAL_PITS - 1;

    private final int[][] players = new int[][]{
            Arrays.copyOf(INITIAL_PITS, TOTAL_PITS), Arrays.copyOf(INITIAL_PITS, TOTAL_PITS)
    };

    private int player = 0;

    public Game() {
    }

    public boolean pick(final int pit) {
        final int[] pits = this.players[this.player];
        final int stones = pits[pit];
        pits[pit] = 0;
        for (int i = 1; i <= stones; i++) {
            pits[(pit + i) % TOTAL_PITS]++;
        }
        final int land = ((pit + stones) % TOTAL_PITS);
        if (land != SCORE_PIT) {
            final int next = (this.player + 1) % 2;
            if (pits[land] == 1) {
                pits[SCORE_PIT] += (1 + this.players[next][land]);
                this.players[next][land] = 0;
                pits[land] = 0;
            }
            this.player = next;
        }
        return false;
    }

    public int[][] getPlayers() {
        return new int[][]{Arrays.copyOf(this.players[0], TOTAL_PITS), Arrays.copyOf(this.players[1], TOTAL_PITS)};
    }
}
