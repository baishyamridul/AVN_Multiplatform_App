# iOS MapLibre Setup

The MapLibre native iOS SDK must be linked into the iOS app at the Xcode level.

## Using CocoaPods (Recommended)

1. Install CocoaPods if not already installed:
   ```bash
   sudo gem install cocoapods
   ```

2. Install the pods:
   ```bash
   cd iosApp
   pod install
   ```

3. Open the generated workspace:
   ```bash
   open iosApp.xcworkspace
   ```

4. Build and run from Xcode.

## Ensuring MapLibre Builds Correctly

MapLibre is packaged as an XCFramework by CocoaPods. During an iOS build, Xcode must run the Pods target before the app target so it can generate:

```text
Build/Products/Debug-iphonesimulator/XCFrameworkIntermediates/MapLibre
```

If this folder is missing, the app can fail with an error similar to:

```text
Search path '.../XCFrameworkIntermediates/MapLibre' not found
```

Use the checklist below to avoid that state.

### Always build the workspace

Open and build:

```text
iosApp/iosApp.xcworkspace
```

Do not build only:

```text
iosApp/iosApp.xcodeproj
```

The workspace includes both the app project and the CocoaPods project. Building only the project can skip the Pods build graph that creates the MapLibre intermediate framework.

### Reinstall pods after Podfile changes

Run this whenever `iosApp/Podfile` changes:

```bash
cd /Users/mridul/AndroidStudioProjects/AVN_Multiplatform_App/iosApp
pod install
```

### Verify the workspace contains Pods

The workspace file should reference both projects:

```bash
cat /Users/mridul/AndroidStudioProjects/AVN_Multiplatform_App/iosApp/iosApp.xcworkspace/contents.xcworkspacedata
```

Expected entries:

```xml
<FileRef location="group:iosApp.xcodeproj">
<FileRef location="group:Pods/Pods.xcodeproj">
```

### Rebuild Android Studio DerivedData

If Android Studio keeps reporting a stale missing search path, rebuild the same DerivedData location Android Studio is using:

```bash
xcodebuild -workspace /Users/mridul/AndroidStudioProjects/AVN_Multiplatform_App/iosApp/iosApp.xcworkspace \
  -scheme iosApp \
  -configuration Debug \
  -sdk iphonesimulator \
  -derivedDataPath /Users/mridul/Library/Caches/Google/AndroidStudio2025.3.4/DerivedData/iosApp-csjavivslfvpjydljnpdmsrvqlda \
  clean build
```

After a successful build, this path should exist:

```text
/Users/mridul/Library/Caches/Google/AndroidStudio2025.3.4/DerivedData/iosApp-csjavivslfvpjydljnpdmsrvqlda/Build/Products/Debug-iphonesimulator/XCFrameworkIntermediates/MapLibre
```

## Quick Diagnosis

If the build fails again:

1. Confirm `iosApp.xcworkspace` is being used.
2. Run `pod install` from `iosApp`.
3. Build once with `xcodebuild` using the workspace and Android Studio DerivedData path.
4. Confirm `XCFrameworkIntermediates/MapLibre/MapLibre.framework` exists under the DerivedData build products directory.

## Using Swift Package Manager

Alternatively, add the MapLibre package to the Xcode project:
- URL: `https://github.com/maplibre/maplibre-gl-native-distribution`
- Version: ~> 6.6
