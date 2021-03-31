package com.udacity.jwdnd.course1.cloudstorage.pageObject;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LoginPage {

    @FindBy(css = "#inputUsername")
    private WebElement usernameField;

    @FindBy(css = "#inputPassword")
    private WebElement passwordField;

    @FindBy(css = "#submitButton")
    private WebElement submitButton;

    private JavascriptExecutor javascriptExecutor;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        javascriptExecutor = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, 3);
    }

    public void visitAndVerifyLoginPage(String baseURL, WebDriver driver) {
        // given
        String loginURL = baseURL + "/login";
        // when
        driver.get(loginURL);
        // then
        assertThat(driver.getTitle()).isEqualTo("Login");
        assertThat(driver.getCurrentUrl()).contains("/login");
    }

    public void login(String username, String password) {
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        submitButton.click();
    }
}
