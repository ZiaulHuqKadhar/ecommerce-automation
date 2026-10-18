package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import base.DriverFactory;

/** Logs test start/pass/fail and takes a screenshot when a UI test fails. */
public class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        log.info("START : {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("PASS  : {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("FAIL  : {} -> {}", result.getMethod().getMethodName(), result.getThrowable());
        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            try {
                String path = ScreenshotUtils.capture(driver, result.getMethod().getMethodName());
                log.info("Screenshot saved: {}", path);
            } catch (Exception e) {
                log.warn("Screenshot failed: {}", e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("SKIP  : {}", result.getMethod().getMethodName());
    }
}
