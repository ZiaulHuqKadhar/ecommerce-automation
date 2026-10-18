package pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage extends BasePage {

    private final By cartItems    = By.className("cart_item");
    private final By itemNames    = By.className("inventory_item_name");
    private final By checkoutBtn  = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    @Step("Read item names in cart")
    public List<String> getItemNames() {
        List<WebElement> els = driver.findElements(itemNames);
        return els.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    @Step("Count items in cart")
    public int getItemCount() {
        return driver.findElements(cartItems).size();
    }

    @Step("Remove {productName} from cart page")
    public void removeItem(String productName) {
        click(By.id("remove-" + productName.toLowerCase().replace(" ", "-")));
    }

    @Step("Click Checkout")
    public CheckoutPage clickCheckout() {
        click(checkoutBtn);
        return new CheckoutPage(driver);
    }
}
