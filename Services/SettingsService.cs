using System;
using System.IO;
using AudioTranscriptionApp.Helpers;
using AudioTranscriptionApp.Models;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;

namespace AudioTranscriptionApp.Services
{
    public sealed class SettingsService : ISettingsService
    {
        private readonly ILogger _logger;
        private readonly string _userSettingsPath;
        private readonly AppSettings _defaultSettings;

        public SettingsService(ILogger logger)
        {
            _logger = logger;
            _userSettingsPath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData), "AudioTranscriptionApp", "userSettings.json");
            _defaultSettings = LoadDefaultSettings();
        }

        public EffectiveSettings GetEffectiveSettings()
        {
            var (userSettings, hasUserSettings) = LoadUserSettings();

            var subscriptionKey = hasUserSettings
                ? SecureSettingsHelper.Unprotect(userSettings.SubscriptionKeyProtected)
                : _defaultSettings.AzureSpeech.SubscriptionKey;

            var region = hasUserSettings
                ? userSettings.Region ?? string.Empty
                : _defaultSettings.AzureSpeech.Region;

            var openAiKey = hasUserSettings
                ? SecureSettingsHelper.Unprotect(userSettings.OpenAiKeyProtected)
                : _defaultSettings.OpenAI.ApiKey;

            var openAiModel = string.IsNullOrWhiteSpace(userSettings.OpenAiModel)
                ? _defaultSettings.OpenAI.Model
                : userSettings.OpenAiModel;

            var useLocalWhisper = userSettings.UseLocalWhisper ?? _defaultSettings.LocalWhisper.Enabled;
            var localWhisperExe = string.IsNullOrWhiteSpace(userSettings.LocalWhisperExecutablePath)
                ? _defaultSettings.LocalWhisper.ExecutablePath
                : userSettings.LocalWhisperExecutablePath;
            var localWhisperModel = string.IsNullOrWhiteSpace(userSettings.LocalWhisperModelPath)
                ? _defaultSettings.LocalWhisper.ModelPath
                : userSettings.LocalWhisperModelPath;

            return new EffectiveSettings
            {
                SubscriptionKey = subscriptionKey,
                OpenAiApiKey = openAiKey,
                Region = region,
                OpenAiModel = openAiModel,
                UseLocalWhisper = useLocalWhisper,
                LocalWhisperExecutablePath = localWhisperExe,
                LocalWhisperModelPath = localWhisperModel,
                Language = string.IsNullOrWhiteSpace(userSettings.Language)
                    ? _defaultSettings.DefaultLanguage
                    : userSettings.Language,
                IncludeTimestamps = userSettings.IncludeTimestamps ?? _defaultSettings.Output.IncludeTimestamps,
                DefaultFormat = string.IsNullOrWhiteSpace(userSettings.DefaultFormat)
                    ? _defaultSettings.Output.DefaultFormat
                    : userSettings.DefaultFormat,
                SupportedFormats = _defaultSettings.SupportedFormats,
                RecentFiles = userSettings.RecentFiles ?? _defaultSettings.RecentFiles
            };
        }

        public void SaveSettings(EffectiveSettings settings)
        {
            var userSettings = new UserSettings
            {
                SubscriptionKeyProtected = SecureSettingsHelper.Protect(settings.SubscriptionKey),
                OpenAiKeyProtected = SecureSettingsHelper.Protect(settings.OpenAiApiKey),
                Region = settings.Region,
                OpenAiModel = settings.OpenAiModel,
                UseLocalWhisper = settings.UseLocalWhisper,
                LocalWhisperExecutablePath = settings.LocalWhisperExecutablePath,
                LocalWhisperModelPath = settings.LocalWhisperModelPath,
                Language = settings.Language,
                IncludeTimestamps = settings.IncludeTimestamps,
                DefaultFormat = settings.DefaultFormat,
                RecentFiles = settings.RecentFiles
            };

            var directory = Path.GetDirectoryName(_userSettingsPath);
            if (!string.IsNullOrWhiteSpace(directory))
            {
                Directory.CreateDirectory(directory);
            }

            File.WriteAllText(_userSettingsPath, JsonConvert.SerializeObject(userSettings, Formatting.Indented));
        }

        private AppSettings LoadDefaultSettings()
        {
            try
            {
                var config = new ConfigurationBuilder()
                    .SetBasePath(AppContext.BaseDirectory)
                    .AddJsonFile("appsettings.json", optional: true, reloadOnChange: false)
                    .Build();

                var settings = new AppSettings();
                config.Bind(settings);
                return settings;
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Failed to load appsettings.json");
                return new AppSettings();
            }
        }

        private (UserSettings Settings, bool HasUserSettings) LoadUserSettings()
        {
            try
            {
                if (!File.Exists(_userSettingsPath))
                {
                    return (new UserSettings(), false);
                }

                var json = File.ReadAllText(_userSettingsPath);
                var settings = JsonConvert.DeserializeObject<UserSettings>(json) ?? new UserSettings();
                return (settings, true);
            }
            catch (Exception ex)
            {
                _logger.LogWarning(ex, "Failed to load user settings");
                return (new UserSettings(), false);
            }
        }
    }
}
