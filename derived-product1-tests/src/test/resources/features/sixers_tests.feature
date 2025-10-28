Feature: NBA Sixers Website Testing - Derived Product 1
  As a user visiting the NBA Sixers website
  I want to test the tickets section slides functionality
  So that I can ensure the slides work correctly

  Background:
    Given I navigate to the Sixers website

  @TC3 @Tickets @Slides
  Scenario: Test Case 3 - Count and validate slides below Tickets Menu
    Given I am on the Sixers homepage
    When I scroll down to the Tickets section
    Then I should see slides container
    When I count the number of slides
    And I get the title of each slide
    And I validate slide titles with expected test data
    When I count the duration of each slide
    And I validate slide durations with expected test data
    Then all slide validations should pass

  @TC3_Alternative @Tickets @Slides
  Scenario Outline: Test Case 3 Alternative - Validate slides with different expected data
    Given I am on the Sixers homepage
    When I scroll down to the Tickets section
    Then I should see slides container
    When I count the number of slides
    And I get the title of each slide
    And I validate slide titles with "<expected_titles>"
    When I count the duration of each slide
    And I validate slide durations with "<expected_durations>"
    Then all slide validations should pass
    
    Examples:
      | expected_titles | expected_durations |
      | Game Tickets,Season Tickets,Group Tickets | 5s,3s,4s |
      | Premium Seating,Single Game,Playoffs | 6s,4s,5s |
