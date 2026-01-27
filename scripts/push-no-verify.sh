#!/bin/sh

COMMIT_MESSAGE="test ci reports"

git add .; git commit -m "$COMMIT_MESSAGE"; git push --no-verify
