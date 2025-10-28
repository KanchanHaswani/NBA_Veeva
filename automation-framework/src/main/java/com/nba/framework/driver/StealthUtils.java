package com.nba.framework.driver;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StealthUtils {
    
    private static final Logger logger = LogManager.getLogger(StealthUtils.class);
    
    /**
     * Applies JavaScript-based stealth techniques to hide automation
     */
    public static void applyStealthTechniques(WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            
            // Hide webdriver property
            js.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
            
            // Override chrome property
            js.executeScript("window.chrome = {runtime: {}}");
            
            // Override permissions
            js.executeScript("Object.defineProperty(navigator, 'permissions', {get: () => undefined})");
            
            // Override plugins
            js.executeScript("Object.defineProperty(navigator, 'plugins', {get: () => [1, 2, 3, 4, 5]})");
            
            // Override languages
            js.executeScript("Object.defineProperty(navigator, 'languages', {get: () => ['en-US', 'en']})");
            
            // Override platform
            js.executeScript("Object.defineProperty(navigator, 'platform', {get: () => 'MacIntel'})");
            
            // Override vendor
            js.executeScript("Object.defineProperty(navigator, 'vendor', {get: () => 'Google Inc.'})");
            
            // Override userAgent
            js.executeScript("Object.defineProperty(navigator, 'userAgent', {get: () => 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/141.0.0.0 Safari/537.36'})");
            
            // Hide automation indicators
            js.executeScript("delete window.cdc_adoQpoasnfa76pfcZLmcfl_Array");
            js.executeScript("delete window.cdc_adoQpoasnfa76pfcZLmcfl_Promise");
            js.executeScript("delete window.cdc_adoQpoasnfa76pfcZLmcfl_Symbol");
            
            logger.info("Stealth techniques applied successfully");
            
        } catch (Exception e) {
            logger.warn("Failed to apply some stealth techniques: " + e.getMessage());
        }
    }
}
