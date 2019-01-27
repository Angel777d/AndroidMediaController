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
            string pf = WebUtility.UrlEncode("Pink Floyd");
            string searchStr = string.Format("search?focus=artist&artist={0}&query={1}", pf, pf);
            MySocketClient.Instance.SendCommand(searchStr);
        }

        private void Log(string message)
        {
            ResultsText.Text = message + "\n" + ResultsText.Text;
        }
    }
}