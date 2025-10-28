package com.nba.framework.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class DriverManager {
    
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    
    private static final String CHROME = "chrome";
    private static final String FIREFOX = "firefox";
    private static final String EDGE = "edge";
    
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }
    
    public static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }
    
    public static void removeDriver() {
        driverThreadLocal.remove();
    }
    
    public static WebDriver initializeDriver(String browserName) {
        WebDriver driver = null;
        
        try {
            switch (browserName.toLowerCase()) {
                case CHROME:
                    driver = createChromeDriver();
                    break;
                case FIREFOX:
                    driver = createFirefoxDriver();
                    break;
                case EDGE:
                    driver = createEdgeDriver();
                    break;
                default:
                    logger.error("Unsupported browser: " + browserName);
                    throw new IllegalArgumentException("Unsupported browser: " + browserName);
            }
            
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            driver.manage().window().maximize();
            
            setDriver(driver);
            logger.info("Driver initialized successfully for browser: " + browserName);
            
        } catch (Exception e) {
            logger.error("Failed to initialize driver for browser: " + browserName, e);
            throw new RuntimeException("Driver initialization failed", e);
        }
        
        return driver;
    }
    
    public static WebDriver initializeRemoteDriver(String browserName, String hubUrl) {
        WebDriver driver = null;
        
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            
            switch (browserName.toLowerCase()) {
                case CHROME:
                    capabilities.setBrowserName("chrome");
                    break;
                case FIREFOX:
                    capabilities.setBrowserName("firefox");
                    break;
                case EDGE:
                    capabilities.setBrowserName("MicrosoftEdge");
                    break;
                default:
                    logger.error("Unsupported browser: " + browserName);
                    throw new IllegalArgumentException("Unsupported browser: " + browserName);
            }
            
            driver = new RemoteWebDriver(new URL(hubUrl), capabilities);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            driver.manage().window().maximize();
            
            setDriver(driver);
            logger.info("Remote driver initialized successfully for browser: " + browserName);
            
        } catch (MalformedURLException e) {
            logger.error("Invalid hub URL: " + hubUrl, e);
            throw new RuntimeException("Invalid hub URL", e);
        } catch (Exception e) {
            logger.error("Failed to initialize remote driver for browser: " + browserName, e);
            throw new RuntimeException("Remote driver initialization failed", e);
        }
        
        return driver;
    }
    
    private static WebDriver createChromeDriver() {
        return DriverUtils.createChromeDriver();
    }
    
    private static WebDriver createFirefoxDriver() {
        return DriverUtils.createFirefoxDriver();
    }
    
    private static WebDriver createEdgeDriver() {
        return DriverUtils.createEdgeDriver();
    }
    
    public static void quitDriver() {
        WebDriver driver = getDriver();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("Driver quit successfully");
            } catch (Exception e) {
                logger.error("Error while quitting driver", e);
            } finally {
                removeDriver();
            }
        }
    }
    
    public static void closeDriver() {
        WebDriver driver = getDriver();
        if (driver != null) {
            try {
                driver.close();
                logger.info("Driver closed successfully");
            } catch (Exception e) {
                logger.error("Error while closing driver", e);
            }
        }
    }
}
