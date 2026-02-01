using System;

namespace AudioTranscriptionApp.Models
{
    public sealed class AudioFile
    {
        public AudioFile(string filePath, TimeSpan duration)
        {
            FilePath = filePath;
            Duration = duration;
        }

        public string FilePath { get; }

        public string FileName => System.IO.Path.GetFileName(FilePath);

        public string Extension => System.IO.Path.GetExtension(FilePath);

        public TimeSpan Duration { get; }
    }
}
