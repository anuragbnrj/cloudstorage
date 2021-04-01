package com.udacity.jwdnd.course1.cloudstorage.pageObject;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SignupPage {

    @FindBy(css = "#inputFirstName")
    private WebElement firstNameField;

    @FindBy(css = "#inputLastName")
    private WebElement lastNameField;

    @FindBy(css = "#inputUsername")
    private WebElement usernameField;

    @FindBy(css = "#inputPassword")
    private WebElement passwordField;

    @FindBy(css = "#submitButton")
    private WebElement submitButton;

    @FindBy(css = "#successMessage")
    private WebElement successMessage;

    private JavascriptExecutor javascriptExecutor;
    private final WebDriverWait wait;

    public SignupPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        javascriptExecutor = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, 3);
    }

    public void visitAndVerifySignupPage(String baseURL, WebDriver driver) {
        // given
        String signupURL = baseURL + "/signup";
        // when
        driver.get(signupURL);
        // then
        assertThat(driver.getTitle()).isEqualTo("Sign Up");
        assertThat(driver.getCurrentUrl()).contains("/signup");
    }

    public void signupTestUser(String fname, String lname, String username, String password) {
        // given
        // when
        signup(fname, lname, username, password);
        // then
        //assertThat(successMessage.getText()).isEqualTo("You successfully signed up! Please continue to the login page.");
    }

    private void signup(String firstName, String lastName, String username, String password) {
        this.firstNameField.sendKeys(firstName);
        this.lastNameField.sendKeys(lastName);
        this.usernameField.sendKeys(username);
        this.passwordField.sendKeys(password);
        this.submitButton.click();
    }

}
