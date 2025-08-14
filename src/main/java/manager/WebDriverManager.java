package manager;
 

import java.net.MalformedURLException;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import enums.DriverType;
import enums.EnvironmentType;

 
public class WebDriverManager {
 
	private WebDriver driver;
	private DriverType driverType;
	private EnvironmentType environmentType;
 
	private static boolean isHeadLessModeEnabled;
 
	public WebDriverManager() {
		driverType = FileReaderManager.getInstance().getConfigReader().getBrowser();
		environmentType = FileReaderManager.getInstance().getConfigReader().getEnvironment();
		isHeadLessModeEnabled = FileReaderManager.getInstance().getConfigReader().getHeadlessModeOption();
	}
 
	public WebDriver getDriver() {
		if (driver == null)
			driver = createDriver();
		return driver;
	}
 
	private WebDriver createDriver() {
		switch (environmentType) {
		case LOCAL:
			driver = createLocalDriver();
			break;
		case REMOTE:
			try {
				driver = createRemoteDriver();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			break;
		}
		return driver;
	}
 
	private WebDriver createRemoteDriver() throws MalformedURLException {
		switch (driverType) {
		case CHROME:
			driver.manage().window().maximize();
			ChromeOptions chromeOptions = new ChromeOptions();
			if (isHeadLessModeEnabled) {
				ChromeOptions options = new ChromeOptions();
				chromeOptions.addArguments("--headless");
				chromeOptions.addArguments("window-size=1920,1080");
				chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
				chromeOptions.addArguments("safebrowsing.enabled", "false");
				chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
				chromeOptions.setAcceptInsecureCerts(true);
				chromeOptions.setExperimentalOption("useAutomationExtension", true);
	            chromeOptions.addArguments("--disable-save-password-bubble");
	            chromeOptions.addArguments("--disable-extensions");
	            Map<String, Object> prefs = new HashMap<>();
	            prefs.put("credentials_enable_service", false);
	            prefs.put("profile.password_manager_enabled", false);
	            chromeOptions.setExperimentalOption("prefs", prefs);
	            driver = new ChromeDriver(options);
			}
			break;
		case FIREFOX:
			driver.manage().window().maximize();
		case EDGE:
			driver.manage().window().maximize();
		}
		return driver;
	}
 
	private WebDriver createLocalDriver() {
		switch (driverType) {
		case FIREFOX:
			driver = new FirefoxDriver();
			break;
		case CHROME:
			ChromeOptions chromeOptions = new ChromeOptions();
			if (isHeadLessModeEnabled) {
				ChromeOptions options = new ChromeOptions();
				chromeOptions.addArguments("--headless");
				chromeOptions.addArguments("window-size=1920,1080");
				chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
				chromeOptions.addArguments("safebrowsing.enabled", "false");
				chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
				chromeOptions.setAcceptInsecureCerts(true);
				chromeOptions.setExperimentalOption("useAutomationExtension", true);
	            chromeOptions.addArguments("--disable-save-password-bubble");
	            chromeOptions.addArguments("--disable-extensions");
	            Map<String, Object> prefs = new HashMap<>();
	            prefs.put("credentials_enable_service", false);
	            prefs.put("profile.password_manager_enabled", false);
	            chromeOptions.setExperimentalOption("prefs", prefs);
	            driver = new ChromeDriver(options);
				
		}
			driver = new ChromeDriver();
			break;
		case EDGE:
			break;
		default:
			break;
		}
		if (FileReaderManager.getInstance().getConfigReader().getBrowserWindowSize()) {
			driver.manage().window().maximize();
		}
		driver.manage().timeouts().implicitlyWait(
				Duration.ofSeconds(FileReaderManager.getInstance().getConfigReader().getImplicitlyWait()));
		return driver;
	}
	public WebDriver gmail(String browserName) {
//		String strWorkingDirectory = System.getProperty("user.dir");
//		String strDownloadLocation = new StringBuilder(strWorkingDirectory).append(File.separator).append(Statics.DOWNLOAD_PATH).toString();

		ChromeOptions options = new ChromeOptions();

		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();

		// Hide save credentials prompt
		chromePrefs.put("credentials_enable_service", false);
		chromePrefs.put("profile.password_manager_enabled", false);

		// Default download directory
//		chromePrefs.put("download.default_directory", strDownloadLocation);
		chromePrefs.put("profile.default_content_setting_values.automatic_downloads", 1);
		chromePrefs.put("safebrowsing.enabled", "false");

		options.addArguments("disable-infobars");
		options.addArguments("disable-notifications");

		//*************************************** Headless--Controller ****************************************/
		options.addArguments("user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
		options.addArguments("--disable-blink-features=AutomationControlled");
		options.addArguments("--headless");
		options.addArguments("--window-size=1920,1032");
		/*************************************** Headless--Controller ****************************************/
		options.addArguments("--disable-password-manager-reauthentication");
		options.addArguments("--disable-save-password-bubble");
		options.addArguments("--disable-password-generation");
		options.addArguments("chrome.switches", "--disable-extensions");
		options.setExperimentalOption("useAutomationExtension", false);
		options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
		options.setExperimentalOption("prefs", chromePrefs);
		options.setAcceptInsecureCerts(true);
		options.setExperimentalOption("useAutomationExtension", false);
		return driver;
	}
	
	public void closeDriver() {
		driver.close();
		if ((driver != null)) {
			System.out.println("Driver is not null. driver.quit called");
			driver.quit();
		}
	}

	
}