package com.nba.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static Properties properties;
    private static ConfigManager instance;
    
    private ConfigManager() {
        loadProperties();
    }
    
    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
    
    private void loadProperties() {
        properties = new Properties();
        try {
            // Load from config.properties file
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
            if (inputStream != null) {
                properties.load(inputStream);
                logger.info("Configuration loaded from config.properties");
            } else {
                logger.warn("config.properties not found, using default values");
                setDefaultProperties();
            }
        } catch (IOException e) {
            logger.error("Error loading configuration properties", e);
            setDefaultProperties();
        }
    }
    
    private void setDefaultProperties() {
        // Default configuration values
        properties.setProperty("browser", "chrome");
        properties.setProperty("browser.version", "latest");
        properties.setProperty("webdriver.auto.download", "true");
        properties.setProperty("webdriver.offline.mode", "false");
        properties.setProperty("webdriver.chrome.path", "");
        properties.setProperty("webdriver.firefox.path", "");
        properties.setProperty("webdriver.edge.path", "");
        properties.setProperty("webdriver.cache.enabled", "true");
        properties.setProperty("webdriver.cache.path", System.getProperty("user.home") + "/.cache/selenium-drivers/");
        properties.setProperty("implicit.wait", "10");
        properties.setProperty("explicit.wait", "10");
        properties.setProperty("page.load.timeout", "30");
        properties.setProperty("remote.execution", "false");
        properties.setProperty("remote.hub.url", "http://localhost:4444/wd/hub");
        properties.setProperty("screenshot.path", "./test-output/screenshots/");
        properties.setProperty("video.recording", "false");
        properties.setProperty("video.path", "./test-output/videos/");
        properties.setProperty("report.path", "./test-output/reports/");
        
        // NBA URLs
        properties.setProperty("nba.warriors.url", "https://www.nba.com/warriors");
        properties.setProperty("nba.sixers.url", "https://www.nba.com/sixers/");
        properties.setProperty("nba.bulls.url", "https://www.nba.com/bulls/");
        
        logger.info("Default configuration properties set");
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }
    
    public int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }
    
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(properties.getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
    
    // Convenience methods for common properties
    public String getBrowser() {
        return getProperty("browser", "chrome");
    }
    
    public String getBrowserVersion() {
        return getProperty("browser.version", "latest");
    }
    
    public int getImplicitWait() {
        return getIntProperty("implicit.wait", 10);
    }
    
    public int getExplicitWait() {
        return getIntProperty("explicit.wait", 10);
    }
    
    public int getPageLoadTimeout() {
        return getIntProperty("page.load.timeout", 30);
    }
    
    public boolean isRemoteExecution() {
        return getBooleanProperty("remote.execution", false);
    }
    
    public String getRemoteHubUrl() {
        return getProperty("remote.hub.url", "http://localhost:4444/wd/hub");
    }
    
    public String getScreenshotPath() {
        return getProperty("screenshot.path", "./test-output/screenshots/");
    }
    
    public boolean isVideoRecording() {
        return getBooleanProperty("video.recording", false);
    }
    
    public String getVideoPath() {
        return getProperty("video.path", "./test-output/videos/");
    }
    
    public String getReportPath() {
        return getProperty("report.path", "./test-output/reports/");
    }
    
    public String getWarriorsUrl() {
        return getProperty("nba.warriors.url", "https://www.nba.com/warriors");
    }
    
    public String getSixersUrl() {
        return getProperty("nba.sixers.url", "https://www.nba.com/sixers/");
    }
    
    public String getBullsUrl() {
        return getProperty("nba.bulls.url", "https://www.nba.com/bulls/");
    }

    // Driver Management Configuration Methods
    public boolean isWebDriverAutoDownload() {
        return getBooleanProperty("webdriver.auto.download", true);
    }

    public boolean isWebDriverOfflineMode() {
        return getBooleanProperty("webdriver.offline.mode", false);
    }

    public String getChromeDriverPath() {
        return getProperty("webdriver.chrome.path", "");
    }

    public String getFirefoxDriverPath() {
        return getProperty("webdriver.firefox.path", "");
    }

    public String getEdgeDriverPath() {
        return getProperty("webdriver.edge.path", "");
    }

    public boolean isWebDriverCacheEnabled() {
        return getBooleanProperty("webdriver.cache.enabled", true);
    }

    public String getWebDriverCachePath() {
        return getProperty("webdriver.cache.path", System.getProperty("user.home") + "/.cache/selenium-drivers/");
    }
}
