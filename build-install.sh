#!/bin/bash
set -e

echo "🔨 Building APK..."
./gradlew assembleDebug

echo "📱 Installing on device..."
adb install -r app/build/outputs/apk/debug/app-debug.apk

echo "🚀 Starting app..."
adb shell am start -n it.faustobe.santibailor/.MainActivity

echo "✅ Done!"
