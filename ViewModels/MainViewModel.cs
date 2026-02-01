using System;
using System.Collections.ObjectModel;
using System.IO;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using AudioTranscriptionApp.Helpers;
using AudioTranscriptionApp.Models;
using AudioTranscriptionApp.Services;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using Microsoft.Win32;

namespace AudioTranscriptionApp.ViewModels
{
    public partial class MainViewModel : ObservableObject
    {
        private readonly IAudioService _audioService;
        private readonly ITranscriptionService _transcriptionService;
        private readonly ISettingsService _settingsService;
        private EffectiveSettings _settings;
        private CancellationTokenSource? _cancellationTokenSource;
        private TranscriptionResult? _lastResult;

        public MainViewModel(IAudioService audioService, ITranscriptionService transcriptionService, ISettingsService settingsService)
        {
            _audioService = audioService;
            _transcriptionService = transcriptionService;
            _settingsService = settingsService;

            _settings = _settingsService.GetEffectiveSettings();

            RecentFiles = new ObservableCollection<string>(_settings.RecentFiles);
            Queue = new ObservableCollection<AudioFile>();

            StatusMessage = "Ready";
            OutputFormat = _settings.DefaultFormat;
            IncludeTimestamps = _settings.IncludeTimestamps;

            Queue.CollectionChanged += (_, __) => StartTranscriptionCommand.NotifyCanExecuteChanged();
        }

        public event EventHandler? SettingsRequested;

        public ObservableCollection<string> RecentFiles { get; }

        public ObservableCollection<AudioFile> Queue { get; }

        [ObservableProperty]
        private string? selectedFilePath;

        [ObservableProperty]
        private string? selectedFileName;

        [ObservableProperty]
        private string? durationText;

        [ObservableProperty]
        private string statusMessage;

        [ObservableProperty]
        private double progressValue;

        [ObservableProperty]
        private bool isBusy;

        [ObservableProperty]
        private string transcriptionText = string.Empty;

        [ObservableProperty]
        private bool includeTimestamps;

        [ObservableProperty]
        private string outputFormat;

        [ObservableProperty]
        private string? selectedRecentFile;

        partial void OnIsBusyChanged(bool value)
        {
            StartTranscriptionCommand.NotifyCanExecuteChanged();
            SelectFileCommand.NotifyCanExecuteChanged();
        }

        partial void OnSelectedFilePathChanged(string? value)
        {
            StartTranscriptionCommand.NotifyCanExecuteChanged();
        }

        [RelayCommand(CanExecute = nameof(CanSelectFile))]
        private async Task SelectFileAsync()
        {
            var dialog = new OpenFileDialog
            {
                Filter = FileHelper.BuildFileFilter(_settings.SupportedFormats),
                Multiselect = true
            };

            if (dialog.ShowDialog() != true)
            {
                return;
            }

            await AddFilesToQueueAsync(dialog.FileNames);
        }

        [RelayCommand(CanExecute = nameof(CanStartTranscription))]
        private async Task StartTranscriptionAsync()
        {
            if (IsBusy)
            {
                return;
            }

            var filesToProcess = Queue.Any() ? Queue.ToList() : new System.Collections.Generic.List<AudioFile>();
            if (!filesToProcess.Any() && !string.IsNullOrWhiteSpace(SelectedFilePath))
            {
                var single = await _audioService.LoadAsync(SelectedFilePath!, CancellationToken.None);
                filesToProcess = new System.Collections.Generic.List<AudioFile> { single };
            }

            if (!filesToProcess.Any())
            {
                return;
            }

            _cancellationTokenSource = new CancellationTokenSource();
            IsBusy = true;
            ProgressValue = 0;

            try
            {
                var batchIndex = 1;
                var batchTotal = filesToProcess.Count;
                TranscriptionText = string.Empty;

                foreach (var audioFile in filesToProcess)
                {
                    StatusMessage = batchTotal > 1
                        ? $"Processing {batchIndex} of {batchTotal}"
                        : "Processing...";

                    IProgress<TranscriptionProgress> progress = new Progress<TranscriptionProgress>(p =>
                    {
                        ProgressValue = p.Percent;
                        StatusMessage = p.Status;
                        if (!string.IsNullOrWhiteSpace(p.CurrentText))
                        {
                            TranscriptionText = p.CurrentText;
                        }
                    });

                    var request = new TranscriptionRequest(
                        audioFile.FilePath,
                        audioFile.Duration,
                        _settings.SubscriptionKey,
                        _settings.Region,
                        _settings.OpenAiApiKey,
                        _settings.OpenAiModel,
                        _settings.UseLocalWhisper,
                        _settings.LocalWhisperExecutablePath,
                        _settings.LocalWhisperModelPath,
                        _settings.Language,
                        IncludeTimestamps);

                    var result = await _transcriptionService.TranscribeAsync(request, progress, _cancellationTokenSource.Token);
                    _lastResult = result;

                    if (batchTotal > 1)
                    {
                        TranscriptionText += Environment.NewLine + Environment.NewLine + $"--- {audioFile.FileName} ---" + Environment.NewLine + result.Text;
                    }
                    else
                    {
                        TranscriptionText = result.Text;
                    }

                    AddRecentFile(audioFile.FilePath);
                    batchIndex++;
                }

                StatusMessage = "Complete";
            }
            catch (Exception ex)
            {
                StatusMessage = "Error";
                MessageBox.Show(ex.Message, "Transcription Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
            finally
            {
                IsBusy = false;
                ProgressValue = 0;
            }
        }

        [RelayCommand]
        private void CopyText()
        {
            if (!string.IsNullOrWhiteSpace(TranscriptionText))
            {
                Clipboard.SetText(TranscriptionText);
            }
        }

        [RelayCommand]
        private void ClearText()
        {
            TranscriptionText = string.Empty;
        }

        [RelayCommand]
        private void NewTranscription()
        {
            _cancellationTokenSource?.Cancel();
            Queue.Clear();
            SelectedFilePath = null;
            SelectedFileName = null;
            DurationText = null;
            TranscriptionText = string.Empty;
            StatusMessage = "Ready";
            ProgressValue = 0;
        }

        [RelayCommand]
        private void SaveText()
        {
            if (_lastResult == null || string.IsNullOrWhiteSpace(TranscriptionText))
            {
                return;
            }

            var defaultIsSrt = string.Equals(_settings.DefaultFormat, "srt", StringComparison.OrdinalIgnoreCase);
            var defaultExt = defaultIsSrt ? ".srt" : ".txt";

            var dialog = new SaveFileDialog
            {
                Filter = "Text File (*.txt)|*.txt|SRT Subtitle (*.srt)|*.srt",
                FileName = Path.GetFileNameWithoutExtension(SelectedFileName ?? "transcription"),
                DefaultExt = defaultExt,
                FilterIndex = defaultIsSrt ? 2 : 1
            };

            if (dialog.ShowDialog() != true)
            {
                return;
            }

            var extension = Path.GetExtension(dialog.FileName).ToLowerInvariant();
            if (extension == ".srt")
            {
                File.WriteAllText(dialog.FileName, SrtHelper.ToSrt(_lastResult.Segments));
            }
            else if (IncludeTimestamps)
            {
                File.WriteAllText(dialog.FileName, SrtHelper.ToTimestampedText(_lastResult.Segments));
            }
            else
            {
                File.WriteAllText(dialog.FileName, TranscriptionText);
            }
        }

        [RelayCommand]
        private void OpenSettings()
        {
            SettingsRequested?.Invoke(this, EventArgs.Empty);
        }

        [RelayCommand(CanExecute = nameof(CanLoadRecent))]
        private async Task LoadRecentAsync()
        {
            if (string.IsNullOrWhiteSpace(SelectedRecentFile) || !File.Exists(SelectedRecentFile))
            {
                return;
            }

            await AddFilesToQueueAsync(new[] { SelectedRecentFile });
        }

        public async Task HandleFileDropAsync(string[] filePaths)
        {
            await AddFilesToQueueAsync(filePaths);
        }

        public void ReloadSettings()
        {
            _settings = _settingsService.GetEffectiveSettings();
            IncludeTimestamps = _settings.IncludeTimestamps;
            OutputFormat = _settings.DefaultFormat;

            RecentFiles.Clear();
            foreach (var file in _settings.RecentFiles)
            {
                RecentFiles.Add(file);
            }
        }

        private async Task AddFilesToQueueAsync(string[] filePaths)
        {
            if (filePaths.Length == 0)
            {
                return;
            }

            StatusMessage = "Loading files...";
            foreach (var filePath in filePaths)
            {
                if (!FileHelper.IsSupportedExtension(filePath, _settings.SupportedFormats))
                {
                    MessageBox.Show($"Unsupported file format: {filePath}", "File Error", MessageBoxButton.OK, MessageBoxImage.Warning);
                    continue;
                }

                try
                {
                    var audioFile = await _audioService.LoadAsync(filePath, CancellationToken.None);
                    Queue.Add(audioFile);

                    SelectedFilePath = audioFile.FilePath;
                    SelectedFileName = audioFile.FileName;
                    DurationText = audioFile.Duration.ToString(@"hh\:mm\:ss");
                }
                catch (Exception ex)
                {
                    MessageBox.Show(ex.Message, "File Error", MessageBoxButton.OK, MessageBoxImage.Error);
                }
            }

            StatusMessage = "Ready";
        }

        private void AddRecentFile(string filePath)
        {
            var existing = RecentFiles.FirstOrDefault(f => string.Equals(f, filePath, StringComparison.OrdinalIgnoreCase));
            if (existing != null)
            {
                RecentFiles.Remove(existing);
            }

            RecentFiles.Insert(0, filePath);
            while (RecentFiles.Count > 10)
            {
                RecentFiles.RemoveAt(RecentFiles.Count - 1);
            }

            _settings.RecentFiles = RecentFiles.ToList();
            _settingsService.SaveSettings(_settings);
        }

        private bool CanSelectFile() => !IsBusy;

        private bool CanStartTranscription() => !IsBusy && (Queue.Any() || !string.IsNullOrWhiteSpace(SelectedFilePath));

        private bool CanLoadRecent() => !IsBusy && !string.IsNullOrWhiteSpace(SelectedRecentFile);
    }
}
