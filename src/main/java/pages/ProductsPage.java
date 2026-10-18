package pages;

import org.openqa.selenium.By;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class ProductsPage extends BasePage {

    private final By pageTitle   = By.className("title");
    private final By cartBadge   = By.className("shopping_cart_badge");
    private final By cartLink    = By.className("shopping_cart_link");
    private final By menuButton  = By.id("react-burger-menu-btn");
    private final By logoutLink  = By.id("logout_sidebar_link");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    /** "Sauce Labs Backpack" -> add-to-cart-sauce-labs-backpack */
    private String toId(String prefix, String productName) {
        return prefix + productName.toLowerCase().replace(" ", "-");
    }

    @Step("Check Products page is displayed")
    public boolean isProductsPageDisplayed() {
        return isDisplayed(pageTitle) && getText(pageTitle).equals("Products");
    }

    @Step("Add product {productName} to cart")
    public void addToCart(String productName) {
        click(By.id(toId("add-to-cart-", productName)));
    }

    @Step("Remove product {productName} from cart")
    public void removeFromCart(String productName) {
        click(By.id(toId("remove-", productName)));
    }

    /** Badge disappears when cart is empty, so use findElements (no exception). */
    @Step("Read cart badge count")
    public int getCartCount() {
        var badges = driver.findElements(cartBadge);
        return badges.isEmpty() ? 0 : Integer.parseInt(badges.get(0).getText());
    }

    @Step("Open cart")
    public CartPage goToCart() {
        click(cartLink);
        return new CartPage(driver);
    }

    @Step("Logout")
    public void logout() {
        click(menuButton);
        click(logoutLink);
    }
}
