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
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Component
public class DriverUtils {

    private static final Logger logger = LogManager.getLogger(DriverUtils.class);
    
    // Common driver executable names
    private static final List<String> CHROME_DRIVER_NAMES = Arrays.asList(
        "chromedriver", "chromedriver.exe", "chrome-driver", "chrome-driver.exe"
    );
    
    private static final List<String> FIREFOX_DRIVER_NAMES = Arrays.asList(
        "geckodriver", "geckodriver.exe", "firefox-driver", "firefox-driver.exe"
    );
    
    private static final List<String> EDGE_DRIVER_NAMES = Arrays.asList(
        "msedgedriver", "msedgedriver.exe", "edge-driver", "edge-driver.exe"
    );
    
           // Common driver paths to check (project drivers first)
           private static final List<String> COMMON_DRIVER_PATHS = Arrays.asList(
               "./drivers/chromedriver-mac-arm64/",                  // ChromeDriver 141 (PRIORITY)
               "../drivers/chromedriver-mac-arm64/",                 // ChromeDriver 141 (from core-product-tests)
               "./drivers/",                                          // Project drivers directory
               "../drivers/",                                         // Project drivers directory (from core-product-tests)
               "./test-drivers/",                                     // Alternative project drivers
               "../chrome-mac-arm64/",                                // Chrome for Testing directory (from core-product-tests)
               "./chrome-mac-arm64/",                                 // Chrome for Testing directory (from root)
               "/usr/local/bin/",
               "/usr/bin/",
               "/opt/homebrew/bin/",
               System.getProperty("user.home") + "/.local/bin/",
               System.getProperty("user.home") + "/bin/",
               System.getProperty("java.io.tmpdir") + "/drivers/"
           );

    /**
     * Creates ChromeDriver with OFFLINE ONLY strategy
     */
    public static WebDriver createChromeDriver() {
        System.out.println("=== DriverUtils.createChromeDriver() START ===");
        System.out.println("=== DriverUtils.createChromeDriver() called ===");
        logger.info("Attempting to create ChromeDriver with OFFLINE ONLY strategy");

        // Strategy 1: Try project drivers directory (PRIORITY)
        logger.info("Strategy 1: Searching for ChromeDriver in common locations...");
        logger.info("Current working directory: " + System.getProperty("user.dir"));
        String driverPath = findDriverInCommonLocations(CHROME_DRIVER_NAMES);
        logger.info("Driver search result: " + (driverPath != null ? driverPath : "No driver found"));
        
        if (driverPath != null) {
            try {
                logger.info("Strategy 1: Attempting driver from project location: " + driverPath);
                System.setProperty("webdriver.chrome.driver", driverPath);
                ChromeDriver driver = new ChromeDriver(getChromeOptions());
                
                // Apply stealth techniques to hide automation
                StealthUtils.applyStealthTechniques(driver);
                
                logger.info("✅ ChromeDriver created successfully from: " + driverPath);
                return driver;
            } catch (Exception e) {
                logger.warn("❌ Project driver failed: " + e.getMessage());
                // If it's a wrapper script or mock driver, provide helpful message
                if (e.getMessage().contains("ChromeDriver") && e.getMessage().contains("wrapper")) {
                    logger.info("ℹ️  Detected ChromeDriver wrapper script. For real testing, install actual ChromeDriver binary.");
                }
            }
        } else {
            logger.warn("❌ No ChromeDriver found in project locations");
        }
        
        // Strategy 2: Try system PATH driver
            try {
                logger.info("Strategy 2: Attempting system PATH driver");
                ChromeDriver driver = new ChromeDriver(getChromeOptions());
                
                // Apply stealth techniques to hide automation
                StealthUtils.applyStealthTechniques(driver);
                
                logger.info("✅ ChromeDriver created successfully using system PATH");
                return driver;
        } catch (Exception e) {
            logger.warn("❌ System PATH driver failed: " + e.getMessage());
        }
        
        // Strategy 3: Try cached driver (offline cache only)
        String cachedDriverPath = getCachedDriverPath("chrome");
        if (cachedDriverPath != null) {
            try {
                logger.info("Strategy 3: Attempting cached driver: " + cachedDriverPath);
                System.setProperty("webdriver.chrome.driver", cachedDriverPath);
                ChromeDriver driver = new ChromeDriver(getChromeOptions());
                logger.info("✅ ChromeDriver created successfully from cache: " + cachedDriverPath);
                return driver;
            } catch (Exception e) {
                logger.warn("❌ Cached driver failed: " + e.getMessage());
            }
        }
        
        // All offline strategies failed
        logger.error("❌ All OFFLINE ChromeDriver creation strategies failed");
        throw new RuntimeException("Unable to create ChromeDriver. Please ensure ChromeDriver is installed in ./drivers/ directory or system PATH.");
    }

    /**
     * Creates FirefoxDriver with OFFLINE ONLY strategy
     */
    public static WebDriver createFirefoxDriver() {
        logger.info("Attempting to create FirefoxDriver with OFFLINE ONLY strategy");
        
        // Strategy 1: Try project drivers directory (PRIORITY)
        String driverPath = findDriverInCommonLocations(FIREFOX_DRIVER_NAMES);
        if (driverPath != null) {
            try {
                logger.info("Strategy 1: Attempting driver from project location: " + driverPath);
                System.setProperty("webdriver.gecko.driver", driverPath);
                FirefoxDriver driver = new FirefoxDriver(getFirefoxOptions());
                logger.info("✅ FirefoxDriver created successfully from: " + driverPath);
                return driver;
            } catch (Exception e) {
                logger.warn("❌ Project driver failed: " + e.getMessage());
            }
        }
        
        // Strategy 2: Try system PATH driver
        try {
            logger.info("Strategy 2: Attempting system PATH driver");
            FirefoxDriver driver = new FirefoxDriver(getFirefoxOptions());
            logger.info("✅ FirefoxDriver created successfully using system PATH");
            return driver;
        } catch (Exception e) {
            logger.warn("❌ System PATH driver failed: " + e.getMessage());
        }
        
        // Strategy 3: Try cached driver (offline cache only)
        String cachedDriverPath = getCachedDriverPath("firefox");
        if (cachedDriverPath != null) {
            try {
                logger.info("Strategy 3: Attempting cached driver: " + cachedDriverPath);
                System.setProperty("webdriver.gecko.driver", cachedDriverPath);
                FirefoxDriver driver = new FirefoxDriver(getFirefoxOptions());
                logger.info("✅ FirefoxDriver created successfully from cache: " + cachedDriverPath);
                return driver;
            } catch (Exception e) {
                logger.warn("❌ Cached driver failed: " + e.getMessage());
            }
        }
        
        // All offline strategies failed
        logger.error("❌ All OFFLINE FirefoxDriver creation strategies failed");
        throw new RuntimeException("Unable to create FirefoxDriver. Please ensure GeckoDriver is installed in ./drivers/ directory or system PATH.");
    }

    /**
     * Creates EdgeDriver with OFFLINE ONLY strategy
     */
    public static WebDriver createEdgeDriver() {
        logger.info("Attempting to create EdgeDriver with OFFLINE ONLY strategy");
        
        // Strategy 1: Try project drivers directory (PRIORITY)
        String driverPath = findDriverInCommonLocations(EDGE_DRIVER_NAMES);
        if (driverPath != null) {
            try {
                logger.info("Strategy 1: Attempting driver from project location: " + driverPath);
                System.setProperty("webdriver.edge.driver", driverPath);
                EdgeDriver driver = new EdgeDriver(getEdgeOptions());
                logger.info("✅ EdgeDriver created successfully from: " + driverPath);
                return driver;
            } catch (Exception e) {
                logger.warn("❌ Project driver failed: " + e.getMessage());
            }
        }
        
        // Strategy 2: Try system PATH driver
        try {
            logger.info("Strategy 2: Attempting system PATH driver");
            EdgeDriver driver = new EdgeDriver(getEdgeOptions());
            logger.info("✅ EdgeDriver created successfully using system PATH");
            return driver;
        } catch (Exception e) {
            logger.warn("❌ System PATH driver failed: " + e.getMessage());
        }
        
        // Strategy 3: Try cached driver (offline cache only)
        String cachedDriverPath = getCachedDriverPath("edge");
        if (cachedDriverPath != null) {
            try {
                logger.info("Strategy 3: Attempting cached driver: " + cachedDriverPath);
                System.setProperty("webdriver.edge.driver", cachedDriverPath);
                EdgeDriver driver = new EdgeDriver(getEdgeOptions());
                logger.info("✅ EdgeDriver created successfully from cache: " + cachedDriverPath);
                return driver;
            } catch (Exception e) {
                logger.warn("❌ Cached driver failed: " + e.getMessage());
            }
        }
        
        // All offline strategies failed
        logger.error("❌ All OFFLINE EdgeDriver creation strategies failed");
        throw new RuntimeException("Unable to create EdgeDriver. Please ensure EdgeDriver is installed in ./drivers/ directory or system PATH.");
    }

    /**
     * Finds driver executable in common locations
     */
    private static String findDriverInCommonLocations(List<String> driverNames) {
        logger.info("Searching for drivers in " + COMMON_DRIVER_PATHS.size() + " common locations");
        logger.info("Driver names to search: " + driverNames);
        for (String path : COMMON_DRIVER_PATHS) {
            logger.info("Checking path: " + path);
            for (String driverName : driverNames) {
                String fullPath = path + driverName;
                logger.info("  Checking: " + fullPath);
                if (Files.exists(Paths.get(fullPath))) {
                    logger.info("Found driver: " + fullPath);
                    return fullPath;
                }
            }
        }
        logger.warn("No driver found in any common location");
        return null;
    }

    /**
     * Gets cached driver path if available (offline cache only)
     */
    private static String getCachedDriverPath(String browserType) {
        String cacheDir = System.getProperty("user.home") + "/.cache/selenium-drivers/" + browserType;
        Path cachePath = Paths.get(cacheDir);
        
        if (Files.exists(cachePath)) {
            try {
                return Files.list(cachePath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().contains("driver"))
                    .map(Path::toString)
                    .findFirst()
                    .orElse(null);
            } catch (Exception e) {
                logger.warn("Error reading cache directory: " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * Gets Chrome options with aggressive stealth settings to bypass robot verification
     */
    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        
        // Basic options
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-web-security");
        options.addArguments("--user-data-dir=/tmp/chrome-test-profile-" + System.currentTimeMillis());
        
        // Aggressive stealth options to bypass robot verification
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--exclude-switches=enable-automation");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");
        options.addArguments("--disable-images");
        options.addArguments("--disable-background-timer-throttling");
        options.addArguments("--disable-backgrounding-occluded-windows");
        options.addArguments("--disable-renderer-backgrounding");
        options.addArguments("--disable-features=TranslateUI,BlinkGenPropertyTrees");
        options.addArguments("--disable-ipc-flooding-protection");
        options.addArguments("--disable-extensions-except");
        options.addArguments("--disable-plugins-discovery");
        options.addArguments("--disable-default-apps");
        options.addArguments("--disable-sync");
        options.addArguments("--disable-translate");
        options.addArguments("--hide-scrollbars");
        options.addArguments("--mute-audio");
        options.addArguments("--no-first-run");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-logging");
        options.addArguments("--disable-permissions-api");
        options.addArguments("--disable-presentation-api");
        options.addArguments("--disable-print-preview");
        options.addArguments("--disable-speech-api");
        options.addArguments("--disable-file-system");
        options.addArguments("--disable-client-side-phishing-detection");
        options.addArguments("--disable-component-extensions-with-background-pages");
        options.addArguments("--disable-background-networking");
        
        // Additional stealth arguments
        options.addArguments("--disable-features=VizDisplayCompositor");
        options.addArguments("--disable-features=site-per-process");
        options.addArguments("--disable-features=TranslateUI");
        options.addArguments("--disable-features=BlinkGenPropertyTrees");
        options.addArguments("--disable-features=AudioServiceOutOfProcess");
        options.addArguments("--disable-features=MediaRouter");
        options.addArguments("--disable-features=OptimizationHints");
        options.addArguments("--disable-features=CalculateNativeWinOcclusion");
        options.addArguments("--disable-features=VaapiVideoDecoder");
        options.addArguments("--disable-features=VaapiVideoEncoder");
        options.addArguments("--disable-features=WebRtcHideLocalIpsWithMdns");
        options.addArguments("--disable-features=WebRtcUseMinMaxVEADimensions");
        options.addArguments("--disable-features=WebRtcUseEchoCanceller3");
        
        // Set realistic user agent
        options.addArguments("--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/141.0.0.0 Safari/537.36");
        
        // Experimental options to hide automation
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation", "enable-logging"});
        options.setExperimentalOption("useAutomationExtension", false);
        
        // Additional prefs to hide automation
        java.util.Map<String, Object> prefs = new java.util.HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("password_manager_enabled", false);
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("profile.managed_default_content_settings.images", 2);
        options.setExperimentalOption("prefs", prefs);
        
        return options;
    }

    /**
     * Gets Firefox options with common settings
     */
    private static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--start-maximized");
        return options;
    }

    /**
     * Gets Edge options with common settings
     */
    private static EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");
        return options;
    }

    /**
     * Checks if a driver is available in system PATH
     */
    public static boolean isDriverInPath(String driverName) {
        try {
            Process process = Runtime.getRuntime().exec("which " + driverName);
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets driver version information
     */
    public static String getDriverVersion(String driverPath) {
        try {
            Process process = Runtime.getRuntime().exec(driverPath + " --version");
            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(process.getInputStream())
            );
            String version = reader.readLine();
            reader.close();
            return version;
        } catch (Exception e) {
            return "Unknown";
        }
    }
}
