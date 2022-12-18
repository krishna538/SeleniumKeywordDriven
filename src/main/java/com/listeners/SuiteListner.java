package com.listeners;

import com.utils.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

class SuiteListener implements ISuiteListener {
    private static final Logger LOGGER = LogManager.getLogger(SuiteListener.class);

    public SuiteListener() {
    }

    public void onStart(ISuite suite) {
        LOGGER.debug("Starting test suite [{}]", suite.getName());
        suite.getXmlSuite().setThreadCount(Integer.parseInt(ConfigManager.getConfigProperty("parallel.test.count")));
    }

    public void onFinish(ISuite suite) {
        LOGGER.debug("Finished test suite [{}]", suite.getName());
    }
}
