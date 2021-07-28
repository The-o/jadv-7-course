package ru.netology.pyas.server;

import java.io.IOException;

public class ServerRunner implements Runnable {

    private Server server;
    private Client client;

    public ServerRunner(Server server, Client client) {
        this.server = server;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            if (!server.handshake(client)) {
                return;
            }
            while (server.processMessage(client));
        } catch (IOException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
        }
    }

}
