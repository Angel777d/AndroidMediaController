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

            TypeBox.SelectedIndex = LocalDataStorage.data.SelectedTypeIndex;
            QueryText.Text = LocalDataStorage.data.LastQuery;
        }

        private void OnBack(object sender, RoutedEventArgs e)
        {
            NavigationService?.GoBack();
        }

        private void OnSearch(object sender, RoutedEventArgs e)
        {
            var sb = new StringBuilder();

            sb.Append("search?");

            var selectedItem = (TextBlock) TypeBox.SelectedItem;
            sb.Append(selectedItem.Text);
            sb.Append("=");
            sb.Append(QueryText.Text);

            var searchStr = sb.ToString();
            MySocketClient.Instance.SendCommand(searchStr);

            LocalDataStorage.data.SelectedTypeIndex = TypeBox.SelectedIndex;
            LocalDataStorage.data.LastQuery = QueryText.Text;
            LocalDataStorage.WriteData();
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