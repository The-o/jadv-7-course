package ru.netology.pyas.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Server {

    private Socket socket;

    private BufferedReader in;
    private PrintWriter out;

    public Server(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public String readMessage() throws IOException {
        return in.readLine();
    }

    public boolean hasMessage() throws IOException {
        return in.ready();
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void disconnect() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

}
