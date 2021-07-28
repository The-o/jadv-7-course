package ru.netology.pyas.chatprotocol;

public class SimpleProtocol implements Protocol {

    private static final String HELLO_MESSAGE = "HELLO";
    private static final String OK_MESSAGE = "OK";
    private static final String ERROR_PREFIX = "ERROR ";

    private static final String NOTIFY_NEW_CLIENT_FORMAT = "[%s подключился к чату]";
    private static final String NOTIFY_CLIENT_EXIT_FORMAT = "[%s вышел из чата]";
    private static final String NAME_IS_TAKEN_FORMAT = "Имя \"%s\" уже занято";
    private static final String MESSAGE_FORMAT = "%s > %s";

    @Override
    public String getHandshakeMessage() {
        return HELLO_MESSAGE;
    }

    @Override
    public boolean isHandshakeMessage(String message) {
        return HELLO_MESSAGE.equals(message);
    }

    @Override
    public String getNameIsTakenMessage(String name) { 
        return ERROR_PREFIX + String.format(NAME_IS_TAKEN_FORMAT, name);
    }

    @Override
    public boolean isErrorMessage(String message) {
        return message.startsWith(ERROR_PREFIX);
    }

    @Override
    public String getErrorDetails(String message) {
        return message.substring(ERROR_PREFIX.length());
    }

    @Override
    public String getHandshakeOKMessage() {
        return OK_MESSAGE;
    }

    @Override
    public boolean isHandshakeOKMessage(String message) {
        return OK_MESSAGE.equals(message);
    }

    @Override
    public String getNotifyNewClientMessage(String name) {
        return String.format(NOTIFY_NEW_CLIENT_FORMAT, name);
    }

    @Override
    public String getNotifyClientExitMessage(String name) {
        return String.format(NOTIFY_CLIENT_EXIT_FORMAT, name);
    }

    @Override
    public String getClientMessage(String name, String message) {
        return String.format(MESSAGE_FORMAT, name, message);
    }

}
