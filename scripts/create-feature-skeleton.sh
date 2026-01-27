#!/bin/sh

#
# Creates a new feature module by FEATURE_NAME
# By default with all the layers (domain, data, ui, test)
#

FEATURE_NAME=sample

cd ..
./gradlew createFeatureModule \
    --featureName="$FEATURE_NAME" \
    --packageName="eu.caraus.kmp.$FEATURE_NAME" \
    --layer="all" # domain, data, ui, test

./gradlew test
