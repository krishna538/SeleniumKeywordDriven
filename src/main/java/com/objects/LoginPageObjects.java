package com.objects;

import org.openqa.selenium.By;

public class LoginPageObjects {

    public static final By USERNAME_INPUT = By.cssSelector("input[name='username']");
    public static final By PASSWORD_INPUT = By.cssSelector("input[name='password']");
    public static final By LOGIN_BTN = By.xpath("//button[@type='submit']");
}
