#!/bin/zsh

FEATURE_NAME=sample

cd ..
./gradlew createFeatureModule \
    --featureName="$FEATURE_NAME" \
    --packageName="eu.caraus.kmp.$FEATURE_NAME" \
    --layer="all" # domain, data, ui, test

./gradlew test
