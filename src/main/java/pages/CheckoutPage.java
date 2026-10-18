package pages;

import org.openqa.selenium.By;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    private final By firstName   = By.id("first-name");
    private final By lastName    = By.id("last-name");
    private final By postalCode  = By.id("postal-code");
    private final By continueBtn = By.id("continue");
    private final By finishBtn   = By.id("finish");
    private final By confirmMsg  = By.className("complete-header");
    private final By errorMsg    = By.cssSelector("[data-test='error']");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    @Step("Enter shipping details {first} {last} {zip}")
    public void enterShippingDetails(String first, String last, String zip) {
        type(firstName, first);
        type(lastName, last);
        type(postalCode, zip);
        click(continueBtn);
    }

    @Step("Click Continue")
    public void clickContinueOnly() {
        click(continueBtn);
    }

    @Step("Click Finish")
    public void clickFinish() {
        click(finishBtn);
    }

    @Step("Read order confirmation")
    public String getConfirmationMessage() {
        return getText(confirmMsg);
    }

    @Step("Read checkout error")
    public String getErrorMessage() {
        return getText(errorMsg);
    }
}
