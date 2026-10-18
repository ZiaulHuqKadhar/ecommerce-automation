package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import base.BaseTest;
import pages.LoginPage;
import pages.ProductsPage;
import utils.ConfigReader;

@Epic("E-commerce Store")
@Feature("Login")
public class LoginTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Valid user can log in")
    @Story("Valid login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Standard user logs in and lands on Products page")
    public void validLogin() {
        LoginPage login = new LoginPage(driver);
        login.login(ConfigReader.get("valid.username"), ConfigReader.get("valid.password"));

        ProductsPage products = new ProductsPage(driver);
        assertTrue(products.isProductsPageDisplayed(), "Products page should be displayed after login");
    }

    @Test(groups = "regression", description = "Wrong password shows error")
    @Story("Invalid login")
    @Severity(SeverityLevel.NORMAL)
    public void invalidLogin() {
        LoginPage login = new LoginPage(driver);
        login.login("standard_user", "wrong_password");

        assertTrue(login.getErrorMessage().contains("do not match"),
                "Expected 'do not match' error but got: " + login.getErrorMessage());
    }

    @Test(groups = "regression", description = "Locked out user cannot log in")
    @Story("Invalid login")
    @Severity(SeverityLevel.NORMAL)
    public void lockedOutUser() {
        LoginPage login = new LoginPage(driver);
        login.login("locked_out_user", "secret_sauce");

        assertTrue(login.getErrorMessage().contains("locked out"));
    }

    @Test(groups = "regression", description = "User can log out")
    @Story("Logout")
    @Severity(SeverityLevel.MINOR)
    public void logout() {
        new LoginPage(driver).login(ConfigReader.get("valid.username"), ConfigReader.get("valid.password"));
        ProductsPage products = new ProductsPage(driver);
        products.logout();

        assertTrue(new LoginPage(driver).isLoginPageDisplayed(), "Login page should be shown after logout");
        assertEquals(driver.getCurrentUrl(), ConfigReader.get("base.url") + "/");
    }
}
