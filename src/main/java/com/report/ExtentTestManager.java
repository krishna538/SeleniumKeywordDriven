package com.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import java.util.HashMap;
import java.util.Map;

public class ExtentTestManager {
    private static final Map<Integer, ExtentTest> extentTestMap = new HashMap();
    private static final ExtentReports extent = ExtentManager.getExtentReports();

    public ExtentTestManager() {
    }

    public static synchronized ExtentTest getTest() {
        return (ExtentTest)extentTestMap.get((int)Thread.currentThread().getId());
    }

    public static synchronized ExtentTest startTest(String testName, String desc) {
        ExtentTest test = extent.createTest(testName, desc);
        extentTestMap.put((int)Thread.currentThread().getId(), test);
        return test;
    }
}
