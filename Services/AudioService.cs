using System;
using System.IO;
using System.Threading;
using System.Threading.Tasks;
using AudioTranscriptionApp.Models;
using NAudio.Wave;

namespace AudioTranscriptionApp.Services
{
    public sealed class AudioService : IAudioService
    {
        public Task<AudioFile> LoadAsync(string filePath, CancellationToken cancellationToken)
        {
            cancellationToken.ThrowIfCancellationRequested();

            var extension = Path.GetExtension(filePath);
            TimeSpan duration;
            if (string.Equals(extension, ".mp3", StringComparison.OrdinalIgnoreCase))
            {
                using var mp3Reader = new Mp3FileReader(filePath);
                duration = mp3Reader.TotalTime;
            }
            else
            {
                using var reader = new MediaFoundationReader(filePath);
                duration = reader.TotalTime;
            }

            return Task.FromResult(new AudioFile(filePath, duration));
        }
    }
}
