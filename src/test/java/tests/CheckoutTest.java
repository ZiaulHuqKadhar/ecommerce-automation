package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ProductsPage;
import utils.ConfigReader;
import utils.RetryAnalyzer;

@Epic("E-commerce Store")
@Feature("Cart and Checkout")
public class CheckoutTest extends BaseTest {

    private static final String PRODUCT = "Sauce Labs Backpack";
    private ProductsPage products;

    /** Runs after BaseTest.setUp() (parent @BeforeMethod runs first). */
    @BeforeMethod(alwaysRun = true)
    public void loginFirst() {
        new LoginPage(driver).login(ConfigReader.get("valid.username"), ConfigReader.get("valid.password"));
        products = new ProductsPage(driver);
    }

    @Test(groups = {"smoke", "regression"}, retryAnalyzer = RetryAnalyzer.class)
    @Story("Add to cart")
    @Severity(SeverityLevel.CRITICAL)
    public void addProductToCart() {
        products.addToCart(PRODUCT);
        assertEquals(products.getCartCount(), 1, "Cart badge should show 1");

        CartPage cart = products.goToCart();
        assertTrue(cart.getItemNames().contains(PRODUCT), "Cart should contain " + PRODUCT);
    }

    @Test(groups = "regression", retryAnalyzer = RetryAnalyzer.class)
    @Story("Remove from cart")
    @Severity(SeverityLevel.NORMAL)
    public void removeProductFromCart() {
        products.addToCart(PRODUCT);
        CartPage cart = products.goToCart();
        cart.removeItem(PRODUCT);

        assertEquals(cart.getItemCount(), 0, "Cart should be empty after removing");
    }

    @Test(groups = {"smoke", "regression"}, retryAnalyzer = RetryAnalyzer.class)
    @Story("Place order")
    @Severity(SeverityLevel.BLOCKER)
    public void completeCheckoutEndToEnd() {
        products.addToCart(PRODUCT);
        CheckoutPage checkout = products.goToCart().clickCheckout();
        checkout.enterShippingDetails("John", "Doe", "635109");
        checkout.clickFinish();

        assertEquals(checkout.getConfirmationMessage(), "Thank you for your order!");
    }

    @Test(groups = "regression", description = "Negative: checkout without shipping details")
    @Story("Checkout validation")
    @Severity(SeverityLevel.NORMAL)
    public void checkoutWithoutDetailsShowsError() {
        products.addToCart(PRODUCT);
        CheckoutPage checkout = products.goToCart().clickCheckout();
        checkout.clickContinueOnly();

        assertTrue(checkout.getErrorMessage().contains("First Name is required"));
    }
}
