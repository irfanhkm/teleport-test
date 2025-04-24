#!/bin/bash

# Download the complete Gradle distribution
curl -L -o gradle-8.5-bin.zip https://services.gradle.org/distributions/gradle-8.5-bin.zip

# Create a temporary directory
mkdir -p temp-gradle
unzip -q gradle-8.5-bin.zip -d temp-gradle

# Copy the wrapper JAR
cp temp-gradle/gradle-8.5/lib/gradle-wrapper.jar gradle/wrapper/

# Clean up
rm -rf temp-gradle gradle-8.5-bin.zip

echo "Gradle wrapper JAR has been fixed." 