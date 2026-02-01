namespace AudioTranscriptionApp.Models
{
    public sealed class TranscriptionProgress
    {
        public TranscriptionProgress(double percent, string status, string currentText)
        {
            Percent = percent;
            Status = status;
            CurrentText = currentText;
        }

        public double Percent { get; }

        public string Status { get; }

        public string CurrentText { get; }
    }
}
