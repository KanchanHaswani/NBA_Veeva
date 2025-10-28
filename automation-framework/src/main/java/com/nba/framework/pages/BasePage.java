package com.nba.framework.pages;

import com.nba.framework.driver.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public abstract class BasePage {
    
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    protected void click(By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator));
            driver.findElement(locator).click();
            logger.info("Clicked element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to click element: " + locator, e);
            throw new RuntimeException("Click action failed", e);
        }
    }
    
    protected void sendKeys(By locator, String text) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            WebElement element = driver.findElement(locator);
            element.clear();
            element.sendKeys(text);
            logger.info("Entered text '" + text + "' in element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to send keys to element: " + locator, e);
            throw new RuntimeException("Send keys action failed", e);
        }
    }
    
    protected String getText(By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            String text = driver.findElement(locator).getText();
            logger.info("Retrieved text '" + text + "' from element: " + locator);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element: " + locator, e);
            throw new RuntimeException("Get text action failed", e);
        }
    }
    
    protected boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    public boolean isElementVisible(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    protected void waitForElementToBeVisible(By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.info("Element is visible: " + locator);
        } catch (Exception e) {
            logger.error("Element not visible: " + locator, e);
            throw new RuntimeException("Element visibility wait failed", e);
        }
    }
    
    protected void waitForElementToBeClickable(By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator));
            logger.info("Element is clickable: " + locator);
        } catch (Exception e) {
            logger.error("Element not clickable: " + locator, e);
            throw new RuntimeException("Element clickability wait failed", e);
        }
    }
    
    protected List<WebElement> findElements(By locator) {
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
            return driver.findElements(locator);
        } catch (Exception e) {
            logger.error("Failed to find elements: " + locator, e);
            throw new RuntimeException("Find elements action failed", e);
        }
    }
    
    protected WebElement findElement(By locator) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return driver.findElement(locator);
        } catch (Exception e) {
            logger.error("Failed to find element: " + locator, e);
            throw new RuntimeException("Find element action failed", e);
        }
    }
    
    protected void scrollToElement(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info("Scrolled to element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to scroll to element: " + locator, e);
            throw new RuntimeException("Scroll action failed", e);
        }
    }
    
    protected void hoverOverElement(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('mouseover'));", element);
            logger.info("Hovered over element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to hover over element: " + locator, e);
            throw new RuntimeException("Hover action failed", e);
        }
    }
    
    protected void waitForPageLoad() {
        try {
            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));
            logger.info("Page loaded successfully");
        } catch (Exception e) {
            logger.error("Page load wait failed", e);
            throw new RuntimeException("Page load wait failed", e);
        }
    }
    
    protected void navigateTo(String url) {
        try {
            driver.get(url);
            waitForPageLoad();
            logger.info("Navigated to URL: " + url);
        } catch (Exception e) {
            logger.error("Failed to navigate to URL: " + url, e);
            throw new RuntimeException("Navigation failed", e);
        }
    }
    
    public String getCurrentUrl() {
        try {
            String url = driver.getCurrentUrl();
            logger.info("Current URL: " + url);
            return url;
        } catch (Exception e) {
            logger.error("Failed to get current URL", e);
            throw new RuntimeException("Get current URL failed", e);
        }
    }
    
    protected String getPageTitle() {
        try {
            String title = driver.getTitle();
            logger.info("Page title: " + title);
            return title;
        } catch (Exception e) {
            logger.error("Failed to get page title", e);
            throw new RuntimeException("Get page title failed", e);
        }
    }
}
