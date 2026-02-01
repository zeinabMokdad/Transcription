using System.Collections.Generic;

namespace AudioTranscriptionApp.Models
{
    public sealed class EffectiveSettings
    {
        public string SubscriptionKey { get; set; } = string.Empty;

        public string Region { get; set; } = string.Empty;

        public string OpenAiApiKey { get; set; } = string.Empty;

        public string OpenAiModel { get; set; } = "whisper-1";

        public bool UseLocalWhisper { get; set; }

        public string LocalWhisperExecutablePath { get; set; } = string.Empty;

        public string LocalWhisperModelPath { get; set; } = string.Empty;

        public string Language { get; set; } = "en-US";

        public bool IncludeTimestamps { get; set; }

        public string DefaultFormat { get; set; } = "txt";

        public List<string> SupportedFormats { get; set; } = new();

        public List<string> RecentFiles { get; set; } = new();
    }
}
