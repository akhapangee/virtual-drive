package com.virtualdrive;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SignupPage {

    private WebDriverWait webDriverWait;

    @FindBy(css = "#inputFirstName")
    private WebElement firstNameField;

    @FindBy(css = "#inputLastName")
    private WebElement lastNameField;

    @FindBy(css = "#inputUsername")
    private WebElement usernameField;

    @FindBy(css = "#inputPassword")
    private WebElement passwordField;

    @FindBy(css = "#buttonSignUp")
    private WebElement signUpButton;

    public SignupPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public SignupPage(WebDriver driver, WebDriverWait webDriverWait) {
        PageFactory.initElements(driver, this);
        this.webDriverWait = webDriverWait;
    }

    public void signup(String firstName, String lastName, String username, String password) {
        // Wait for required elements
        webDriverWait.until(ExpectedConditions.visibilityOf(firstNameField));
        webDriverWait.until(ExpectedConditions.visibilityOf(lastNameField));
        webDriverWait.until(ExpectedConditions.visibilityOf(usernameField));
        webDriverWait.until(ExpectedConditions.visibilityOf(passwordField));

        this.firstNameField.sendKeys(firstName);
        this.lastNameField.sendKeys(lastName);
        this.usernameField.sendKeys(username);
        this.passwordField.sendKeys(password);
        this.signUpButton.click();
    }

}
