# Build Instructions for Stock Management Android App

## Prerequisites

### Required Software

1. **Android Studio**
   - Version: Hedgehog (2023.1.1) or later
   - Download: https://developer.android.com/studio
   - Installation: Follow the official installation guide for your OS

2. **Java Development Kit (JDK)**
   - Version: JDK 17 (required)
   - Included with Android Studio or
   - Download: https://adoptium.net/temurin/releases/

3. **Android SDK**
   - Minimum API Level: 24 (Android 7.0)
   - Target API Level: 34 (Android 14)
   - Build Tools: 34.0.0
   - Install via Android Studio SDK Manager

4. **Gradle**
   - Version: 8.2
   - Included with the project (Gradle Wrapper)

### System Requirements

**Minimum**:
- RAM: 8 GB
- Disk Space: 10 GB available
- Screen Resolution: 1280 x 800

**Recommended**:
- RAM: 16 GB or more
- Disk Space: 20 GB SSD
- Screen Resolution: 1920 x 1080 or higher

---

## Project Setup

### 1. Clone the Repository

```bash
git clone https://github.com/zeinabMokdad/Transcription.git
cd Transcription/StockManagement
```

### 2. Open Project in Android Studio

1. Launch Android Studio
2. Click "Open an Existing Project"
3. Navigate to `StockManagement` directory
4. Click "OK"

### 3. Gradle Sync

Android Studio will automatically:
- Download Gradle wrapper
- Sync project dependencies
- Configure build settings

If sync doesn't start automatically:
1. Click "File" > "Sync Project with Gradle Files"
2. Wait for sync to complete

### 4. Install Missing Components

If Android Studio prompts for missing components:
- Click "Install" buttons for any missing SDK versions
- Install Build Tools if needed
- Update Android Gradle Plugin if prompted

---

## Building the Project

### Build Variants

The project has two build variants:
- **debug**: For development and testing
- **release**: For production deployment

### Debug Build

#### Using Android Studio

1. Select "Build" > "Make Project" (Ctrl+F9 / Cmd+F9)
2. Or click the hammer icon in the toolbar
3. Wait for build to complete
4. Check "Build" window for results

#### Using Command Line

```bash
cd StockManagement
./gradlew assembleDebug
```

**Output Location**:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Release Build

#### Step 1: Create Keystore

Create a keystore for signing (first time only):

```bash
keytool -genkey -v -keystore my-release-key.keystore \
  -alias my-key-alias \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

You'll be prompted for:
- Keystore password (remember this!)
- Key password (remember this!)
- Your name and organization details

**Important**: Keep your keystore file safe and secure!

#### Step 2: Configure Signing (Optional - for automated builds)

Create `keystore.properties` in the project root:

```properties
storeFile=/path/to/my-release-key.keystore
storePassword=your_keystore_password
keyAlias=my-key-alias
keyPassword=your_key_password
```

**Warning**: Do NOT commit this file to version control!

Add to `.gitignore`:
```
keystore.properties
*.keystore
*.jks
```

Update `app/build.gradle.kts` to use the properties file:

```kotlin
// Load keystore properties
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    signingConfigs {
        if (keystorePropertiesFile.exists()) {
            create("release") {
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            }
        }
    }
    
    buildTypes {
        release {
            if (keystorePropertiesFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
            // ... other configurations
        }
    }
}
```

#### Step 3: Build Release APK

##### Using Android Studio (Recommended)

1. Select "Build" > "Generate Signed Bundle / APK"
2. Choose "APK" and click "Next"
3. Select your keystore file or create new
4. Enter keystore password
5. Select key alias
6. Enter key password
7. Choose "release" build variant
8. Click "Finish"

**Output Location**:
```
app/release/app-release.apk
```

##### Using Command Line

With configured signing:
```bash
./gradlew assembleRelease
```

Without configured signing (manual signing required):
```bash
./gradlew assembleRelease
jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 \
  -keystore my-release-key.keystore \
  app/build/outputs/apk/release/app-release-unsigned.apk \
  my-key-alias
zipalign -v 4 app-release-unsigned.apk app-release.apk
```

---

## Building AAB (Android App Bundle)

For Google Play Store distribution:

### Using Android Studio

1. Select "Build" > "Generate Signed Bundle / APK"
2. Choose "Android App Bundle" and click "Next"
3. Configure signing (same as APK)
4. Click "Finish"

**Output Location**:
```
app/release/app-release.aab
```

### Using Command Line

```bash
./gradlew bundleRelease
```

---

## Build Configuration

### Modifying Build Settings

Edit `app/build.gradle.kts`:

#### Change Version

```kotlin
android {
    defaultConfig {
        versionCode = 2  // Increment for each release
        versionName = "1.1"  // User-visible version
    }
}
```

#### Change Application ID

```kotlin
android {
    defaultConfig {
        applicationId = "com.yourcompany.stockmanagement"
    }
}
```

#### ProGuard Configuration

Edit `app/proguard-rules.pro` to add custom rules:

```proguard
# Keep specific classes
-keep class com.your.package.** { *; }

# Keep class members
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
```

---

## Testing Builds

### Install Debug APK

#### Using ADB

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

#### Using Android Studio

1. Connect device or start emulator
2. Click "Run" (green play button)
3. Select device
4. App will be installed and launched

### Install Release APK

```bash
adb install app/build/outputs/apk/release/app-release.apk
```

Or drag-and-drop APK to emulator window.

---

## Continuous Integration / Continuous Deployment

### GitHub Actions Example

Create `.github/workflows/android.yml`:

```yaml
name: Android CI

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: Grant execute permission for gradlew
      run: chmod +x StockManagement/gradlew
      
    - name: Build Debug APK
      run: cd StockManagement && ./gradlew assembleDebug
      
    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: app-debug
        path: StockManagement/app/build/outputs/apk/debug/app-debug.apk
```

---

## Troubleshooting

### Common Build Issues

#### Issue: Gradle sync failed

**Solutions**:
```bash
# Clear Gradle cache
rm -rf ~/.gradle/caches/

# Clean project
./gradlew clean

# Invalidate caches in Android Studio
File > Invalidate Caches / Restart
```

#### Issue: Out of memory

**Solution**: Increase Gradle memory in `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxPermSize=1024m
```

#### Issue: Build tools not found

**Solution**: Install via SDK Manager
```bash
# Via command line
sdkmanager "build-tools;34.0.0"
```

#### Issue: Dependency resolution errors

**Solutions**:
```bash
# Update dependencies
./gradlew --refresh-dependencies

# Clear build directory
./gradlew cleanBuildCache
```

#### Issue: Signing errors

**Solutions**:
- Verify keystore path is correct
- Check passwords are correct
- Ensure keystore has not expired
- Regenerate keystore if needed

### Build Performance

#### Speed up builds:

1. **Enable Gradle Daemon** (add to `gradle.properties`):
```properties
org.gradle.daemon=true
org.gradle.parallel=true
org.gradle.configureondemand=true
```

2. **Use Build Cache**:
```properties
android.enableBuildCache=true
org.gradle.caching=true
```

3. **Increase Memory**:
```properties
org.gradle.jvmargs=-Xmx4096m
```

---

## Build Verification

### Check APK Contents

```bash
# List APK contents
unzip -l app-release.apk

# Verify signing
apksigner verify --verbose app-release.apk

# Check APK size
ls -lh app-release.apk
```

### Analyze APK

In Android Studio:
1. Select "Build" > "Analyze APK"
2. Choose your APK file
3. Review size breakdown
4. Check for optimizations

---

## Distribution

### Internal Testing

1. **Email**: Send APK directly
2. **Cloud Storage**: Upload to Drive/Dropbox
3. **Firebase App Distribution**: For team testing

### Google Play Store

1. Build AAB (Android App Bundle)
2. Create Play Console account
3. Create app listing
4. Upload AAB
5. Configure release
6. Submit for review

### Alternative Stores

- Amazon Appstore
- Samsung Galaxy Store
- Direct download from website

---

## Automation Scripts

### Build All Variants

Create `build-all.sh`:

```bash
#!/bin/bash
echo "Building all variants..."

./gradlew clean
./gradlew assembleDebug
./gradlew assembleRelease
./gradlew bundleRelease

echo "Build complete!"
echo "Debug APK: app/build/outputs/apk/debug/"
echo "Release APK: app/build/outputs/apk/release/"
echo "Release AAB: app/build/outputs/bundle/release/"
```

Make executable:
```bash
chmod +x build-all.sh
./build-all.sh
```

---

## Documentation

For more information:
- [Android Developer Guide](https://developer.android.com/guide)
- [Gradle Build Guide](https://docs.gradle.org/current/userguide/userguide.html)
- [App Signing Guide](https://developer.android.com/studio/publish/app-signing)

---

Last Updated: February 2026
