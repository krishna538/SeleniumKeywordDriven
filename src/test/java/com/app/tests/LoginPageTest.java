package com.app.tests;

import com.App.WebApp;
import com.base.AppWebTest;
import org.testng.annotations.Test;

import java.util.Map;

public class LoginPageTest extends AppWebTest {


    @Test(dataProvider = "testDataProvider", priority = 1, testName = "TC-001",
            description = "Verify if user is able to Login")
    public void verifyLoginFunctionality(Map<String, String> data){
        WebApp app = new WebApp();
        app.launchApplication();
        app.loginPage.doLogin(data);
    }
}
