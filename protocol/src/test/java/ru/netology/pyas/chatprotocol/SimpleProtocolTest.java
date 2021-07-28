package ru.netology.pyas.chatprotocol;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SimpleProtocolTest {

    private Protocol protocol;

    @BeforeEach 
    void init() {
        protocol = new SimpleProtocol();
    }

    @Test
    void testGetClientMessage() {
        String expected = "client > message";
        String actual = protocol.getClientMessage("client", "message");
        assertEquals(expected, actual);
    }

    @Test
    void testGetErrorDetails() {
        String expected = "error";
        String actual = protocol.getErrorDetails("ERROR error");
        assertEquals(expected, actual);
    }

    @Test
    void testGetHandshakeMessage() {
        String expected = "HELLO";
        String actual = protocol.getHandshakeMessage();
        assertEquals(expected, actual);
    }

    @Test
    void testGetHandshakeOKMessage() {
        String expected = "OK";
        String actual = protocol.getHandshakeOKMessage();
        assertEquals(expected, actual);
    }

    @Test
    void testGetNameIsTakenMessage() {
        String expected = "ERROR Имя \"client\" уже занято";
        String actual = protocol.getNameIsTakenMessage("client");
        assertEquals(expected, actual);
    }

    @Test
    void testGetNotifyClientExitMessage() {
        String expected = "[client вышел из чата]";
        String actual = protocol.getNotifyClientExitMessage("client");
        assertEquals(expected, actual);
    }

    @Test
    void testGetNotifyNewClientMessage() {
        String expected = "[client подключился к чату]";
        String actual = protocol.getNotifyNewClientMessage("client");
        assertEquals(expected, actual);
    }

    @Test
    void testIsErrorMessageTrue() {
        boolean actual = protocol.isErrorMessage("ERROR error");
        assertTrue(actual);
    }

    @Test
    void testIsErrorMessageFalse() {
        boolean actual = protocol.isErrorMessage("OK");
        assertFalse(actual);
    }

    @Test
    void testIsHandshakeMessageTrue() {
        boolean actual = protocol.isHandshakeMessage("HELLO");
        assertTrue(actual);
    }

    @Test
    void testIsHandshakeMessageFalse() {
        boolean actual = protocol.isHandshakeMessage("HI");
        assertFalse(actual);
    }

    @Test
    void testIsHandshakeOKMessageTrue() {
        boolean actual = protocol.isHandshakeOKMessage("OK");
        assertTrue(actual);
    }

    @Test
    void testIsHandshakeOKMessageFalse() {
        boolean actual = protocol.isHandshakeOKMessage("YES");
        assertFalse(actual);
    }
}
