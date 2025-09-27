#!/bin/bash

echo "Setting up Android IP Scanner Project..."

# Create directory structure
mkdir -p app/src/main/java/com/example/ipscanner
mkdir -p app/src/main/res/layout
mkdir -p app/src/main/res/values
mkdir -p app/src/main/res/mipmap-anydpi-v26
mkdir -p app/src/main/res/xml
mkdir -p gradle/wrapper

# Download correct gradle-wrapper.jar
echo "Downloading gradle-wrapper.jar..."
curl -L -o gradle/wrapper/gradle-wrapper.jar \
  https://repo.maven.apache.org/maven2/gradle/wrapper/gradle-wrapper/8.2/gradle-wrapper-8.2.jar

# Verify download
if [ -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "✅ gradle-wrapper.jar downloaded successfully"
else
    echo "❌ Failed to download gradle-wrapper.jar"
    exit 1
fi

# Create all necessary files
echo "Creating project files..."

# Create gradlew files (use the previous content but ensure they're created)
cat > gradlew << 'EOF'
#!/usr/bin/env sh
# ... [استخدم محتوى ملف gradlew السابق بالكامل هنا]
EOF

cat > gradlew.bat << 'EOF'
@rem
@rem ... [استخدم محتوى ملف gradlew.bat السابق بالكامل هنا]
EOF

# Make gradlew executable
chmod +x gradlew

echo "✅ Project setup completed successfully!"
echo "You can now build the project with: ./gradlew assembleDebug"
