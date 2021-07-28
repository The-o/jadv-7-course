package ru.netology.pyas.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private Socket socket;
    private String name;

    private BufferedReader in;
    private PrintWriter out;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public String readMessage() throws IOException {
        return in.readLine();
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void disconnect() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
