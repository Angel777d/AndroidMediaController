package ru.angelovich.sockets;

import android.util.Log;
import android.util.Xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import ru.angelovich.events.EventDispatcher;
import ru.angelovich.events.IEvent;
import ru.angelovich.events.IEventDispatcher;

public class SocketA extends EventDispatcher {

    public static final String SOCKET_A_CLIENT_CONNECTED = "SocketA.SOCKET_A_CLIENT_CONNECTED";
    public static final String SOCKET_A_CLIENT_CLOSED = "SocketA.SOCKET_A_CLIENT_CLOSED";
    public static final String SOCKET_A_ON_MESSAGE = "SocketA.SOCKET_A_ON_MESSAGE";


    private int port;

    public SocketA() {
        this(8083);
    }

    public SocketA(int port) {
        this.port = port;
    }

    private static void Print(String message) {
        Log.d("SocketA", message);
    }

    private static void PrintInSocket(Socket socket, int count) {
        Print(String.format("# %s from ip: %s port: %s.\r\n",
                count,
                socket.getInetAddress(),
                socket.getPort()
        ));
    }

    public void start() {
        try {
            ServerSocket ss = new ServerSocket(port);
            Thread socketServerThread = new Thread(new SocketServerThread(ss));
            socketServerThread.start();
            Print("Socket server started");
        } catch (IOException e) {
            Print("ServerSocket is not initialised");
            e.printStackTrace();
        }
    }

    private class SocketServerThread extends Thread {
        int count = 0;
        ServerSocket serverSocket;

        SocketServerThread(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            try {
                //noinspection InfiniteLoopStatement
                while (true) {
                    Socket socket = serverSocket.accept();
                    PrintInSocket(socket, ++count);
                    SocketAClient client = new SocketAClient(socket, count);
                    dispatch(new SocketEvent(SOCKET_A_CLIENT_CONNECTED, client));
                }
            } catch (IOException e) {
                Print("serverSocket.accept() failed");
                e.printStackTrace();
            }

            Print("Socket server thread started");
        }

        private class SocketAClient implements ISocketAClient {
            private Socket socket;
            private int count;

            SocketAClient(Socket socket, int count) {
                this.socket = socket;
                this.count = count;

                ReadThread t = new ReadThread();
                t.client = this;
                t.start();
            }

            @Override
            public void Disconnect() {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Print("Something wrong! " + e.toString() + "\n");
                } finally {
                    dispatch(new SocketEvent(SOCKET_A_CLIENT_CLOSED));
                }
            }

            @Override
            public void Send(String message) {
                try {
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                    writer.println(message);
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    Print("Send:: Something wrong! " + e.toString() + "\n");
                }
            }

            private class ReadThread extends Thread {
                ISocketAClient client;
                InputStream is;
                private byte[] contents = new byte[1024];

                @Override
                public void run() {
                    try {
                        is = socket.getInputStream();

                        int bytesRead;

                        //noinspection InfiniteLoopStatement
                        while (true) {
                            bytesRead = is.read(contents);

                            if (bytesRead > 0) {
                                String msg = new String(contents, 0, bytesRead);
                                dispatch(new SocketEvent(SOCKET_A_ON_MESSAGE, msg, client));
                                Print("Got message: " + msg + "\n");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Print("Something wrong! " + e.toString() + "\n");
                    }
                }
            }
        }
    }

    private class SocketEvent implements IEvent, ISocketEvent {
        private String name;
        private IEventDispatcher target;
        private ISocketAClient client;
        private String message;

        SocketEvent(String name, String message, ISocketAClient client) {
            this(name);
            this.client = client;
            this.message = message;
        }

        SocketEvent(String name, ISocketAClient client) {
            this(name);
            this.client = client;
        }

        SocketEvent(String name, String message) {
            this(name);
            this.message = message;
        }

        SocketEvent(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public IEventDispatcher getTarget() {
            return target;
        }

        @Override
        public void setTarget(IEventDispatcher target) {
            this.target = target;
        }

        @Override
        public ISocketAClient getClient() {
            return client;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }
}