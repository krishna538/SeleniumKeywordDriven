//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.listeners;

import com.report.ExtentManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.xml.XmlClass;

public class TestListener implements ITestListener {
    private static final Logger LOGGER = LogManager.getLogger(TestListener.class);

    public TestListener() {
    }

    public void onTestStart(ITestResult result) {
        LOGGER.debug("Executing test method : [{}] in class [{}] | Thread id : [{}]", result.getMethod().getMethodName(), result.getTestClass().getName(), Thread.currentThread().getId());
    }

    public void onTestSuccess(ITestResult result) {
        LOGGER.debug("Passed test method : [{}] in class [{}]", result.getMethod().getMethodName(), result.getTestClass().getName());
    }

    public void onTestFailure(ITestResult iTestResult) {
        LOGGER.error("Failed test method : [{}] in class [{}]", iTestResult.getMethod().getMethodName(), iTestResult.getTestClass().getName());
        LOGGER.error(iTestResult.getThrowable());
    }

    public void onTestSkipped(ITestResult result) {
        LOGGER.error("Skipped test method : [{}] in class [{}]", result.getMethod().getMethodName(), result.getTestClass().getName());
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    public void onStart(ITestContext context) {
        LOGGER.debug("Executing test sequence : [{}] from class [{}]", context.getCurrentXmlTest().getName(), ((XmlClass)context.getCurrentXmlTest().getXmlClasses().get(0)).getName());
    }

    public void onFinish(ITestContext context) {
        LOGGER.debug("Finished test sequence : [{}] from class [{}]", context.getCurrentXmlTest().getName(), ((XmlClass)context.getCurrentXmlTest().getXmlClasses().get(0)).getName());
        ExtentManager.getExtentReports().flush();
    }
}
