package com.nba.tests.sixers.pages;

import com.nba.framework.pages.BasePage;
import com.nba.framework.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SixersPage extends BasePage {
    
    private static final ConfigManager config = ConfigManager.getInstance();
    
    // Locators
    private static final By TICKETS_MENU = By.xpath("//a[contains(text(), 'Tickets') or contains(@href, 'ticket')]");
    private static final By SLIDES_CONTAINER = By.cssSelector(".slides-container, .carousel, .slider, [class*='slide']");
    private static final By SLIDE_ITEMS = By.cssSelector(".slide, .carousel-item, .slider-item, [class*='slide-item']");
    private static final By SLIDE_TITLE = By.cssSelector(".slide-title, .carousel-title, h2, h3");
    private static final By SLIDE_DURATION = By.cssSelector(".slide-duration, .duration, [class*='duration']");
    private static final By SLIDE_INDICATOR = By.cssSelector(".slide-indicator, .carousel-indicator, .slider-dot");
    private static final By NEXT_SLIDE_BUTTON = By.cssSelector(".next-slide, .carousel-next, .slider-next");
    private static final By PREV_SLIDE_BUTTON = By.cssSelector(".prev-slide, .carousel-prev, .slider-prev");
    
    public void navigateToSixersPage() {
        navigateTo(config.getSixersUrl());
    }
    
    public void clickTicketsMenu() {
        click(TICKETS_MENU);
    }
    
    public List<WebElement> getAllSlides() {
        return findElements(SLIDE_ITEMS);
    }
    
    public int countSlides() {
        return getAllSlides().size();
    }
    
    public String getSlideTitle(WebElement slideElement) {
        try {
            return slideElement.findElement(SLIDE_TITLE).getText();
        } catch (Exception e) {
            return "Title not found";
        }
    }
    
    public String getSlideDuration(WebElement slideElement) {
        try {
            return slideElement.findElement(SLIDE_DURATION).getText();
        } catch (Exception e) {
            return "Duration not found";
        }
    }
    
    public boolean isSlideActive(WebElement slideElement) {
        try {
            return slideElement.getAttribute("class").contains("active") || 
                   slideElement.getAttribute("class").contains("current");
        } catch (Exception e) {
            return false;
        }
    }
    
    public void clickNextSlide() {
        if (isElementPresent(NEXT_SLIDE_BUTTON) && isElementVisible(NEXT_SLIDE_BUTTON)) {
            click(NEXT_SLIDE_BUTTON);
        }
    }
    
    public void clickPreviousSlide() {
        if (isElementPresent(PREV_SLIDE_BUTTON) && isElementVisible(PREV_SLIDE_BUTTON)) {
            click(PREV_SLIDE_BUTTON);
        }
    }
    
    public boolean hasNextSlide() {
        return isElementPresent(NEXT_SLIDE_BUTTON) && isElementVisible(NEXT_SLIDE_BUTTON);
    }
    
    public boolean hasPreviousSlide() {
        return isElementPresent(PREV_SLIDE_BUTTON) && isElementVisible(PREV_SLIDE_BUTTON);
    }
    
    public List<WebElement> getSlideIndicators() {
        return findElements(SLIDE_INDICATOR);
    }
    
    public void clickSlideIndicator(int index) {
        List<WebElement> indicators = getSlideIndicators();
        if (index < indicators.size()) {
            indicators.get(index).click();
        }
    }
    
    public boolean isSlidesContainerPresent() {
        return isElementPresent(SLIDES_CONTAINER);
    }
    
    public WebElement getSlidesContainer() {
        return findElement(SLIDES_CONTAINER);
    }
    
    public void waitForSlideToLoad() {
        waitForElementToBeVisible(SLIDES_CONTAINER);
    }
    
    public void scrollToSlidesSection() {
        scrollToElement(SLIDES_CONTAINER);
    }
}
