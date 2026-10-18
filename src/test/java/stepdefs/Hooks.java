package stepdefs;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import base.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/** Runs before/after EVERY Cucumber scenario. */
public class Hooks {

    @Before
    public void setUp() {
        DriverFactory.initDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverFactory.getDriver();
        if (scenario.isFailed() && driver != null) {
            byte[] shot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(shot, "image/png", "Failure screenshot");
        }
        DriverFactory.quitDriver();
    }
}
