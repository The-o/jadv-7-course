package ru.netology.pyas.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClientTest {

    private Socket socketMock;
    private InputStream in;
    private OutputStream out;
    private Client client;

    @BeforeEach
    void initTests() {
        in = new ByteArrayInputStream("Input message".getBytes());
        out = new ByteArrayOutputStream();
        socketMock = mock(Socket.class);
        try {
            when(socketMock.getInputStream()).thenReturn(in);
            when(socketMock.getOutputStream()).thenReturn(out);
            client = new Client(socketMock);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testReadMessage() {
        try {
            String actual = client.readMessage();
            String expected = "Input message";

            assertEquals(expected, actual);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testSendMessage() {
        client.sendMessage("Output message");
        String actual = out.toString();
        String expected = String.format("Output message%n");

        assertEquals(expected, actual);
    }

}
