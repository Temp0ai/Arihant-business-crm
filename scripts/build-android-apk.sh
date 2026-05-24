#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

if [[ -z "${ANDROID_HOME:-}" && -z "${ANDROID_SDK_ROOT:-}" ]]; then
  echo "ERROR: ANDROID_HOME or ANDROID_SDK_ROOT is not set."
  echo "Install Android SDK and export ANDROID_HOME (example: \$HOME/Android/Sdk)."
  exit 1
fi

SDK_DIR="${ANDROID_HOME:-${ANDROID_SDK_ROOT}}"
if [[ ! -d "$SDK_DIR" ]]; then
  echo "ERROR: Android SDK directory not found at: $SDK_DIR"
  exit 1
fi

if ! command -v java >/dev/null 2>&1; then
  echo "ERROR: Java is not installed or not available in PATH."
  exit 1
fi

JAVA_MAJOR="$(java -version 2>&1 | awk -F '[\".]' '/version/ {print $2; exit}')"
if [[ "$JAVA_MAJOR" != "17" ]]; then
  echo "ERROR: Java 17 is required. Current Java major version: $JAVA_MAJOR"
  echo "Set JAVA_HOME to a JDK 17 installation and ensure PATH uses it first."
  exit 1
fi

echo "Using Android SDK: $SDK_DIR"
echo "Using Java version:"
java -version

if [[ -f package-lock.json ]]; then
  npm ci
else
  npm install
fi

npm run build
npx cap sync android

cat > android/local.properties <<EOP
sdk.dir=$SDK_DIR
EOP

(
  cd android
  ./gradlew assembleDebug
)

echo "APK generated at: android/app/build/outputs/apk/debug/app-debug.apk"
