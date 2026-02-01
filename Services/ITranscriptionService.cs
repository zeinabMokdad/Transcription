using System;
using System.Threading;
using System.Threading.Tasks;
using AudioTranscriptionApp.Models;

namespace AudioTranscriptionApp.Services
{
    public interface ITranscriptionService
    {
        Task<TranscriptionResult> TranscribeAsync(TranscriptionRequest request, IProgress<TranscriptionProgress> progress, CancellationToken cancellationToken);
    }
}
