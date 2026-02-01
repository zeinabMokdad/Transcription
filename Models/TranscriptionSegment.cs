using System;

namespace AudioTranscriptionApp.Models
{
    public sealed class TranscriptionSegment
    {
        public TranscriptionSegment(TimeSpan start, TimeSpan end, string text, string? speaker = null)
        {
            Start = start;
            End = end;
            Text = text;
            Speaker = speaker;
        }

        public TimeSpan Start { get; }

        public TimeSpan End { get; }

        public string Text { get; }

        public string? Speaker { get; }
    }
}
