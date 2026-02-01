using System.Collections.Generic;

namespace AudioTranscriptionApp.Models
{
    public sealed class AppSettings
    {
        public AzureSpeechSettings AzureSpeech { get; set; } = new();

        public OpenAiSettings OpenAI { get; set; } = new();

        public LocalWhisperSettings LocalWhisper { get; set; } = new();

        public string DefaultLanguage { get; set; } = "en-US";

        public List<string> SupportedFormats { get; set; } = new()
        {
            ".mp3",
            ".wav",
            ".m4a",
            ".flac",
            ".ogg"
        };

        public OutputSettings Output { get; set; } = new();

        public List<string> RecentFiles { get; set; } = new();
    }

    public sealed class AzureSpeechSettings
    {
        public string SubscriptionKey { get; set; } = string.Empty;

        public string Region { get; set; } = string.Empty;
    }

    public sealed class OpenAiSettings
    {
        public string ApiKey { get; set; } = string.Empty;

        public string Model { get; set; } = "whisper-1";
    }

    public sealed class LocalWhisperSettings
    {
        public bool Enabled { get; set; }

        public string ExecutablePath { get; set; } = string.Empty;

        public string ModelPath { get; set; } = string.Empty;
    }

    public sealed class OutputSettings
    {
        public bool IncludeTimestamps { get; set; }

        public string DefaultFormat { get; set; } = "txt";
    }
}
