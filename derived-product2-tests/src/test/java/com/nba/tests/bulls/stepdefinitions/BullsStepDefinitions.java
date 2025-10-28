package com.nba.tests.bulls.stepdefinitions;

import com.nba.framework.assertions.BaseAssertion;
import com.nba.framework.utils.FileUtils;
import com.nba.tests.bulls.pages.BullsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BullsStepDefinitions {
    
    private static final Logger logger = LogManager.getLogger(BullsStepDefinitions.class);
    
    private BullsPage bullsPage;
    private BaseAssertion assertions;
    private List<String> footerLinkUrls;
    private List<String> footerLinkTexts;
    private List<String> footerLinkCategories;
    private Map<String, Integer> duplicateLinks;
    
    public BullsStepDefinitions() {
        this.bullsPage = new BullsPage();
        this.assertions = new BaseAssertion();
        this.footerLinkUrls = new ArrayList<>();
        this.footerLinkTexts = new ArrayList<>();
        this.footerLinkCategories = new ArrayList<>();
        this.duplicateLinks = new HashMap<>();
    }
    
    @Given("I navigate to the Bulls website")
    public void iNavigateToTheBullsWebsite() {
        bullsPage.navigateToBullsPage();
        logger.info("Navigated to Bulls website");
    }
    
    @Given("I am on the Bulls homepage")
    public void iAmOnTheBullsHomepage() {
        logger.info("On Bulls homepage");
    }
    
    @When("I scroll down to the footer")
    public void iScrollDownToTheFooter() {
        bullsPage.scrollToFooter();
        bullsPage.waitForFooterToLoad();
        logger.info("Scrolled down to footer");
    }
    
    @Then("I should see footer section")
    public void iShouldSeeFooterSection() {
        assertions.assertTrue(bullsPage.isFooterVisible(), "Footer section should be visible");
        logger.info("Footer section is visible");
    }
    
    @When("I find all hyperlinks in the footer")
    public void iFindAllHyperlinksInTheFooter() {
        footerLinkUrls = bullsPage.getAllFooterLinkUrls();
        footerLinkTexts = bullsPage.getLinkTexts();
        footerLinkCategories = bullsPage.getLinkCategories();
        
        logger.info("Found " + footerLinkUrls.size() + " footer links");
    }
    
    @And("I categorize the footer links")
    public void iCategorizeTheFooterLinks() {
        logger.info("Footer links categorized:");
        logger.info("- Team links: " + bullsPage.countTeamLinks());
        logger.info("- Tickets links: " + bullsPage.countTicketsLinks());
        logger.info("- Shop links: " + bullsPage.countShopLinks());
        logger.info("- Social links: " + bullsPage.countSocialLinks());
    }
    
    @Then("I should store footer links to a CSV file")
    public void iShouldStoreFooterLinksToACSVFile() {
        String fileName = "./test-output/footer_links_" + FileUtils.getTimestamp() + ".csv";
        
        List<String[]> csvData = new ArrayList<>();
        csvData.add(new String[]{"Link Text", "URL", "Category"});
        
        for (int i = 0; i < footerLinkUrls.size(); i++) {
            String text = i < footerLinkTexts.size() ? footerLinkTexts.get(i) : "";
            String url = footerLinkUrls.get(i);
            String category = i < footerLinkCategories.size() ? footerLinkCategories.get(i) : "";
            
            csvData.add(new String[]{text, url, category});
        }
        
        FileUtils.writeCSVFile(fileName, csvData);
        logger.info("Footer links stored to CSV file: " + fileName);
    }
    
    @And("I should check for duplicate hyperlinks")
    public void iShouldCheckForDuplicateHyperlinks() {
        duplicateLinks.clear();
        
        for (String url : footerLinkUrls) {
            duplicateLinks.put(url, duplicateLinks.getOrDefault(url, 0) + 1);
        }
        
        logger.info("Duplicate link check completed");
    }
    
    @Then("I should report any duplicate hyperlinks found")
    public void iShouldReportAnyDuplicateHyperlinksFound() {
        List<String> duplicates = new ArrayList<>();
        
        for (Map.Entry<String, Integer> entry : duplicateLinks.entrySet()) {
            if (entry.getValue() > 1) {
                duplicates.add(entry.getKey() + " (appears " + entry.getValue() + " times)");
            }
        }
        
        if (duplicates.isEmpty()) {
            logger.info("No duplicate hyperlinks found");
            assertions.assertTrue(true, "No duplicate hyperlinks should be found");
        } else {
            logger.warn("Duplicate hyperlinks found:");
            for (String duplicate : duplicates) {
                logger.warn("- " + duplicate);
            }
            assertions.assertTrue(false, "Duplicate hyperlinks found: " + duplicates.toString());
        }
    }
    
    @When("I filter links by {string}")
    public void iFilterLinksByCategory(String category) {
        logger.info("Filtering links by category: " + category);
    }
    
    @Then("I should validate {string} links")
    public void iShouldValidateCategoryLinks(String category) {
        int count = 0;
        switch (category.toLowerCase()) {
            case "team":
                count = bullsPage.countTeamLinks();
                break;
            case "tickets":
                count = bullsPage.countTicketsLinks();
                break;
            case "shop":
                count = bullsPage.countShopLinks();
                break;
            case "social":
                count = bullsPage.countSocialLinks();
                break;
        }
        
        assertions.assertTrue(count > 0, category + " links should be present");
        logger.info("Validated " + count + " " + category + " links");
    }
    
    @And("I should store {string} links to CSV file")
    public void iShouldStoreCategoryLinksToCSVFile(String category) {
        String fileName = "./test-output/" + category.toLowerCase() + "_links_" + FileUtils.getTimestamp() + ".csv";
        
        List<String[]> csvData = new ArrayList<>();
        csvData.add(new String[]{"Link Text", "URL", "Category"});
        
        List<WebElement> categoryLinks = new ArrayList<>();
        switch (category.toLowerCase()) {
            case "team":
                categoryLinks = bullsPage.getTeamLinks();
                break;
            case "tickets":
                categoryLinks = bullsPage.getTicketsLinks();
                break;
            case "shop":
                categoryLinks = bullsPage.getShopLinks();
                break;
            case "social":
                categoryLinks = bullsPage.getSocialLinks();
                break;
        }
        
        for (WebElement link : categoryLinks) {
            String text = link.getText();
            String url = link.getAttribute("href");
            csvData.add(new String[]{text, url, category});
        }
        
        FileUtils.writeCSVFile(fileName, csvData);
        logger.info(category + " links stored to CSV file: " + fileName);
    }
}
