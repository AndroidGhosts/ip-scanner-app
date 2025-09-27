#!/bin/bash

# Create gradle wrapper directory
mkdir -p gradle/wrapper

# Download gradle wrapper jar
curl -L https://github.com/gradle/gradle/raw/master/gradle/wrapper/gradle-wrapper.jar -o gradle/wrapper/gradle-wrapper.jar

# Make gradlew executable
chmod +x gradlew

echo "Gradle wrapper setup complete!"
