using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;

namespace MediaControllerRemote
{
    public partial class Connectpage : Page
    {
        public Connectpage()
        {
            InitializeComponent();
            MySocketClient.Instance.SocketConnectedEvent += OnInstanceOnSocketConnectedEvent;
            MySocketClient.Instance.SocketDisconnectedEvent += OnInstanceOnSocketDisconnectedEvent;

            ipInput.Text = LocalDataStorage.data.IPAddress;
        }

        private void OnConnect(object sender, RoutedEventArgs e)
        {
            var addressText = ipInput.Text;

            try
            {
                //IPHostEntry ipHostInfo = Dns.GetHostEntry(host);
                //_remoteEp = new IPEndPoint(ipHostInfo.AddressList[0], port);

                var address = IPAddress.Parse(addressText);
                var defaultPort = 8083;
                MySocketClient.Instance.Connect(address, defaultPort);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
                StatusLabel.Content = "Wrong Ip formant:";
                return;
            }

            LocalDataStorage.data.IPAddress = addressText;
            LocalDataStorage.WriteData();

            StatusLabel.Content = "Connecting...";
        }

        private void OnInstanceOnSocketDisconnectedEvent(object sender, EventArgs e)
        {
            Dispatcher.Invoke(() => StatusLabel.Content = "Disconnected");
        }

        private void OnInstanceOnSocketConnectedEvent(object sender, EventArgs e)
        {
            Dispatcher.Invoke(() => StatusLabel.Content = "Connected!");
        }
    }
}