package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.LoginPage;
import pages.ProductsPage;
import utils.ExcelUtils;

/** Data-driven: one test method runs once per Excel row. */
@Epic("E-commerce Store")
@Feature("Login")
public class LoginDataDrivenTest extends BaseTest {

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return ExcelUtils.getSheetData("src/test/resources/testdata/LoginData.xlsx", "LoginData");
    }

    @Test(dataProvider = "loginData", groups = "regression")
    @Story("Data-driven login (Excel)")
    @Severity(SeverityLevel.CRITICAL)
    public void loginWithMultipleUsers(String username, String password, String expected) {
        LoginPage login = new LoginPage(driver);
        login.login(username, password);

        if (expected.equals("SUCCESS")) {
            assertTrue(new ProductsPage(driver).isProductsPageDisplayed(),
                    "Login should succeed for " + username);
        } else {
            String actual = login.getErrorMessage();
            assertTrue(actual.contains(expected),
                    "Expected error containing [" + expected + "] but got [" + actual + "]");
        }
    }
}
