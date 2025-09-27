#!/bin/bash

# Create a simple Java program to generate the wrapper
cat > GenerateWrapper.java << 'EOF'
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class GenerateWrapper {
    public static void main(String[] args) throws Exception {
        // Download gradle-wrapper.jar from Maven Central
        String wrapperUrl = "https://repo.maven.apache.org/maven2/gradle/wrapper/gradle-wrapper/8.2/gradle-wrapper-8.2.jar";
        String outputPath = "gradle/wrapper/gradle-wrapper.jar";
        
        System.out.println("Downloading gradle-wrapper.jar...");
        
        try (InputStream in = new URL(wrapperUrl).openStream()) {
            Files.createDirectories(Paths.get("gradle/wrapper"));
            Files.copy(in, Paths.get(outputPath), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("✅ gradle-wrapper.jar downloaded successfully");
        } catch (Exception e) {
            System.out.println("❌ Error downloading: " + e.getMessage());
        }
    }
}
EOF

# Compile and run the Java program
javac GenerateWrapper.java
java GenerateWrapper

# Clean up
rm GenerateWrapper.java GenerateWrapper.class

echo "✅ Gradle wrapper setup complete!"
