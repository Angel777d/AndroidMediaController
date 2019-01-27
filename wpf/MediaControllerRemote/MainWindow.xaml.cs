using System;
using System.Net;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;

namespace MediaControllerRemote
{
    public partial class MainWindow
    {
        public MainWindow()
        {
            LocalDataStorage.ReadData();

            InitializeComponent();

            MySocketClient.Instance.SocketConnectedEvent += OnInstanceOnSocketConnectedEvent;
            MySocketClient.Instance.SocketDisconnectedEvent += OnInstanceOnSocketDisconnectedEvent;
        }

        private void OnInstanceOnSocketDisconnectedEvent(object sender, EventArgs e)
        {
            Dispatcher.Invoke(() => NavTo("ConnectPage.xaml"));
        }

        private void OnInstanceOnSocketConnectedEvent(object sender, EventArgs e)
        {
            Dispatcher.Invoke(() => NavTo("MainPage.xaml"));
        }

        private void NavTo(string page)
        {
            FrameWithinGrid.Navigate(new Uri(page, UriKind.RelativeOrAbsolute));
        }
    }
}