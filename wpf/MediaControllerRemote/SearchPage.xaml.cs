using System.Diagnostics;
using System.Net;
using System.Text;
using System.Windows;
using System.Windows.Controls;

namespace MediaControllerRemote
{
    public partial class SearchPage : Page
    {
        public SearchPage()
        {
            InitializeComponent();
        }

        private void OnBack(object sender, RoutedEventArgs e)
        {
            NavigationService?.GoBack();
        }

        private void OnSearch(object sender, RoutedEventArgs e)
        {
            StringBuilder sb = new StringBuilder();

            sb.Append("search?");
            
            TextBlock selectedItem = (TextBlock)TypeBox.SelectedItem;
            sb.Append(selectedItem.Text);
            sb.Append("=");
            sb.Append(QueryText.Text);
           
            string searchStr = sb.ToString();
            MySocketClient.Instance.SendCommand(searchStr);
        }

        private void fill(StringBuilder sb, string field, TextBox box)
        {
            if (box.Text.Length <= 0) return;
            sb.Append("&");
            sb.Append(field);
            sb.Append("=");
            
            sb.Append(box.Text);
        }
    }
}