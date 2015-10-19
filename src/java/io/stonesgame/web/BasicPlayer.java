package io.stonesgame.web;

public abstract class BasicPlayer implements Player {
    protected volatile int num;

    @Override
    public void setNum(final int num) {
        this.num = num;
    }

    @Override
    public int getNum() {
        return this.num;
    }
}
