#!/bin/sh

COMMIT_MESSAGE="test ci reports"

is_project_root="$(stat ./gradlew 2>/dev/null)"
while [ ! "$is_project_root" ]; do
    cd ..
    is_project_root="$(stat ./gradlew 2>/dev/null)"
done

git add -A; git commit -m "$COMMIT_MESSAGE"; git push --no-verify; git status
