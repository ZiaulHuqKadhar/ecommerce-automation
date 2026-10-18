package pages;

import org.openqa.selenium.By;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton   = By.id("login-button");
    private final By errorMessage  = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Login with username {username}")
    public void login(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);
    }

    @Step("Read login error message")
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    @Step("Check login page is displayed")
    public boolean isLoginPageDisplayed() {
        return isDisplayed(loginButton);
    }
}
