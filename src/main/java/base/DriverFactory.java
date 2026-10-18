package base;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import utils.ConfigReader;

/**
 * Creates and stores the WebDriver. ThreadLocal makes it safe for parallel runs:
 * every thread gets its own browser.
 * Selenium 4.6+ downloads the right driver automatically (Selenium Manager).
 */
public class DriverFactory {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initDriver() {
        String browser = ConfigReader.get("browser").toLowerCase();
        boolean headless = Boolean.parseBoolean(ConfigReader.get("headless"));

        WebDriver wd;
        switch (browser) {
            case "firefox":
                FirefoxOptions ff = new FirefoxOptions();
                if (headless) ff.addArguments("-headless");
                wd = new FirefoxDriver(ff);
                break;
            case "edge":
                EdgeOptions edge = new EdgeOptions();
                if (headless) edge.addArguments("--headless=new");
                wd = new EdgeDriver(edge);
                break;
            default:
                ChromeOptions chrome = new ChromeOptions();
                if (headless) {
                    chrome.addArguments("--headless=new");
                }
                chrome.addArguments("--window-size=1920,1080", "--disable-notifications",
                        "--no-sandbox", "--disable-dev-shm-usage");
                // Stops Chrome's "change your password" popup on saucedemo
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("profile.password_manager_leak_detection", false);
                chrome.setExperimentalOption("prefs", prefs);
                wd = new ChromeDriver(chrome);
        }
        wd.manage().window().maximize();
        driver.set(wd);
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
