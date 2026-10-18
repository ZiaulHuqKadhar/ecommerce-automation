@checkout
Feature: Shopping cart and checkout

  Background:
    Given the user is logged in as "standard_user"

  @smoke
  Scenario: Add a product to the cart
    When the user adds "Sauce Labs Backpack" to the cart
    Then the cart badge should show 1

  @smoke @regression
  Scenario: Complete an order end to end
    When the user adds "Sauce Labs Backpack" to the cart
    And the user opens the cart and starts checkout
    And the user enters first name "John" last name "Doe" and postal code "635109"
    And the user finishes the order
    Then the order confirmation should say "Thank you for your order!"
