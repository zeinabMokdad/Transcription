using System;
using System.Security.Cryptography;
using System.Text;

namespace AudioTranscriptionApp.Helpers
{
    public static class SecureSettingsHelper
    {
        public static string Protect(string value)
        {
            if (string.IsNullOrWhiteSpace(value))
            {
                return string.Empty;
            }

            var bytes = Encoding.UTF8.GetBytes(value);
            var protectedBytes = ProtectedData.Protect(bytes, null, DataProtectionScope.CurrentUser);
            return Convert.ToBase64String(protectedBytes);
        }

        public static string Unprotect(string? protectedValue)
        {
            if (string.IsNullOrWhiteSpace(protectedValue))
            {
                return string.Empty;
            }

            try
            {
                var bytes = Convert.FromBase64String(protectedValue);
                var unprotected = ProtectedData.Unprotect(bytes, null, DataProtectionScope.CurrentUser);
                return Encoding.UTF8.GetString(unprotected);
            }
            catch
            {
                return string.Empty;
            }
        }
    }
}
