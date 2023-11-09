package com.virtualdrive;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    private WebDriverWait webDriverWait;

    @FindBy(css = "#btn-logout")
    private WebElement buttonLogout;

    // Files
    @FindBy(css = "#fileUpload")
    private WebElement fileUploadChooseButton;

    @FindBy(css = "#nav-files-tab")
    private WebElement filesTab;

    @FindBy(css = "#uploadButton")
    private WebElement buttonUploadButton;

    @FindBy(css = ".buttonFileView")
    private WebElement buttonFileView;

    @FindBy(css = ".buttonFileDelete")
    private WebElement buttonFileDelete;

    @FindBy(css = ".fileNameDisplay")
    private WebElement fileNameDisplay;

    // Notes
    @FindBy(css = "#nav-notes-tab")
    private WebElement notesTab;

    @FindBy(css = "#btnAddNewCredential")
    private WebElement btnAddNewCredential;

    @FindBy(css = "#btn-add-new-note")
    private WebElement buttonAddNewNote;

    @FindBy(css = "#note-title")
    private WebElement noteTitleField;

    @FindBy(css = ".noteTitleFromDisplayTable")
    private WebElement noteTitleFromDisplayTable;

    @FindBy(css = ".noteDescriptionFromDisplayTable")
    private WebElement noteDescriptionFromDisplayTable;

    @FindBy(css = "#note-description")
    private WebElement noteDescriptionField;

    @FindBy(css = "#noteSubmit")
    private WebElement buttonNoteSubmit;

    @FindBy(css = ".btnEditNote")
    private WebElement btnEditNote;

    @FindBy(css = ".btnDeleteNote")
    private WebElement btnDeleteNote;

    // Credentials
    @FindBy(css = "#nav-credentials-tab")
    private WebElement credentialsTab;

    // Credential button
    @FindBy(css = ".btnEditCredential")
    private WebElement btnEditCredential;

    @FindBy(css = ".btnDeleteCredential")
    private WebElement btnDeleteCredential;

    @FindBy(css = "#credentialSubmit")
    private WebElement btnCredentialSubmit;

    // Credential input fields
    @FindBy(css = "#credential-url")
    private WebElement credentialUrlField;

    @FindBy(css = "#credential-username")
    private WebElement credentialUsernameField;

    @FindBy(css = "#credential-password")
    private WebElement credentialPasswordField;

    // Credential display fields
    @FindBy(css = ".credential-url-row")
    private WebElement credentialUrlRow;

    @FindBy(css = ".credential-username-row")
    private WebElement credentialUsernameRow;

    @FindBy(css = ".credential-password-row")
    private WebElement credentialPasswordRow;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public HomePage(WebDriver driver, WebDriverWait webDriverWait) {
        PageFactory.initElements(driver, this);
        this.webDriverWait = webDriverWait;
    }

    public void logout() {
        this.buttonLogout.click();
    }

    public void createNote(String noteTitle, String noteDescription) {
        webDriverWait.until(ExpectedConditions.visibilityOf(noteTitleField));
        webDriverWait.until(ExpectedConditions.visibilityOf(noteDescriptionField));

        this.noteTitleField.clear();
        this.noteTitleField.sendKeys(noteTitle);

        this.noteDescriptionField.clear();
        this.noteDescriptionField.sendKeys(noteDescription);

        this.buttonNoteSubmit.submit();
    }

    public void createCredential(String url, String username, String password) {
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialUrlField));
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialUsernameField));
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialUsernameField));
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialPasswordField));

        this.credentialUrlField.clear();
        this.credentialUrlField.sendKeys(url);

        this.credentialUsernameField.clear();
        this.credentialUsernameField.sendKeys(username);

        this.credentialPasswordField.clear();
        this.credentialPasswordField.sendKeys(password);

        this.btnCredentialSubmit.submit();
    }

    public void clickFilesTab() {
        filesTab.click();
    }

    public void clickNotesTab() {
        notesTab.click();
    }

    public void clickCredentialsTab() {
        credentialsTab.click();
    }

    public void clickAddNewNoteButton() {
        webDriverWait.until(ExpectedConditions.visibilityOf(buttonAddNewNote));
        buttonAddNewNote.click();
    }

    public void clickAddNewCredentialButton() {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(btnAddNewCredential));
        btnAddNewCredential.click();
    }

    public void clickEditNoteButton() {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(btnEditNote));
        btnEditNote.click();
    }

    public void clickEditCredentialButton() {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(btnEditCredential));
        btnEditCredential.click();
    }

    public void clickDeleteNote() {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(btnDeleteNote));
        btnDeleteNote.click();
    }

    public void clickDeleteCredential() {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(btnDeleteCredential));
        btnDeleteCredential.click();
    }

    public String getDisplayedNoteTitleValue() {
        try {
            return noteTitleFromDisplayTable.getAttribute("innerHTML");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDisplayedNoteDescriptionValue() {
        try {
            return noteDescriptionFromDisplayTable.getAttribute("innerHTML");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDisplayedCredentialUrlValue() {
        try {
            webDriverWait.until(ExpectedConditions.visibilityOf(credentialUrlRow));
            return credentialUrlRow.getAttribute("innerHTML");
        } catch (Exception e) {
            System.out.println("Entered credential url doesn't exist!");
            return null;
        }
    }

    public String getDisplayedCredentialUsernameValue() {
        try {
            webDriverWait.until(ExpectedConditions.visibilityOf(credentialUsernameRow));
            return credentialUsernameRow.getAttribute("innerHTML");
        } catch (Exception e) {
            System.out.println("Entered credential username doesn't exist!");
            return null;
        }
    }

    public String getDisplayedCredentialPasswordValue() {
        try {
            webDriverWait.until(ExpectedConditions.visibilityOf(credentialPasswordRow));
            return credentialPasswordRow.getAttribute("innerHTML");
        } catch (Exception e) {
            System.out.println("Entered credential password doesn't exist!");
            return null;
        }
    }

    public String getPasswordFromEditWindow() {
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialPasswordField));
        return credentialPasswordField.getAttribute("value");
    }

    public String getUploadedFileName() {
        try {
            return fileNameDisplay.getAttribute("innerHTML");
        } catch (Exception e) {
            System.out.println("Uploaded fle name doesn't exist!");
            return null;
        }

    }

    public void uploadFile(java.io.File file) {
        webDriverWait.until(ExpectedConditions.visibilityOf(fileUploadChooseButton));
        fileUploadChooseButton.sendKeys(file.getAbsolutePath());
        buttonUploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
    }

    public void downloadFile() {
        webDriverWait.until(ExpectedConditions.visibilityOf(buttonFileView));
        buttonFileView.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("File view failed");
        }
    }

    public void deleteFile() {
        webDriverWait.until(ExpectedConditions.visibilityOf(buttonFileDelete));
        buttonFileDelete.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("File view failed");
        }
    }


}
