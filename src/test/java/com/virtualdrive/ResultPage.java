package com.virtualdrive;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage {
    @FindBy(css = "#success")
    private WebElement successMessage;

    @FindBy(css = ".returnToHomePageLink")
    private WebElement returnToHomePageLink;

    public ResultPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void returnToHomePage(){
        returnToHomePageLink.click();
    }
    public WebElement getSuccessMessage() {
        return successMessage;
    }

    public WebElement getReturnToHomePageLink() {
        return returnToHomePageLink;
    }
}
