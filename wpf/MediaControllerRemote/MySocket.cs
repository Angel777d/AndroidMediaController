using System;
using System.Net;
using System.Net.Sockets;
using System.Text;

namespace MediaControllerRemote
{
    public class MySocketClient
    {
        public static MySocketClient Instance = new MySocketClient();
        private Socket _socket;
        private bool _connected;

        //Events
        public event EventHandler SocketConnectedEvent;
        public event EventHandler SocketDisconnectedEvent;
        public event EventHandler<string> MessageEvent;

        private class StateObject
        {
            public IPEndPoint RemoteEp;
            public MySocketClient Client;
            public Socket Socket;

            public const int BufferSize = 256;
            public readonly byte[] Buffer = new byte[BufferSize];
        }

        private MySocketClient()
        {
        }

        public void Connect(IPAddress address, int port)
        {
            if (_connected)
            {
                return;
            }

            _connected = true;
            IPEndPoint remoteEp = new IPEndPoint(address, port);
            _socket = new Socket(remoteEp.Address.AddressFamily, SocketType.Stream, ProtocolType.Tcp);
            StateObject so = new StateObject {Client = this, RemoteEp = remoteEp, Socket = _socket};

            StartClient(so);
        }

        public void Disconnect()
        {
            if (!_connected) return;
            _connected = false;

            // Release the socket.  
            try
            {
                _socket.Shutdown(SocketShutdown.Both);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }

            _socket.Close();
            _socket = null;

            SocketDisconnectedEvent?.Invoke(this, null);
        }

        public void SendCommand(string command)
        {
            StateObject so = new StateObject {Client = this, Socket = _socket};
            Send(so, command);
        }

        public void OnConnected()
        {
            SocketConnectedEvent?.Invoke(this, null);
        }

        public void OnMessage(string cmd)
        {
            Console.WriteLine("Got data from socket {0}", cmd);
            MessageEvent?.Invoke(this, cmd);
        }

        private static void StartClient(StateObject state)
        {
            try
            {
                state.Socket.BeginConnect(state.RemoteEp, OnConnect, state);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        private static void OnConnect(IAsyncResult ar)
        {
            StateObject state = (StateObject) ar.AsyncState;
            try
            {
                state.Socket.EndConnect(ar);
                state.Client.OnConnected();
                Receive(state);
                Console.WriteLine("Socket connected to {0}", state.Socket.RemoteEndPoint);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
                state.Client.Disconnect();
            }
        }


        private static void Receive(StateObject state)
        {
            try
            {
                state.Socket.BeginReceive(state.Buffer, 0, StateObject.BufferSize, 0, OnReceive, state);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        private static void OnReceive(IAsyncResult ar)
        {
            try
            {
                StateObject state = (StateObject) ar.AsyncState;
                int bytesRead = state.Socket.EndReceive(ar);
                if (bytesRead > 0)
                {
                    state.Client.OnMessage(Encoding.ASCII.GetString(state.Buffer, 0, bytesRead));
                }

                state.Socket.BeginReceive(state.Buffer, 0, StateObject.BufferSize, 0, OnReceive, state);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        private static void Send(StateObject state, string data)
        {
            byte[] byteData = Encoding.ASCII.GetBytes(data);
            state.Socket.BeginSend(byteData, 0, byteData.Length, 0, OnSend, state);
        }

        private static void OnSend(IAsyncResult ar)
        {
            StateObject state = (StateObject) ar.AsyncState;
            try
            {
                state.Socket.EndSend(ar);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }
    }
}