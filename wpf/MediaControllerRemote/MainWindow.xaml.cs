using System;
using System.Net;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;

namespace MediaControllerRemote
{
    public class Constants
    {
        public const string TOGGLE = "toggle";
        public const string NEXT = "next";
        public const string PREV = "prev";
        public const string VOL_UP = "volume_up";
        public const string VOL_DOWN = "volume_down";
        public const string SEARCH = "search";

        public const string ADDRESS = "192.168.1.48";
        public const int PORT = 8083;
    }

    public partial class MainWindow
    {

        public MainWindow()
        {
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