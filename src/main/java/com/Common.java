package com;

import com.base.Actions;
import com.base.DriverManager;
import com.utils.Helper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class Common {
    public static final By LOADER_IMAGE = By.xpath("//img[contains(@class, 'loader-img')]");

    public static void waitForLoaderImgToDisappear() {
        if(Actions.checkIfWebElementExists(LOADER_IMAGE)) {
            Actions.waitForElementInvisibilityAtIntervals(LOADER_IMAGE,2,30);
        }
    }
    public static void moveMouseToElement(By by, String message) {
        WebDriver driver = DriverManager.getDriver();
        org.openqa.selenium.interactions.Actions action = new org.openqa.selenium.interactions.Actions(driver);
        action.moveToElement(DriverManager.getDriver().findElement(by)).perform();
        Helper.log(message);
    }

    public static List<String> getMultipleElementsText(By by) {
        List<String> texts = new ArrayList<>();
        List<WebElement> allElements = Actions.getWebElements(by);
        for(WebElement element:allElements) {
            texts.add(Actions.getText(element).trim());
        }
        return texts;
    }

    public static WebElement getWebElement(By by) {
        return DriverManager.getDriver().findElement(by);
    }

    public static void navigateBack() {
        DriverManager.getDriver().navigate().back();
        Actions.sleep(1);
    }

    public static void switchToChildWindowAndClose(String message) {
        String mainWindowHandle = DriverManager.getDriver().getWindowHandle();
        Set<String> allWindowHandles = DriverManager.getDriver().getWindowHandles();
        assertThat(allWindowHandles.size())
                .as("Application is opened in new window")
                .isGreaterThan(1);
        Iterator var4 = allWindowHandles.iterator();

        while(var4.hasNext()) {
            String child = (String)var4.next();
            if (!mainWindowHandle.equalsIgnoreCase(child)) {
                DriverManager.getDriver().switchTo().window(child);
                Helper.log(message);
                Actions.sleep(2);
                Actions.takeScreenshot();
                DriverManager.getDriver().close();
                DriverManager.getDriver().switchTo().window(mainWindowHandle);
            }
        }
    }

}
