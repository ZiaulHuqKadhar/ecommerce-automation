package utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import io.qameta.allure.Allure;

public class ScreenshotUtils {

    /** Saves a PNG to target/screenshots AND attaches it to the Allure report. */
    public static String capture(WebDriver driver, String testName) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            Path dest = Paths.get("target", "screenshots", testName + "_" + time + ".png");
            Files.createDirectories(dest.getParent());
            Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);

            Allure.addAttachment("Failure screenshot - " + testName, "image/png",
                    new ByteArrayInputStream(Files.readAllBytes(dest)), "png");
            return dest.toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not save screenshot", e);
        }
    }
}
