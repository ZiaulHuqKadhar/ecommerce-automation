package stepdefs;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebDriver;

import base.DriverFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ProductsPage;
import utils.ConfigReader;

/** Step definitions reuse the SAME page objects as the TestNG tests. */
public class UiSteps {

    private WebDriver driver;
    private LoginPage loginPage;
    private ProductsPage productsPage;
    private CheckoutPage checkoutPage;

    @Given("the user is on the login page")
    public void userIsOnLoginPage() {
        driver = DriverFactory.getDriver();
        driver.get(ConfigReader.get("base.url"));
        loginPage = new LoginPage(driver);
    }

    @Given("the user is logged in as {string}")
    public void userIsLoggedInAs(String username) {
        userIsOnLoginPage();
        loginPage.login(username, ConfigReader.get("valid.password"));
        productsPage = new ProductsPage(driver);
    }

    @When("the user logs in with username {string} and password {string}")
    public void userLogsIn(String username, String password) {
        loginPage.login(username, password);
    }

    @Then("the products page should be displayed")
    public void productsPageDisplayed() {
        assertTrue(new ProductsPage(driver).isProductsPageDisplayed());
    }

    @Then("the error message should contain {string}")
    public void errorShouldContain(String message) {
        assertTrue(loginPage.getErrorMessage().contains(message));
    }

    @When("the user adds {string} to the cart")
    public void userAddsToCart(String product) {
        productsPage.addToCart(product);
    }

    @Then("the cart badge should show {int}")
    public void cartBadgeShows(int count) {
        assertEquals(productsPage.getCartCount(), count);
    }

    @And("the user opens the cart and starts checkout")
    public void openCartAndCheckout() {
        CartPage cart = productsPage.goToCart();
        checkoutPage = cart.clickCheckout();
    }

    @And("the user enters first name {string} last name {string} and postal code {string}")
    public void userEntersDetails(String first, String last, String zip) {
        checkoutPage.enterShippingDetails(first, last, zip);
    }

    @And("the user finishes the order")
    public void userFinishesOrder() {
        checkoutPage.clickFinish();
    }

    @Then("the order confirmation should say {string}")
    public void confirmationShouldSay(String expected) {
        assertEquals(checkoutPage.getConfirmationMessage(), expected);
    }
}
