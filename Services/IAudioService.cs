using System.Threading;
using System.Threading.Tasks;
using AudioTranscriptionApp.Models;

namespace AudioTranscriptionApp.Services
{
    public interface IAudioService
    {
        Task<AudioFile> LoadAsync(string filePath, CancellationToken cancellationToken);
    }
}
