#!/bin/sh

./gradlew android:assembleDebug
cp android/build/outputs/apk/debug/android-debug.apk "$GDRIVE_PATH"/igt.apk
gdrive push
notify-send "Apk sent to "
