# E-commerce Hybrid Automation Framework

UI (Selenium + TestNG + POM + Cucumber) and API (RestAssured) automation with Jenkins CI/CD.

| Layer | Tech |
|---|---|
| Language / Build | Java 17, Maven |
| UI | Selenium 4, TestNG, Page Object Model |
| BDD | Cucumber 7 (TestNG runner) |
| Data-driven | TestNG @DataProvider + Excel (Apache POI), Cucumber Scenario Outline |
| API | RestAssured, Jackson POJOs |
| Logging / Reports | Log4j2, Allure (graphs, steps, trends), Surefire, failure screenshots |
| CI/CD | Jenkins (nightly cron + SCM polling) |

Applications under test:
- UI: https://www.saucedemo.com
- API: https://restful-booker.herokuapp.com

## Run

```
mvn clean test                                   # everything (UI + API + BDD)
mvn test -Dheadless=true                         # no browser window
mvn test -Dbrowser=firefox                       # other browser
mvn test -DsuiteXmlFile=testng-api.xml           # only API tests
mvn test -Dgroups=smoke                          # only smoke tests (TestNG tests)
mvn test -Dcucumber.filter.tags="@smoke"         # only smoke Cucumber scenarios
```

Reports:
```
mvn allure:serve        # builds and opens the Allure report in your browser
```
Raw results: `target/allure-results`. Also `target/surefire-reports`, `target/cucumber-reports/cucumber.html`, `target/screenshots`, `logs/automation.log`.

## Architecture

```
Test class -> Page object -> BasePage (waits) -> WebDriver (ThreadLocal, DriverFactory)
Test class -> BookingApi -> RestAssured -> POJOs (Booking, BookingResponse)
Cucumber feature -> UiSteps -> same Page objects
```
