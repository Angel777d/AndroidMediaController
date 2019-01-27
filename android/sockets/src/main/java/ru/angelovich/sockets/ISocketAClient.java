package ru.angelovich.sockets;

public interface ISocketAClient {
    void Send(String message);

    void Disconnect();
}
