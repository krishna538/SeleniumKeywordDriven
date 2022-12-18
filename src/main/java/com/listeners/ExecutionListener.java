package com.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IExecutionListener;

public class ExecutionListener implements IExecutionListener {
    private static final Logger LOGGER = LogManager.getLogger(ExecutionListener.class);

    public ExecutionListener() {
    }

    public void onExecutionStart() {
        LOGGER.info("Start of automated test execution");
    }

    public void onExecutionFinish() {
        LOGGER.info("End of automated test execution");
    }
}
