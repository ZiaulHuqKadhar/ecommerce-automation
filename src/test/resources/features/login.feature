@login
Feature: Login functionality
  As a customer
  I want to log in to the store
  So that I can shop for products

  @smoke
  Scenario: Successful login with valid credentials
    Given the user is on the login page
    When the user logs in with username "standard_user" and password "secret_sauce"
    Then the products page should be displayed

  @regression
  Scenario Outline: Login fails with invalid data
    Given the user is on the login page
    When the user logs in with username "<username>" and password "<password>"
    Then the error message should contain "<message>"

    Examples:
      | username        | password     | message                    |
      | locked_out_user | secret_sauce | locked out                 |
      | standard_user   | wrong_pass   | do not match               |
      |                 | secret_sauce | Username is required       |
      | standard_user   |              | Password is required       |
