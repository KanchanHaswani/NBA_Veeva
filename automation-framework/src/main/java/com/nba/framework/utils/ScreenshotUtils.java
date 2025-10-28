package com.nba.framework.utils;

import com.nba.framework.config.ConfigManager;
import com.nba.framework.driver.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScreenshotUtils {
    
    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    public static String captureScreenshot(String testName) {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) {
            logger.error("Driver is null, cannot capture screenshot");
            return null;
        }
        
        try {
            String screenshotPath = config.getScreenshotPath();
            FileUtils.createDirectory(screenshotPath);
            
            String fileName = testName + "_" + FileUtils.getTimestamp() + ".png";
            String fullPath = screenshotPath + fileName;
            
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            
            Path destinationPath = Paths.get(fullPath);
            Files.copy(sourceFile.toPath(), destinationPath);
            
            logger.info("Screenshot captured: " + fullPath);
            return fullPath;
            
        } catch (IOException e) {
            logger.error("Failed to capture screenshot", e);
            return null;
        }
    }
    
    public static String captureScreenshot() {
        return captureScreenshot("screenshot");
    }
    
    public static byte[] captureScreenshotAsBytes() {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) {
            logger.error("Driver is null, cannot capture screenshot");
            return null;
        }
        
        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            return takesScreenshot.getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.error("Failed to capture screenshot as bytes", e);
            return null;
        }
    }
}
