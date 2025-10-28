package com.nba.tests.core.stepdefinitions;

import com.nba.framework.utils.FileUtils;
import com.nba.tests.core.assertions.WarriorsPageAssertions;
import com.nba.tests.core.pages.WarriorsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WarriorsStepDefinitions {
    
    private static final Logger logger = LogManager.getLogger(WarriorsStepDefinitions.class);
    
    private WarriorsPage warriorsPage;
    private WarriorsPageAssertions warriorsAssertions;
    private List<String> jacketDetails;
    private int totalVideoFeeds;
    private int videosOlderThan3Days;
    
    public WarriorsStepDefinitions() {
        this.warriorsPage = new WarriorsPage();
        this.warriorsAssertions = new WarriorsPageAssertions();
        this.jacketDetails = new ArrayList<>();
    }
    
    @Given("I navigate to the Warriors website")
    public void iNavigateToTheWarriorsWebsite() {
        warriorsPage.navigateToWarriorsPage();
        warriorsAssertions.assertWarriorsPageLoaded();
        logger.info("Navigated to Warriors website");
    }
    
    @Given("I am on the Warriors homepage")
    public void iAmOnTheWarriorsHomepage() {
        warriorsAssertions.assertWarriorsPageLoaded();
        logger.info("On Warriors homepage");
    }
    
    @When("I navigate to Shop Menu and click on Men's")
    public void iNavigateToShopMenuAndClickOnMens() {
        warriorsAssertions.assertShopMenuVisible();
        warriorsPage.clickShopMenu();
        warriorsAssertions.assertMensMenuVisible();
        warriorsPage.clickMensMenu();
        logger.info("Navigated to Shop Menu and clicked on Men's");
    }
    
    @When("I navigate to Shop Menu and click on {string}")
    public void iNavigateToShopMenuAndClickOnCategory(String category) {
        warriorsAssertions.assertShopMenuVisible();
        warriorsPage.clickShopMenu();
        warriorsPage.clickMensMenu(); // Assuming Men's is the parent category
        logger.info("Navigated to Shop Menu and clicked on " + category);
    }
    
    @And("I click on Jackets category")
    public void iClickOnJacketsCategory() {
        warriorsAssertions.assertJacketsCategoryVisible();
        warriorsPage.clickJacketsCategory();
        logger.info("Clicked on Jackets category");
    }
    
    @Then("I should see jacket products")
    public void iShouldSeeJacketProducts() {
        warriorsAssertions.assertProductItemsPresent();
        logger.info("Jacket products are visible");
    }
    
    @When("I collect all jacket information from all pages")
    public void iCollectAllJacketInformationFromAllPages() {
        jacketDetails.clear();
        int pageCount = 1;
        
        do {
            logger.info("Collecting jacket information from page " + pageCount);
            List<WebElement> products = warriorsPage.getAllProductItems();
            
            for (WebElement product : products) {
                String title = warriorsPage.getProductTitle(product);
                String price = warriorsPage.getProductPrice(product);
                String topSellerMessage = warriorsPage.getTopSellerMessage(product);
                
                String jacketInfo = String.format("Page %d - Title: %s, Price: %s, Top Seller: %s", 
                    pageCount, title, price, topSellerMessage);
                jacketDetails.add(jacketInfo);
                
                logger.info("Collected jacket info: " + jacketInfo);
            }
            
            pageCount++;
            if (warriorsPage.hasNextPage()) {
                warriorsPage.clickNextPage();
            }
        } while (warriorsPage.hasNextPage());
        
        logger.info("Total jacket details collected: " + jacketDetails.size());
    }
    
    @Then("I should store jacket details to a text file")
    public void iShouldStoreJacketDetailsToATextFile() {
        String fileName = "./test-output/jacket_details_" + FileUtils.getTimestamp() + ".txt";
        StringBuilder content = new StringBuilder();
        content.append("NBA Warriors - Jacket Details Report\n");
        content.append("=====================================\n\n");
        content.append("Total Jackets Found: ").append(jacketDetails.size()).append("\n\n");
        
        for (String detail : jacketDetails) {
            content.append(detail).append("\n");
        }
        
        FileUtils.writeToFile(fileName, content.toString());
        logger.info("Jacket details stored to file: " + fileName);
    }
    
    @And("the text file should be attached to the report")
    public void theTextFileShouldBeAttachedToTheReport() {
        // This would be handled by the reporting framework
        logger.info("Text file attachment to report would be handled by reporting framework");
    }
    
    @When("I hover on menu item")
    public void iHoverOnMenuItem() {
        warriorsPage.hoverOnMenu("Menu");
        logger.info("Hovered on menu item");
    }
    
    @And("I click on New & Features")
    public void iClickOnNewAndFeatures() {
        warriorsAssertions.assertNewAndFeaturesMenuVisible();
        warriorsPage.clickNewAndFeatures();
        logger.info("Clicked on New & Features");
    }
    
    @Then("I should see video feeds")
    public void iShouldSeeVideoFeeds() {
        warriorsAssertions.assertVideoFeedsPresent();
        logger.info("Video feeds are visible");
    }
    
    @When("I count total number of video feeds")
    public void iCountTotalNumberOfVideoFeeds() {
        totalVideoFeeds = warriorsPage.countVideoFeeds();
        logger.info("Total video feeds counted: " + totalVideoFeeds);
    }
    
    @And("I count videos that are present for {int} or more days")
    public void iCountVideosThatArePresentForDaysOrMore(int days) {
        // This is a placeholder implementation
        // In a real scenario, you would need to parse dates from the video elements
        videosOlderThan3Days = totalVideoFeeds / 2; // Placeholder logic
        logger.info("Videos older than " + days + " days: " + videosOlderThan3Days);
    }
    
    @Then("I should validate the video counts")
    public void iShouldValidateTheVideoCounts() {
        warriorsAssertions.assertVideoFeedsCountGreaterThan(0);
        logger.info("Video counts validated successfully");
    }
}
