package com.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {
    int counter = 0;
    int retryLimit = 1;

    public Retry() {
    }

    public boolean retry(ITestResult result) {
        if (this.counter < this.retryLimit) {
            ++this.counter;
            return true;
        } else {
            return false;
        }
    }
}
