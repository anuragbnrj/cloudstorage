package com.udacity.jwdnd.course1.cloudstorage.pageObject;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ResultPage {

    @FindBy(css = "#alertSuccessHomeLink")
    private WebElement alertSuccessHomeLink;

    private JavascriptExecutor javascriptExecutor;
    private final WebDriverWait wait;

    public ResultPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        javascriptExecutor = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, 3);
    }

    public void clickAlertSuccessHome() {
        javascriptExecutor.executeScript("arguments[0].click();", alertSuccessHomeLink);
    }

}
