package com.nba.tests.core.assertions;

import com.nba.framework.assertions.BaseAssertion;
import com.nba.tests.core.pages.WarriorsPage;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WarriorsPageAssertions extends BaseAssertion {
    
    private WarriorsPage warriorsPage;
    
    public WarriorsPageAssertions() {
        this.warriorsPage = new WarriorsPage();
    }
    
    public void assertWarriorsPageLoaded() {
        String currentUrl = warriorsPage.getCurrentUrl();
        assertContains(currentUrl, "warriors", "Warriors page should be loaded");
    }
    
    public void assertShopMenuVisible() {
        assertTrue(warriorsPage.isElementVisible(warriorsPage.SHOP_MENU), "Shop menu should be visible");
    }
    
    public void assertMensMenuVisible() {
        assertTrue(warriorsPage.isElementVisible(warriorsPage.MENS_MENU), "Men's menu should be visible");
    }
    
    public void assertJacketsCategoryVisible() {
        assertTrue(warriorsPage.isElementVisible(warriorsPage.JACKETS_CATEGORY), "Jackets category should be visible");
    }
    
    public void assertProductItemsPresent() {
        List<WebElement> products = warriorsPage.getAllProductItems();
        assertTrue(products.size() > 0, "Product items should be present");
    }
    
    public void assertProductHasTitle(WebElement product) {
        String title = warriorsPage.getProductTitle(product);
        assertNotNull(title, "Product should have a title");
        assertTrue(!title.isEmpty(), "Product title should not be empty");
    }
    
    public void assertProductHasPrice(WebElement product) {
        String price = warriorsPage.getProductPrice(product);
        assertNotNull(price, "Product should have a price");
        assertTrue(!price.isEmpty(), "Product price should not be empty");
    }
    
    public void assertTopSellerMessage(WebElement product, boolean expectedTopSeller) {
        String topSellerMessage = warriorsPage.getTopSellerMessage(product);
        if (expectedTopSeller) {
            assertTrue(!topSellerMessage.isEmpty(), "Product should have top seller message");
        } else {
            assertTrue(topSellerMessage.isEmpty(), "Product should not have top seller message");
        }
    }
    
    public void assertVideoFeedsPresent() {
        int videoCount = warriorsPage.countVideoFeeds();
        assertTrue(videoCount > 0, "Video feeds should be present");
    }
    
    public void assertVideoContainersPresent() {
        int containerCount = warriorsPage.countVideoContainers();
        assertTrue(containerCount > 0, "Video containers should be present");
    }
    
    public void assertVideoFeedsCount(int expectedCount) {
        int actualCount = warriorsPage.countVideoFeeds();
        assertEquals(actualCount, expectedCount, "Video feeds count should match expected");
    }
    
    public void assertVideoFeedsCountGreaterThan(int minCount) {
        int actualCount = warriorsPage.countVideoFeeds();
        assertTrue(actualCount >= minCount, "Video feeds count should be greater than or equal to " + minCount);
    }
    
    public void assertNewAndFeaturesMenuVisible() {
        assertTrue(warriorsPage.isElementVisible(warriorsPage.NEW_FEATURES_MENU), "New & Features menu should be visible");
    }
    
    public void assertPaginationPresent() {
        assertTrue(warriorsPage.isPaginationPresent(), "Pagination should be present");
    }
    
    public void assertNextPageAvailable() {
        assertTrue(warriorsPage.hasNextPage(), "Next page should be available");
    }
    
    public void assertNextPageNotAvailable() {
        assertFalse(warriorsPage.hasNextPage(), "Next page should not be available");
    }
}
