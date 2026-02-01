using System;
using System.Windows;
using AudioTranscriptionApp.Services;
using AudioTranscriptionApp.ViewModels;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

namespace AudioTranscriptionApp
{
    /// <summary>
    /// Interaction logic for App.xaml
    /// </summary>
    public partial class App : Application
    {
        public IServiceProvider Services { get; private set; } = null!;

        protected override void OnStartup(StartupEventArgs e)
        {
            base.OnStartup(e);

            DispatcherUnhandledException += (_, args) =>
            {
                MessageBox.Show(args.Exception.Message, "Unhandled UI Exception", MessageBoxButton.OK, MessageBoxImage.Error);
                args.Handled = true;
            };

            AppDomain.CurrentDomain.UnhandledException += (_, args) =>
            {
                if (args.ExceptionObject is Exception exception)
                {
                    MessageBox.Show(exception.Message, "Unhandled Exception", MessageBoxButton.OK, MessageBoxImage.Error);
                }
            };

            try
            {
                var serviceCollection = new ServiceCollection();
                ConfigureServices(serviceCollection);
                Services = serviceCollection.BuildServiceProvider();

                var mainWindow = new MainWindow
                {
                    DataContext = Services.GetRequiredService<MainViewModel>()
                };

                MainWindow = mainWindow;
                ShutdownMode = ShutdownMode.OnMainWindowClose;
                mainWindow.Show();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Startup Error", MessageBoxButton.OK, MessageBoxImage.Error);
                Shutdown(-1);
            }
        }

        private static void ConfigureServices(IServiceCollection services)
        {
            services.AddLogging(builder =>
            {
                builder.AddDebug();
                builder.SetMinimumLevel(LogLevel.Information);
            });

            services.AddSingleton<IAudioService, AudioService>();
            services.AddSingleton<ISettingsService>(provider =>
            {
                var logger = provider.GetRequiredService<ILogger<SettingsService>>();
                return new SettingsService(logger);
            });

            services.AddSingleton<ITranscriptionService>(provider =>
            {
                var logger = provider.GetRequiredService<ILogger<TranscriptionService>>();
                return new TranscriptionService(logger);
            });

            services.AddSingleton(provider =>
            {
                var audioService = provider.GetRequiredService<IAudioService>();
                var settingsService = provider.GetRequiredService<ISettingsService>();
                var transcriptionService = provider.GetRequiredService<ITranscriptionService>();
                return new MainViewModel(audioService, transcriptionService, settingsService);
            });

            services.AddTransient(provider =>
            {
                var settingsService = provider.GetRequiredService<ISettingsService>();
                return new SettingsViewModel(settingsService);
            });
        }
    }
}
