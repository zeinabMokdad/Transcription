using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;

namespace AudioTranscriptionApp.Helpers
{
    public static class FileHelper
    {
        public static bool IsSupportedExtension(string filePath, IEnumerable<string> supportedExtensions)
        {
            var ext = Path.GetExtension(filePath);
            return supportedExtensions.Any(e => string.Equals(e, ext, StringComparison.OrdinalIgnoreCase));
        }

        public static string BuildFileFilter(IEnumerable<string> supportedExtensions)
        {
            var extensions = supportedExtensions
                .Select(e => e.StartsWith(".") ? $"*{e}" : $"*.{e}")
                .ToArray();

            var joined = string.Join(";", extensions);
            return $"Audio Files ({joined})|{joined}|All Files (*.*)|*.*";
        }
    }
}
