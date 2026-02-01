using System;
using System.Collections.Generic;
using System.IO;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Diagnostics;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Text.Json;
using AudioTranscriptionApp.Models;
using Microsoft.CognitiveServices.Speech;
using Microsoft.CognitiveServices.Speech.Audio;
using Microsoft.Extensions.Logging;
using NAudio.Wave;

namespace AudioTranscriptionApp.Services
{
    public sealed class TranscriptionService : ITranscriptionService
    {
        private readonly ILogger _logger;

        public TranscriptionService(ILogger logger)
        {
            _logger = logger;
        }

        public async Task<TranscriptionResult> TranscribeAsync(TranscriptionRequest request, IProgress<TranscriptionProgress> progress, CancellationToken cancellationToken)
        {
            if (!File.Exists(request.FilePath))
            {
                throw new FileNotFoundException("Audio file not found.", request.FilePath);
            }

            if (request.UseLocalWhisper)
            {
                return await TranscribeWithLocalWhisperAsync(request, progress, cancellationToken).ConfigureAwait(false);
            }

            if (!string.IsNullOrWhiteSpace(request.OpenAiApiKey))
            {
                return await TranscribeWithOpenAiAsync(request, progress, cancellationToken).ConfigureAwait(false);
            }

            if (!string.IsNullOrWhiteSpace(request.SubscriptionKey) && !string.IsNullOrWhiteSpace(request.Region))
            {
                return await TranscribeWithAzureAsync(request, progress, cancellationToken).ConfigureAwait(false);
            }

            throw new InvalidOperationException("Missing Azure Speech or OpenAI credentials.");
        }

        private async Task<TranscriptionResult> TranscribeWithLocalWhisperAsync(TranscriptionRequest request, IProgress<TranscriptionProgress> progress, CancellationToken cancellationToken)
        {
            if (string.IsNullOrWhiteSpace(request.LocalWhisperExecutablePath) || !File.Exists(request.LocalWhisperExecutablePath))
            {
                throw new InvalidOperationException("Local whisper executable not found.");
            }

            if (string.IsNullOrWhiteSpace(request.LocalWhisperModelPath) || !File.Exists(request.LocalWhisperModelPath))
            {
                throw new InvalidOperationException("Local whisper model not found.");
            }

            progress.Report(new TranscriptionProgress(5, "Running local whisper...", string.Empty));

            var outputDir = Path.Combine(Path.GetTempPath(), "whisper_output");
            Directory.CreateDirectory(outputDir);

            var outputBase = Path.Combine(outputDir, Path.GetFileNameWithoutExtension(request.FilePath));
            var args = new StringBuilder();
            args.Append("-m \"").Append(request.LocalWhisperModelPath).Append("\" ");
            args.Append("-f \"").Append(request.FilePath).Append("\" ");
            args.Append("-of \"").Append(outputBase).Append("\" ");
            args.Append("-otxt ");

            if (request.IncludeTimestamps)
            {
                args.Append("-osrt ");
            }

            var language = request.Language;
            if (!string.IsNullOrWhiteSpace(language) && language.Contains('-', StringComparison.Ordinal))
            {
                language = language.Split('-')[0];
            }

            if (!string.IsNullOrWhiteSpace(language))
            {
                args.Append("-l ").Append(language).Append(' ');
            }

            var startInfo = new ProcessStartInfo
            {
                FileName = request.LocalWhisperExecutablePath,
                Arguments = args.ToString(),
                RedirectStandardOutput = true,
                RedirectStandardError = true,
                UseShellExecute = false,
                CreateNoWindow = true
            };

            using var process = new Process { StartInfo = startInfo };
            process.Start();

            var stdErrTask = process.StandardError.ReadToEndAsync();
            var stdOutTask = process.StandardOutput.ReadToEndAsync();

            await process.WaitForExitAsync(cancellationToken).ConfigureAwait(false);

            var stdErr = await stdErrTask.ConfigureAwait(false);
            var stdOut = await stdOutTask.ConfigureAwait(false);

            if (process.ExitCode != 0)
            {
                throw new InvalidOperationException($"Local whisper failed: {stdErr}{stdOut}");
            }

            progress.Report(new TranscriptionProgress(90, "Processing output...", string.Empty));

            var textPath = outputBase + ".txt";
            var srtPath = outputBase + ".srt";

            var text = File.Exists(textPath) ? await File.ReadAllTextAsync(textPath, cancellationToken).ConfigureAwait(false) : string.Empty;
            var segments = new List<TranscriptionSegment>();

            if (request.IncludeTimestamps && File.Exists(srtPath))
            {
                segments = SrtHelperParse(srtPath);
            }

            progress.Report(new TranscriptionProgress(100, "Complete", text.Trim()));
            return new TranscriptionResult(text.Trim(), segments);
        }

        private static List<TranscriptionSegment> SrtHelperParse(string path)
        {
            var segments = new List<TranscriptionSegment>();
            var lines = File.ReadAllLines(path);
            for (var i = 0; i < lines.Length; i++)
            {
                var line = lines[i].Trim();
                if (string.IsNullOrWhiteSpace(line))
                {
                    continue;
                }

                if (i + 1 < lines.Length && lines[i + 1].Contains("-->", StringComparison.Ordinal))
                {
                    var timeLine = lines[i + 1];
                    var parts = timeLine.Split("-->");
                    if (parts.Length == 2 && TryParseSrtTime(parts[0].Trim(), out var start) && TryParseSrtTime(parts[1].Trim(), out var end))
                    {
                        var textBuilder = new StringBuilder();
                        var j = i + 2;
                        while (j < lines.Length && !string.IsNullOrWhiteSpace(lines[j]))
                        {
                            textBuilder.AppendLine(lines[j]);
                            j++;
                        }

                        var text = textBuilder.ToString().Trim();
                        if (!string.IsNullOrWhiteSpace(text))
                        {
                            segments.Add(new TranscriptionSegment(start, end, text));
                        }
                    }
                }
            }

            return segments;
        }

        private static bool TryParseSrtTime(string value, out TimeSpan result)
        {
            return TimeSpan.TryParseExact(value.Replace(',', '.'), "hh\\:mm\\:ss.fff", null, out result);
        }

        private async Task<TranscriptionResult> TranscribeWithAzureAsync(TranscriptionRequest request, IProgress<TranscriptionProgress> progress, CancellationToken cancellationToken)
        {
            var wavPath = await EnsureWavPathAsync(request.FilePath, cancellationToken);
            var shouldDeleteTemp = !string.Equals(wavPath, request.FilePath, StringComparison.OrdinalIgnoreCase);

            var speechConfig = SpeechConfig.FromSubscription(request.SubscriptionKey, request.Region);
            speechConfig.SpeechRecognitionLanguage = request.Language;

            var builder = new StringBuilder();
            var segments = new List<TranscriptionSegment>();

            using var audioConfig = AudioConfig.FromWavFileInput(wavPath);
            using var recognizer = new SpeechRecognizer(speechConfig, audioConfig);

            var completionSource = new TaskCompletionSource<bool>(TaskCreationOptions.RunContinuationsAsynchronously);

            recognizer.Recognizing += (_, args) =>
            {
                if (!string.IsNullOrWhiteSpace(args.Result.Text))
                {
                    progress.Report(new TranscriptionProgress(GetPercent(args.Result, request.Duration), "Listening...", builder.ToString()));
                }
            };

            recognizer.Recognized += (_, args) =>
            {
                if (args.Result.Reason == ResultReason.RecognizedSpeech && !string.IsNullOrWhiteSpace(args.Result.Text))
                {
                    var text = args.Result.Text.Trim();
                    builder.Append(text).Append(' ');

                    var offset = TimeSpan.FromTicks(args.Result.OffsetInTicks);
                    var end = offset + args.Result.Duration;
                    segments.Add(new TranscriptionSegment(offset, end, text));

                    progress.Report(new TranscriptionProgress(GetPercent(args.Result, request.Duration), "Processing...", builder.ToString().Trim()));
                }
            };

            recognizer.Canceled += (_, args) =>
            {
                var message = args.ErrorDetails ?? "Transcription canceled.";
                _logger.LogWarning("Transcription canceled: {Message}", message);
                completionSource.TrySetException(new InvalidOperationException(message));
            };

            recognizer.SessionStopped += (_, _) =>
            {
                completionSource.TrySetResult(true);
            };

            using var registration = cancellationToken.Register(() => _ = recognizer.StopContinuousRecognitionAsync());

            await recognizer.StartContinuousRecognitionAsync().ConfigureAwait(false);
            await completionSource.Task.ConfigureAwait(false);
            await recognizer.StopContinuousRecognitionAsync().ConfigureAwait(false);

            if (shouldDeleteTemp)
            {
                TryDeleteTemp(wavPath);
            }

            var finalText = builder.ToString().Trim();
            progress.Report(new TranscriptionProgress(100, "Complete", finalText));

            return new TranscriptionResult(finalText, segments);
        }

        private async Task<TranscriptionResult> TranscribeWithOpenAiAsync(TranscriptionRequest request, IProgress<TranscriptionProgress> progress, CancellationToken cancellationToken)
        {
            progress.Report(new TranscriptionProgress(5, "Uploading...", string.Empty));

            using var httpClient = new HttpClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", request.OpenAiApiKey);

            using var form = new MultipartFormDataContent();
            await using var fileStream = File.OpenRead(request.FilePath);
            var fileContent = new StreamContent(fileStream);
            fileContent.Headers.ContentType = new MediaTypeHeaderValue("application/octet-stream");
            form.Add(fileContent, "file", Path.GetFileName(request.FilePath));
            form.Add(new StringContent(request.OpenAiModel), "model");

            var openAiLanguage = request.Language;
            if (!string.IsNullOrWhiteSpace(openAiLanguage) && openAiLanguage.Contains('-', StringComparison.Ordinal))
            {
                openAiLanguage = openAiLanguage.Split('-')[0];
            }

            if (!string.IsNullOrWhiteSpace(openAiLanguage))
            {
                form.Add(new StringContent(openAiLanguage), "language");
            }

            if (request.IncludeTimestamps)
            {
                form.Add(new StringContent("verbose_json"), "response_format");
            }

            using var response = await httpClient.PostAsync("https://api.openai.com/v1/audio/transcriptions", form, cancellationToken).ConfigureAwait(false);
            var json = await response.Content.ReadAsStringAsync(cancellationToken).ConfigureAwait(false);
            if (!response.IsSuccessStatusCode)
            {
                throw new InvalidOperationException($"OpenAI transcription failed: {json}");
            }

            progress.Report(new TranscriptionProgress(90, "Processing...", string.Empty));

            var segments = new List<TranscriptionSegment>();
            var text = string.Empty;

            using var doc = JsonDocument.Parse(json);
            if (request.IncludeTimestamps && doc.RootElement.TryGetProperty("segments", out var segmentsElement))
            {
                foreach (var segment in segmentsElement.EnumerateArray())
                {
                    var start = TimeSpan.FromSeconds(segment.GetProperty("start").GetDouble());
                    var end = TimeSpan.FromSeconds(segment.GetProperty("end").GetDouble());
                    var segmentText = segment.GetProperty("text").GetString() ?? string.Empty;
                    segments.Add(new TranscriptionSegment(start, end, segmentText.Trim()));
                }
            }

            if (doc.RootElement.TryGetProperty("text", out var textElement))
            {
                text = textElement.GetString() ?? string.Empty;
            }

            progress.Report(new TranscriptionProgress(100, "Complete", text));
            return new TranscriptionResult(text, segments);
        }

        private static double GetPercent(SpeechRecognitionResult result, TimeSpan duration)
        {
            if (duration.TotalMilliseconds <= 0)
            {
                return 0;
            }

            var processed = TimeSpan.FromTicks(result.OffsetInTicks) + result.Duration;
            var percent = processed.TotalMilliseconds / duration.TotalMilliseconds * 100;
            return Math.Min(100, Math.Max(0, percent));
        }

        private static Task<string> EnsureWavPathAsync(string filePath, CancellationToken cancellationToken)
        {
            cancellationToken.ThrowIfCancellationRequested();

            if (string.Equals(Path.GetExtension(filePath), ".wav", StringComparison.OrdinalIgnoreCase))
            {
                return Task.FromResult(filePath);
            }

            var tempPath = Path.Combine(Path.GetTempPath(), $"transcription_{Guid.NewGuid():N}.wav");
            using var reader = new MediaFoundationReader(filePath);
            WaveFileWriter.CreateWaveFile(tempPath, reader);

            return Task.FromResult(tempPath);
        }

        private static void TryDeleteTemp(string path)
        {
            try
            {
                File.Delete(path);
            }
            catch
            {
                // ignore temp cleanup failures
            }
        }
    }
}
