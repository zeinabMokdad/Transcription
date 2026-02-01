using System.Windows;
using AudioTranscriptionApp.ViewModels;

namespace AudioTranscriptionApp.Views
{
    public partial class SettingsWindow : Window
    {
        public SettingsWindow(SettingsViewModel viewModel)
        {
            InitializeComponent();
            DataContext = viewModel;
            viewModel.CloseAction = result =>
            {
                DialogResult = result;
                Close();
            };
        }
    }
}
