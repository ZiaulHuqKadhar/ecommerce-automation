package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Entry point for Cucumber. Run only some scenarios with tags:
 *   mvn test -Dcucumber.filter.tags="@smoke"
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepdefs"},
        plugin = {
                "pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json"
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
}
