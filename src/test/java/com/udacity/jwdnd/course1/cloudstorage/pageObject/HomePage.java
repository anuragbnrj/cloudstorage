package com.udacity.jwdnd.course1.cloudstorage.pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class HomePage {

    @FindBy(css = "#buttonLogout")
    private WebElement logoutButton;

    // Beginning of notes tab elments
    @FindBy(css = "#nav-notes-tab")
    private WebElement notesTab;

    @FindBy(css = "#addNewNoteButton")
    private WebElement addNewNoteButton;

    @FindBy(className = "noteRow")
    private List<WebElement> noteRowList;

    @FindBy(css = "#note-title")
    private WebElement noteTitleField;

    @FindBy(css = "#note-description")
    private WebElement noteDescriptionField;

    @FindBy(css = "#saveNoteButton")
    private WebElement saveNoteButton;
    // End of notes tab elements

    // Beginning of credential tab elements
    @FindBy(css = "#nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(css = "#addNewCredentialButton")
    private WebElement addNewCredentialButton;

    @FindBy(className = "credentialRow")
    private List<WebElement> credentialRowList;

    @FindBy(css = "#credential-url")
    private WebElement credentialUrlField;

    @FindBy(css = "#credential-username")
    private WebElement credentialUsernameField;

    @FindBy(css = "#credential-password")
    private WebElement credentialPasswordField;

    @FindBy(css = "#saveCredentialButton")
    private WebElement saveCredentialButton;
    // End of credential tab elements

    private final JavascriptExecutor javascriptExecutor;
    private final WebDriverWait wait;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        javascriptExecutor = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, 3);
    }

    public void tryHomePageUrl(String baseURL, WebDriver driver) {
        // given
        String url = baseURL + "/home";
        // when
        driver.get(url);
    }

    public void logout() {
        javascriptExecutor.executeScript("arguments[0].click();", logoutButton);
    }

    public void openNotesTab() {
        wait.until(ExpectedConditions.visibilityOfAllElements(notesTab));
        javascriptExecutor.executeScript("arguments[0].click()", notesTab);
    }

    public void openCredentialsTab() {
        wait.until(ExpectedConditions.visibilityOfAllElements(credentialsTab));
        javascriptExecutor.executeScript("arguments[0].click()", credentialsTab);
    }

    public void clickCreateNoteButton() {
        // open add note modal
        wait.until(ExpectedConditions.elementToBeClickable(addNewNoteButton)).click();
    }

    public void clickCreateCredentialButton() {
        // open add credential modal
        wait.until(ExpectedConditions.elementToBeClickable(addNewCredentialButton)).click();
    }

    public void clickEditNoteButton(String title, String description) {
        // open edit note modal
        wait.until(ExpectedConditions.visibilityOfAllElements(addNewNoteButton));
        for (WebElement noteRow : noteRowList) {
            WebElement titleElement = noteRow.findElement(By.className("noteTitle"));
            WebElement descriptionElement = noteRow.findElement(By.className("noteDescription"));
            if (titleElement.getText().equals(title) && descriptionElement.getText().equals(description)) {
                WebElement deleteNoteButton = noteRow.findElement(By.className("editNoteButton"));
                deleteNoteButton.click();
            }
        }
    }

    public void clickEditCredentialButton(String url, String username) {
        // open edit credential modal
        wait.until(ExpectedConditions.visibilityOfAllElements(addNewCredentialButton));
        for (WebElement credentialRow : credentialRowList) {
            WebElement urlElement = credentialRow.findElement(By.className("credentialUrl"));
            WebElement usernameElement = credentialRow.findElement(By.className("credentialUsername"));
            if (urlElement.getText().equals(url) && usernameElement.getText().equals(username)) {
                WebElement deleteNoteButton = credentialRow.findElement(By.className("editCredentialButton"));
                deleteNoteButton.click();
            }
        }
    }

    public void createOrEditNote(String title, String description) {
        // fill note details
        wait.until(ExpectedConditions.visibilityOf(noteTitleField));
        noteTitleField.clear();
        noteTitleField.sendKeys(title);
        noteDescriptionField.clear();
        noteDescriptionField.sendKeys(description);

        // save note
        wait.until(ExpectedConditions.elementToBeClickable(saveNoteButton)).click();
    }

    public void createOrEditCredential(String url, String username, String password) {
        // fill credential details
        wait.until(ExpectedConditions.visibilityOf(credentialUrlField));
        credentialUrlField.clear();
        credentialUrlField.sendKeys(url);
        credentialUsernameField.clear();
        credentialUsernameField.sendKeys(username);
        credentialPasswordField.clear();
        credentialPasswordField.sendKeys(password);

        // save credential
        wait.until(ExpectedConditions.elementToBeClickable(saveCredentialButton)).click();
    }

    public boolean isNoteListed(String title, String description) {
        // check whether note with given title and description exists or not
        wait.until(ExpectedConditions.visibilityOfAllElements(addNewNoteButton));
        boolean isNoteListed = false;
        for (WebElement noteRow : noteRowList) {
            WebElement titleElement = noteRow.findElement(By.className("noteTitle"));
            WebElement descriptionElement = noteRow.findElement(By.className("noteDescription"));
            if (titleElement.getText().equals(title) && descriptionElement.getText().equals(description)) {
                isNoteListed = true;
            }
        }
        return isNoteListed;
    }

    public boolean isCredentialListed(String url, String username) {
        // check whether credential with given url and username exists or not
        wait.until(ExpectedConditions.visibilityOfAllElements(addNewCredentialButton));
        boolean isCredentialListed = false;
        for (WebElement credentialRow : credentialRowList) {
            WebElement urlElement = credentialRow.findElement(By.className("credentialUrl"));
            WebElement usernameElement = credentialRow.findElement(By.className("credentialUsername"));
            if (urlElement.getText().equals(url) && usernameElement.getText().equals(username)) {
                isCredentialListed = true;
            }
        }
        return isCredentialListed;
    }

    public void deleteNote(String title, String description) {
        wait.until(ExpectedConditions.visibilityOfAllElements(addNewNoteButton));
        for (WebElement noteRow : noteRowList) {
            WebElement titleElement = noteRow.findElement(By.className("noteTitle"));
            WebElement descriptionElement = noteRow.findElement(By.className("noteDescription"));
            if (titleElement.getText().equals(title) && descriptionElement.getText().equals(description)) {
                WebElement deleteNoteButton = noteRow.findElement(By.className("deleteNoteButton"));
                deleteNoteButton.click();
            }
        }
    }

    public void deleteCredential(String url, String username) {
        wait.until(ExpectedConditions.visibilityOfAllElements(addNewCredentialButton));
        for (WebElement credentialRow : credentialRowList) {
            WebElement urlElement = credentialRow.findElement(By.className("credentialUrl"));
            WebElement usernameElement = credentialRow.findElement(By.className("credentialUsername"));
            if (urlElement.getText().equals(url) && usernameElement.getText().equals(username)) {
                WebElement deleteCredentialButton = credentialRow.findElement(By.className("deleteCredentialButton"));
                deleteCredentialButton.click();
            }
        }
    }

}
