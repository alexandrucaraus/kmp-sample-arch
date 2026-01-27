#!/bin/sh

echo "Running linters"

cd ..
./gradlew ktlintCheck detekt
LINT_STATUS=$?

if [ $LINT_STATUS -ne 0 ]; then
  echo "Lint checks failed! Push aborted."
  exit 1
fi

echo "Linters OK"
