//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.base;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class ConfigManager {
    private static final String TEST_CONFIG_FILE;
    private static final Logger LOGGER;
    private static Map<String, String> configMap;

    private ConfigManager() {
    }

    public static synchronized String getConfigProperty(String key) {
        if (configMap.size() == 0) {
            Properties properties = new Properties();

            try {
                properties.load(new FileInputStream(TEST_CONFIG_FILE));
                configMap = new HashMap((Map)properties.entrySet().stream().collect(Collectors.toMap((e) -> {
                    return e.getKey().toString();
                }, (e) -> {
                    return e.getValue().toString();
                })));
                LOGGER.debug("Loaded config properties : " + TEST_CONFIG_FILE);
            } catch (Exception var3) {
                LOGGER.error(var3);
                throw new RuntimeException(var3);
            }
        }

        return (String)configMap.get(key);
    }

    public static String getBrowser() {
        return (StringUtils.isNotBlank(System.getProperty("browser")) ? System.getProperty("browser") : getConfigProperty("browser")).toUpperCase();
    }

    public static boolean isDriverAutoDownload() {
        return StringUtils.isNotBlank(getConfigProperty("driver.auto.download")) ? Boolean.parseBoolean(getConfigProperty("driver.auto.download")) : Boolean.parseBoolean(System.getProperty("driver.auto.download", "false"));
    }

    public static boolean getBrowserTearDownSettings() {
        return getConfigProperty("test.settings.browser.teardown").equalsIgnoreCase("true");
    }

    public static boolean getTestFailureRetrySetings() {
        return getConfigProperty("test.settings.test.retry.failed").equalsIgnoreCase("true");
    }

    static {
        TEST_CONFIG_FILE = Paths.get(Constants.TEST_RESOURCES_DIR, "config.properties").toString();
        LOGGER = LogManager.getLogger(ConfigManager.class);
        configMap = new HashMap();
    }
}
