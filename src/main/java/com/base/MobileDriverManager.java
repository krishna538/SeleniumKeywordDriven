//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.base;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class MobileDriverManager {
    private static final Logger LOGGER = LogManager.getLogger(MobileDriverManager.class);
    private static final ThreadLocal<AndroidDriver<MobileElement>> WEB_DRIVER_THREAD_LOCAL = new ThreadLocal();
    public static URL appiumUrl;
    private static AppiumDriverLocalService appiumDriverLocalService;

    private MobileDriverManager() {
    }

    public static AndroidDriver<MobileElement> getDriver() {
        return (AndroidDriver)WEB_DRIVER_THREAD_LOCAL.get();
    }

    private static void setDriver(AndroidDriver<MobileElement> androidDriver) {
        WEB_DRIVER_THREAD_LOCAL.set(androidDriver);
    }

    public static void quitDriver() {
        if (WEB_DRIVER_THREAD_LOCAL.get() != null) {
            ((AndroidDriver)WEB_DRIVER_THREAD_LOCAL.get()).quit();
        }

    }

    public static void initDriver(DesiredCapabilities userProvidedCapabilities) {
        try {
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            if (userProvidedCapabilities != null) {
                LOGGER.info("Merging user provided selenium capabilities");
                desiredCapabilities.merge(userProvidedCapabilities);
                LOGGER.info(desiredCapabilities);
            } else {
                LOGGER.info("User provided capability is null. Ignoring...");
            }

            AndroidDriver<MobileElement> androidDriver = new AndroidDriver(appiumUrl, desiredCapabilities);
            androidDriver.manage().timeouts().implicitlyWait(30L, TimeUnit.SECONDS);
            setDriver(androidDriver);
            LOGGER.info("Android Driver successfully initialized. Session id : [{}]", androidDriver.getSessionId());
        } catch (Exception var3) {
            LOGGER.error(var3);
            throw new RuntimeException(var3);
        }
    }

    public static void startAppium() {
        appiumDriverLocalService = AppiumDriverLocalService.buildDefaultService();
        appiumDriverLocalService.start();
    }

    public static void stopAppium() {
        appiumDriverLocalService.stop();
    }
}
