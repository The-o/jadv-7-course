package ru.netology.pyas.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.netology.pyas.chatprotocol.Protocol;
import ru.netology.pyas.logger.Logger;

public class Server {

    private final int port;
    private final Protocol protocol;
    private final Logger logger;

    private final ConcurrentHashMap<String, Client> clients = new ConcurrentHashMap<String, Client>();

    public Server(int port, Protocol protocol, Logger logger) {
        this.port = port;
        this.protocol = protocol;
        this.logger = logger;
    }

    public void run() {
        ServerSocket serverSocket = openServerSocket();
        if (serverSocket == null) {
            return;
        }

        ExecutorService executor = Executors.newCachedThreadPool();

        while (true) {
            Socket clientSocket = acceptIncomingConnection(serverSocket);
            if (clientSocket == null) {
                continue;
            }
            Client client;
            try {
                client = new Client(clientSocket);
            } catch (IOException e) {
                continue;
            }
            ServerRunner serverRunner = new ServerRunner(this, client);
            executor.execute(serverRunner);
        }
    }

    private Socket acceptIncomingConnection(ServerSocket serverSocket) {
        Socket clientSocket;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Ошибка открытия сокета клиента: " + e.getMessage());
            return null;
        }
        return clientSocket;
    }

    private ServerSocket openServerSocket() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Ошибка открытия сокета сервера: " + e.getMessage());
            return null;
        }
        return serverSocket;
    }

    private void registerClient(String name, Client client) {
        client.setName(name);
        clients.put(name, client);
        sendMessageToAllClients(protocol.getNotifyNewClientMessage(name));
    }

    private void disconnectClient(Client client) throws IOException {
        String name = client.getName();
        clients.remove(name);
        client.disconnect();
        sendMessageToAllClients(protocol.getNotifyClientExitMessage(name));
    }

    private boolean hasClient(String name) {
        return clients.containsKey(name);
    }

    public boolean handshake(Client client) throws IOException {
        client.sendMessage(protocol.getHandshakeMessage());
        String name = client.readMessage();
        if (hasClient(name)) {
            client.sendMessage(protocol.getNameIsTakenMessage(name));
            client.disconnect();
            return false;
        }
        client.sendMessage(protocol.getHandshakeOKMessage());
        registerClient(name, client);
        return true;

    }

    public boolean processMessage(Client client) throws IOException {
        String message = client.readMessage();
        if (message == null) {
            disconnectClient(client);
            return false;
        }
        sendMessageToAllClients(protocol.getClientMessage(client.getName(), message));
        return true;
    }

    private synchronized void sendMessageToAllClients(String message) {
        logger.log(message);

        Enumeration<Client> clientsEnum = clients.elements();
        while (clientsEnum.hasMoreElements()) {
            clientsEnum.nextElement().sendMessage(message);
        }
    }
}
