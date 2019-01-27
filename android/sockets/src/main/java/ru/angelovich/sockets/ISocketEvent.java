package ru.angelovich.sockets;

public interface ISocketEvent {
    ISocketAClient getClient();

    String getMessage();
}
