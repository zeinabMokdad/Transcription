using AudioTranscriptionApp.Models;

namespace AudioTranscriptionApp.Services
{
    public interface ISettingsService
    {
        EffectiveSettings GetEffectiveSettings();

        void SaveSettings(EffectiveSettings settings);
    }
}
