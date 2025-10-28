package com.nba.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    
    private static final Logger logger = LogManager.getLogger(FileUtils.class);
    
    public static void createDirectory(String directoryPath) {
        try {
            Path path = Paths.get(directoryPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                logger.info("Created directory: " + directoryPath);
            }
        } catch (IOException e) {
            logger.error("Failed to create directory: " + directoryPath, e);
            throw new RuntimeException("Directory creation failed", e);
        }
    }
    
    public static void writeToFile(String filePath, String content) {
        try {
            createDirectory(getDirectoryPath(filePath));
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(content);
                logger.info("Content written to file: " + filePath);
            }
        } catch (IOException e) {
            logger.error("Failed to write to file: " + filePath, e);
            throw new RuntimeException("File write failed", e);
        }
    }
    
    public static void appendToFile(String filePath, String content) {
        try {
            createDirectory(getDirectoryPath(filePath));
            try (FileWriter writer = new FileWriter(filePath, true)) {
                writer.write(content + System.lineSeparator());
                logger.info("Content appended to file: " + filePath);
            }
        } catch (IOException e) {
            logger.error("Failed to append to file: " + filePath, e);
            throw new RuntimeException("File append failed", e);
        }
    }
    
    public static List<String> readFileLines(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            logger.info("Read " + lines.size() + " lines from file: " + filePath);
        } catch (IOException e) {
            logger.error("Failed to read file: " + filePath, e);
            throw new RuntimeException("File read failed", e);
        }
        return lines;
    }
    
    public static String readFileContent(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
            logger.info("Read content from file: " + filePath);
        } catch (IOException e) {
            logger.error("Failed to read file: " + filePath, e);
            throw new RuntimeException("File read failed", e);
        }
        return content.toString();
    }
    
    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
    
    public static void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
            logger.info("File deleted: " + filePath);
        } catch (IOException e) {
            logger.error("Failed to delete file: " + filePath, e);
            throw new RuntimeException("File deletion failed", e);
        }
    }
    
    public static String getTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
    }
    
    public static String getDirectoryPath(String filePath) {
        return Paths.get(filePath).getParent().toString();
    }
    
    public static String getFileName(String filePath) {
        return Paths.get(filePath).getFileName().toString();
    }
    
    public static String getFileExtension(String filePath) {
        String fileName = getFileName(filePath);
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex > 0 ? fileName.substring(lastDotIndex + 1) : "";
    }
    
    public static void writeCSVFile(String filePath, List<String[]> data) {
        try {
            createDirectory(getDirectoryPath(filePath));
            try (FileWriter writer = new FileWriter(filePath)) {
                for (String[] row : data) {
                    writer.write(String.join(",", row) + System.lineSeparator());
                }
                logger.info("CSV file written: " + filePath);
            }
        } catch (IOException e) {
            logger.error("Failed to write CSV file: " + filePath, e);
            throw new RuntimeException("CSV file write failed", e);
        }
    }
    
    public static List<String[]> readCSVFile(String filePath) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line.split(","));
            }
            logger.info("CSV file read: " + filePath);
        } catch (IOException e) {
            logger.error("Failed to read CSV file: " + filePath, e);
            throw new RuntimeException("CSV file read failed", e);
        }
        return data;
    }
}
