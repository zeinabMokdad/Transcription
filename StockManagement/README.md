# Stock Management Android Application

A comprehensive, professional stock management system for Android devices with full inventory tracking, product management, and reporting capabilities.

## Features

### Product Management
- Add, edit, and delete products
- Product details: name, SKU, barcode, category, description, supplier
- Low stock threshold configuration
- Stock quantity tracking
- Price management

### Inventory Tracking
- Real-time stock levels
- Stock in/out/adjustment transactions
- Transaction history with timestamps
- Low stock alerts and monitoring

### Categories & Organization
- Create and manage product categories
- Category hierarchy support
- Search and filter functionality

### Supplier Management
- Add and manage suppliers
- Supplier contact information (name, email, phone, address)
- Link products to suppliers

### Reports & Analytics
- Stock summary reports
- Low stock reports
- Transaction history
- Export reports to PDF and CSV

### User Interface
- Modern Material Design 3 UI
- Dark/Light theme support (follows system settings)
- Dashboard with key metrics
- Intuitive bottom navigation
- Responsive design

### Data Management
- Local Room database for data persistence
- Data backup and restore functionality
- Export/Import capabilities

## Technical Specifications

- **Language**: Kotlin
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Architecture**: MVVM (Model-View-ViewModel) pattern
- **Database**: Room Database
- **UI Framework**: Jetpack Compose
- **Dependencies**: 
  - AndroidX Core libraries
  - Material Design Components 3
  - Room Database for data persistence
  - Coroutines for asynchronous operations
  - Navigation Compose for screen navigation
  - ML Kit for barcode scanning
  - iText7 for PDF generation
  - OpenCSV for CSV exports
  - MPAndroidChart for analytics visualizations
  - Coil for image loading

## Project Structure

```
StockManagement/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/stockmanagement/app/
│   │   │   │   ├── data/
│   │   │   │   │   ├── entity/        # Database entities
│   │   │   │   │   ├── dao/           # Data Access Objects
│   │   │   │   │   ├── database/      # Room database setup
│   │   │   │   │   └── repository/    # Repository layer
│   │   │   │   ├── presentation/
│   │   │   │   │   ├── ui/
│   │   │   │   │   │   ├── dashboard/ # Dashboard screen
│   │   │   │   │   │   ├── product/   # Product screens
│   │   │   │   │   │   ├── inventory/ # Inventory screens
│   │   │   │   │   │   ├── category/  # Category screens
│   │   │   │   │   │   ├── supplier/  # Supplier screens
│   │   │   │   │   │   ├── reports/   # Reports screens
│   │   │   │   │   │   ├── settings/  # Settings screen
│   │   │   │   │   │   ├── components/# Reusable UI components
│   │   │   │   │   │   ├── navigation/# Navigation setup
│   │   │   │   │   │   └── theme/     # App theme
│   │   │   │   │   └── viewmodel/     # ViewModels
│   │   │   │   └── MainActivity.kt
│   │   │   ├── res/
│   │   │   │   ├── values/            # String resources, colors, themes
│   │   │   │   ├── xml/               # Backup rules, file paths
│   │   │   │   └── mipmap-*/          # App icons
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## Setup Instructions

### Prerequisites

1. **Android Studio**: Download and install the latest version of Android Studio (Hedgehog or later)
   - Download from: https://developer.android.com/studio

2. **Java Development Kit (JDK)**: JDK 17 or later
   - Included with Android Studio or download from: https://adoptium.net/

3. **Android SDK**: API Level 34 (Android 14)
   - Install via Android Studio SDK Manager

### Installation Steps

1. **Clone the repository**:
   ```bash
   git clone https://github.com/zeinabMokdad/Transcription.git
   cd Transcription/StockManagement
   ```

2. **Open in Android Studio**:
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the `StockManagement` folder
   - Click "OK"

3. **Sync Gradle**:
   - Android Studio will automatically detect the Gradle project
   - Click "Sync Now" if prompted
   - Wait for Gradle to download all dependencies

4. **Configure SDK**:
   - Go to File > Project Structure > SDK Location
   - Ensure Android SDK is properly configured
   - Install any missing SDK versions if prompted

5. **Build the project**:
   ```bash
   ./gradlew build
   ```
   Or use Android Studio: Build > Make Project

6. **Run on device/emulator**:
   - Connect an Android device (API 24+) with USB debugging enabled, or
   - Launch an Android emulator from AVD Manager
   - Click the "Run" button (green play icon) in Android Studio
   - Select your device/emulator

## Building APK

### Debug APK

To build a debug APK for testing:

```bash
./gradlew assembleDebug
```

The APK will be located at:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Release APK

To build a release APK:

1. **Create a keystore** (if you don't have one):
   ```bash
   keytool -genkey -v -keystore my-release-key.keystore -alias my-key-alias -keyalg RSA -keysize 2048 -validity 10000
   ```

2. **Configure signing** in `app/build.gradle.kts`:
   ```kotlin
   android {
       signingConfigs {
           create("release") {
               storeFile = file("path/to/my-release-key.keystore")
               storePassword = "your-store-password"
               keyAlias = "my-key-alias"
               keyPassword = "your-key-password"
           }
       }
       buildTypes {
           release {
               signingConfig = signingConfigs.getByName("release")
               // ... other configurations
           }
       }
   }
   ```

3. **Build the release APK**:
   ```bash
   ./gradlew assembleRelease
   ```

   The signed APK will be located at:
   ```
   app/build/outputs/apk/release/app-release.apk
   ```

### Building from Android Studio

1. Go to Build > Generate Signed Bundle / APK
2. Select "APK" and click "Next"
3. Select or create a keystore
4. Enter keystore password and key alias
5. Select "release" build variant
6. Click "Finish"

The signed APK will be generated in the `app/release/` folder.

## Usage Guide

### First Launch

1. Open the app on your Android device
2. You'll see the Dashboard with empty metrics
3. Start by adding categories, suppliers, and products

### Adding Products

1. Navigate to Products tab
2. Tap the '+' floating action button
3. Fill in product details:
   - Name (required)
   - SKU (required)
   - Barcode (optional - use scan icon)
   - Price (required)
   - Quantity (required)
   - Low stock threshold
   - Category and supplier (select from existing)
4. Tap "Add Product"

### Managing Inventory

1. Navigate to Inventory tab to see low stock items
2. Tap on a product to adjust stock
3. Select transaction type:
   - Stock In: Add inventory
   - Stock Out: Remove inventory
   - Adjustment: Set specific quantity
4. Enter quantity and optional note
5. Tap "Confirm"

### Categories & Suppliers

1. Navigate to respective tabs
2. Use '+' button to add new entries
3. Tap item to view details
4. Use delete icon to remove items

### Reports

1. Navigate to Reports tab
2. Select report type:
   - Stock Summary
   - Low Stock Report
   - Transaction History
   - Category Analysis
   - Supplier Report
3. Choose export format (PDF or CSV)
4. Reports are saved to device storage

### Settings

1. Navigate to Settings tab
2. Configure:
   - Theme preference (Light/Dark/System)
   - Notification settings
   - Low stock alerts
3. Manage data:
   - Backup database
   - Restore from backup
   - Export all data

## Database Schema

See [DATABASE_SCHEMA.md](./docs/DATABASE_SCHEMA.md) for detailed database structure.

## Permissions

The app requires the following permissions:

- **CAMERA**: For barcode scanning
- **WRITE_EXTERNAL_STORAGE**: For exporting reports (Android 9 and below)
- **READ_EXTERNAL_STORAGE**: For importing data (Android 12 and below)
- **READ_MEDIA_IMAGES**: For accessing images (Android 13+)

Permissions are requested at runtime when needed.

## Troubleshooting

### Build Issues

**Problem**: Gradle sync fails
- Solution: Check internet connection, ensure you have the latest Gradle version
- Try: File > Invalidate Caches / Restart

**Problem**: Missing SDK
- Solution: Open SDK Manager (Tools > SDK Manager) and install required SDK versions

**Problem**: Dependency resolution errors
- Solution: Clear Gradle cache: `./gradlew clean`

### Runtime Issues

**Problem**: App crashes on startup
- Solution: Check if device meets minimum SDK requirement (API 24)
- Try: Uninstall and reinstall the app

**Problem**: Database errors
- Solution: Clear app data from device settings
- Note: This will delete all local data

## Development

### Code Style

- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add comments for complex logic
- Keep functions small and focused

### Testing

Run tests with:
```bash
./gradlew test
./gradlew connectedAndroidTest
```

### Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues, questions, or suggestions:
- Open an issue on GitHub
- Contact: [Repository Owner]

## Acknowledgments

- Material Design 3 for UI components
- Android Jetpack libraries
- Room Database for data persistence
- ML Kit for barcode scanning capabilities
