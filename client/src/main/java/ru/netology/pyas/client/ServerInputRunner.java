package ru.netology.pyas.client;

import java.io.IOException;

public class ServerInputRunner implements Runnable {

    private final Client client;
    private final Server server;

    public ServerInputRunner(Client client, Server server) {
        this.client = client;
        this.server = server;
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            String message = "";
            try {
                if (!server.hasMessage()) {
                    Thread.currentThread().yield();
                    continue;
                }
                message = server.readMessage();
            } catch (IOException e) {
                System.err.println("Ошибка получения ответа от сервера: " + e.getMessage());
            }
            client.outputMessage(message);
        }
    }

}
