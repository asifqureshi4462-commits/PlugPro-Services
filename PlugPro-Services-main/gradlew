#!/bin/sh
#
# Standard Gradle execution script for CI / Android environments
#
set -e

APP_HOME="$(cd "$(dirname "$0")" && pwd)"
WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

if [ -f "$WRAPPER_JAR" ]; then
    exec java -Xmx2048m -Dfile.encoding=UTF-8 -jar "$WRAPPER_JAR" "$@"
elif command -v gradle >/dev/null 2>&1; then
    exec gradle "$@"
else
    echo "Gradle not found. Running Gradle wrapper generation on CI environment..."
    exit 1
fi
