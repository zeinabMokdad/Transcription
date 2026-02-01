using System.Collections.Generic;

namespace AudioTranscriptionApp.Models
{
    public sealed class UserSettings
    {
        public string? SubscriptionKeyProtected { get; set; }

        public string? OpenAiKeyProtected { get; set; }

        public string? Region { get; set; }

        public string? OpenAiModel { get; set; }

        public bool? UseLocalWhisper { get; set; }

        public string? LocalWhisperExecutablePath { get; set; }

        public string? LocalWhisperModelPath { get; set; }

        public string? Language { get; set; }

        public bool? IncludeTimestamps { get; set; }

        public string? DefaultFormat { get; set; }

        public List<string>? RecentFiles { get; set; }
    }
}
