package ru.netology.pyas.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import ru.netology.pyas.chatprotocol.Protocol;
import ru.netology.pyas.logger.Logger;

public class Client {

    private final static String QUIT_STRING = "/exit";

    private String host;
    private int port;
    private Protocol protocol;
    private Logger logger;

    public Client(String host, int port, Protocol protocol, Logger logger) {
        this.host = host;
        this.port = port;
        this.protocol = protocol;
        this.logger = logger;
    }

    public void run() {
        Socket socket = connectToServer();
        if (socket == null) {
            return;
        }

        Server server = initServer(socket);
        if (server == null) {
            closeServerSocket(socket);
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите имя > ");
        String name = scanner.nextLine();

        if (!handshake(server, name)) {
            scanner.close();
            disconnectFromServer(server);
            return;
        }

        Thread serverInputThread = new Thread(new ServerInputRunner(this, server));
        serverInputThread.start();
        while (true) {
            System.out.print("> ");
            String message = scanner.nextLine();
            if (message.equals(QUIT_STRING)) {
                break;
            }
            server.sendMessage(message);
        }
        serverInputThread.interrupt();
        scanner.close();
        disconnectFromServer(server);
    }

    private void disconnectFromServer(Server server) {
        try {
            server.disconnect();
        } catch(IOException e) {
            System.err.println("Ошибка отключения от сервера: " + e.getMessage());
        }
    }

    private boolean handshake(Server server, String name) {
        try {
            String message = server.readMessage();
            if(!protocol.isHandshakeMessage(message)){
                System.out.println("Ошибка установления соединения с сервером: ошибка протокола");
                return false;
            }
            server.sendMessage(name);
            message = server.readMessage();
            if (protocol.isErrorMessage(message)){
                System.out.println("Ошибка: " + protocol.getErrorDetails(message));
                return false;
            }
            if (!protocol.isHandshakeOKMessage(message)) {
                System.out.println("Ошибка установления соединения с сервером: ошибка протокола");
                return false;
            }
            return true;
        } catch (IOException e) {
            System.err.println("Ошибка взаимодействия с сервером: " +e.getMessage());
            return false;
        }
    }

    private void closeServerSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Ошибка закрытия сокета: " + e.getMessage());
        }
    }

    private Server initServer(Socket socket) {
        try {
            return new Server(socket);
        } catch (IOException e) {
            System.err.println("Ошибка инициализации сервера: " + e.getMessage());
            return null;
        }
    }

    private Socket connectToServer() {
        try {
            return new Socket(host, port);
        } catch (IOException e) {
            System.err.println("Ошибка подключения к серверу: " + e.getMessage());
            return null;
        }
    }

    public void outputMessage(String message) {
        logger.log(message);
        System.out.println(message);
    }

}
