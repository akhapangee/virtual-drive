package com.virtualdrive;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private WebDriverWait webDriverWait;
    @FindBy(css = "#inputUsername")
    private WebElement usernameField;

    @FindBy(css = "#inputPassword")
    private WebElement passwordField;

    @FindBy(css = "#login-button")
    private WebElement loginButton;

    @FindBy(css = "#signup-link")
    private WebElement signUpLink;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public LoginPage(WebDriver driver, WebDriverWait webDriverWait) {
        PageFactory.initElements(driver, this);
        this.webDriverWait = webDriverWait;
    }

    public void login(String username, String password) {
        webDriverWait.until(ExpectedConditions.visibilityOf(usernameField));
        webDriverWait.until(ExpectedConditions.visibilityOf(passwordField));
        webDriverWait.until(ExpectedConditions.visibilityOf(loginButton));

        this.usernameField.sendKeys(username);
        this.passwordField.sendKeys(password);
        this.loginButton.click();
    }

}