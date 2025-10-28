package com.nba.tests.core;

import com.nba.framework.driver.DriverUtils;
import com.nba.framework.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RobotVerificationTest {

    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = DriverUtils.createChromeDriver();
        DriverManager.setDriver(driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testNavigateToWarriorsWebsite() {
        System.out.println("=== Testing NBA Warriors Website Navigation ===");
        try {
            driver.get("https://www.nba.com/warriors");
            Thread.sleep(5000); // Wait for page to load
            
            String pageTitle = driver.getTitle();
            String currentUrl = driver.getCurrentUrl();
            
            System.out.println("Page Title: " + pageTitle);
            System.out.println("Current URL: " + currentUrl);
            
            if (currentUrl.contains("warriors") && !currentUrl.contains("robot") && !currentUrl.contains("captcha")) {
                System.out.println("✅ Successfully navigated to Warriors website without robot verification!");
            } else if (currentUrl.contains("robot") || currentUrl.contains("captcha")) {
                System.out.println("❌ Robot verification page detected: " + currentUrl);
            } else {
                System.out.println("ℹ️  Navigated to: " + currentUrl);
            }
            
        } catch (Exception e) {
            System.out.println("❌ Navigation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
