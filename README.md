# Windows Audio Transcription Application

A WPF desktop app for transcribing audio files to text using Azure Speech Services.

## Requirements
- Windows 10/11
- .NET 8.0 SDK or later
- Azure Speech subscription key and region OR OpenAI API key

## Setup
1. Build the project with .NET 8.0.
2. Launch the app.
3. Open **Settings** and enter your Azure Speech subscription key and region, or your OpenAI API key.
4. Select an audio file (MP3, WAV, M4A, FLAC, OGG) and click **Start Transcription**.

## Features
- File picker and drag-and-drop support
- Audio duration display
- Real-time progress reporting
- Copy, save, clear, and new transcription actions
- Export to TXT or SRT
- Recent files list and batch queue

## Configuration
- Default settings live in appsettings.json.
- User settings are stored under `%AppData%\AudioTranscriptionApp\userSettings.json` and the API key is encrypted for the current user.

## Azure Speech Key
Create an Azure Speech resource in the Azure Portal, then use the **Key** and **Region** values in the Settings window.

## OpenAI Whisper
Set the OpenAI API key in Settings. The default model is `whisper-1`. If you use another model, enter it in the OpenAI Model field.

## Notes
- Large audio files may take longer to process depending on network and API latency.
- SRT export requires recognized segments; if a file has no segments, the SRT output will be empty.
