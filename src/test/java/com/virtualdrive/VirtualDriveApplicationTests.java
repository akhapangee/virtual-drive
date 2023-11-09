package com.virtualdrive;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VirtualDriveApplicationTests {
    @LocalServerPort
    private int port;

    private String baseURL;

    private static WebDriver driver;

    private WebDriverWait webDriverWait;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        this.webDriverWait = new WebDriverWait(driver, 3);
        this.baseURL = "http://localhost:" + port;
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    /**
     * Tests if login and sign up pages can be accessed by unauthorized user
     */
    @Test
    public void testSignUpLoginPageAccess() {
        driver.get(baseURL + "/login");
        Assertions.assertEquals(driver.getTitle(), "Login");
        driver.get(baseURL + "/signup");
        Assertions.assertEquals(driver.getTitle(), "Sign Up");
    }

    /**
     * Test only authorized user can access home page
     */
    @Test
    public void testHomePageAccess() throws InterruptedException {
        signUpAndLogin();

        driver.get(baseURL + "/home");
        Assertions.assertEquals(driver.getTitle(), "Home");

        // Logout from home page and see if home page is still accessible
        HomePage homePage = new HomePage(driver);
        homePage.logout();
        driver.get(baseURL + "/home");
        Assertions.assertFalse(driver.getTitle().contains("Home"));
    }

    /**
     * Test that creates a note, and verifies it is displayed.
     * Test that edits an existing note and verifies that the changes are displayed.
     * Test that deletes a note and verifies that the note is no longer displayed.
     *
     * @throws Exception
     */
    @Test
    void testAddEditDeleteNote() throws Exception {
        // Sign up and login
        signUpAndLogin();

        // Get required page objects
        HomePage homePage = new HomePage(driver, webDriverWait);
        ResultPage resultPage = new ResultPage(driver);

        // Create note
        createNote(homePage, resultPage);
        Thread.sleep(2000);

        // Edit note
        editNote(homePage, resultPage);
        Thread.sleep(2000);

        // Delete note
        deleteNote(homePage, resultPage);

    }

    /**
     * Test that creates a set of credentials, verifies that they are displayed, and verifies that the displayed password is encrypted.
     * Test that views an existing set of credentials, verifies that the viewable password is unencrypted, edits the credentials, and verifies that the changes are displayed.
     * Test that deletes an existing set of credentials and verifies that the credentials are no longer displayed.
     *
     * @throws Exception
     */
    @Test
    void testAddEditDeleteCredential() throws Exception {
        // Sign up and login
        signUpAndLogin();

        // Get required page objects
        HomePage homePage = new HomePage(driver, webDriverWait);
        ResultPage resultPage = new ResultPage(driver);

        // Create note
        createCredential(homePage, resultPage);
        Thread.sleep(2000);

        // Edit note
        editCredential(homePage, resultPage);
        Thread.sleep(2000);

        // Delete note
        deleteCredential(homePage, resultPage);

    }

    /**
     * Test file upload, download and delete feature of application
     *
     * @throws Exception
     */
    @Test
    void testUploadDownloadDeleteFile() throws Exception {
        String fileName = "test_file.txt";
        // Sign up and login
        signUpAndLogin();

        // Get required page objects
        HomePage homePage = new HomePage(driver, webDriverWait);
        ResultPage resultPage = new ResultPage(driver);

        // Upload file
        homePage.clickFilesTab();
        homePage.uploadFile(new File(fileName));
        resultPage.returnToHomePage();
        Assertions.assertEquals(homePage.getUploadedFileName(), fileName);
        Thread.sleep(1000);

        // Download file and verify the file downloaded
        homePage.downloadFile();
        Thread.sleep(1000);
        String home = System.getProperty("user.home");
        File file = new File(home + "/Downloads/" + fileName);
        Assertions.assertTrue(file.exists());

        // Delete file
        homePage.deleteFile();
        resultPage.returnToHomePage();
        Assertions.assertNull(homePage.getUploadedFileName());
        Thread.sleep(1000);

    }

    private static void deleteNote(HomePage homePage, ResultPage resultPage) {
        homePage.clickDeleteNote();
        resultPage.returnToHomePage();
        Assertions.assertNull(homePage.getDisplayedNoteTitleValue());
        Assertions.assertNull(homePage.getDisplayedNoteDescriptionValue());
    }

    private static void deleteCredential(HomePage homePage, ResultPage resultPage) {
        homePage.clickDeleteCredential();
        resultPage.returnToHomePage();
        Assertions.assertNull(homePage.getDisplayedCredentialUrlValue());
        Assertions.assertNull(homePage.getDisplayedCredentialUsernameValue());
    }

    private static void editNote(HomePage homePage, ResultPage resultPage) {
        String testUpdatedNoteTitle = "Updated Title";
        String testUpdatedNoteDescription = "Updated Description";

        homePage.clickEditNoteButton();
        homePage.createNote(testUpdatedNoteTitle, testUpdatedNoteDescription);
        resultPage.returnToHomePage();

        // Check input value vs updated value
        Assertions.assertEquals(homePage.getDisplayedNoteTitleValue(), testUpdatedNoteTitle);
        Assertions.assertEquals(homePage.getDisplayedNoteDescriptionValue(), testUpdatedNoteDescription);
    }

    private static void editCredential(HomePage homePage, ResultPage resultPage) throws InterruptedException {
        String testUpdatedUrl = "www.abc.com";
        String testUpdatedUserName = "updateuser1";
        String testPassword = "test";

        homePage.clickEditCredentialButton();

        // Check if password in edit window is plain text (unencrypted) as created before
        Assertions.assertEquals(homePage.getPasswordFromEditWindow(), "password");

        homePage.createCredential(testUpdatedUrl, testUpdatedUserName, testPassword);
        resultPage.returnToHomePage();

        // Check input value vs updated value
        Assertions.assertEquals(homePage.getDisplayedCredentialUrlValue(), testUpdatedUrl);
        Assertions.assertEquals(homePage.getDisplayedCredentialUsernameValue(), testUpdatedUserName);
    }

    private static void createNote(HomePage homePage, ResultPage resultPage) {
        // Open notes tab
        homePage.clickNotesTab();

        // Click add new note button
        homePage.clickAddNewNoteButton();

        // Create new note button
        String testNoteTitle = "Note title";
        String testNoteDescription = "Note Description";
        homePage.createNote(testNoteTitle, testNoteDescription);

        // Check success message on result page
        Assertions.assertEquals(driver.getTitle(), "Result");
        Assertions.assertTrue(resultPage.getSuccessMessage().getText().contains("Success"));

        // Return back to home page to view data
        resultPage.getReturnToHomePageLink().click();
        Assertions.assertEquals(driver.getTitle(), "Home");

        // Check input value vs saved value
        Assertions.assertEquals(homePage.getDisplayedNoteTitleValue(), testNoteTitle);
        Assertions.assertEquals(homePage.getDisplayedNoteDescriptionValue(), testNoteDescription);
    }

    private static void createCredential(HomePage homePage, ResultPage resultPage) {
        // Open notes tab
        homePage.clickCredentialsTab();

        // Click add new note button
        homePage.clickAddNewCredentialButton();

        // Create new note button
        String testUrl = "http://gmail.com";
        String testUserName = "username";
        String testPassword = "password";
        homePage.createCredential(testUrl, testUserName, testPassword);

        // Check success message on result page
        Assertions.assertEquals(driver.getTitle(), "Result");
        Assertions.assertTrue(resultPage.getSuccessMessage().getText().contains("Success"));

        // Return back to home page to view data
        resultPage.getReturnToHomePageLink().click();
        Assertions.assertEquals(driver.getTitle(), "Home");

        // Check input value vs saved value
        Assertions.assertEquals(homePage.getDisplayedCredentialUrlValue(), testUrl);
        Assertions.assertEquals(homePage.getDisplayedCredentialUsernameValue(), testUserName);

        // Encrypted password and normal password will be different
        Assertions.assertNotEquals(homePage.getDisplayedCredentialPasswordValue(), testPassword);
    }

    /**
     * Sign up
     */
    private void signUpAndLogin() {
        String firstName = "FirstName";
        String lastName = "LastName";
        String userName = "user";
        String password = "password12";

        // Sign up, log in and check if home page is accessible
        signup(firstName, lastName, userName, password);
        login(userName, password);
    }

    /**
     * Login
     *
     * @param userName
     * @param password
     */
    private void login(String userName, String password) {
        driver.get(baseURL + "/login");
        LoginPage loginPage = new LoginPage(driver, webDriverWait);
        loginPage.login(userName, password);
    }

    private void signup(String firstName, String lastName, String userName, String password) {
        // Sign up user
        driver.get(baseURL + "/signup");
        SignupPage signupPage = new SignupPage(driver, webDriverWait);
        signupPage.signup(firstName, lastName, userName, password);
    }

    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        driver.get(baseURL + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doLogIn(String userName, String password) {
        // Log in to our dummy account.
        driver.get(baseURL + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));

    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling redirecting users
     * back to the login page after a succesful sign up.
     * Read more about the requirement in the rubric:
     * https://review.udacity.com/#!/rubrics/2724/view
     */
    @Test
    public void testRedirection() {
        // Create a test account
        doMockSignUp("Redirection", "Test", "RT", "123");

        // Check if we have been redirected to the log in page.
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling bad URLs
     * gracefully, for example with a custom error page.
     * <p>
     * Read more about custom error pages at:
     * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
     */
    @Test
    public void testBadUrl() {
        // Create a test account
        doMockSignUp("URL", "Test", "UT", "123");
        doLogIn("UT", "123");

        // Try to access a random made-up URL.
        driver.get(baseURL + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }


    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     * <p>
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
    public void testLargeUpload() {
        // Create a test account
        doMockSignUp("Large File", "Test", "LFT", "123");
        doLogIn("LFT", "123");

        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        String fileName = "upload5m.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

    }


}
