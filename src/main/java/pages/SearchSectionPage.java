package pages;


import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import objectRepo.SearchBarObjRepo;
import utils.Common;

public final class SearchSectionPage  extends SearchBarObjRepo{

	public SearchSectionPage(WebDriver driver) 
	{
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}

	public void searchbarClikable() {
		Common.waitForElement(5);
		click(searchBarInput);
		try {
			if (headingRelatedProducts.isDisplayed()) 
			{
				System.out.println("the Search bar cliked");
			}
			else {
				System.out.println("the Search bar not  cliked");
			}
		} catch (Exception e) 
		{
			System.out.println(e);
		}

	}

	public void TrendingAndRelatedHeading()
	{
		searchbarClikable();
		String trendings = headingTrendings.getText();
		System.out.println("heading is displaying:"+trendings);
		String related = headingRelatedProducts.getText();
		System.out.println("heading is displaying:"+related);
	}

	public void clickAllTrendingProductsAndVerify() throws InterruptedException, TimeoutException {
		searchbarClikable();
		// XPath for trending product links
		String trendingLinksXPath = "//a[@class='product-redirect-tag cls_search_collection']";
		// XPath for product page heading - replace with actual XPath of the heading on product page
		String productHeadingXPath = "//h3[@class='prod_list_topic']"; 
		String trendings1 = headingTrendings.getText();
		System.out.println("heading is displaying:"+trendings1);
		// Fetch the trending product links initially
		List<WebElement> trendingsOptionList = driver.findElements(By.xpath(trendingLinksXPath));
		int total = trendingsOptionList.size();
		System.out.println("🔥 Total Trending Products: " + total);

		for (int i = 0; i < total; i++) {
			// Re-fetch the list each time, because DOM reloads after navigation
			trendingsOptionList = driver.findElements(By.xpath(trendingLinksXPath));
			WebElement product = trendingsOptionList.get(i);

			String productName = product.getText().trim();
			System.out.println("👉 Clicking on Trendings sub options: " + productName);

			// Scroll into view and click using JavaScript (safer)
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", product);
			Thread.sleep(500);

			try {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", product);
			} catch (Exception e) {
				System.out.println("Normal click failed, trying JavaScript click...");
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", product);

			}

			// Wait for product page heading to appear and verify
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(productHeadingXPath)));
			String headingText = driver.findElement(By.xpath(productHeadingXPath)).getText();
			System.out.println("✔️ Product page heading verified: " + headingText);

			// Navigate back to trending/search page
			searchbarClikable();
			Thread.sleep(1000);
		}
	}


	public void searchBarUserEnterLessThanThreeCharater() 
	{

		searchbarClikable();
		Common.waitForElement(2);

		// 2. Get search data from Excel (assume multiple words like "kurta set")
		String value = Common.getValueFromTestDataMap("Search bar");
		System.out.println("Testing name: " + value);

		// 3. Enter value in search bar
		searchBarInput.sendKeys(value);
		System.out.println("Entered Search Keyword: " + searchBarInput.getAttribute("value"));
		Common.waitForElement(2);

		// 4. Get URL before hitting ENTER
		String urlBeforeSearch = driver.getCurrentUrl();
		System.out.println("🔗 URL before search: " + urlBeforeSearch);

		// 5. Hit Enter
		searchBarInput.sendKeys(Keys.ENTER);
		Common.waitForElement(3); // wait for results

		// 6. Get URL after hitting Enter
		String urlAfterSearch = driver.getCurrentUrl();
		System.out.println("🔗 URL after search: " + urlAfterSearch);

		// 7. Compare URLs
		if (urlBeforeSearch.equals(urlAfterSearch)) {
			System.out.println("✅ Search did NOT cause a redirect. Same page retained.");
		} else {
			System.out.println("❌ Search caused a redirect. URLs do not match.");
		}
	}
	public void verifySearchSuggestionHeading() {
		searchbarClikable();
		Common.waitForElement(2);
		String value = Common.getValueFromTestDataMap("Search bar");
		System.out.println("🔍 Testing search keyword: " + value);	    
		searchBarInput.sendKeys(value);
		System.out.println("✅ Entered keyword: " + searchBarInput.getAttribute("value"));
		Common.waitForElement(2);
		searchBarInput.sendKeys(Keys.ENTER);
		Common.waitForElement(3);
		String actualMessage = heading.getText();
		System.out.println("🧾 Heading displayed: " + actualMessage);
		Assert.assertTrue("❌ Heading is empty or not as expected!", !actualMessage.trim().isEmpty());
		System.out.println("\u001B[32m" + "✅ The heading message displayed correctly: " + actualMessage + "\u001B[0m");
	}


	public void verifySearchHistoryDisplaying()
	{
		Common.waitForElement(2);

		click(searchBarInput);

		Common.waitForElement(2);

		// Step 2: Fetch search keyword from Excel/TestData
		String value = Common.getValueFromTestDataMap("Search bar");
		System.out.println("🔍 Testing search keyword: " + value);	    

		// Step 3: Type into search bar and press Enter
		searchBarInput.sendKeys(value);
		System.out.println("✅ Entered keyword: " + searchBarInput.getAttribute("value"));
		Common.waitForElement(2);
		searchBarInput.sendKeys(Keys.ENTER);
		Common.waitForElement(3);

		// Step 4: Reopen the search bar (to check search history/suggestion)
		click(searchBarInput);
		Common.waitForElement(3);

		String actualMessage =headingSearchHistory.getText();
		System.out.println("🧾 Heading displayed: " + actualMessage);

	}
	public void verifysearchHistoryKeyworddisplayAnduserabletoDelete() {
		Common.waitForElement(2);
		click(searchBarInput);
		String value = Common.getValueFromTestDataMap("Search bar");
		System.out.println("🔍 Testing search keyword: " + value);

		searchBarInput.sendKeys(value);
		System.out.println("✅ Entered keyword: " + searchBarInput.getAttribute("value"));
		Common.waitForElement(2);
		searchBarInput.sendKeys(Keys.ENTER); // Hit Enter
		click(searchBarInput);
		Common.waitForElement(3);

		String historyHeading = headingSearchHistory.getText();
		System.out.println("🧾 History heading displayed: " + historyHeading);

		String displayedKeyword = newSearchHistorykeywrod.getText();
		System.out.println("🧾 Search history keyword displayed: " + displayedKeyword);

		click(searchHistoryRemoveButtons);  // Assuming this deletes the correct keyword

		click(searchBarInput);
		Common.waitForElement(2);

		List<WebElement> remainingKeywords = driver.findElements(By.xpath("//your_xpath_for_history_keywords_here"));

		boolean keywordStillExists = remainingKeywords.stream()
				.anyMatch(el -> el.getText().equalsIgnoreCase(value));

		if (!keywordStillExists) {
			System.out.println("✅ Keyword successfully deleted from search history.");
		} else {
			System.out.println("❌ Keyword still present in search history.");
		}

		Assert.assertFalse("Search keyword should be removed from history!", keywordStillExists);
	}

	public void enterProductNameExactlyRedirectToProduct() {
		Common.waitForElement(2);
		click(searchBarInput);
		Common.waitForElement(2);
		String value = Common.getValueFromTestDataMap("Search bar");
		System.out.println("🔍 Testing search keyword: " + value);

		searchBarInput.clear();
		searchBarInput.sendKeys(value);
		System.out.println("✅ Entered keyword: " + searchBarInput.getAttribute("value"));
		Common.waitForElement(1);
		searchBarInput.sendKeys(Keys.ENTER);
		Common.waitForElement(3);

		String actualHeading = heading.getText().trim();
		System.out.println("🧾 Search Result Heading: " + actualHeading);
		Assert.assertFalse("❌ Heading is empty!", actualHeading.isEmpty());
		System.out.println("\u001B[32m✅ Heading displayed correctly: " + actualHeading + "\u001B[0m");

		String displayedProductName = productName.getText().trim();
		System.out.println("🧾 Product Name Displayed: " + displayedProductName);

		Assert.assertEquals("❌ Heading and product name mismatch!",
				actualHeading.toLowerCase(), displayedProductName.toLowerCase());

		System.out.println("\u001B[32m✅ Heading and product name match: " + actualHeading + "\u001B[0m");
	}

	public void recentlyViewProductAppears() {
		Common.waitForElement(2);

		click(searchBarInput);
		Common.waitForElement(2);

		String value = Common.getValueFromTestDataMap("Search bar");
		System.out.println("🔍 Testing search keyword: " + value);

		searchBarInput.clear();
		searchBarInput.sendKeys(value);
		System.out.println("✅ Entered keyword: " + searchBarInput.getAttribute("value"));
		Common.waitForElement(1);
		searchBarInput.sendKeys(Keys.ENTER);
		Common.waitForElement(3);

		String actualHeading = heading.getText().trim();
		System.out.println("🧾 Search Result Heading: " + actualHeading);
		Assert.assertFalse("❌ Heading is empty!", actualHeading.isEmpty());
		System.out.println("\u001B[32m✅ Heading displayed correctly: " + actualHeading + "\u001B[0m");

		click(productListingImage);
		Common.waitForElement(2);
		click(buyNowButton);
		Common.waitForElement(3);

		click(searchBarInput);
		Common.waitForElement(2);

		String recentHeading = recentlyViwed.getText().trim();
		System.out.println("🧾 Recently Viewed Section Heading: " + recentHeading);

		String recentProduct = recentlyViwedProduct.getText().trim();
		System.out.println("🧾 Recently Viewed Product Name: " + recentProduct);

		Assert.assertEquals("❌ Recently viewed product does not match searched product!",
				actualHeading.toLowerCase(), recentProduct.toLowerCase());

		System.out.println("\u001B[32m✅ Recently viewed product matches the searched product: " + recentProduct + "\u001B[0m");
	}


	public void validateRelatedQueriesAndHeadings() throws InterruptedException {
		String keyword = Common.getValueFromTestDataMap("Search bar"); // e.g., "yellow"
		System.out.println("🔍 Searching for keyword: " + keyword);

		click(searchBarInput);
		searchBarInput.sendKeys(keyword);
		Common.waitForElement(2); // Wait for related queries dropdown

		List<WebElement> queries = driver.findElements(By.xpath("//a[@class='product-redirect-tag cls_search_collection']"));
		int totalQueries = queries.size();
		System.out.println("🔽 Total related queries found: " + totalQueries);

		for (int i = 0; i < totalQueries; i++) {
			queries = driver.findElements(By.xpath("//a[@class='product-redirect-tag cls_search_collection']"));
			WebElement query = queries.get(i);
			String expectedHeading = query.getText().trim();
			System.out.println("👉 Clicking related query: " + expectedHeading);

			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", query);
			Thread.sleep(500);
			try {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", query);
			} catch (Exception e) {
				System.out.println("⚠️ Normal click failed. Trying JS click.");
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", query);
			}
			Common.waitForElement(2);
			String actualHeading = heading.getText().trim();

			if (actualHeading.equalsIgnoreCase(expectedHeading)) {
				System.out.println("✅ Heading matched: " + actualHeading);
			} else {
				System.err.println("❌ Heading mismatch! Expected: '" + expectedHeading + "' but found: '" + actualHeading + "'");
				Assert.fail("Heading mismatch for: " + expectedHeading);
			}
			Common.waitForElement(1);
			click(searchBarInput);
			searchBarInput.clear();
			searchBarInput.sendKeys(keyword);
			Common.waitForElement(1);
		}

		System.out.println("🏁 Validation completed for all related queries.");
	}



	public void searchKeyWordRedirectToCorrectpage() {
		Common.waitForElement(2);
		click(searchBarInput);
		Common.waitForElement(2);
		String value = Common.getValueFromTestDataMap("Search bar");
		System.out.println("🔍 Step 1: Entering search keyword from Excel: " + value);
		searchBarInput.sendKeys(value);
		System.out.println("✅ Step 1: Keyword entered in input: " + searchBarInput.getAttribute("value"));
		Common.waitForElement(2);
		searchBarInput.sendKeys(Keys.ENTER); // First redirection
		Common.waitForElement(3);

		String firstRedirectionHeading = heading.getText();
		System.out.println("📄 Step 2: Heading after first redirection: " + firstRedirectionHeading);
		click(searchBarInput);
		Common.waitForElement(3);
		String historyHeading = headingSearchHistory.getText();
		System.out.println("🧾 Step 3: Search history heading displayed: " + historyHeading);

		String displayedKeyword = newSearchHistorykeywrod.getText();
		System.out.println("🧾 Step 3: Search history keyword displayed: " + displayedKeyword);

		click(newSearchHistorykeywrod); // Second redirection
		Common.waitForElement(3);

		String secondRedirectionHeading = heading.getText();
		System.out.println("📄 Step 4: Heading after second redirection: " + secondRedirectionHeading);

		if (firstRedirectionHeading.equals(secondRedirectionHeading)) {
			System.out.println("❌ FAIL: Both redirections landed on the SAME page.");
		}
		else {
			System.out.println("❌ FAIL: Both redirections differnE page.");

		}
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

