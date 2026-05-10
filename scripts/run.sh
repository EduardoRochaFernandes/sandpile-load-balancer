#!/usr/bin/env bash
# ============================================================
# run.sh — Quick launcher for Sandpile Load Balancer
# ============================================================
# Usage:
#   ./scripts/run.sh                            # Interactive mode
#   ./scripts/run.sh -f 2 -a matrix5.csv -o result.txt  # Non-interactive
#
# The script automatically locates the latest release JAR.
# ============================================================

RELEASE_DIR="$(cd "$(dirname "$0")/../releases/final-release_1.0.0" && pwd)"
JAR="$RELEASE_DIR/main.jar"

if [ ! -f "$JAR" ]; then
  echo "[ERROR] JAR not found at: $JAR"
  echo "        Make sure you are running this script from the project root."
  exit 1
fi

# Change to the release directory so relative input/output paths work
cd "$RELEASE_DIR" || exit 1

echo "[INFO] Running: java -jar main.jar $*"
java -jar main.jar "$@"
