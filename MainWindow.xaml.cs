using System;
using System.Threading.Tasks;
using System.Windows;
using AudioTranscriptionApp.ViewModels;
using AudioTranscriptionApp.Views;
using Microsoft.Extensions.DependencyInjection;

namespace AudioTranscriptionApp
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private MainViewModel? _viewModel;

        public MainWindow()
        {
            InitializeComponent();
            DataContextChanged += OnDataContextChanged;
        }

        private void OnDataContextChanged(object sender, DependencyPropertyChangedEventArgs e)
        {
            if (e.OldValue is MainViewModel oldViewModel)
            {
                oldViewModel.SettingsRequested -= OnSettingsRequested;
            }

            if (e.NewValue is MainViewModel newViewModel)
            {
                _viewModel = newViewModel;
                newViewModel.SettingsRequested += OnSettingsRequested;
            }
        }

        private void OnSettingsRequested(object? sender, EventArgs e)
        {
            var app = (App)Application.Current;
            var settingsViewModel = app.Services.GetRequiredService<SettingsViewModel>();
            var settingsWindow = new SettingsWindow(settingsViewModel)
            {
                Owner = this
            };

            if (settingsWindow.ShowDialog() == true)
            {
                _viewModel?.ReloadSettings();
            }
        }

        private async void Window_Drop(object sender, DragEventArgs e)
        {
            if (_viewModel == null)
            {
                return;
            }

            if (e.Data.GetData(DataFormats.FileDrop) is string[] files && files.Length > 0)
            {
                await _viewModel.HandleFileDropAsync(files);
            }
        }

        private void Window_DragOver(object sender, DragEventArgs e)
        {
            e.Effects = e.Data.GetDataPresent(DataFormats.FileDrop) ? DragDropEffects.Copy : DragDropEffects.None;
            e.Handled = true;
        }
    }
}
