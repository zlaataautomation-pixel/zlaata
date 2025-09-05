package pages;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import manager.FileReaderManager;
import objectRepo.AdminPanelObjRepo;
import utils.Common;

public final class AdminPanelPage extends AdminPanelObjRepo  {


	public AdminPanelPage(WebDriver driver) 
	{
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}
	public void clickUsingJavaScript(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", element);
	}



	public void uploadImage(String imagePath) {
	    driver.get(FileReaderManager.getInstance().getConfigReader().getApplicationAdminUrl());
	    type(adminEmail, FileReaderManager.getInstance().getJsonReader().getValueFromJson("AdminName"));
	    type(adminPassword, FileReaderManager.getInstance().getJsonReader().getValueFromJson("AdminPassword"));
	    click(adminLogin);
	    Common.waitForElement(2);

	    driver.get(Common.getValueFromTestDataMap("ExcelPath"));
	    click(homePageBannerDropDown);
	    selectHomePageValue.get(0).click();
	    Common.waitForElement(1);
		click(status);
		statusFilterSelect.get(0).click();
		Common.waitForElement(1);
	    boolean foundSortBy1 = false;

	    // Check if Sort By = 1 already exists
	    List<WebElement> bannerRows = driver.findElements(By.xpath(".//span[@class='d-inline-flex']"));
	    for (int i = 0; i < bannerRows.size(); i++) {
	        try {
	            WebElement currentRow = driver.findElements(By.xpath("(.//tr[@class='odd'])[1]")).get(i);

	            WebElement sortByInput = currentRow.findElement(By.xpath(".//input[@type='number']"));
	            String value = sortByInput.getAttribute("value").trim();

	            if (value.equals("1")) {
	                // Found row with Sort By = 1 → Click Edit and Save
	                WebElement editButton = currentRow.findElement(By.xpath(".//i[@class='las la-edit']"));
	                clickUsingJavaScript(editButton);
	                Common.waitForElement(2);
	                type(bannerTitle,"Home Page Automation Banner");
	                uploadImageInput.sendKeys(imagePath);
	                click(uploadButton); 
	                System.out.println("Edited and saved existing banner with Sort By = 1");
	                foundSortBy1 = true;
	                break;
	            }
	        } catch (Exception e) {
	            // Ignore rows without input/edit
	        }
	    }

	    // If no row with Sort By = 1 → Add new banner
	    if (!foundSortBy1) {
	        click(addHomePageBanner);
	        type(bannerTitle, "Home Page Automation Banner");
	        uploadImageInput.sendKeys(imagePath);
	        click(uploadButton);

	        // set Sort By = 1
	        type(sortBy, "1");
	        click(sortBySave);

	        System.out.println("Added new banner and saved with Sort By = 1");
	    }
	}


	public void verifyBannerOnHomePage(String expectedBannerTitle) {
	    switchToWindow(1);
	    driver.get(FileReaderManager.getInstance().getConfigReader().getApplicationUrl());

	    // FluentWait with 20 minutes max, polling every 1 second
	    Wait<WebDriver> wait = new FluentWait<>(driver)
	            .withTimeout(Duration.ofMinutes(20))
	            .pollingEvery(Duration.ofSeconds(1))
	            .ignoring(NoSuchElementException.class);

	    WebElement banner = wait.until(driver -> {
	        driver.navigate().refresh(); // refresh each poll to get updated banner
	        try {
	            WebElement element = driver.findElement(
	                By.xpath("//div[contains(@class,'banner') or contains(@class,'carousel')]//img[contains(@alt,'" 
	                         + expectedBannerTitle + "')]")
	            );
	            String alt = element.getAttribute("alt");
	            if (alt != null && alt.contains(expectedBannerTitle)) {
	                return element; // ✅ condition met
	            }
	        } catch (Exception e) {
	            // ignore and keep polling
	        }
	        return null; // keep waiting
	    });

	    // Final values
	    String actualAltText = banner.getAttribute("alt").trim();
	    String actualSrc = banner.getAttribute("src").trim();

	    System.out.println("✅ Expected banner: " + expectedBannerTitle);
	    System.out.println("👉 Actual alt: " + actualAltText);
	    System.out.println("👉 Actual src: " + actualSrc);

	    Assert.assertTrue("Banner alt does not match!", actualAltText.contains(expectedBannerTitle));
	}

	































	@Override
	public boolean verifyExactText(WebElement ele, String expectedText) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public WebDriver gmail(String browserName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isAt() {
		// TODO Auto-generated method stub
		return false;
	}


































}
