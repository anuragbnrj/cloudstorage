package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.pageObject.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.pageObject.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pageObject.ResultPage;
import com.udacity.jwdnd.course1.cloudstorage.pageObject.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    private static final String TEST_USER_FIRST_NAME = "John";
    private static final String TEST_USER_LAST_NAME = "Doe";
    private static final String TEST_USER_USERNAME = "johndoe";
    private static final String TEST_USER_PASSWORD = "123456";

    private static final String TEST_NOTE_1_TITLE = "Note_1_Title";
    private static final String TEST_NOTE_1_DESCRIPTION = "Note_1_Description";
    private static final String TEST_NOTE_1_TITLE_EDITED = "Note_1_Title_Edited";
    private static final String TEST_NOTE_1_DESCRIPTION_EDITED = "Note_1_Description_Edited";

    private static final String TEST_CREDENTIAL_1_URL = "Credential_1_Url";
    private static final String TEST_CREDENTIAL_1_USERNAME = "Credential_1_Username";
    private static final String TEST_CREDENTIAL_1_PASSWORD = "Credential_1_Password";
    private static final String TEST_CREDENTIAL_1_URL_EDITED = "Credential_1_Url_Edited";
    private static final String TEST_CREDENTIAL_1_USERNAME_EDITED = "Credential_1_Username_Edited";
    private static final String TEST_CREDENTIAL_1_PASSWORD_EDITED = "Credential_1_Password_Edited";

    @Autowired
    private UserMapper userMapper;

    @LocalServerPort
    private int port;

    private static WebDriver driver;
    private static LoginPage loginPage;
    private static SignupPage signupPage;
    private static HomePage homePage;
    private static ResultPage resultPage;
    private static String baseURL;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        loginPage = new LoginPage(driver);
        signupPage = new SignupPage(driver);
        homePage = new HomePage(driver);
        resultPage = new ResultPage(driver);
    }

    @BeforeEach
    void setUp() {
        baseURL = "http://localhost:" + port;
    }

    @AfterEach
    void tearDown() {
        userMapper.deleteUserByUsername(TEST_USER_USERNAME);
    }

    @AfterAll
    static void afterAll() {
        driver.quit();
        driver = null;
    }

    @Test
    void testHomePageInaccessibleWithoutLogin() {
        // given
        // when
        homePage.tryHomePageUrl(baseURL, driver);

        // then
        assertThat(driver.getTitle()).isNotEqualTo("Home");
        assertThat(driver.getTitle()).isEqualTo("Login");
        assertThat(driver.getCurrentUrl()).contains("/login");
    }

    @Test
    void testSignupAndLogoutFlow() {
        signupPage.visitAndVerifySignupPage(baseURL, driver);
        signupPage.signupTestUser(TEST_USER_FIRST_NAME, TEST_USER_LAST_NAME, TEST_USER_USERNAME, TEST_USER_PASSWORD);
        loginPage.visitAndVerifyLoginPage(baseURL, driver);

        // when
        loginPage.login(TEST_USER_USERNAME, TEST_USER_PASSWORD);
        // then
        assertThat(driver.getTitle()).isEqualTo("Home");
        assertThat(driver.getCurrentUrl()).contains("/home");

        // when
        homePage.logout();
        // then
        assertThat(driver.getTitle()).isEqualTo("Login");
        assertThat(driver.getCurrentUrl()).contains("/login");

        // given
        // when
        homePage.tryHomePageUrl(baseURL, driver);
        // then
        assertThat(driver.getTitle()).isEqualTo("Login");
        assertThat(driver.getCurrentUrl()).contains("/login");
    }

    @Test
    void testCreateNoteFunctionality() {
        signupPage.visitAndVerifySignupPage(baseURL, driver);
        signupPage.signupTestUser(TEST_USER_FIRST_NAME, TEST_USER_LAST_NAME, TEST_USER_USERNAME, TEST_USER_PASSWORD);
        loginPage.visitAndVerifyLoginPage(baseURL, driver);

        // login with test user
        loginPage.login(TEST_USER_USERNAME, TEST_USER_PASSWORD);

        // open notes tab
        homePage.openNotesTab();

        // click create note button
        homePage.clickCreateNoteButton();

        // create new note
        homePage.createOrEditNote(TEST_NOTE_1_TITLE, TEST_NOTE_1_DESCRIPTION);

        // verify result page
        assertThat(driver.getTitle()).isEqualTo("Result");

        // click home on result page
        resultPage.clickAlertSuccessHome();

        // open notes tab
        homePage.openNotesTab();

        // verify new note exists
        assertTrue(homePage.isNoteListed(TEST_NOTE_1_TITLE, TEST_NOTE_1_DESCRIPTION));

        // delete note so that user can be cleared from db for other tests
        homePage.deleteNote(TEST_NOTE_1_TITLE, TEST_NOTE_1_DESCRIPTION);

        // click home on result page
        resultPage.clickAlertSuccessHome();

        // logout user
        homePage.logout();
    }

    @Test
    void testEditNoteFunctionality() {
        signupPage.visitAndVerifySignupPage(baseURL, driver);
        signupPage.signupTestUser(TEST_USER_FIRST_NAME, TEST_USER_LAST_NAME, TEST_USER_USERNAME, TEST_USER_PASSWORD);
        loginPage.visitAndVerifyLoginPage(baseURL, driver);

        // login with test user
        loginPage.login(TEST_USER_USERNAME, TEST_USER_PASSWORD);

        // open notes tab
        homePage.openNotesTab();

        // click create note button
        homePage.clickCreateNoteButton();

        // create new note
        homePage.createOrEditNote(TEST_NOTE_1_TITLE, TEST_NOTE_1_DESCRIPTION);

        // verify result page
        assertThat(driver.getTitle()).isEqualTo("Result");
        // click home on result page
        resultPage.clickAlertSuccessHome();

        // open notes tab
        homePage.openNotesTab();

        // verify new note exists
        assertTrue(homePage.isNoteListed(TEST_NOTE_1_TITLE, TEST_NOTE_1_DESCRIPTION));

        // logout user
        homePage.logout();

        // visit login page
        loginPage.visitAndVerifyLoginPage(baseURL, driver);

        // login with test user
        loginPage.login(TEST_USER_USERNAME, TEST_USER_PASSWORD);

        // open notes tab
        homePage.openNotesTab();

        // click on edit note button for existing note
        homePage.clickEditNoteButton(TEST_NOTE_1_TITLE, TEST_NOTE_1_DESCRIPTION);

        // fill in new details in existing note
        homePage.createOrEditNote(TEST_NOTE_1_TITLE_EDITED, TEST_NOTE_1_DESCRIPTION_EDITED);

        // click home on result page
        resultPage.clickAlertSuccessHome();

        // open notes tab
        homePage.openNotesTab();

        // verify new note exists
        assertTrue(homePage.isNoteListed(TEST_NOTE_1_TITLE_EDITED, TEST_NOTE_1_DESCRIPTION_EDITED));

        // delete note so that user can be cleared from db for other tests
        homePage.deleteNote(TEST_NOTE_1_TITLE_EDITED, TEST_NOTE_1_DESCRIPTION_EDITED);

        // click home on result page
        resultPage.clickAlertSuccessHome();

        // logout user
        homePage.logout();
    }

    @Test
    void testDeleteNoteFunctionality() {
        signupPage.visitAndVerifySignupPage(baseURL, driver);
        signupPage.signupTestUser(TEST_USER_FIRST_NAME, TEST_USER_LAST_NAME, TEST_USER_USERNAME, TEST_USER_PASSWORD);
        loginPage.visitAndVerifyLoginPage(baseURL, driver);

        // login with test user
        loginPage.login(TEST_USER_USERNAME, TEST_USER_PASSWORD);

        // open notes tab
        homePage.openNotesTab();

        // click create note button
        homePage.clickCreateNoteButton();

        // create new note
        homePage.createOrEditNote(TEST_NOTE_1_TITLE, TEST_NOTE_1_DESCRIPTION);

        // verify result page
        assertThat(driver.getTitle()).isEqualTo("Result");
        // click home on result page
        resultPage.clickAlertSuccessHome();

        // open notes tab
        homePage.openNotesTab();

        // verify new note exists
        assertTrue(homePage.isNoteListed(TEST_NOTE_1_TITLE, TEST_NOTE_1_DESCRIPTION));

        // logout user
        homePage.logout();

        // visit login page
        loginPage.visitAndVerifyLoginPage(baseURL, driver);

        // login with test user
        loginPage.login(TEST_USER_USERNAME, TEST_USER_PASSWORD);

        // open notes tab
        homePage.openNotesTab();

        // verify new note exists
        assertTrue(homePage.isNoteListed(TEST_NOTE_1_TITLE, TEST_NOTE_1_DESCRIPTION));

        // delete note
        homePage.deleteNote(TEST_NOTE_1_TITLE, TEST_NOTE_1_DESCRIPTION);

        // click home on result page
        resultPage.clickAlertSuccessHome();

        // open notes tab
        homePage.openNotesTab();

        // verify new note exists
        assertFalse(homePage.isNoteListed(TEST_NOTE_1_TITLE, TEST_NOTE_1_DESCRIPTION));

        // logout user
        homePage.logout();
    }

    @Test
    void testCreateCredentialFunctionality() {
        signupPage.visitAndVerifySignupPage(baseURL, driver);
        signupPage.signupTestUser(TEST_USER_FIRST_NAME, TEST_USER_LAST_NAME, TEST_USER_USERNAME, TEST_USER_PASSWORD);
        loginPage.visitAndVerifyLoginPage(baseURL, driver);

        // login with test user
        loginPage.login(TEST_USER_USERNAME, TEST_USER_PASSWORD);

        // open credentials tab
        homePage.openCredentialsTab();

        // click create credential button
        homePage.clickCreateCredentialButton();

        // create new credential
        homePage.createOrEditCredential(TEST_CREDENTIAL_1_URL, TEST_CREDENTIAL_1_USERNAME, TEST_CREDENTIAL_1_PASSWORD);

        // verify result page
        assertThat(driver.getTitle()).isEqualTo("Result");

        // click home on result page
        resultPage.clickAlertSuccessHome();

        // open credentials tab
        homePage.openCredentialsTab();

        // verify new credential exists
        assertTrue(homePage.isCredentialListed(TEST_CREDENTIAL_1_URL, TEST_CREDENTIAL_1_USERNAME));

        // delete credential so that user can be cleared from db for other tests
        homePage.deleteCredential(TEST_CREDENTIAL_1_URL, TEST_CREDENTIAL_1_USERNAME);

        // click home on result page
        resultPage.clickAlertSuccessHome();

        // logout user
        homePage.logout();
    }

    @Test
    void testEditCredentialFunctionality() {
        signupPage.visitAndVerifySignupPage(baseURL, driver);
        signupPage.signupTestUser(TEST_USER_FIRST_NAME, TEST_USER_LAST_NAME, TEST_USER_USERNAME, TEST_USER_PASSWORD);
        loginPage.visitAndVerifyLoginPage(baseURL, driver);

        // login with test user
        loginPage.login(TEST_USER_USERNAME, TEST_USER_PASSWORD);

        // open credentials tab
        homePage.openCredentialsTab();

        // click create credential button
        homePage.clickCreateCredentialButton();

        // create new credential
        homePage.createOrEditCredential(TEST_CREDENTIAL_1_URL, TEST_CREDENTIAL_1_USERNAME, TEST_CREDENTIAL_1_PASSWORD);

        // verify result page
        assertThat(driver.getTitle()).isEqualTo("Result");

        // click home on result page
        resultPage.clickAlertSuccessHome();

        // open credentials tab
        homePage.openCredentialsTab();

        // verify new credential exists
        assertTrue(homePage.isCredentialListed(TEST_CREDENTIAL_1_URL, TEST_CREDENTIAL_1_USERNAME));

        // logout user
        homePage.logout();

        // visit login page
        loginPage.visitAndVerifyLoginPage(baseURL, driver);

        // login with test user
        loginPage.login(TEST_USER_USERNAME, TEST_USER_PASSWORD);

        // open credentials tab
        homePage.openCredentialsTab();

        // click on edit credential button for existing credential
        homePage.clickEditCredentialButton(TEST_CREDENTIAL_1_URL, TEST_CREDENTIAL_1_USERNAME);

        // fill in new details in existing credential
        homePage.createOrEditCredential(TEST_CREDENTIAL_1_URL_EDITED, TEST_CREDENTIAL_1_USERNAME_EDITED, TEST_CREDENTIAL_1_PASSWORD_EDITED);

        // click home on result page
        resultPage.clickAlertSuccessHome();

        // open credentials tab
        homePage.openCredentialsTab();

        // verify old credential does not exist
        assertFalse(homePage.isCredentialListed(TEST_CREDENTIAL_1_URL, TEST_CREDENTIAL_1_USERNAME));
        // verify edited credential exists
        assertTrue(homePage.isCredentialListed(TEST_CREDENTIAL_1_URL_EDITED, TEST_CREDENTIAL_1_USERNAME_EDITED));

        // delete credential so that user can be cleared from db for other tests
        homePage.deleteCredential(TEST_CREDENTIAL_1_URL_EDITED, TEST_CREDENTIAL_1_USERNAME_EDITED);

        // click home on result page
        resultPage.clickAlertSuccessHome();

        // logout user
        homePage.logout();
    }

    @Test
    void testDeleteCredentialFunctionality() {
        signupPage.visitAndVerifySignupPage(baseURL, driver);
        signupPage.signupTestUser(TEST_USER_FIRST_NAME, TEST_USER_LAST_NAME, TEST_USER_USERNAME, TEST_USER_PASSWORD);
        loginPage.visitAndVerifyLoginPage(baseURL, driver);

        // login with test user
        loginPage.login(TEST_USER_USERNAME, TEST_USER_PASSWORD);

        // open credentials tab
        homePage.openCredentialsTab();

        // click create credential button
        homePage.clickCreateCredentialButton();

        // create new credential
        homePage.createOrEditCredential(TEST_CREDENTIAL_1_URL, TEST_CREDENTIAL_1_USERNAME, TEST_CREDENTIAL_1_PASSWORD);

        // verify result page
        assertThat(driver.getTitle()).isEqualTo("Result");

        // click home on result page
        resultPage.clickAlertSuccessHome();

        // open credentials tab
        homePage.openCredentialsTab();

        // verify new credential exists
        assertTrue(homePage.isCredentialListed(TEST_CREDENTIAL_1_URL, TEST_CREDENTIAL_1_USERNAME));

        // logout user
        homePage.logout();

        // visit login page
        loginPage.visitAndVerifyLoginPage(baseURL, driver);

        // login with test user
        loginPage.login(TEST_USER_USERNAME, TEST_USER_PASSWORD);

        // open credentials tab
        homePage.openCredentialsTab();

        // delete credential
        homePage.deleteCredential(TEST_CREDENTIAL_1_URL, TEST_CREDENTIAL_1_USERNAME);

        // click home on result page
        resultPage.clickAlertSuccessHome();

        // open credentials tab
        homePage.openCredentialsTab();

        // verify deleted credential does not exist
        assertFalse(homePage.isCredentialListed(TEST_CREDENTIAL_1_URL, TEST_CREDENTIAL_1_USERNAME));

        // logout user
        homePage.logout();
    }


}
