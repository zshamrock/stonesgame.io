package com.bol.game.web;

import com.bol.game.web.websocket.GameWebSocketServlet;
import com.codahale.metrics.Gauge;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.servlet.ServletRegistration;
import java.util.UUID;
import java.util.concurrent.*;

public class GameApplication extends Application<GameConfiguration> {
    private BlockingQueue<Player> players = new ArrayBlockingQueue<>(100);
    private ConcurrentMap<UUID, WebGame> games = new ConcurrentHashMap<>();


    public static void main(String[] args) throws Exception {
        new GameApplication().run(args);
    }

    @Override
    public void run(GameConfiguration configuration, Environment environment) throws Exception {
        registerGameWebSocketServlet(environment);
        runGameJoiner(environment);
        registerMetrics(environment);
    }

    private void registerGameWebSocketServlet(final Environment environment) {
        final ServletRegistration.Dynamic ws = environment.servlets()
                .addServlet("game", new GameWebSocketServlet(this.players, this.games, environment.getObjectMapper()));
        ws.addMapping("/game");
        ws.setAsyncSupported(true);
    }

    private void runGameJoiner(Environment environment) {
        final ExecutorService executorService = environment.lifecycle()
                .executorService("game-joiner-%d")
                .maxThreads(1)
                .build();
        executorService.execute(new GameJoiner(this.players, this.games));
    }

    private void registerMetrics(Environment environment) {
        environment.metrics().register("active.games", (Gauge<Integer>) () -> GameApplication.this.games.size());
    }

    @Override
    public void initialize(Bootstrap<GameConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html", "assets"));
    }
}
