package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pageObject.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.pageObject.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pageObject.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	private static final String TEST_USER_FIRST_NAME = "John";
	private static final String TEST_USER_LAST_NAME = "Doe";
	private static final String TEST_USER_USERNAME = "johndoe";
	private static final String TEST_USER_PASSWORD = "123456";

	@LocalServerPort
	private int port;

	public static WebDriver driver;

	public String baseURL;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@BeforeEach
	public void beforeEach() {
		baseURL = "http://localhost:" + port;
	}

	@AfterAll
	static void afterAll() {
		driver.quit();
		driver = null;
	}

	@Test
	public void testHomePageInaccessibleWithoutLogin() {
		// given
		String url = baseURL + "/home";
		// when
		driver.get(url);
		// then
		assertThat(driver.getTitle()).isNotEqualTo("Home");
		assertThat(driver.getTitle()).isEqualTo("Login");
		assertThat(driver.getCurrentUrl()).contains("/login");
	}

	@Test
	public void testSignupAndLogoutFlow() throws InterruptedException {
		// given
		String signupURL = baseURL + "/signup";
		// when
		driver.get(signupURL);
		// then
		assertThat(driver.getTitle()).isEqualTo("Sign Up");
		assertThat(driver.getCurrentUrl()).contains("/signup");

		// given
		SignupPage signupPage = new SignupPage(driver);
		// when
		signupPage.signup(TEST_USER_FIRST_NAME, TEST_USER_LAST_NAME, TEST_USER_USERNAME, TEST_USER_PASSWORD);
		// then
		WebElement successMessage = signupPage.getSuccessMessage();
		assertThat(successMessage.getText()).isEqualTo("You successfully signed up! Please continue to the login page.");

		// given
		String loginURL = baseURL + "/login";
		// when
		driver.get(loginURL);
		// then
		assertThat(driver.getTitle()).isEqualTo("Login");
		assertThat(driver.getCurrentUrl()).contains("/login");

		// given
		LoginPage loginPage = new LoginPage(driver);
		// when
		loginPage.login(TEST_USER_USERNAME, TEST_USER_PASSWORD);
		// then
		assertThat(driver.getTitle()).isEqualTo("Home");
		assertThat(driver.getCurrentUrl()).contains("/home");

		// given
		HomePage homePage = new HomePage(driver);
		// when
		homePage.logout();
		// then
		assertThat(driver.getTitle()).isEqualTo("Login");
		assertThat(driver.getCurrentUrl()).contains("/login");

		// given
		String homeURL = baseURL + "/home";
		// when
		driver.get(homeURL);
		// then
		assertThat(driver.getTitle()).isEqualTo("Login");
		assertThat(driver.getCurrentUrl()).contains("/login");
	}

}
