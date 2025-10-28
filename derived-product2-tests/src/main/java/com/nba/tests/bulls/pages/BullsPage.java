package com.nba.tests.bulls.pages;

import com.nba.framework.pages.BasePage;
import com.nba.framework.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BullsPage extends BasePage {
    
    private static final ConfigManager config = ConfigManager.getInstance();
    
    // Locators
    private static final By FOOTER_SECTION = By.cssSelector("footer, .footer, [class*='footer']");
    private static final By FOOTER_LINKS = By.cssSelector("footer a, .footer a, [class*='footer'] a");
    private static final By TEAM_LINKS = By.cssSelector("footer a[href*='team'], .footer a[href*='team']");
    private static final By TICKETS_LINKS = By.cssSelector("footer a[href*='ticket'], .footer a[href*='ticket']");
    private static final By SHOP_LINKS = By.cssSelector("footer a[href*='shop'], .footer a[href*='shop']");
    private static final By SOCIAL_LINKS = By.cssSelector("footer a[href*='facebook'], footer a[href*='twitter'], footer a[href*='instagram']");
    
    public void navigateToBullsPage() {
        navigateTo(config.getBullsUrl());
    }
    
    public void scrollToFooter() {
        scrollToElement(FOOTER_SECTION);
    }
    
    public List<WebElement> getAllFooterLinks() {
        return findElements(FOOTER_LINKS);
    }
    
    public List<String> getAllFooterLinkUrls() {
        List<String> urls = new ArrayList<>();
        List<WebElement> links = getAllFooterLinks();
        
        for (WebElement link : links) {
            String href = link.getAttribute("href");
            if (href != null && !href.isEmpty()) {
                urls.add(href);
            }
        }
        return urls;
    }
    
    public List<WebElement> getTeamLinks() {
        return findElements(TEAM_LINKS);
    }
    
    public List<WebElement> getTicketsLinks() {
        return findElements(TICKETS_LINKS);
    }
    
    public List<WebElement> getShopLinks() {
        return findElements(SHOP_LINKS);
    }
    
    public List<WebElement> getSocialLinks() {
        return findElements(SOCIAL_LINKS);
    }
    
    public List<String> getLinkTexts() {
        List<String> linkTexts = new ArrayList<>();
        List<WebElement> links = getAllFooterLinks();
        
        for (WebElement link : links) {
            String text = link.getText();
            if (text != null && !text.trim().isEmpty()) {
                linkTexts.add(text.trim());
            }
        }
        return linkTexts;
    }
    
    public List<String> getLinkCategories() {
        List<String> categories = new ArrayList<>();
        List<WebElement> links = getAllFooterLinks();
        
        for (WebElement link : links) {
            String href = link.getAttribute("href");
            String text = link.getText();
            
            if (href != null) {
                if (href.contains("team")) {
                    categories.add("Team");
                } else if (href.contains("ticket")) {
                    categories.add("Tickets");
                } else if (href.contains("shop")) {
                    categories.add("Shop");
                } else if (href.contains("facebook") || href.contains("twitter") || href.contains("instagram")) {
                    categories.add("Social");
                } else {
                    categories.add("Other");
                }
            } else {
                categories.add("Unknown");
            }
        }
        return categories;
    }
    
    public boolean isFooterVisible() {
        return isElementVisible(FOOTER_SECTION);
    }
    
    public int countFooterLinks() {
        return getAllFooterLinks().size();
    }
    
    public int countTeamLinks() {
        return getTeamLinks().size();
    }
    
    public int countTicketsLinks() {
        return getTicketsLinks().size();
    }
    
    public int countShopLinks() {
        return getShopLinks().size();
    }
    
    public int countSocialLinks() {
        return getSocialLinks().size();
    }
    
    public void waitForFooterToLoad() {
        waitForElementToBeVisible(FOOTER_SECTION);
    }
}
