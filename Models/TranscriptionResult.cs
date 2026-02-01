using System.Collections.Generic;

namespace AudioTranscriptionApp.Models
{
    public sealed class TranscriptionResult
    {
        public TranscriptionResult(string text, IReadOnlyList<TranscriptionSegment> segments)
        {
            Text = text;
            Segments = segments;
        }

        public string Text { get; }

        public IReadOnlyList<TranscriptionSegment> Segments { get; }
    }
}
