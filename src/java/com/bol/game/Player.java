package com.bol.game;

import java.util.Arrays;

public class Player {
    public static final int TOTAL_PITS = 7;
    private final int[] pits;

    public Player(int[] pits) {
        this.pits = Arrays.copyOf(pits, TOTAL_PITS);
    }

    private int getStonesAt(int pit) {
        return this.pits[pit];
    }

    public void pick(int pit) {
        int stones = getStonesAt(pit);
        this.pits[pit] = 0;
        for (int i = 1; i <= stones; i++) {
            this.pits[(pit + i) % TOTAL_PITS]++;
        }
    }

    public int[] getPits() {
        return Arrays.copyOf(this.pits, TOTAL_PITS);
    }

//    public int getScore() {
//        return this.pits[TOTAL_PITS - 1];
//    }

}
