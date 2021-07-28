package ru.netology.pyas.chatprotocol;

public interface Protocol {
    
    public String getHandshakeMessage();
    public boolean isHandshakeMessage(String message);

    public String getNameIsTakenMessage(String name);
    
    public String getHandshakeOKMessage();
    public String getErrorDetails(String message);
    
    public boolean isErrorMessage(String message);
    public boolean isHandshakeOKMessage(String message);

    public String getNotifyNewClientMessage(String name);
    public String getNotifyClientExitMessage(String name);
    public String getClientMessage(String name, String message);
}
