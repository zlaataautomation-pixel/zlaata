package stepDef;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.Augmenter;
import com.google.common.io.Files;
import context.Context;
import context.TestContext;
import dataProviders.TestData;
import manager.FileReaderManager;
import utils.Common;
import utils.DateUtils;
import utils.EmailSendUtils;
import utils.ExcelUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import java.util.logging.Level;
import java.util.logging.Logger;




public class Hooks {
	TestContext testContext;
	static {
        Logger.getLogger("org.openqa.selenium.remote.http.WebSocket$Listener").setLevel(Level.OFF);
    }

	public Hooks(TestContext context) {
		testContext = context;
		driver=context.getWebDriverManager().getDriver();
	}

	public static Map<String, String> testDataMap;
	public static Map<String, String> paymentFileUploadMap;
	public static String scenarioName;
	public static Scenario myScenario;
	public static WebDriver driver;
	private static Scenario scenario;
	




	@Before(order=0)
	public void preCondition(Scenario scenario) throws IOException {
		
		
//		myScenario = scenario;
//		try {
//	        if (driver != null) {
//	            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
//	            scenario.attach(screenshot, "image/png", "Initial Screenshot");
//	        } else {
//	            scenario.log("⚠️ Driver is null, screenshot not taken.");
//	        }
//	    } catch (Exception e) {
//	        scenario.log("❌ Failed to capture screenshot in @Before hook: " + e.getMessage());
//	        e.printStackTrace();
//	    }
        
        
		scenarioName = scenario.getName();
		Long startTime = System.currentTimeMillis();
		scenarioName = scenario.getName();
		testContext.getScenarioContext().setContext(Context.TC_START_TIME, startTime);
		String scenarioName = scenario.getName().trim();
		String[] subStrings = scenarioName.split("\\|");
		String tcID = subStrings[0].trim();
		testContext.getScenarioContext().setContext(Context.TC_ID, tcID);
		scenarioName = subStrings[1].trim();
		String tdID = subStrings[2].replace("\"", "").trim();
		testContext.getScenarioContext().setContext(Context.TD_ID, tdID);
		TestData testData = new TestData(tcID);
		testDataMap = testData.getTestData(tcID, tdID, scenarioName);
		//				String reportFileName = FileReaderManager.getInstance().getConfigReader().getTestReportPath(tcID, tdID);
		//				testContext.getScenarioContext().setContext(Context.TEST_REPORT_FILE_NAME, reportFileName);

	}
	@Before
	public void before_Scenario(Scenario scenario) {
	    System.out.println("\n---------------------------------------------------");
	    System.out.println("Starting scenario: " + scenario.getName());
	    Hooks.scenario = scenario;
	}
	public static Scenario getScenario() {
        return scenario;
    }
	
//	@After(order = 1)
//	public void attachScreenshot(Scenario scenario) {
//		if (scenario.isFailed()) {
//			
//			String scenarioName = scenario.getName();
//			String[] subStrings = scenarioName.split("\\|");
//			String tcID = subStrings[0].trim();
//			if ((tcID.split("_")[1]).contains("UI")) {
//				try {
//					// This takes a screenshot from the driver at save it to the specified location
//
//					File sourcePath = ((TakesScreenshot) testContext.getWebDriverManager().getDriver())
//							.getScreenshotAs(OutputType.FILE);
//					String filePath = System.getProperty("user.dir") + "/target/cucumber-reports/screenshots/";
//					Common.createDirIfNotExists(filePath);
//					File destinationPath = new File(filePath + File.separator + tcID + ".png");
//					// Copy taken screenshot from source location to destination location
//					Files.copy(sourcePath, destinationPath);
//					// This attach the specified screenshot to the test
//					//					Reporter.addScreenCaptureFromPath(destinationPath.toString());
//
//
//				} catch (IOException e) {
//					throw new RuntimeException("Some exceptions occurred while taking sceenshot!!! Please check ");
//				}
//			} else {
//				System.out.println("Sorry can't take any screenshot");
//			}
//		} else {
//			System.out.println("Scenario getting passed!!! No Screen capture required!!!");
//		}
//
//	}

	public static void attachFullPageScreenshot(WebDriver driver) {
		boolean takeScreenshotForAll = FileReaderManager.getInstance().getConfigReader()
				.isScreenShotRequiredForAllScenario();
		System.out.println("Scenario Name :: " + Hooks.scenarioName);

		if (takeScreenshotForAll) {
			String[] subStrings = Hooks.scenarioName.split("\\|");
			String tcID = subStrings[0].trim();

			if ((tcID.split("_")[1]).contains("UI")) {
				try {
					// Ensure the driver is augmented to take full page screenshots
					WebDriver augmentedDriver = new Augmenter().augment(driver);

					// This takes a full page screenshot from the driver and saves it to the
					// specified location
					File sourcePath = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
					String filePath = System.getProperty("user.dir") + "/target/cucumber-reports/screenshots/";
					Common.createDirIfNotExists(filePath);
					File destinationPath = new File(filePath + File.separator + tcID + "-"
							+ DateUtils.getCurrentLocalDateTimeStamp("yyyyMMddHHmmssSSS") + ".png");

					// Copy the taken screenshot from source location to destination location
					FileHandler.copy(sourcePath, destinationPath);

					// Attach the specified screenshot to the test
					//					Reporter.addScreenCaptureFromPath(destinationPath.toString());

					System.out.println("Screenshot taken: " + destinationPath.toString());
				} catch (IOException e) {
					throw new RuntimeException("Some exceptions occurred while taking screenshot!!! Please check ", e);
				}
			} else {
				System.out.println("Scenario ID does not contain 'UI'. No screenshot taken.");
			}
		} else {
			System.out.println("Scenario passed!!!! No Screen capture required!!!");
		}
	}	


	//	@After(order = 2)
	public void findTotalTimeForExecution() {
		String tcID = (String) testContext.getScenarioContext().getContext(Context.TC_ID);
		//		Long startTime = (Long) testContext.getScenarioContext().getContext(Context.TC_START_TIME);
		//		Long totalTimeTaken = System.currentTimeMillis() - startTime;
		String module = tcID.split("_")[1].toUpperCase();
		switch (module) {
		case "UI":
			//		case "RT":
			//			Reporter.addScenarioLog("<p><b>" + "Execution time in Seconds: "			//					+ TimeUnit.MILLISECONDS.toSeconds(totalTimeTaken) + "</b></p>");
			//			break;
			//		case "BP":
			//			Reporter.addScenarioLog("<p><b>" + "Execution time in Minutes: "
			//					+ TimeUnit.MILLISECONDS.toMinutes(totalTimeTaken) + "</b></p>");
			break;
		default:
			throw new RuntimeException(
					"Test Data Path not specified in the application.properties file for module" + module);
		}


	}
	 
	
	

	@After
	public void afterScenario(Scenario scenario) {
		
		
		if (scenario.isFailed()) {
			System.out.println("\n---------------------------------------------------");
	        System.out.println("\u001B[31m" + "Scenario failed: " + scenario.getName() + "\u001B[0m");
	        System.out.println("---------------------------------------------------\n");
	       
	       
	        try {
		        if (driver != null) {
		            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		            scenario.attach(screenshot, "image/png", "Initial Screenshot");
		            
		           
		        } else {
		        	
		            scenario.log("⚠️ Driver is null, screenshot not taken.");
		        }
		    } catch (Exception e) {
		        scenario.log("❌ Failed to capture screenshot in @After hook: " + e.getMessage());
		        e.printStackTrace();
		    }
	        
	        
			
		} else {
			System.out.println("\n---------------------------------------------------");
	        System.out.println("\u001B[32m"+ "Scenario passed: " + scenario.getName() + "\u001B[0m");
	        System.out.println("Scenario getting passed!!! No Screen capture required!!!");
		}
	}
	
	
	@After(order = 1)
	public void tearDown() throws IOException {
		testContext.getWebDriverManager().closeDriver();
		EmailSendUtils.sendEmail();

	}
	
	public static void takeScreenshot() {

		try {
			myScenario.attach(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES), "image/png", "New");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

