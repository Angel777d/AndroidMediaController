using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;

namespace MediaControllerRemote
{
    public partial class MainPage : Page
    {
        public MainPage()
        {
            InitializeComponent();

            MySocketClient.Instance.MessageEvent += OnInstanceOnMessageEvent;
        }

        private void OnInstanceOnMessageEvent(object sender, string message)
        {
            Dispatcher.Invoke(() => Log("IN:" + message));
        }

        private void OnClose(object sender, RoutedEventArgs e)
        {
            MySocketClient.Instance.Disconnect();
        }

        private void OnAction(object sender, RoutedEventArgs e)
        {
            var btn = (Button) sender;
            MySocketClient.Instance.SendCommand(btn.Name);
        }

        private void OnSearch(object sender, RoutedEventArgs e)
        {
            NavigationService?.Navigate(new Uri("SearchPage.xaml", UriKind.RelativeOrAbsolute));
        }

        private void Log(string message)
        {
            ResultsText.Text = message + "\n" + ResultsText.Text;
        }
    }
}