package com.base;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.utils.ConfigManager;
import com.github.javafaker.Faker;
import com.report.ExtentTestManager;
import com.utils.Helper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class Actions {
    private static final Logger LOGGER = LogManager.getLogger(Actions.class);

    public Actions() {
    }

    public static void takeScreenshot() {
        String base64Screenshot = (String)((TakesScreenshot)Objects.requireNonNull(DriverManager.getDriver())).getScreenshotAs(OutputType.BASE64);
        ExtentTestManager.getTest().info("Screenshot", MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
    }

    public static void sleep(int sleepInSeconds) {
        try {
            LOGGER.info("Waiting for {} Seconds", sleepInSeconds);
            Thread.sleep((long)sleepInSeconds * 1000L);
        } catch (Exception var2) {
        }

    }

    public static void openUrl(String url) {
        openUrl(url, "Opened url : " + url);
    }

    public static void openUrl(String url, String message) {
        DriverManager.getDriver().get(url);
        Helper.log(message);
    }

    public static void clearText(By by) {
        clearText(by, "Cleared : " + by.toString());
    }

    public static void clearText(By by, String message) {
        clearText(find(by), message);
    }

    public static void clearText(WebElement webElement, String message) {
        webElement.clear();
        Helper.log(message);
    }

    public static WebElement find(By by) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), (long)Integer.parseInt(ConfigManager.getConfigProperty("explicit.wait.time")));
        return (WebElement)wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    public static WebElement find(WebElement webElement) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), (long)Integer.parseInt(ConfigManager.getConfigProperty("explicit.wait.time")));
        return (WebElement)wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }

    public static void click(By by) {
        click(by, "Clicked : " + by.toString());
    }

    public static void click(By by, String message) {
        click(find(by), message);
    }

    public static void click(WebElement webElement, String message) {
        webElement.click();
        Helper.log(message);
    }

    public static void waitUntilClickable(By by) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), (long)Integer.parseInt(ConfigManager.getConfigProperty("explicit.wait.time")));
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    public static void waitUntilClickable(WebElement webElement) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), (long)Integer.parseInt(ConfigManager.getConfigProperty("explicit.wait.time")));
        wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }

    public static boolean checkIfWebElementExists(By by) {
        try {
            if (DriverManager.getDriver().findElement(by).isDisplayed()) {
                LOGGER.info("WebElement [{}] found", by.toString());
                return true;
            } else {
                return false;
            }
        } catch (Exception var2) {
            return false;
        }
    }

    public static boolean checkIfWebElementExists(By by, String message) {
        try {
            if (DriverManager.getDriver().findElement(by).isDisplayed()) {
                LOGGER.info("WebElement [{}] found", by.toString());
                Helper.log(message);
                return true;
            } else {
                return false;
            }
        } catch (Exception var3) {
            return false;
        }
    }

    public static boolean checkIfWebElementExists(WebElement webelement) {
        try {
            return webelement.isDisplayed();
        } catch (Exception var2) {
            return false;
        }
    }

    public static boolean waitForElementAtIntervals(By by, int interval, int maxWait) {
        boolean elementExists = false;

        try {
            int initialWait = 0;

            while(initialWait <= maxWait) {
                if (checkIfWebElementExists(by)) {
                    elementExists = true;
                    LOGGER.info("Found element [{}] after waiting for [{}]", by.toString(), initialWait);
                    initialWait = maxWait + 1;
                } else {
                    sleep(1);
                    initialWait += interval;
                }
            }

            if (!elementExists) {
                LOGGER.info("WebElement [{}] not found", by.toString());
            }
        } catch (Exception var5) {
        }

        return elementExists;
    }

    public static boolean waitForElementInvisibilityAtIntervals(By by, int interval, int maxWait) {
        boolean elementExists = false;

        try {
            for(int initialWait = 0; initialWait <= maxWait && checkIfWebElementExists(by); ++initialWait) {
                sleep(interval);
            }
        } catch (Exception var5) {
        }

        return elementExists;
    }

    public static boolean waitForElementAtIntervalsAndClick(By by, int interval, int maxTime) {
        boolean flag = waitForElementAtIntervals(by, interval, maxTime);
        if (flag) {
            click(by);
        }

        return flag;
    }

    public static boolean waitForElementAtIntervalsAndClick(By by, int interval, int maxTime, String message) {
        boolean flag = waitForElementAtIntervals(by, interval, maxTime);
        if (flag) {
            click(by);
            Helper.log(message);
        }

        return flag;
    }

    public static void clickByJS(By by) {
        clickByJS(DriverManager.getDriver().findElement(by), "Clicked web element by JS : " + by.toString());
    }

    public static void clickByJS(WebElement webElement, String message) {
        JavascriptExecutor executor = (JavascriptExecutor)DriverManager.getDriver();
        executor.executeScript("arguments[0].click();", new Object[]{webElement});
        Helper.log(message);
    }

    public static void enterText(By by, String value) {
        enterText(find(by), value, String.format("Entered text [%s] in webElement [%s]", value, by.toString()));
    }

    public static void enterText(By by, String value, String message) {
        enterText(find(by), value, message);
    }

    public static void enterText(WebElement webElement, String value, String message) {
        webElement.sendKeys(new CharSequence[]{value});
        Helper.log(message);
    }

    public static String getText(By by) {
        return find(by).getText();
    }

    public static String getAttribute(By by, String attributeName) {
        return DriverManager.getDriver().findElement(by).getAttribute(attributeName);
    }

    public static String getText(WebElement webElement) {
        return webElement.getText();
    }

    public static void scrollDown(By by) {
        sleep(1);
        scrollDown(DriverManager.getDriver().findElement(by), "scrolldown to web element by JS : " + by.toString());
    }

    public static void scrollDown(WebElement webElement, String message) {
        JavascriptExecutor executor = (JavascriptExecutor)DriverManager.getDriver();
        executor.executeScript("arguments[0].scrollIntoView();", new Object[]{webElement});
        Helper.log(message);
    }

    public static void scrollToElement(WebElement webElement, String message) {
        JavascriptExecutor executor = (JavascriptExecutor)DriverManager.getDriver();
        executor.executeScript("arguments[0].scrollIntoView();", new Object[]{webElement});
        Helper.log(message);
    }

    public static void scrollUp(By by) {
        sleep(1);
        scrollDown(DriverManager.getDriver().findElement(by), "scrolldown to web element by JS : " + by.toString());
    }

    public static Object executeJs(String javaScript) {
        JavascriptExecutor executor = (JavascriptExecutor)DriverManager.getDriver();
        return executor.executeScript(javaScript, new Object[0]);
    }

    public static Response getRequest(String url, RequestSpecBuilder requestSpecBuilder) {
        return getRequest(url, requestSpecBuilder, RestAssuredConfig.newConfig());
    }

    public static Response postRequest(String url, RequestSpecBuilder requestSpecBuilder) {
        return postRequest(url, requestSpecBuilder, RestAssuredConfig.newConfig());
    }

    public static Response putRequest(String url, RequestSpecBuilder requestSpecBuilder) {
        return putRequest(url, requestSpecBuilder, RestAssuredConfig.newConfig());
    }

    public static Response deleteRequest(String url, RequestSpecBuilder requestSpecBuilder) {
        return deleteRequest(url, requestSpecBuilder, RestAssuredConfig.newConfig());
    }

    public static Response optionsRequest(String url, RequestSpecBuilder requestSpecBuilder) {
        return optionsRequest(url, requestSpecBuilder, RestAssuredConfig.newConfig());
    }

    public static Response patchRequest(String url, RequestSpecBuilder requestSpecBuilder) {
        return getRequest(url, requestSpecBuilder, RestAssuredConfig.newConfig());
    }

    public static Response soapRequest(String url, RequestSpecBuilder requestSpecBuilder) {
        return soapRequest(url, requestSpecBuilder, RestAssuredConfig.newConfig());
    }

    public static Response getRequest(String url, RequestSpecBuilder requestSpecBuilder, RestAssuredConfig restAssuredConfig) {
        Response response = (Response)((ValidatableResponse)((ValidatableResponse)((ValidatableResponse)((Response)((RequestSpecification)RestAssured.given(requestSpecBuilder.build()).log().all(true)).config(restAssuredConfig).when().get(url, new Object[0])).then()).log().all(true)).and()).extract().response();
        LOGGER.debug("Response {} ", response.asString());
        return response;
    }

    public static Response postRequest(String url, RequestSpecBuilder requestSpecBuilder, RestAssuredConfig restAssuredConfig) {
        Response response = (Response)((ValidatableResponse)((ValidatableResponse)((ValidatableResponse)((Response)((RequestSpecification)RestAssured.given(requestSpecBuilder.build()).log().all(true)).config(restAssuredConfig).when().post(url, new Object[0])).then()).log().all(true)).and()).extract().response();
        LOGGER.debug("Response {} ", response.asString());
        return response;
    }

    public static Response putRequest(String url, RequestSpecBuilder requestSpecBuilder, RestAssuredConfig restAssuredConfig) {
        Response response = (Response)((ValidatableResponse)((ValidatableResponse)((ValidatableResponse)((Response)((RequestSpecification)RestAssured.given(requestSpecBuilder.build()).log().all(true)).config(restAssuredConfig).when().put(url, new Object[0])).then()).log().all(true)).and()).extract().response();
        LOGGER.debug("Response {} ", response.asString());
        return response;
    }

    public static Response deleteRequest(String url, RequestSpecBuilder requestSpecBuilder, RestAssuredConfig restAssuredConfig) {
        Response response = (Response)((ValidatableResponse)((ValidatableResponse)((ValidatableResponse)((Response)((RequestSpecification)RestAssured.given(requestSpecBuilder.build()).log().all(true)).config(restAssuredConfig).when().delete(url, new Object[0])).then()).log().all(true)).and()).extract().response();
        LOGGER.debug("Response {} ", response.asString());
        return response;
    }

    public static Response optionsRequest(String url, RequestSpecBuilder requestSpecBuilder, RestAssuredConfig restAssuredConfig) {
        Response response = (Response)((ValidatableResponse)((ValidatableResponse)((ValidatableResponse)((Response)((RequestSpecification)RestAssured.given(requestSpecBuilder.build()).log().all(true)).config(restAssuredConfig).when().options(url, new Object[0])).then()).log().all(true)).and()).extract().response();
        LOGGER.debug("Response {} ", response.asString());
        return response;
    }

    public static Response patchRequest(String url, RequestSpecBuilder requestSpecBuilder, RestAssuredConfig restAssuredConfig) {
        Response response = (Response)((ValidatableResponse)((ValidatableResponse)((ValidatableResponse)((Response)((RequestSpecification)RestAssured.given(requestSpecBuilder.build()).log().all(true)).config(restAssuredConfig).when().patch(url, new Object[0])).then()).log().all(true)).and()).extract().response();
        LOGGER.debug("Response {} ", response.asString());
        return response;
    }

    public static Response soapRequest(String url, RequestSpecBuilder requestSpecBuilder, RestAssuredConfig restAssuredConfig) {
        Response response = (Response)((ValidatableResponse)((ValidatableResponse)((ValidatableResponse)((Response)((RequestSpecification)RestAssured.given(requestSpecBuilder.build()).log().all(true)).config(restAssuredConfig).when().post(url, new Object[0])).then()).log().all(true)).and()).extract().response();
        LOGGER.debug("Response {} ", response.asString());
        return response;
    }

    public static void validateResponseIsNotNull(Response response) {
        Assert.assertNotNull(response);
    }

    public static void validateApiResponseStatusCode(Response response, int expectedCode) {
        Assert.assertEquals(response.getStatusCode(), expectedCode);
    }

    public static void acceptAlert() {
        Alert alert = MobileDriverManager.getDriver().switchTo().alert();
        alert.accept();
    }

    public static void clearText(WebElement webElement) {
        webElement.clear();
    }

    public static String getAttribute(By by) {
        return find(by).getAttribute("value");
    }

    public static Response getRequestWithBasicAuth(String url, RequestSpecBuilder requestSpecBuilder, RestAssuredConfig restAssuredConfig, String UID, String PWD) {
        Response response = (Response)((ValidatableResponse)((ValidatableResponse)((ValidatableResponse)((Response)((RequestSpecification)RestAssured.given(requestSpecBuilder.build()).auth().basic(UID, PWD).log().all(true)).config(restAssuredConfig).when().get(url, new Object[0])).then()).log().all(true)).and()).extract().response();
        LOGGER.debug("Response {} ", response.asString());
        return response;
    }

    public static Response getRequestSNMMessageWithQueryParams(String url, RequestSpecBuilder requestSpecBuilder, RestAssuredConfig restAssuredConfig, String UID, String PWD, String entityId, String entityType) {
        String date = getSystemDateYYYMMDD();
        Response response = (Response)((ValidatableResponse)((ValidatableResponse)((ValidatableResponse)((Response)((RequestSpecification)RestAssured.given(requestSpecBuilder.build()).auth().basic(UID, PWD).queryParam("entityType", new Object[]{"PART"}).queryParam("entityId", new Object[]{entityId}).queryParam("startDate", new Object[]{date}).queryParam("endDate", new Object[]{date}).queryParam("eventTypes", new Object[]{entityType}).log().all(true)).config(restAssuredConfig).when().get(url, new Object[0])).then()).log().all(true)).and()).extract().response();
        LOGGER.debug("Response {} ", response.asString());
        return response;
    }

    public static Response getRequestJobStatus(String url, RequestSpecBuilder requestSpecBuilder, RestAssuredConfig restAssuredConfig, String UID, String PWD) {
        String date = getSystemDateYYYMMDD();
        Response response = (Response)((ValidatableResponse)((ValidatableResponse)((ValidatableResponse)((Response)((RequestSpecification)RestAssured.given(requestSpecBuilder.build()).auth().basic(UID, PWD).log().all(true)).config(restAssuredConfig).when().get(url, new Object[0])).then()).log().all(true)).and()).extract().response();
        LOGGER.debug("Response {} ", response.asString());
        return response;
    }

    public static Response postRequestWithBasicAuth(String url, RequestSpecBuilder requestSpecBuilder, RestAssuredConfig restAssuredConfig, String UID, String PWD) {
        Response response = (Response)((ValidatableResponse)((ValidatableResponse)((ValidatableResponse)((Response)((RequestSpecification)RestAssured.given(requestSpecBuilder.build()).auth().basic(UID, PWD).log().all(true)).config(restAssuredConfig).when().post(url, new Object[0])).then()).log().all(true)).and()).extract().response();
        LOGGER.debug("Response {} ", response.asString());
        return response;
    }

    public static void selectFromListByValue(WebElement select, WebElement options, String value, String message) {
        Select list = new Select(select);
        list.selectByVisibleText(value);
        Helper.log(message);
    }

    public static void waitUntilElementIsNotVisible(By by) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), 200L);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
        sleep(2);
    }

    public static boolean isElementVisible(By by) {
        return isElementVisible(by, "Check if element: " + by.toString() + " is exist");
    }

    public static boolean isElementVisible(By by, String message) {
        return isElementVisible(find(by), message);
    }

    public static boolean isElementVisible(WebElement webElement, String message) {
        return webElement.isDisplayed();
    }

    public static void clickElementWithVisibleText(By by, String value) {
        clickElementWithVisibleText(by, value, "Clicked : " + by);
    }

    public static void clickElementWithVisibleText(By by, String value, String message) {
        String oldXpath = by.toString().substring(10);
        String xpath = oldXpath + "[text()=\"" + value + "\"]";
        waitUntilClickable(By.xpath(xpath));
        WebDriver driver = DriverManager.getDriver();
        org.openqa.selenium.interactions.Actions action = new org.openqa.selenium.interactions.Actions(driver);
        action.moveToElement(DriverManager.getDriver().findElement(By.xpath(xpath))).click().build().perform();
        Helper.log(message);
    }

    public static void writeAtEndOfExistingText(By by, String value) {
        writeAtEndOfExistingText(by, value, String.format("Entered text [%s] in webElement [%s]", value, by.toString()));
    }

    public static void writeAtEndOfExistingText(By by, String value, String message) {
        writeAtEndOfExistingText(find(by), value, message);
    }

    public static void writeAtEndOfExistingText(WebElement webElement, String value, String message) {
        webElement.sendKeys(new CharSequence[]{Keys.END, value});
        Helper.log(message);
    }

    public static void selectFromListByValue(By by, String value) {
        selectFromListByValue(by, value, String.format("Select value [%s] from list [%s]", value, by.toString()));
    }

    public static void selectFromListByValue(By by, String value, String message) {
        selectFromListByValue(find(by), value, message);
    }

    public static void selectFromListByValue(WebElement webElement, String value, String message) {
        Select select = new Select(webElement);
        select.selectByValue(value);
        LOGGER.info(message);
    }

    public static void selectFromListByIndex(By by, int index) {
        selectFromListByIndex(by, index, String.format("Select index [%s] from list [%s]", index, by.toString()));
    }

    public static void selectFromListByIndex(By by, int index, String message) {
        selectFromListByIndex(find(by), index, message);
    }

    public static void selectFromListByIndex(WebElement webElement, int index, String message) {
        Select select = new Select(webElement);
        select.selectByIndex(index);
        LOGGER.info(message);
    }

    public static void selectFromListByVisibleText(By by, String value) {
        selectFromListByVisibleText(by, value, String.format("Select text [%s] from list [%s]", value, by.toString()));
    }

    public static void selectFromListByVisibleText(By by, String value, String message) {
        selectFromListByVisibleText(find(by), value, message);
    }

    public static void selectFromListByVisibleText(WebElement webElement, String value, String message) {
        Select select = new Select(webElement);
        select.selectByVisibleText(value);
        LOGGER.info(message);
    }

    public static void deSelectFromListByValue(By by, String value) {
        deSelectFromListByValue(by, value, String.format("Select value [%s] from list [%s]", value, by.toString()));
    }

    public static void deSelectFromListByValue(By by, String value, String message) {
        deSelectFromListByValue(find(by), value, message);
    }

    public static void deSelectFromListByValue(WebElement webElement, String value, String message) {
        Select select = new Select(webElement);
        select.selectByValue(value);
        LOGGER.info(message);
    }

    public static void deSelectFromListByIndex(By by, int index) {
        deSelectFromListByIndex(by, index, String.format("Select index [%s] from list [%s]", index, by.toString()));
    }

    public static void deSelectFromListByIndex(By by, int index, String message) {
        deSelectFromListByIndex(find(by), index, message);
    }

    public static void deSelectFromListByIndex(WebElement webElement, int index, String message) {
        Select select = new Select(webElement);
        select.deselectByIndex(index);
        LOGGER.info(message);
    }

    public static void deSelectFromListByVisibleText(By by, String value) {
        deSelectFromListByVisibleText(by, value, String.format("Select text [%s] from list [%s]", value, by.toString()));
    }

    public static void deSelectFromListByVisibleText(By by, String value, String message) {
        deSelectFromListByVisibleText(find(by), value, message);
    }

    public static void deSelectFromListByVisibleText(WebElement webElement, String value, String message) {
        Select select = new Select(webElement);
        select.deselectByVisibleText(value);
        LOGGER.info(message);
    }

    public static void deSelectAllinList(By by) {
        deSelectAllinList(by, String.format("Deselect all selected items in list: [%s]", by.toString()));
    }

    public static void deSelectAllinList(By by, String message) {
        deSelectAllinList(find(by), message);
    }

    public static void deSelectAllinList(WebElement webElement, String message) {
        Select select = new Select(webElement);
        select.deselectAll();
        LOGGER.info(message);
    }

    public static boolean isMultipleChoices(By by) {
        return isMultipleChoices(by, String.format("Check if list [%s] is multiple: ", by.toString()));
    }

    public static boolean isMultipleChoices(By by, String message) {
        return isMultipleChoices(find(by), message);
    }

    public static boolean isMultipleChoices(WebElement webElement, String message) {
        Select select = new Select(webElement);
        boolean result = select.isMultiple();
        LOGGER.info(message + result);
        return result;
    }

    public static void getListOptions(By by) {
        getListOptions(by, String.format("Get all options in list: [%s]", by.toString()));
    }

    public static void getListOptions(By by, String message) {
        getListOptions(find(by), message);
    }

    public static List<String> getListOptions(WebElement webElement, String message) {
        List<String> options = new ArrayList();
        List<WebElement> elements = (new Select(webElement)).getOptions();
        Iterator var4 = elements.iterator();

        while(var4.hasNext()) {
            WebElement element = (WebElement)var4.next();
            options.add(element.getText());
        }

        LOGGER.info(message);
        return options;
    }

    public static void getAllSelectedOptionsFromList(By by) {
        getAllSelectedOptionsFromList(by, String.format("Get all selected options in list: [%s]", by.toString()));
    }

    public static void getAllSelectedOptionsFromList(By by, String message) {
        getAllSelectedOptionsFromList(find(by), message);
    }

    public static List<String> getAllSelectedOptionsFromList(WebElement webElement, String message) {
        List<String> options = new ArrayList();
        List<WebElement> elements = (new Select(webElement)).getAllSelectedOptions();
        Iterator var4 = elements.iterator();

        while(var4.hasNext()) {
            WebElement element = (WebElement)var4.next();
            options.add(element.getText());
        }

        LOGGER.info(message);
        return options;
    }

    public static void switchToWindow(By by) {
        switchToWindow(by, String.format("Switch Window to window with locator:[%s]", by.toString()));
    }

    public static void switchToWindow(By by, String message) {
        switchToWindow(find(by), message);
    }

    public static void switchToWindow(WebElement webElement, String message) {
        String mainWindowHandle = DriverManager.getDriver().getWindowHandle();
        Set<String> allWindowHandles = DriverManager.getDriver().getWindowHandles();
        Iterator var4 = allWindowHandles.iterator();

        while(var4.hasNext()) {
            String child = (String)var4.next();
            if (!mainWindowHandle.equalsIgnoreCase(child)) {
                DriverManager.getDriver().switchTo().window(child);
                LOGGER.info(message);
                LOGGER.info("Switched to window with heading" + getText(webElement));
            }
        }

    }

    public static void DoubleClick(By by) {
        DoubleClick(find(by), String.format("Double click on: ", by.toString()));
    }

    public static void DoubleClick(By by, String message) {
        DoubleClick(find(by), message);
    }

    public static void DoubleClick(WebElement webElement, String message) {
        WebDriver driver = DriverManager.getDriver();
        org.openqa.selenium.interactions.Actions bulider = new org.openqa.selenium.interactions.Actions(driver);
        bulider.moveToElement(webElement).doubleClick().build().perform();
        Helper.log(message);
    }

    public static int getElementCount(By by) {
        return getElementCount(by, String.format("Get Count of Element: %s", by.toString()));
    }

    public static int getElementCount(By by, String message) {
        Helper.log(message);
        return DriverManager.getDriver().findElements(by).size();
    }

    public static RestAssuredConfig keyStoreConfig(String certPath, String certPassword) {
        SSLSocketFactory socketFactory = null;
        String jksPath = certPath;
        String jksPassword = certPassword;

        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(jksPath), jksPassword.toCharArray());
            socketFactory = new SSLSocketFactory(keyStore, jksPassword);
        } catch (Exception var7) {
            Helper.log(var7.getLocalizedMessage());
        }

        return RestAssured.config().sslConfig((new SSLConfig()).allowAllHostnames().relaxedHTTPSValidation().sslSocketFactory(socketFactory));
    }

    public static String getSystemDateMMDDYYYHHMMSS() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Riyadh"));
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String FutureDays(int days) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        Calendar c = Calendar.getInstance();
        c.add(5, days);
        System.out.println(dateFormat.format(c.getTime()));
        return dateFormat.format(c.getTime());
    }

    public static String getSystemDateMMDDYYYHHMM() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Riyadh"));
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getSystemDateYYYMMDD() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Riyadh"));
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getFutureDateMMDDYYY() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar c = Calendar.getInstance();
        c.add(5, 6);
        System.out.println(dateFormat.format(c.getTime()));
        return dateFormat.format(c.getTime());
    }

    public static void ScrollDownWebPage() {
        WebDriver driver = DriverManager.getDriver();
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("window.scrollBy(0,500)", new Object[0]);
    }

    public static String generateRandomUserName() {
        Faker fk = new Faker();
        String userName = fk.letterify("????????");
        return userName;
    }

    public static String generateRandomPass() {
        Random rand = new SecureRandom();
        char[] allChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*[]{}()?-<>_:;~".toCharArray();
        char[] upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char[] lower = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        char[] numbers = "0123456789".toCharArray();
        char[] special = "!@#$%^&*[]{}()?-<>_:;~".toCharArray();
        char[] password = new char[12];
        password[0] = upper[rand.nextInt(upper.length)];
        password[1] = lower[rand.nextInt(lower.length)];
        password[2] = numbers[rand.nextInt(numbers.length)];
        password[3] = special[rand.nextInt(special.length)];

        for(int i = 4; i <= 11; ++i) {
            password[i] = allChars[rand.nextInt(allChars.length)];
        }

        return new String(password);
    }

    public static Map<String, String> getCurrentYear_Date_Month() {
        try {
            Map<String, String> data = new HashMap();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            int currentDay = LocalDateTime.now().getDayOfMonth();
            int Year = LocalDateTime.now().getYear();
            String currentYear = Integer.toString(Year).trim();
            String currentMonth = LocalDateTime.now().getMonth().toString().toLowerCase();
            String Day = Integer.toString(currentDay).trim();
            data.put("year", currentYear);
            data.put("month", currentMonth);
            data.put("day", Day);
            return data;
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }

    public static void openNewTab() {
        JavascriptExecutor jse = (JavascriptExecutor)DriverManager.getDriver();
        jse.executeScript("window.open()", new Object[0]);
        Helper.log("Opened New Tab");
    }

    public static String generate10DigitRandomIDStartsWith1() {
        try {
            Random rand = new Random();
            int number = rand.nextInt(999999999);
            String s = "1" + String.format("%09d", number);
            return s;
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static void sendKeyStroke(By by, Keys key, String message) {
        DriverManager.getDriver().findElement(by).sendKeys(new CharSequence[]{key});
        Helper.log(message);
    }

    public static boolean isWebElementPresent(By by, String message) {
        Helper.log(message);

        try {
            DriverManager.getDriver().findElement(by);
            return true;
        } catch (Exception var3) {
            return false;
        }
    }

    public static List<WebElement> getWebElements(By by) {
        return DriverManager.getDriver().findElements(by);
    }

    public static void selectAllExistingText(By by) {
        selectAllExistingText(find(by));
    }

    public static void selectAllExistingText(WebElement webElement) {
        webElement.sendKeys(new CharSequence[]{Keys.chord(new CharSequence[]{Keys.CONTROL, "a"})});
    }
}
