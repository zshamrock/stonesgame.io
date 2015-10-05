package com.bol.game.web;

import java.io.IOException;

public interface Player {
    void setNum(int num);

    int getNum();

    void join(WebGame game) throws IOException;

    void go(int[][] board, boolean over, boolean won) throws IOException;

    void idle(int[][] board, boolean over, boolean won) throws IOException;

    boolean isReady();

    WebGame getGame();

    void over();
}
