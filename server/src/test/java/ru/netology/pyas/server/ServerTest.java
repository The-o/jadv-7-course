package ru.netology.pyas.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.netology.pyas.chatprotocol.Protocol;
import ru.netology.pyas.logger.Logger;

public class ServerTest {

    private Protocol protocolMock;
    private Logger loggerMock;
    private Server server;

    @BeforeEach
    void initTests() {
        protocolMock = mock(Protocol.class);
        when(protocolMock.getHandshakeMessage()).thenReturn("HI");
        when(protocolMock.getHandshakeOKMessage()).thenReturn("OK");

        loggerMock = mock(Logger.class);
        server = new Server(8080, protocolMock, loggerMock);
    }

    @Test
    void testHandshakeOk() {
        Client clientMock = mock(Client.class);
        try {
            when(clientMock.readMessage()).thenReturn("client1");

            server.handshake(clientMock);

            assertAll(
                () -> verify(clientMock).sendMessage("HI"),
                () -> verify(clientMock).sendMessage("OK"),
                () -> verify(clientMock).setName("client1")
            );
            
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testHandshakeFail() {
        Client clientMock1 = mock(Client.class);
        Client clientMock2 = mock(Client.class);
        try {
            when(clientMock1.readMessage()).thenReturn("client1");
            when(clientMock2.readMessage()).thenReturn("client1");

            when(protocolMock.getNameIsTakenMessage("client1")).thenReturn("ERR client1");

            server.handshake(clientMock1);
            server.handshake(clientMock2);

            assertAll(
                () -> verify(clientMock2).sendMessage("HI"),
                () -> verify(clientMock2).sendMessage("ERR client1"),
                () -> verify(clientMock2).disconnect()
            );
            
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testProcessMessage() {
        Client clientMock = mock(Client.class);
        try {
            when(clientMock.readMessage())
                .thenReturn("client1")
                .thenReturn("test message");
            when(clientMock.getName()).thenReturn("client1");
            
            when(protocolMock.getNotifyNewClientMessage("client1")).thenReturn("client1 connected");
            when(protocolMock.getClientMessage("client1", "test message")).thenReturn("client1 : test message");

            server.handshake(clientMock);
            server.processMessage(clientMock);

            assertAll(
                () -> verify(clientMock).sendMessage("HI"),
                () -> verify(clientMock).sendMessage("OK"),
                () -> verify(clientMock).sendMessage("client1 connected"),
                () -> verify(clientMock).sendMessage("client1 : test message")
            );
            
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testProcessMessageDisconnect() {
        Client clientMock = mock(Client.class);
        try {
            when(clientMock.readMessage())
                .thenReturn("client1")
                .thenReturn(null);
            
            when(protocolMock.getNotifyNewClientMessage("client1")).thenReturn("client1 connected");

            server.handshake(clientMock);
            server.processMessage(clientMock);

            assertAll(
                () -> verify(clientMock).sendMessage("HI"),
                () -> verify(clientMock).sendMessage("OK"),
                () -> verify(clientMock).sendMessage("client1 connected"),
                () -> verify(clientMock).disconnect()
            );
            
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

}
