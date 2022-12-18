package com.pages;

import com.base.Actions;
import com.utils.Helper;

import java.util.Map;

import static com.objects.LoginPageObjects.*;

public class LoginPage {

    public void doLogin(Map<String, String> data){
        Actions.enterText(USERNAME_INPUT, data.getOrDefault("Username", "Admin"));
        Helper.log("Entered username as " + data.getOrDefault("Username", "Admin"));
        Actions.enterText(PASSWORD_INPUT, data.getOrDefault("Password", "admin123"));
        Helper.log("Entered password as " + data.getOrDefault("Password", "admin123"));
        Actions.click(LOGIN_BTN);
        Helper.log("Clicked on login button");
    }
}
