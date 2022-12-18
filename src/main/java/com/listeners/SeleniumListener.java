//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.listeners;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

public class SeleniumListener extends AbstractWebDriverEventListener {
    private static final Logger LOGGER = LogManager.getLogger(SeleniumListener.class);

    public SeleniumListener() {
    }

    public void beforeAlertAccept(WebDriver driver) {
        LOGGER.info("Trying to accept an alert");
    }

    public void afterAlertAccept(WebDriver driver) {
        LOGGER.info("Accepted an alert");
    }

    public void afterAlertDismiss(WebDriver driver) {
        LOGGER.info("Dismissed an alert");
    }

    public void beforeAlertDismiss(WebDriver driver) {
        LOGGER.info("Trying to dismiss an alert");
    }

    public void beforeNavigateTo(String url, WebDriver driver) {
        LOGGER.debug("Trying to open url : " + url);
    }

    public void afterNavigateTo(String url, WebDriver driver) {
        LOGGER.debug("Opened url : " + url);
    }

    public void beforeNavigateBack(WebDriver driver) {
        LOGGER.info("Trying to navigate back");
    }

    public void afterNavigateBack(WebDriver driver) {
        LOGGER.info("Navigated back");
    }

    public void beforeNavigateForward(WebDriver driver) {
        LOGGER.info(String.format("Trying to navigate forward. Current url : %s", driver.getCurrentUrl()));
    }

    public void afterNavigateForward(WebDriver driver) {
        LOGGER.info(String.format("Navigated forward. Current url : %s", driver.getCurrentUrl()));
    }

    public void beforeNavigateRefresh(WebDriver driver) {
        LOGGER.info(String.format("Trying to refresh the current page. Current url : %s", driver.getCurrentUrl()));
    }

    public void afterNavigateRefresh(WebDriver driver) {
        LOGGER.info(String.format("Refreshed the current page. Current url : %s", driver.getCurrentUrl()));
    }

    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        LOGGER.debug(String.format("Trying to find web element : %s ", by.toString()));
    }

    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        LOGGER.debug(String.format("Found web element : %s ", by.toString()));
    }

    public void beforeClickOn(WebElement element, WebDriver driver) {
        LOGGER.debug(String.format("Trying to click web element : %s ", element.toString()));
    }

    public void afterClickOn(WebElement element, WebDriver driver) {
        LOGGER.debug(String.format("Clicked : %s ", element.toString()));
    }

    public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        LOGGER.debug(String.format("Value before send keys : [{}] for web element : [{}]%s | Value : %s", element.toString(), Arrays.toString(keysToSend)));
    }

    public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        LOGGER.debug("Entered text : [{}] | Web element : [{}]", Arrays.toString(keysToSend), element.toString());
    }

    public void beforeScript(String script, WebDriver driver) {
        LOGGER.debug(String.format("Trying to execute script : %s", script));
    }

    public void afterScript(String script, WebDriver driver) {
        LOGGER.info(String.format("Executed script : %s", script));
    }

    public void afterSwitchToWindow(String windowName, WebDriver driver) {
        LOGGER.info(String.format("Switched to window : %s", windowName));
    }

    public void beforeSwitchToWindow(String windowName, WebDriver driver) {
        LOGGER.info(String.format("Trying to switch to window : %s", windowName));
    }

    public void onException(Throwable throwable, WebDriver driver) {
        LOGGER.error("Exception occurred : ", throwable);
    }

    public <X> void beforeGetScreenshotAs(OutputType<X> target) {
        LOGGER.debug("Trying to take screenshot");
    }

    public <X> void afterGetScreenshotAs(OutputType<X> target, X screenshot) {
        LOGGER.debug("Successfully captured screenshot");
    }

    public void beforeGetText(WebElement element, WebDriver driver) {
        LOGGER.debug("Trying to get text of web element : [{}]", element.toString());
    }

    public void afterGetText(WebElement element, WebDriver driver, String text) {
        LOGGER.info("Captured text : [{}] of web element : [{}]", text, element.toString());
    }
}
