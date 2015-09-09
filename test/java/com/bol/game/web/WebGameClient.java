package com.bol.game.web;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;

public class WebGameClient {
    private static final String ENDPOINT = "ws://localhost:58080/game";
    private final WebSocketClient client;

    public WebGameClient() {
        this.client = new WebSocketClient();
    }

    public void start() throws Exception {
        this.client.start();
    }

    public void stop() throws Exception {
        this.client.stop();
    }

    public void connect(final Object socket) throws Exception {
        final ClientUpgradeRequest request = new ClientUpgradeRequest();
        this.client.connect(socket, new URI(ENDPOINT), request);
    }
}
