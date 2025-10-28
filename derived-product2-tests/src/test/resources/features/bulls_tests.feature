Feature: NBA Bulls Website Testing - Derived Product 2
  As a user visiting the NBA Bulls website
  I want to test the footer links functionality
  So that I can ensure all footer links work correctly

  Background:
    Given I navigate to the Bulls website

  @TC4 @Footer @Links
  Scenario: Test Case 4 - Find and validate footer links
    Given I am on the Bulls homepage
    When I scroll down to the footer
    Then I should see footer section
    When I find all hyperlinks in the footer
    And I categorize the footer links
    Then I should store footer links to a CSV file
    And I should check for duplicate hyperlinks
    Then I should report any duplicate hyperlinks found

  @TC4_Alternative @Footer @Links
  Scenario Outline: Test Case 4 Alternative - Validate footer links by category
    Given I am on the Bulls homepage
    When I scroll down to the footer
    Then I should see footer section
    When I find all hyperlinks in the footer
    And I filter links by "<category>"
    Then I should validate "<category>" links
    And I should store "<category>" links to CSV file
    
    Examples:
      | category |
      | Team     |
      | Tickets  |
      | Shop     |
      | Social   |
