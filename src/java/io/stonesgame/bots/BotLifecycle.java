package io.stonesgame.bots;

public interface BotLifecycle {
    void joined(Bot bot);

    void over(Bot bot);
}
