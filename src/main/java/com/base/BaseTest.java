package com.base;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.report.ExtentTestManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class BaseTest {
    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);

    public BaseTest() {
    }

    @DataProvider(
            name = "testDataProvider"
    )
    public static Object[] testDataProvider(Method method) throws Exception {
        List<Map<String, String>> controllerRowsList = ExcelManager.getControllerRowsList();
        List<Map<String, String>> rowMapList = (List)controllerRowsList.stream().filter((rowMap) -> {
            return method.getName().equals(rowMap.get("TestMethodName"));
        }).collect(Collectors.toList());
        Map<String, String> controllerRowMap = (Map)rowMapList.get(0);
        String testDataSheetName = (String)controllerRowMap.get("Data_Sheet");
        LOGGER.debug("Will read from sheet [{}] for test method [{}]", testDataSheetName, method.getName());
        List<Map<String, String>> excelDataRowsAsListOfMap = ExcelManager.getExcelRowsAsListOfMap(Constants.RUN_MANAGER_WORKBOOK.toString(), testDataSheetName, method.getName());
        return excelDataRowsAsListOfMap.toArray(new Object[0]);
    }

    public DesiredCapabilities addCapabilities(String testName) throws Exception {
        return null;
    }

    @BeforeMethod(
            description = "Set Up",
            alwaysRun = true
    )
    protected void setUp(ITestResult result, ITestContext context, Object[] objects) throws Exception {

        Map<String, String> controllerRowMap = ExcelManager.getControllerRowMapByTestMethodName(result.getMethod().getMethodName());
        ExtentTestManager.startTest(result.getMethod().getMethodName(), (String)controllerRowMap.get("Description"));
        ExtentTest extentTest = ExtentTestManager.getTest();
        extentTest.info("Test Started");
        extentTest.assignCategory(new String[]{(String)controllerRowMap.get("Data_Sheet")});
        LOGGER.info("Executing test method [{}]", result.getMethod().getMethodName());
        String tcType = (String)controllerRowMap.get("TC_TYPE");
        context.setAttribute("TC_TYPE", tcType);
        if (!tcType.equalsIgnoreCase("web") && !tcType.equalsIgnoreCase("web-api")) {
            if (tcType.equalsIgnoreCase("mobile")) {
                LOGGER.info("Current Test_Type is Mobile. Will instantiate mobile driver");
                MobileDriverManager.startAppium();
                MobileDriverManager.initDriver(this.addCapabilities(result.getMethod().getMethodName()));
            }
        } else {
            extentTest.info("Browser : " + ConfigManager.getBrowser());
            LOGGER.info("Current Test_Type is either web or web-api. Will instantiate web driver");
            DriverManager.initDriver(Browser.valueOf(ConfigManager.getBrowser()), this.addCapabilities(result.getMethod().getMethodName()));
        }

    }

    @AfterMethod(
            description = "Tear Down",
            alwaysRun = true
    )
    protected void tearDown(ITestResult result, ITestContext context) {
        if (result.getStatus() == 2) {
            if (DriverManager.getDriver() != null) {
                String base64Screenshot = (String)((TakesScreenshot)Objects.requireNonNull(DriverManager.getDriver())).getScreenshotAs(OutputType.BASE64);
                ExtentTestManager.getTest().fail(result.getThrowable(), MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
            } else {
                ExtentTestManager.getTest().fail(result.getThrowable());
                ExtentTestManager.getTest().fail("Test Failed");
            }

            LOGGER.info("Test method [{}] Failed", result.getMethod().getMethodName());
        } else if (result.getStatus() == 1) {
            LOGGER.info("Test method [{}] Passed", result.getMethod().getMethodName());
            ExtentTestManager.getTest().pass("Test Passed");
        } else if (result.getStatus() == 3) {
            LOGGER.info("Test method [{}] Skipped", result.getMethod().getMethodName());
            ExtentTestManager.getTest().skip("Test Skipped");
        } else {
            ExtentTestManager.getTest().fail("Test Failed");
            if (result.getThrowable() != null) {
                ExtentTestManager.getTest().fail(result.getThrowable());
            }
        }

        if (context.getAttribute("TC_TYPE").toString().equalsIgnoreCase("mobile")) {
            MobileDriverManager.quitDriver();
            MobileDriverManager.stopAppium();
        } else if (ConfigManager.getBrowserTearDownSettings()) {
            DriverManager.quitDriver();
        } else {
            LOGGER.info("Browser teardown skipped.");
        }

        ExcelManager.writeTestStatusToExcel(result);
    }
}
