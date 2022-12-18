package com.App;

import com.base.Actions;
import com.pages.HomePage;
import com.pages.LoginPage;
import com.utils.ConfigManager;

public class WebApp {

    public final HomePage homePage;
    public final LoginPage loginPage;

    public WebApp(){
        homePage = new HomePage();
        loginPage = new LoginPage();
    }

    public void launchApplication(){
        Actions.openUrl(ConfigManager.getConfigProperty("application.url"), "Application url launched");
    }
}
