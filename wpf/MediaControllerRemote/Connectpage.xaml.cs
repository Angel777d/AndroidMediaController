using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Threading;

namespace MediaControllerRemote
{
    public partial class Connectpage : Page
    {
        public Connectpage()
        {
            InitializeComponent();
            MySocketClient.Instance.SocketConnectedEvent += OnInstanceOnSocketConnectedEvent;
            MySocketClient.Instance.SocketDisconnectedEvent += OnInstanceOnSocketDisconnectedEvent;
        }

        private void OnConnect(object sender, RoutedEventArgs e)
        {
            string addressText = ipInput.Text;

            try
            {
                //IPHostEntry ipHostInfo = Dns.GetHostEntry(host);
                //_remoteEp = new IPEndPoint(ipHostInfo.AddressList[0], port);

                IPAddress address = IPAddress.Parse(addressText);
                MySocketClient.Instance.Connect(address, Constants.PORT);
            }
            catch (Exception ex)
            {
                Console.WriteLine(e);
                StatusLabel.Content = "Wrong Ip formant:";
                return;
                
            }
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