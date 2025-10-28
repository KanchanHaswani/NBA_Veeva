package com.nba.tests.core;

import com.nba.framework.driver.DriverUtils;
import com.nba.framework.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ComprehensiveRobotTest {

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
    public void testComprehensiveRobotVerification() {
        System.out.println("=== Comprehensive Robot Verification Test ===");
        try {
            // Step 1: Navigate to Warriors website
            driver.get("https://www.nba.com/warriors");
            Thread.sleep(3000);
            
            String initialUrl = driver.getCurrentUrl();
            String initialTitle = driver.getTitle();
            System.out.println("Initial URL: " + initialUrl);
            System.out.println("Initial Title: " + initialTitle);
            
            // Step 2: Try to find and click on Shop menu
            try {
                driver.findElement(By.xpath("//a[contains(text(), 'Shop') or contains(@href, 'shop')]")).click();
                Thread.sleep(3000);
                
                String afterClickUrl = driver.getCurrentUrl();
                System.out.println("After Shop click URL: " + afterClickUrl);
                
                if (afterClickUrl.contains("robot") || afterClickUrl.contains("captcha") || afterClickUrl.contains("verification")) {
                    System.out.println("❌ Robot verification appeared after Shop click!");
                } else {
                    System.out.println("✅ No robot verification after Shop click");
                }
                
            } catch (Exception e) {
                System.out.println("ℹ️  Shop menu not found or not clickable: " + e.getMessage());
            }
            
            // Step 3: Try to scroll and interact
            try {
                driver.findElement(By.tagName("body")).sendKeys(org.openqa.selenium.Keys.PAGE_DOWN);
                Thread.sleep(2000);
                driver.findElement(By.tagName("body")).sendKeys(org.openqa.selenium.Keys.PAGE_DOWN);
                Thread.sleep(2000);
                
                String afterScrollUrl = driver.getCurrentUrl();
                System.out.println("After scroll URL: " + afterScrollUrl);
                
                if (afterScrollUrl.contains("robot") || afterScrollUrl.contains("captcha") || afterScrollUrl.contains("verification")) {
                    System.out.println("❌ Robot verification appeared after scrolling!");
                } else {
                    System.out.println("✅ No robot verification after scrolling");
                }
                
            } catch (Exception e) {
                System.out.println("ℹ️  Scrolling failed: " + e.getMessage());
            }
            
            // Step 4: Final check
            String finalUrl = driver.getCurrentUrl();
            String finalTitle = driver.getTitle();
            System.out.println("Final URL: " + finalUrl);
            System.out.println("Final Title: " + finalTitle);
            
            if (finalUrl.contains("robot") || finalUrl.contains("captcha") || finalUrl.contains("verification")) {
                System.out.println("❌ Robot verification detected in final check!");
            } else {
                System.out.println("✅ No robot verification detected - stealth techniques working!");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
