using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using AudioTranscriptionApp.Models;

namespace AudioTranscriptionApp.Helpers
{
    public static class SrtHelper
    {
        public static string ToSrt(IReadOnlyList<TranscriptionSegment> segments)
        {
            if (segments == null || segments.Count == 0)
            {
                return string.Empty;
            }

            var builder = new StringBuilder();
            for (var i = 0; i < segments.Count; i++)
            {
                var segment = segments[i];
                builder.AppendLine((i + 1).ToString());
                builder.AppendLine($"{FormatTime(segment.Start)} --> {FormatTime(segment.End)}");
                builder.AppendLine(segment.Text?.Trim() ?? string.Empty);
                builder.AppendLine();
            }

            return builder.ToString();
        }

        public static string ToTimestampedText(IReadOnlyList<TranscriptionSegment> segments)
        {
            if (segments == null || segments.Count == 0)
            {
                return string.Empty;
            }

            return string.Join(Environment.NewLine, segments.Select(s => $"[{FormatTimeSimple(s.Start)}] {s.Text?.Trim()}"));
        }

        private static string FormatTime(TimeSpan time)
        {
            return time.ToString(@"hh\:mm\:ss\,fff");
        }

        private static string FormatTimeSimple(TimeSpan time)
        {
            return time.ToString(@"hh\:mm\:ss");
        }
    }
}
