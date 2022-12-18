package com.listeners;

import com.base.ExcelManager;
import com.utils.ConfigManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

public class AnnotationTransformer implements IAnnotationTransformer {
    private static final Logger LOGGER = LogManager.getLogger(AnnotationTransformer.class);

    public AnnotationTransformer() {
    }

    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        try {
            List<Map<String, String>> excelRow = ExcelManager.getControllerRowsList();
            Iterator var6 = excelRow.iterator();

            while(var6.hasNext()) {
                Map<String, String> stringStringMap = (Map)var6.next();
                if (testMethod.getName().equalsIgnoreCase((String)stringStringMap.get("TestMethodName"))) {
                    if (((String)stringStringMap.get("Execute")).equalsIgnoreCase("No")) {
                        annotation.setEnabled(false);
                    } else {
                        annotation.setDataProvider("testDataProvider");

                        try {
                            int priority = Integer.parseInt(((String)stringStringMap.get("RunOrder")).split("\\.")[0]);
                            annotation.setPriority(priority);
                        } catch (NumberFormatException var9) {
                            annotation.setPriority(1);
                        } catch (NullPointerException var10) {
                        }
                    }
                }
            }

            if (ConfigManager.getTestFailureRetrySetings()) {
                annotation.setRetryAnalyzer(Retry.class);
            }

        } catch (Exception var11) {
            LOGGER.error(var11);
            throw new RuntimeException(var11);
        }
    }
}
