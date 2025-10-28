package com.nba.framework.hooks;

import com.nba.framework.config.ConfigManager;
import com.nba.framework.driver.DriverManager;
import com.nba.framework.utils.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CucumberHooks {
    
    private static final Logger logger = LogManager.getLogger(CucumberHooks.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    @Before
    public void setUp(Scenario scenario) {
        logger.info("Starting scenario: " + scenario.getName());
        
        // Initialize driver based on configuration
        if (config.isRemoteExecution()) {
            DriverManager.initializeRemoteDriver(
                config.getBrowser(), 
                config.getRemoteHubUrl()
            );
        } else {
            DriverManager.initializeDriver(config.getBrowser());
        }
        
        logger.info("Driver initialized for scenario: " + scenario.getName());
    }
    
    @After
    public void tearDown(Scenario scenario) {
        logger.info("Finishing scenario: " + scenario.getName() + " - Status: " + scenario.getStatus());
        
        // Capture screenshot if scenario failed
        if (scenario.isFailed()) {
            String screenshotPath = ScreenshotUtils.captureScreenshot(scenario.getName());
            if (screenshotPath != null) {
                // Attach screenshot to Cucumber report
                byte[] screenshot = ScreenshotUtils.captureScreenshotAsBytes();
                if (screenshot != null) {
                    scenario.attach(screenshot, "image/png", "Screenshot");
                    logger.info("Screenshot attached to failed scenario: " + scenario.getName());
                }
            }
        }
        
        // Quit driver
        DriverManager.quitDriver();
        logger.info("Driver quit for scenario: " + scenario.getName());
    }
}
