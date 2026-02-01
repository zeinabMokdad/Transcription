using System;
using AudioTranscriptionApp.Models;
using AudioTranscriptionApp.Services;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;

namespace AudioTranscriptionApp.ViewModels
{
    public partial class SettingsViewModel : ObservableObject
    {
        private readonly ISettingsService _settingsService;

        public SettingsViewModel(ISettingsService settingsService)
        {
            _settingsService = settingsService;
            var settings = _settingsService.GetEffectiveSettings();

            SubscriptionKey = settings.SubscriptionKey;
            OpenAiApiKey = settings.OpenAiApiKey;
            Region = settings.Region;
            OpenAiModel = settings.OpenAiModel;
            UseLocalWhisper = settings.UseLocalWhisper;
            LocalWhisperExecutablePath = settings.LocalWhisperExecutablePath;
            LocalWhisperModelPath = settings.LocalWhisperModelPath;
            Language = settings.Language;
            IncludeTimestamps = settings.IncludeTimestamps;
            DefaultFormat = settings.DefaultFormat;
        }

        public Action<bool>? CloseAction { get; set; }

        [ObservableProperty]
        private string subscriptionKey = string.Empty;

        [ObservableProperty]
        private string openAiApiKey = string.Empty;

        [ObservableProperty]
        private string region = string.Empty;

        [ObservableProperty]
        private string openAiModel = "whisper-1";

        [ObservableProperty]
        private bool useLocalWhisper;

        [ObservableProperty]
        private string localWhisperExecutablePath = string.Empty;

        [ObservableProperty]
        private string localWhisperModelPath = string.Empty;

        [ObservableProperty]
        private string language = "en-US";

        [ObservableProperty]
        private bool includeTimestamps;

        [ObservableProperty]
        private string defaultFormat = "txt";

        [RelayCommand]
        private void Save()
        {
            var settings = _settingsService.GetEffectiveSettings();
            settings.SubscriptionKey = SubscriptionKey;
            settings.OpenAiApiKey = OpenAiApiKey;
            settings.Region = Region;
            settings.OpenAiModel = OpenAiModel;
            settings.UseLocalWhisper = UseLocalWhisper;
            settings.LocalWhisperExecutablePath = LocalWhisperExecutablePath;
            settings.LocalWhisperModelPath = LocalWhisperModelPath;
            settings.Language = Language;
            settings.IncludeTimestamps = IncludeTimestamps;
            settings.DefaultFormat = DefaultFormat;

            _settingsService.SaveSettings(settings);
            CloseAction?.Invoke(true);
        }

        [RelayCommand]
        private void Cancel()
        {
            CloseAction?.Invoke(false);
        }
    }
}
