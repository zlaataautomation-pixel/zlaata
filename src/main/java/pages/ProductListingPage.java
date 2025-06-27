package pages;


import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import objectRepo.ProductListObjRepo;
import utils.Common;

public final class ProductListingPage extends ProductListObjRepo {



	public ProductListingPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}

	public void homeCrumbLink() {
		Common.waitForElement(5);
		Actions actions = new Actions(driver);
		actions.moveToElement(shopMenu);
		actions.moveToElement(category).click().build().perform();
		Common.waitForElement(2);
		click(homeCrumbLink);
		String banner = banners.getTagName();
		Assert.assertTrue("Banner is Visible", banner.length() <= 50);

	}
	public void pLpHeading() {
		Common.waitForElement(5);
		Actions actions = new Actions(driver);
		actions.moveToElement(shopMenu);
		actions.moveToElement(category).click().build().perform();
		String heading = shopPageHead.getText();
		Assert.assertTrue("Product List Page Heading is displaying", heading.length() <= 50);
}

	public void pagination() {
		Common.waitForElement(5);
		Actions actions = new Actions(driver);
		actions.moveToElement(shopMenu);
		actions.moveToElement(category).click().build().perform();

		try {
			actions.moveToElement(pagination);
			if (pagination.isDisplayed()) {
				Assert.assertTrue((verifyDisplayed(pagination)));
			}

		} catch (Exception e) {
			System.out.println("Caught an exception: " + e.getMessage());
			NoSuchElementException e1 = new NoSuchElementException("A NoSuchElementException exception occurred");
			e1.initCause(e);
			throw e1;
		}
	}

	public void pagiNationArrows() {
		Common.waitForElement(5);
		Actions actions = new Actions(driver);
		actions.moveToElement(shopMenu);
		actions.moveToElement(category).click().build().perform();

		try {
			if (pagination.isDisplayed()) {
				actions.moveToElement(paginationNext).click().build().perform();
			}
			else if(paginationNext.isEnabled()){

				actions.moveToElement(paginationNext).click().build().perform();

			}
			else  {
				actions.moveToElement(shopMenu);
				actions.moveToElement(category).click().build().perform();
				actions.moveToElement(paginationNext).click().build().perform();
				Common.waitForElement(2);
				actions.moveToElement(paginationPrv).click().build().perform();

			}

		} catch (Exception e) {
			System.out.println("Caught an exception: " + e.getMessage());
			NoSuchElementException e1 = new NoSuchElementException("A NoSuchElementException exception occurred");
			e1.initCause(e);
			throw e1;

		}

	}

	public void paginationNumber() {
		Common.waitForElement(5);
		Actions actions = new Actions(driver);
		actions.moveToElement(shopMenu);
		actions.moveToElement(category).click().build().perform();
		List<WebElement> clickRandomNumber = driver.findElements(By.xpath("//div[@class='pagi_count_wrap']"));
		Collections.shuffle(clickRandomNumber);

		if (!clickRandomNumber.isEmpty()) {
			WebElement randomNumber = clickRandomNumber.get(0);
			actions.moveToElement(randomNumber).click().build().perform();

		}
	}

	public void showFilter() {
		Common.waitForElement(5);
		Actions actions = new Actions(driver);
		actions.moveToElement(shopMenu);
		actions.moveToElement(category).click().build().perform();
		Common.waitForElement(5);
		actions.moveToElement(showFilter).click().build().perform();
		try {

			if (showFilterMenu.isDisplayed()) {
				Assert.assertTrue((verifyDisplayed(showFilterMenu)));
			}

		} catch (Exception e) {
			System.out.println("Caught an exception: " + e.getMessage());
			NoSuchElementException e1 = new NoSuchElementException("A NoSuchElementException exception occurred");
			e1.initCause(e);
			throw e1;
		}
	}

	public void sortByFilter() {
		Common.waitForElement(5);
		Actions actions = new Actions(driver);
		actions.moveToElement(shopMenu);
		actions.moveToElement(category).click().build().perform();
		Common.waitForElement(5);
		actions.moveToElement(sortBy).click().build().perform();

	}
	public void basicFilterFunction() {
		Common.waitForElement(5);
		Actions actions = new Actions(driver);
		actions.moveToElement(shopMenu);
		actions.moveToElement(category).click().build().perform();
		Common.waitForElement(2);
		actions.moveToElement(showFilter).click().build().perform();
		List<WebElement> clickShowFilterMenu = driver.findElements(By.xpath("//span[@class='prod_filter_heading']"));
		Collections.shuffle(clickShowFilterMenu);

		if (!clickShowFilterMenu.isEmpty()) {
			WebElement randomFilter = clickShowFilterMenu.get(0);
			actions.moveToElement(randomFilter).click().build().perform();
		}
		List<WebElement> clickShowFilterSubMenu = driver.findElements(By.xpath("//div[@class='prod_filter_value']"));
		Collections.shuffle(clickShowFilterSubMenu);

		if (priceRangeFilter.isDisplayed()) {
			click(maxPriceFilter);
			maxPriceFilter.clear();
			Common.waitForElement(2);
			type(maxPriceFilter,Common.getValueFromTestDataMap("Mobile Number"));
			Common.waitForElement(2);
			click(filterApply);
			click(showFilter);
			click(filterClearAll);
			Common.waitForElement(2);
			click(closeShowFilter);
		}


		click(filterApply);
		Common.waitForElement(2);
		click(showFilter);
		click(filterClearAll);
	}

	public void allsortBy() {
		Common.waitForElement(5);
		Actions actions = new Actions(driver);
		actions.moveToElement(shopMenu);
		actions.moveToElement(category).click().build().perform();
		Common.waitForElement(2);
		click(sortBy);
		actions.moveToElement(sortByWhatsNew).click().build().perform();
		List<WebElement> newTag = driver.findElements(By.xpath("//span[@class='product_list_tag product_list_new_product_tag']"));
		newTag.get(0);
		System.out.println("The New product found in the List:"+newTag);
		Common.waitForElement(1);
		actions.moveToElement(sortBy).click().build().perform();
		actions.moveToElement(sortByDiscountHightoLow).click().build().perform();
		Common.waitForElement(1);
		actions.moveToElement(sortBy).click().build().perform();
		actions.moveToElement(sortByDiscountLowtoHigh).click().build().perform();
		Common.waitForElement(1);
		actions.moveToElement(sortBy).click().build().perform();
		actions.moveToElement(sortByPriceHightoLow).click().build().perform();
		Common.waitForElement(1);
		actions.moveToElement(sortBy).click().build().perform();
		actions.moveToElement(sortByPriceLowtoHigh).click().build().perform();
		
//		try {

			


//			List<Double> allDiscounts = new ArrayList<>();
//
//			// Loop through pagination if exists
//			while (true) {
//				List<WebElement> discountElements = driver.findElements(By.xpath("//span[@class='product_list_cards_discount_percent']"));
//
//				for (WebElement element : discountElements) {
//					String discountText = element.getText(); // Example: "25% Off"
//					double discount = extractDiscount(discountText);
//					allDiscounts.add(discount);
//				}
//
//				// Try to click next if exists, else break
//				List<WebElement> nextButtons = driver.findElements(By.xpath("//div[@class='pagi_next_btn']"));
//				if (nextButtons.size() > 0 && nextButtons.get(0).isDisplayed()) {
//					nextButtons.get(0).click();
//					Common.waitForElement(5);// wait for next page load
//				} else {
//					break;
//				}
//			}
//
//			// Check if the list is sorted in ascending order
//			List<Double> sortedDiscounts = new ArrayList<>(allDiscounts);
//			Collections.sort(sortedDiscounts);
//
//			if (allDiscounts.equals(sortedDiscounts)) {
//				System.out.println("✅ Discount values are correctly sorted from Low to High.");
//			} else {
//				System.out.println("❌ Discount sorting is incorrect!");
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
		

	}



	private double extractPrice(String priceText) {
		// TODO Auto-generated method stub
		return 0;

	}

	// Helper function to extract numeric discount
	public static double extractDiscount(String text) {
		return Double.parseDouble(text.replaceAll("[^0-9]", ""));
	}
















	public void wishListIcon() {
        LoginPage login = new LoginPage(driver);
        login.userLogin();
		Common.waitForElement(5);
		Actions actions = new Actions(driver);
		actions.moveToElement(shopMenu);
		actions.moveToElement(category).click().build().perform();
		List<WebElement> clickWishList = driver.findElements(By.xpath("//div[@class='product_list_cards_btn_group product_list_wishlist_icon Cls_whistList ']"));
		Collections.shuffle(clickWishList);

		if (!clickWishList.isEmpty()) {
			WebElement randomwishList = clickWishList.get(0);
			actions.moveToElement(randomwishList).click().build().perform();


		}
		try {

			if (wishListMsg.isDisplayed()) {
				Assert.assertTrue((verifyDisplayed(wishListMsg)));
			}

		} catch (Exception e) {
			System.out.println("Caught an exception: " + e.getMessage());
			NoSuchElementException e1 = new NoSuchElementException("A NoSuchElementException exception occurred");
			e1.initCause(e);
			throw e1;
		}
	}



	public void addToCart() {
		Common.waitForElement(5);
		Actions actions = new Actions(driver);
		actions.moveToElement(shopMenu);
		actions.moveToElement(category).click().build().perform();
		List<WebElement> addProduct = driver.findElements(By.xpath("//button[@class='product_list_cards_btn']"));
		Collections.shuffle(addProduct);

		if (!addProduct.isEmpty()) {
			WebElement randomProduct = addProduct.get(0);
			actions.moveToElement(randomProduct).click().build().perform();
			Common.waitForElement(2);
			click(addToCart);
			
			
		    

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
