#!/bin/bash
./gradlew server:build && ./gradlew client-android:assembleDebug && ./gradlew desktop:run && echo "[SUCCESS]" || echo "[FAIL !!!]"
