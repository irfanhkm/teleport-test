#!/bin/bash

# Create the gradle/wrapper directory if it doesn't exist
mkdir -p gradle

# Download the Gradle wrapper JAR
curl -o gradle/gradle-wrapper.jar https://raw.githubusercontent.com/gradle/gradle/v8.5.0/gradle/wrapper/gradle-wrapper.jar

# Make the script executable
chmod +x gradlew
chmod +x gradlew.bat

echo "Gradle wrapper JAR downloaded successfully."

brew install gradle

gradle wrapper 