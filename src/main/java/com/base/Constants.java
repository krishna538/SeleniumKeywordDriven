package com.base;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {
    public static final String CURRENT_DIR = System.getProperty("user.dir");
    public static final String TEST_RESOURCES_DIR;
    public static final Path RUN_MANAGER_WORKBOOK;
    public static final String CONTROLLER_SHEET_NAME = "Controller";
    public static final String SETTINGS_SHEET_NAME = "Settings";
    public static final String APP_DIR;

    public Constants() {
    }

    static {
        TEST_RESOURCES_DIR = Paths.get(CURRENT_DIR, "src", "test", "resources").toString();
        RUN_MANAGER_WORKBOOK = Paths.get(TEST_RESOURCES_DIR, ConfigManager.getConfigProperty("run.manager.workbook.name"));
        APP_DIR = Paths.get(CURRENT_DIR, "app").toString();
    }
}
