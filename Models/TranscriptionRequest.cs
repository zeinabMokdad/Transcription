using System;

namespace AudioTranscriptionApp.Models
{
    public sealed class TranscriptionRequest
    {
        public TranscriptionRequest(string filePath, TimeSpan duration, string subscriptionKey, string region, string openAiApiKey, string openAiModel, bool useLocalWhisper, string localWhisperExecutablePath, string localWhisperModelPath, string language, bool includeTimestamps)
        {
            FilePath = filePath;
            Duration = duration;
            SubscriptionKey = subscriptionKey;
            Region = region;
            OpenAiApiKey = openAiApiKey;
            OpenAiModel = openAiModel;
            UseLocalWhisper = useLocalWhisper;
            LocalWhisperExecutablePath = localWhisperExecutablePath;
            LocalWhisperModelPath = localWhisperModelPath;
            Language = language;
            IncludeTimestamps = includeTimestamps;
        }

        public string FilePath { get; }

        public TimeSpan Duration { get; }

        public string SubscriptionKey { get; }

        public string Region { get; }

        public string OpenAiApiKey { get; }

        public string OpenAiModel { get; }

        public bool UseLocalWhisper { get; }

        public string LocalWhisperExecutablePath { get; }

        public string LocalWhisperModelPath { get; }

        public string Language { get; }

        public bool IncludeTimestamps { get; }
    }
}
