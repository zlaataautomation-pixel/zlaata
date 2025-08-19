package pages;

import java.time.Duration; // CORRECT

import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.Scenario;
import objectRepo.ProductDetailsPageObjRepo;
import utils.Common;

public final class ProductDetailsPage extends ProductDetailsPageObjRepo {
	String currentProductName;

	public ProductDetailsPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}
	private void RandomProduct() {

		Actions actions = new Actions(driver);
		actions.moveToElement(shopMenu);
		actions.moveToElement(category).click().build().perform();
		actions.moveToElement(sortBy).click().build().perform();
		actions.moveToElement(sortByPriceHightoLow).click().build().perform();

		List<WebElement> clickRandomProduct = driver.findElements(By.xpath("//div[@class='product_list_cards_list ']"));
		Collections.shuffle(clickRandomProduct);

		if (!clickRandomProduct.isEmpty()) {
			WebElement randomProduct = clickRandomProduct.get(0);
			currentProductName = productListingName.getText().trim().replaceAll("\\s+", " ").toLowerCase();
			actions.moveToElement(randomProduct).click().build().perform();

		}
	}


	public void clickUsingJavaScript(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", element);
	}


	public void productPrice() {

		Actions actions = new Actions(driver);
		actions.moveToElement(shopMenu);
		actions.moveToElement(category).click().build().perform();
		List<WebElement> clickRandomProduct = driver.findElements(By.xpath("//div[@class='product_list_cards_list ']"));
		Collections.shuffle(clickRandomProduct);

		if (!clickRandomProduct.isEmpty()) {
			WebElement randomProduct = clickRandomProduct.get(0);
			actions.moveToElement(randomProduct).click().build().perform();
			Common.waitForElement(1);
			String productPrices = productPrice.getText();
			System.out.println("The product is : " +productPrices );
		}
		else {
			System.err.println("Not redirected to Product details page");
		}

	}


	public void discountPercentageCalculation() {

		RandomProduct();
		Common.waitForElement(1);

		String PricesText = "";
		if (!promotionalPriceElement.isEmpty()) {
			PricesText = promotionalPriceElement.get(0).getText().replaceAll("[^0-9]", "");
		} else {
			PricesText = currentPrice.getText().replaceAll("[^0-9]", "");
		}

		String actualPricesText = actualPrice.getText().replaceAll("[^0-9]", "");
		String discountPercentText = discountPercentage.getText().replaceAll("[^0-9]", "");

		if (PricesText.isEmpty() || actualPricesText.isEmpty() || discountPercentText.isEmpty()) {
			System.out.println("⚠️ One or more price values are missing or unreadable.");
			return;
		}

		int PriceValue = Integer.parseInt(PricesText);
		int atualPriceValue = Integer.parseInt(actualPricesText);
		int displayedDiscountPercent = Integer.parseInt(discountPercentText);

		int calculatedDiscountPercent = Math.round(((atualPriceValue - PriceValue) * 100.0f) / atualPriceValue);

		System.out.println("-------------------------------------");
		System.out.println("Product Discounted Percentage Summary");
		System.out.println("-------------------------------------");
		System.out.println("Product Actual Price     : " + atualPriceValue);
		System.out.println("Product Current Price    : " + PriceValue);
		System.out.println("Expected Discount %     : " + displayedDiscountPercent);
		System.out.println("Actual Discount %    : " + calculatedDiscountPercent);

		if (displayedDiscountPercent == calculatedDiscountPercent) {
			System.out.println("✅ Discount % is correct.");
		} else {
			System.out.println("❌ Mismatch in Discount %. Please verify.");
		}
	}

	public void productImageChage() {
		ProductDetailsPage productPage = new ProductDetailsPage(driver) ;
		RandomProduct();
		Common.waitForElement(1);
		System.out.println("📌 Verifying initial state...");
		if (productPage.isBackArrowDisabled()) {
			System.out.println("✅ Back arrow is disabled initially.");
		} else {
			System.out.println("❌ Back arrow should be disabled initially.");
		}

		// Click next arrow until it's disabled or unavailable
		int forwardClicks = 0;
		while (productPage.isNextArrowEnabled()) {
			productPage.clickNextArrow();
			Common.waitForElement(1);
			forwardClicks++;
		}

		System.out.println("➡️ Viewed all images with " + forwardClicks + " next clicks.");

		// Now click back until back arrow becomes disabled
		int backClicks = 0;
		while (productPage.isBackArrowEnabled()) {
			productPage.clickBackArrow();
			Common.waitForElement(1);
			backClicks++;
		}

		System.out.println("⬅️ Returned to first image with " + backClicks + " back clicks.");

		if (productPage.isBackArrowDisabled()) {
			System.out.println("✅ Back arrow is disabled again on the first image.");
		} else {
			System.out.println("❌ Back arrow should be disabled again on the first image.");
		}
	}



	public int getImageCount() {
		return productImages.size();
	}

	public boolean isBackArrowEnabled() {
		return !driver.findElements(backArrowEnabled).isEmpty();
	}

	public boolean isBackArrowDisabled() {
		return !driver.findElements(backArrowDisabled).isEmpty();
	}

	public boolean isNextArrowEnabled() {
		try {
			return nextArrow.isDisplayed() && nextArrow.isEnabled() && 
					!nextArrow.getAttribute("class").contains("disabled");
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void clickNextArrow() {
		if (isNextArrowEnabled()) {
			nextArrow.click();
		}
	}

	public void clickBackArrow() {
		List<WebElement> backArrowElements = driver.findElements(backArrowEnabled);
		if (!backArrowElements.isEmpty()) {
			backArrowElements.get(0).click();
		}
	}

	public void wishList() {
		ProductDetailsPage productPage = new ProductDetailsPage(driver);
		LoginPage login = new LoginPage(driver);
		login.userLogin();
		Common.waitForElement(2);
		RandomProduct();
		String productName = productPage.productPrice.findElement(By.xpath(".//h4[@class='prod_name']")).getText().trim();
		System.out.println("Product details Product Name is: " + productName);
//		WebElement wishlistBtn = productPage.productPrice.findElement(By.xpath("//div[@class='prod_main_details']//div[@class='prod_wishlist_btn Cls_prod_wishlist_btn']"));
//		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", wishlistBtn);
//		clickUsingJavaScript(wishlistBtn);
//		Common.waitForElement(2);
		
		
		
//		// Click wishlist icon to navigate
//		productPage.wishlistIcon.click();
		
		new WebDriverWait(driver, Duration.ofSeconds(10));

		// Use flexible locator that always finds the wishlist button (whether liked or not)
		WebElement wishlistBtn = productPage.productPrice.findElement(
		    By.xpath("//div[@class='prod_main_details']//div[contains(@class,'prod_wishlist_btn')]")
		);

		// Check class attribute
		String wishlistClass = wishlistBtn.getAttribute("class");
//		System.out.println("🔍 Wishlist button class: " + wishlistClass);

		if (!wishlistClass.contains("liked")) {
		    // 👉 Case: Wishlist not selected — click to add
		    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", wishlistBtn);
		    clickUsingJavaScript(wishlistBtn);
		    Common.waitForElement(2);
		    System.out.println("✅ Wishlist was not highlighted. Product added.");
		    productPage.wishlistIcon.click();
		} else {
		    // 👉 Case: Wishlist already selected — remove it
		    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", wishlistBtn);
		    clickUsingJavaScript(wishlistBtn);  // remove
		    System.out.println("💔 Wishlist already selected. Removed.");
		    Common.waitForElement(2);

		    // 👉 Re-locate button after DOM change
		    WebElement reAddBtn = productPage.productPrice.findElement(
		        By.xpath("//div[@class='prod_main_details']//div[contains(@class,'prod_wishlist_btn')]")
		    );
		    clickUsingJavaScript(reAddBtn);  // re-add
		    System.out.println("💖 Wishlist re-added.");
		    Common.waitForElement(2);
		    productPage.wishlistIcon.click();
		}
	
		Common.waitForElement(3);

		// Verify product present in wishlist
		boolean found = false;
		for (WebElement product : productPage.wishlistProduct) {
			if (product.getText().trim().equalsIgnoreCase(productName)) {
				found = true;
				break;
			}
		}

		if (found) {
			System.out.println("✅ Product Name added to WishList is: " + productName);
			System.out.println("successfully moved ");
		} else {
			System.out.println("❌ Product '" + productName + "' NOT found in wishlist.");
		}
	}


		public void verifyBestPriceCalculation() {
			LoginPage login = new LoginPage(driver);
			login.userLogin();
			Common.waitForElement(3);
			Actions actions = new Actions(driver);
			// Step 0: Clear cart before starting
			click(bagIcon);
			Common.waitForElement(2);
			List<WebElement> removeIcons = driver.findElements(By.xpath("//div[@title='Delete']"));
			for (WebElement removeBtn : removeIcons) {
				Common.waitForElement(2);
				removeBtn.click();
				Common.waitForElement(5);
			}
			click(closeBag); // Close cart and go back
			Common.waitForElement(2);
			// Step 1: Navigate to product listing
			actions.moveToElement(shopMenu).moveToElement(category).click().build().perform();
			Common.waitForElement(2);
			// Step 2: Sort by "Price: Low to High"
			click(sortBy);
			Common.waitForElement(1);
			actions.moveToElement(sortByPriceLowtoHigh).click().build().perform();
			Common.waitForElement(2);
			// Step 3: Add lowest priced product
			List<WebElement> allProducts = driver.findElements(By.xpath("//div[@class='product_list_cards_list ']"));
			if (allProducts.isEmpty()) {
				System.out.println(":x: No products found.");
				return;
			}
			WebElement lowestProduct = allProducts.get(0);
			String currentProductName = lowestProduct.findElement(By.xpath(".//h2[@class='product_list_cards_heading']")).getText().trim().toLowerCase();
			System.out.println(":shopping_trolley: Lowest Product Selected: " + currentProductName);
			actions.moveToElement(lowestProduct).click().build().perform();
			Common.waitForElement(2);
			// Step 4: Validate PDP product name
			String pdpProductName = productName.getText().trim().toLowerCase();
			if (!pdpProductName.contains(currentProductName)) {
				System.out.println(":x: Product mismatch between listing and detail page.");
				return;
			}
			// Step 5: Get promotional or normal price
			double price = 0;
			try {
				WebElement promoPrice = driver.findElement(By.xpath("//span[@class='product_list_cards_actual_price_txt']"));
				price = Double.parseDouble(promoPrice.getText().replace("₹", "").replace(",", "").trim());
				System.out.println(":moneybag: Promotional Product Price: ₹" + price);
			} catch (NoSuchElementException e1) {
				WebElement regularPrice = driver.findElement(By.xpath("//span[@class='product_list_cards_actual_price_txt']"));
				price = Double.parseDouble(regularPrice.getText().replace("₹", "").replace(",", "").trim());
				System.out.println(":moneybag: Regular Product Price: ₹" + price);
			}
			// Step 6: Add product to cart
			click(addCartButton);
			Common.waitForElement(2);
			click(bagIcon);
			Common.waitForElement(2);
			click(buyNowButton);
			// Step 7: Expand all coupons if more than 3
//			List<WebElement> availableCoupons = driver.findElements(By.xpath("//div[@class='available_offer_list']"));
//			WebElement showMoreOffers = driver.findElement(By.xpath("//button[@class='view_more_coupon_btn Cls_viewmore']"));
//			if (showMoreOffers.isDisplayed()) {
//				try {
//					
//					click(showMoreOffers);
//					Common.waitForElement(2);
//				} catch (Exception e) {
//					System.out.println(":warning: Unable to click 'Available Offers' – maybe already expanded.");
//				}
//			}
			List<WebElement> offerButtons = driver.findElements(By.xpath("//button[@class='view_more_coupon_btn Cls_viewmore']"));

			if (!offerButtons.isEmpty()) {
			    WebElement showMoreOffers = offerButtons.get(0);
			    
			    if (showMoreOffers.isDisplayed()) {
			        try {
			            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", showMoreOffers);
			            Common.waitForElement(1);

			            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", showMoreOffers); // safe click
			            System.out.println("✅ Clicked on 'View More Offers' button.");
			            Common.waitForElement(2);
			        } catch (Exception e) {
			            System.out.println("⚠️ Exception while clicking 'View More Offers': " + e.getMessage());
			        }
			    } else {
			        System.out.println("⚠️ Button found but not visible.");
			    }
			} else {
			    // 👉 Else block when button is not in DOM at all
			    System.out.println("ℹ️ 'View More Offers' button not present – skipping click.");
			    
			    // You can write fallback logic here if needed:
			    // e.g., check coupons, log, or continue with test
			}
			// Step 8: Try applying all coupons and print validation
			List<WebElement> allCouponButtons = driver.findElements(By.xpath("//button[@class='offer_list_item_apply_btn Cls_apply_coupon']"));
			List<WebElement> couponCodes = driver.findElements(By.xpath("//div[@class='available_offer_card']//div[@class='offer_list_item_heading']"));
			System.out.println(":clipboard: Applying all coupons on low price product...");
			for (int i = 0; i < couponCodes.size(); i++) {
				String code = couponCodes.get(i).getText().trim();
				click(allCouponButtons.get(i));
				Common.waitForElement(2);
				String validationMsg = "";
				try {
					validationMsg = driver.findElement(By.xpath("//p[contains(@class,'coupon_apply_msg')]")).getText();
				} catch (Exception ignored) {}
				System.out.println(":bookmark: Coupon: " + code + " → Validation: " + validationMsg);
				// Remove coupon if apply was successful
				try {
					WebElement removeCoupon = driver.findElement(By.xpath("//button[@class='coupon_apply_btn Cls_coupon_apply_rmv_btn']"));
					if (removeCoupon.isDisplayed()) {
						removeCoupon.click();
						Common.waitForElement(1);
					}
				} catch (Exception e) {
					// Ignore if remove not present
				}
			}
			// Step 9: Go back and sort "Price: High to Low"
			driver.navigate().back();
			driver.navigate().back();
			Common.waitForElement(2);
			click(sortBy);
			actions.moveToElement(sortByPriceHightoLow).click().build().perform();
			Common.waitForElement(2);
			// Step 10: Pick product > ₹999
			List<WebElement> highPriceProducts = driver.findElements(By.xpath("//div[@class='product_list_cards_list ']"));
			for (WebElement prod : highPriceProducts) {
				try {
					double highPrice = Double.parseDouble(prod.findElement(By.xpath(".//span[@class='product_list_cards_actual_price_txt']")).getText().replace("₹", "").replace(",", "").trim());
					if (highPrice > 999) {
						actions.moveToElement(prod).click().build().perform();
						Common.waitForElement(2);
						break;
					}
				} catch (Exception ignored) {}
			}
			// Step 11: Get high-price product actual price
			double highPrice = 0;
			try {
				highPrice = Double.parseDouble(driver.findElement(By.xpath("//div[@class='prod_current_price']")).getText().replace("₹", "").replace(",", "").trim());
			} catch (Exception e) {
				highPrice = Double.parseDouble(driver.findElement(By.xpath("//span[@class='product_list_cards_actual_price_txt']")).getText().replace("₹", "").replace(",", "").trim());
			}
			System.out.println(":package: High Price Product: ₹" + highPrice);
			// Step 12: Validate Best Price logic
			System.out.println(":receipt: Validating Best Price Calculation...");
			List<WebElement> couponCodeElements = driver.findElements(By.xpath("//span[@class='prod_bp_coupen']"));
			List<WebElement> bestPriceElements = driver.findElements(By.xpath("//span[@class='prod_bp_value']"));
			for (int i = 0; i < couponCodeElements.size(); i++) {
				String coupon = couponCodeElements.get(i).getText().trim();
				double bestPrice = Double.parseDouble(bestPriceElements.get(i).getText().replace("₹", "").replace(",", "").trim());
				double expectedPrice = highPrice;
				if (coupon.equals("THANKU100")) {
					if (highPrice > 999) expectedPrice -= 100;
				} else if (coupon.equals("FIRSTBUY200")) {
					if (highPrice > 1999) expectedPrice -= 200;
				} else if (coupon.equals("GRAB300")) {
					expectedPrice -= 300;
				} else if (coupon.equals("GRAB500")) {
					expectedPrice -= 500;
				} else if (coupon.contains("%")) {
					try {
						double percent = Double.parseDouble(coupon.replace("%", ""));
						expectedPrice -= (highPrice * percent / 100);
					} catch (Exception ignored) {}
				}
				expectedPrice = Math.round(expectedPrice);
				System.out.println("\nCoupon: " + coupon);
				System.out.println("Expected Best Price: ₹" + expectedPrice + ", Actual: ₹" + bestPrice);
				if ((int) expectedPrice == (int) bestPrice) {
					System.out.println(":white_tick: Best price matched.");
				} else {
					System.out.println(":x: Mismatch → Expected: ₹" + expectedPrice + ", Actual: ₹" + bestPrice);
				}
			}
		}











	public void verifyColorOptions() {
		Actions actions = new Actions(driver);
		Common.waitForElement(3);
		actions.moveToElement(shopMenu);
		actions.moveToElement(category).click().build().perform();

		List<WebElement> clickRandomProduct = driver.findElements(By.xpath("//div[@class='product_list_cards_list ']"));
		Collections.shuffle(clickRandomProduct);

		if (!clickRandomProduct.isEmpty()) {
			WebElement randomProduct = clickRandomProduct.get(0);
			WebElement productNameInListing = randomProduct.findElement(By.xpath(".//h2[@class='product_list_cards_heading']"));
			currentProductName = productNameInListing.getText().trim().replaceAll("\\s+", " ").toLowerCase();
			System.out.println("Product Name in Listing page: " + currentProductName);
			actions.moveToElement(randomProduct).click().build().perform();

		}
		String productNameOnPage = productName.getText().trim().replaceAll("\\s+", " ").toLowerCase();
		if (productNameOnPage.contains(currentProductName) || currentProductName.contains(productNameOnPage)) {
			System.out.println("Product Name: " + productName.getText());

			double initialPrice = 0;
			if (!promotionalPriceElement.isEmpty()) {
				initialPrice = Double.parseDouble(promotionalPriceElement.get(0).getText().replace("₹", "").replace(",", "").trim());
				System.out.println("Product is on promotional price: ₹" + initialPrice);
			} else if (currentPrice.isDisplayed()) {
				initialPrice = Double.parseDouble(currentPrice.getText().replace("₹", "").replace(",", "").trim());
				System.out.println("Product is on normal price: ₹" + initialPrice);
			} else {
				System.out.println("Price not found");
				return;
			}

			List<WebElement> colorOptions = driver.findElements(By.xpath("//div[@class='prod_color_name Cls_prod_color_name']"));
			System.out.println("Available colors: ");
			for (WebElement color : colorOptions) {
				System.out.print(color.getText() + " , ");
			}
			System.out.println();

			String initialColor = colorOptions.get(0).getText();
			System.out.println("Initial color: " + initialColor);
			System.out.println(initialColor + " " + productName.getText() + " - Product price is Rs " + initialPrice);

			for (int i = 1; i < colorOptions.size(); i++) {
				colorOptions.get(i).click();
				String clickedColor = colorOptions.get(i).getText();
				System.out.println("Clicked color: " + clickedColor);
				String changedProductName = productName.getText().trim().replaceAll("\\s+", " ").toLowerCase();
				System.out.println("The product name changed - " + changedProductName);

				double changedPrice = 0;
				if (!promotionalPriceElement.isEmpty()) {
					changedPrice = Double.parseDouble(promotionalPriceElement.get(0).getText().replace("₹", "").replace(",", "").trim());
					System.out.println(clickedColor + " " + changedProductName + " - Product price is Rs " + changedPrice);
				} else if (currentPrice.isDisplayed()) {
					changedPrice = Double.parseDouble(currentPrice.getText().replace("₹", "").replace(",", "").trim());
					System.out.println(" " + changedProductName + " - Product price is Rs " + changedPrice);
				} else {
					System.out.println("Price not found");
				}
			}
		} else {
			System.out.println("Product name mismatch");
		}
	}


	public void colordropDown() {
		RandomProduct();
		Common.waitForElement(1);
		Actions actions = new Actions(driver);
		actions.moveToElement(colorDropDown).click().build().perform();
		try {
			if (colorDropDown.isDisplayed()) {
				System.out.println("Color drop down is clicked");
			}
			else {
				System.out.println("Color drop down is not visbile or clickable");
			}
		} catch (Exception e) {
			System.out.println("Caught an exception: " + e.getMessage());
		}

	}

	public void sizeChart(Scenario scenario) {
		RandomProduct();
		Common.waitForElement(1);
		Actions actions = new Actions(driver);

		try {
			if (colorDropDown.isDisplayed()) {
				actions.moveToElement(sizeChart).click().build().perform();
				Common.waitForElement(1);
				byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
				scenario.attach(screenshot, "image/png", "Initial Screenshot");
				Common.waitForElement(1);
				if (sizeChartBottom.isDisplayed()) {
					Common.waitForElement(2);
					clickUsingJavaScript(sizeChartBottom);
					Common.waitForElement(3);
					scenario.attach(screenshot, "image/png", "Bottom Screenshot");
					System.out.println("Screen Shot attached for Size chart");
				}
			} else {
				scenario.log("⚠️ Size chart is null, screenshot not taken.");
			}
		} catch (Exception e) {
			scenario.log("❌ Failed to capture screenshot : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void verifySizeOptions() {
		RandomProduct();
		List<WebElement> topSizes = driver.findElements(By.xpath("//div[@class='prod_size_list Cls_prod_size_list']"));
		List<WebElement> bottomSizes = driver.findElements(By.xpath("//div[@class='prod_size_list Cls_prod_size_list_bottom']"));

		if (!topSizes.isEmpty() && !bottomSizes.isEmpty()) {
			System.out.println("Product contains both top and bottom size");

			String defaultTopSize = topSizes.get(0).getText();
			System.out.println("Default selected top size: " + defaultTopSize);
			System.out.println("default top sizes are: ");
			for (WebElement topSize : topSizes) {
				if (topSize.isEnabled()) {
					topSize.click();
					System.out.print(topSize.getText() + " ");
				}
			}

			String defaultBottomSize = bottomSizes.get(0).getText();
			System.out.println("\nDefault selected bottom size: " + defaultBottomSize);
			System.out.println("default bottom sizes are: ");
			for (WebElement bottomSize : bottomSizes) {
				if (bottomSize.isEnabled()) {
					bottomSize.click();
					System.out.print(bottomSize.getText() + " ");
				}
			}
		} else if (!topSizes.isEmpty()) {
			System.out.println("Product contains only top size");

			String defaultTopSize = topSizes.get(0).getText();
			System.out.println("Default selected size: " + defaultTopSize);
			System.out.println("Visible sizes are: ");
			for (WebElement topSize : topSizes) {
				if (topSize.isEnabled()) {
					topSize.click();
					System.out.print(topSize.getText() + " ");
				}
			}
		} else if (!bottomSizes.isEmpty()) {
			System.out.println("Product contains only bottom size");

			String defaultBottomSize = bottomSizes.get(0).getText();
			System.out.println("Default selected size: " + defaultBottomSize);
			System.out.println("Visible sizes are: ");
			for (WebElement bottomSize : bottomSizes) {
				if (bottomSize.isEnabled()) {
					bottomSize.click();
					System.out.print(bottomSize.getText() + " ");
				}
			}
		} else {
			System.out.println("No sizes available");
		}
	}
	public void askUsAnything(Scenario scenario) {
		Actions action = new Actions(driver);
		LoginPage login = new LoginPage(driver);
		login.userLogin();
		Common.waitForElement(2);
		RandomProduct();
		try {
			if (askUsAnythings.isDisplayed()) {
				click(askUsAnythings);
				click(askUsAnythingsDescription);
				Common.waitForElement(5);
				type(askUsAnythingsDescription,Common.getValueFromTestDataMap("Description"));
				Common.waitForElement(2);
				action.moveToElement(askUsSend).build().perform();
				byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
				scenario.attach(screenshot, "image/png", "Initial Screenshot");

			}
			else {
				scenario.log("⚠️ Ask us anything is null, screenshot not taken.");
			}
		} catch (Exception e) {
			scenario.log("❌ Failed to capture screenshot : " + e.getMessage());
			e.printStackTrace();
		}


	}
	public void addToCartButton() {
		Actions action = new Actions(driver);
		RandomProduct();
		Common.waitForElement(1);
		try {
			if (addCartButton.isDisplayed()) {
				action.moveToElement(addCartButton).click().build().perform();
				Common.waitForElement(2);
				String message = addCartMessage.getText();
				System.out.println("The product is added successfully to the cart:");
				System.out.println("Validation Message Displayed " +message);

			}
			else {
				Common.waitForElement(2);
				String message = addCartMessage.getText();
				System.err.println("The Validation message displayed wrong:" + message);
			}

		} catch (Exception e) {
			System.out.println("Caught an exception: " + e.getMessage());
		}


	}
	public void buyNow(Scenario scenario) {
		Actions action = new Actions(driver);
		RandomProduct();
		Common.waitForElement(1);
		try {
			if (buyNowbutton.isDisplayed()) {
				action.moveToElement(buyNowbutton).click().build().perform();
				Common.waitForElement(2);
				byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
				scenario.attach(screenshot, "image/png", "Initial Screenshot");

			}
			else {
				scenario.log("⚠️ Buy Now is null, screenshot not taken.");
			}
		} catch (Exception e) {
			scenario.log("❌ Failed to capture screenshot : " + e.getMessage());
			e.printStackTrace();
		}
	}
	public void verifyPincode() {
		RandomProduct();
		Common.waitForElement(1);
		Random rand = new Random();
		int randomPincode = rand.nextInt(900000) + 100000; // generates a 6-digit random number
		String pincode = String.valueOf(randomPincode);

		pinCode.sendKeys(pincode);
		checkPincodeButton.click();

		try {
			Thread.sleep(2000); // wait for the response
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		if (deliveryDate.isDisplayed()) {
			System.out.println("Delivery date is displaying for pincode: " + pincode);
			System.out.println("Delivery date: " + deliveryDate.getText());
		} else if (invalidPincodeError.isDisplayed()) {
			System.out.println("Invalid pincode: " + pincode);
			System.out.println("Error message: " + invalidPincodeError.getText());
		} else {
			System.out.println("No response for pincode: " + pincode);
		}
	}

	public void tryAlongSection(Scenario scenario) {
		Actions action = new Actions(driver);
		RandomProduct();
		Common.waitForElement(1);
		action.moveToElement(tryAlongSection).build().perform();
		try {
			if (tryAlongSection.isDisplayed()) {
				scrollToElementUsingJSE(tryAlongSection);
				System.out.println("The try along section is displaying verify screen shot in report");
				byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
				scenario.attach(screenshot, "image/png", "Initial Screenshot");

			}
			else {
				scenario.log("⚠️ Try along section not displaying , screenshot not taken.");
			}


		} catch (Exception e) {
			System.out.println("Caught an exception: " + e.getMessage());
		}


	}

	public void tryAlongProducts(Scenario scenario) {
		RandomProduct();
		Common.waitForElement(1);
		scrollUsingJSWindow();
		Common.waitForElement(2);
		if (!selectingOfcheckBox.isEmpty()) {
			for (WebElement checkBox : selectingOfcheckBox ) {
				if (checkBox.isEnabled()) {
					checkBox.click();
					Common.waitForElement(2);
					byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
					scenario.attach(screenshot, "image/png", "Initial Screenshot");
				}
				else {
					scrollUsingJSWindow();
					System.out.println("Try along section check box not clickable ");
					byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
					scenario.attach(screenshot, "image/png", "Initial Screenshot");

				}
			}

		}

	}
	public void quickViewIconTryAlong(Scenario scenario) {
		RandomProduct();
		Common.waitForElement(1);
		scrollUsingJSWindow();
		Common.waitForElement(2);
		Collections.shuffle(clickOnQuickViewButton);
		if (!clickOnQuickViewButton.isEmpty()) {
			WebElement randomIcon = clickOnQuickViewButton.get(0);
			click(randomIcon);
			System.out.println("Try along Quick view clicked");
			Common.waitForElement(2);
			byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
			scenario.attach(screenshot, "image/png", "Initial Screenshot");
		}
		else {
			System.out.println("Try along Quick view is not clickable");
		}
	}
	public void tryAlongQuickViewClose() {
		RandomProduct();
		Common.waitForElement(1);
		scrollUsingJSWindow();
		Common.waitForElement(2);
		Collections.shuffle(clickOnQuickViewButton);
		try {
			if (!clickOnQuickViewButton.isEmpty()) {
				WebElement randomIcon = clickOnQuickViewButton.get(0);
				click(randomIcon);
				Common.waitForElement(2);
				click(closeTheQuickViewPopup);
				System.out.println("Quick view close button clicked");
			}
			else {
				System.out.println("Quick view close button not clickable");
			}
		} catch (Exception e) {
			System.out.println("Caught an exception: " + e.getMessage());
		}


	}
	public void productDescriptionDropDDown() {
		RandomProduct();
		Common.waitForElement(1);
		scrollUsingJSWindow();
		Common.waitForElement(2);
		if (!clickAllDropDownArrow.isEmpty()) {
			for (WebElement arrow : clickAllDropDownArrow ) {
				if (arrow.isEnabled()) {
					Common.waitForElement(1);
					arrow.click();
				}
				else {

					System.out.println("Product Description arrows are Not clickable");

				}
			}

		}

	}

	public void returnAndExchangeLink() {
		RandomProduct();
		Common.waitForElement(1);
		scrollUsingJSWindow();
		try {
			if (clickOnReturn_ExchangeDropDownArrow.isDisplayed()) {
				click(clickOnReturn_ExchangeDropDownArrow);
				Common.waitForElement(1);
				click(clickOnTheLink);
				Common.waitForElement(2);
			}
			String currentUrl = driver.getCurrentUrl();
			if (!currentUrl.equals(driver.getCurrentUrl())) {
				System.out.println("Redirection of return and exchange link is success");
			} else {
				System.out.println("Redirection of return and exchange link in the same page");
			}
		} catch (Exception e) {
			System.out.println("Caught an exception: " + e.getMessage());
		}
	}
	public void reviewViewAll() {
		RandomProduct();
		Common.waitForElement(1);
		scrollUsingJSWindow();
		try {
			if (!viewAllButton.isEmpty()) {
				viewAllButton.get(0).click();
				Common.waitForElement(1);
				System.out.println("View all Review button is Clicked ");
			} else {
				System.out.println("Product Review is not available ");
			}
		} catch (Exception e) {
			System.out.println("Caught an exception: " + e.getMessage());
		}
	}


	public void reviewCalculation() {
		RandomProduct();
		Common.waitForElement(2);  // Wait for product page to load
		scrollUsingJSWindow();

		try {
			if (!viewAllButton.isEmpty()) {
				viewAllButton.get(0).click();
				Common.waitForElement(2);
				System.out.println("✅ View all Review button is Clicked");

				int totalStarCount = 0;
				int totalReviewsParsed = 0;

				List<WebElement> reviewBlocks = driver.findElements(By.xpath("//div[@class='product_review_card_star_rating']"));

				for (WebElement review : reviewBlocks) {
					List<WebElement> stars = review.findElements(By.xpath(".//img[contains(@src, 'star')]"));
					int starsInReview = 0;

					for (WebElement star : stars) {
						String src = star.getAttribute("src");
						if (src.contains("filled_star")) {
							starsInReview++;
						}
					}

					if (starsInReview > 0) {
						totalStarCount += starsInReview;
						totalReviewsParsed++;
					}
				}

				// Handle fallback if all stars are 'empty' and no filled stars were counted
				if (totalReviewsParsed == 0 || totalStarCount == 0) {

					String totalReviewsText = totalReviewCount.getText().replaceAll("[^0-9]", "");
					int totalReviews = Integer.parseInt(totalReviewsText);

					String displayedRatingText = reviewRatingElement.getText().replaceAll("[^0-9.]", "");
					double displayedRating = Double.parseDouble(displayedRatingText);

					double expectedStarCount = totalReviews * displayedRating;
					System.out.println("Expected Total Star Count: " + expectedStarCount);
					System.out.println("Total Reviews : " + totalReviews);
					System.out.println("Displayed Review Rating: " + displayedRating);
					System.out.println("✅ Review calculation fallback assumed correct");
					return;
				}

				int totalReviews = totalReviewsParsed;
				double calculatedRating = totalReviews > 0 ? (double) totalStarCount / totalReviews : 0;
				System.out.println("Total Star Count: " + totalStarCount);
				System.out.println("Total Reviews Parsed: " + totalReviews);
				System.out.println("Calculated Review Rating: " + calculatedRating);

				String reviewRatingText = reviewRatingElement.getText().replaceAll("[^0-9.]", "");
				double displayedReviewRating = Double.parseDouble(reviewRatingText);

				double calculatedRounded = Math.round(calculatedRating * 10.0) / 10.0;
				double displayedRounded = Math.round(displayedReviewRating * 10.0) / 10.0;

				if (calculatedRounded == displayedRounded) {
					System.out.println("✅ Review calculation is correct");
				} else {
					System.out.println("❌ Review calculation is incorrect");
					System.out.println("Displayed Review Rating: " + displayedReviewRating);
				}
			} else {
				System.out.println("ℹ️ Product Review is not available.");
			}
		} catch (Exception e) {
			System.out.println("❗ Exception occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void reviewButtonclickable() {
		{
			try {
				Common.waitForElement(5);
				Actions actions = new Actions(driver);
				actions.moveToElement(shopMenu)
				.moveToElement(category)
				.click()
				.build()
				.perform();
				System.out.println(":white_check_mark: Navigated to category via Shop menu.");
				List<WebElement> addProduct = driver.findElements(By.xpath("//div[@class='product_list_cards_list ']"));
				if (addProduct.isEmpty()) {
					System.out.println(":x: No products found under the selected category.");
					return;
				}
				Collections.shuffle(addProduct);
				WebElement randomProduct = addProduct.get(0);
				actions.moveToElement(randomProduct).click().build().perform();
				System.out.println(":white_check_mark: Random product clicked.");
				Common.waitForElement(2);
				click(clickOnWriteReviewButton);
				System.out.println(":white_check_mark: Clicked on 'Write Review' button.");
			} catch (Exception e) {
				System.out.println(":exclamation: Error in clickRandomProductAndWriteReview(): " + e.getMessage());
			}
		}
	}





	public void moreForYouSection() 
	{
		Common.waitForElement(5);
		Actions actions = new Actions(driver);
		actions.moveToElement(shopMenu);
		actions.moveToElement(category).click().build().perform();
		List<WebElement> addProduct = driver.findElements(By.xpath("//div[@class='product_list_cards_list ']"));
		Collections.shuffle(addProduct);

		if (!addProduct.isEmpty()) {
			WebElement randomProduct = addProduct.get(0);
			actions.moveToElement(randomProduct).click().build().perform();
			Common.waitForElement(2);

			String moreFor = moreForSectionProduct.getText();
			if (!moreFor.isEmpty()) {

				System.out.println("More for you section is displaying :"+ moreFor);
			}
			else {

				System.out.println("More for you section is not displaying");
			}
		}
	}

	public void recentlyViewed() {
		LoginPage login = new LoginPage(driver);
		login.userLogin();
		Common.waitForElement(5);
		Actions actions = new Actions(driver);
		actions.moveToElement(shopMenu);
		actions.moveToElement(category).click().build().perform();
		List<WebElement> addProduct = driver.findElements(By.xpath("//div[@class='product_list_cards_list ']"));
		Collections.shuffle(addProduct);

		if (!addProduct.isEmpty()) {
			WebElement randomProduct = addProduct.get(0);
			actions.moveToElement(randomProduct).click().build().perform();
			Common.waitForElement(2);
			String recentlyProduct = recentlyViewSectionProduct.getText();
			System.out.println("Recently viewed is dispalying :"+ recentlyProduct);
		}
	}

	public void suggestedForYou() 
	{
		Common.waitForElement(5);
		Actions actions = new Actions(driver);
		actions.moveToElement(shopMenu);
		actions.moveToElement(category).click().build().perform();
		List<WebElement> addProduct = driver.findElements(By.xpath("//div[@class='product_list_cards_list ']"));
		Collections.shuffle(addProduct);

		if (!addProduct.isEmpty()) {
			WebElement randomProduct = addProduct.get(0);
			actions.moveToElement(randomProduct).click().build().perform();
			Common.waitForElement(2);
			String suggestedForYou = suggestedForYouSectionProduct.getText();
			if (!suggestedForYou.isEmpty()) {

				System.out.println("Suggest for you section is displaying :"+ suggestedForYou);
			}
			else {
				System.out.println("Suggest for you section is not displaying");
			}

		}
	}
	public void ReviewPopupWithoutEnterAllDataClickOnS() {
	    Common.waitForElement(5);
	    Actions actions = new Actions(driver);
	    actions.moveToElement(shopMenu);
	    actions.moveToElement(category).click().build().perform();

	    List<WebElement> addProduct = driver.findElements(By.xpath("//div[@class='product_list_cards_list ']"));
	    Collections.shuffle(addProduct);

	    if (!addProduct.isEmpty()) {
	        WebElement randomProduct = addProduct.get(0);
	        actions.moveToElement(randomProduct).click().build().perform();
	        Common.waitForElement(2);

	        click(clickOnWriteReviewButton);
	        Common.waitForElement(2);

	        reviewUserName.clear();
	        reviewEmailID.clear();
	        click(clickOnSubmitButton);
	        Common.waitForElement(2);

	        // Scroll the popup into view (for all messages)
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", validationMessageForReviewName);
	        Common.waitForElement(1);

	        // Assert 1: Rating validation
	        String actualMessage = validationMessageForRating.getText().trim();
	        Assert.assertEquals("The rating is a required field", actualMessage);
	        System.out.println("\u001B[32mThe validation message displayed: " + actualMessage + "\u001B[0m");

//	        // Assert 2: Review description validation
//	        String actualMessage1 = validationMessageForReviewDescription.getText().trim();
//	        Assert.assertEquals("Please enter a review between 3 and 500 characters.", actualMessage1);
//	        System.out.println("\u001B[32mThe validation message displayed: " + actualMessage1 + "\u001B[0m");

	        // Assert 3: Name validation
	        String actualMessage2 = validationMessageForReviewName.getText().trim();
	        Assert.assertEquals("Name should be between 3 and 50 characters.", actualMessage2);
	        System.out.println("\u001B[32mThe validation message displayed: " + actualMessage2 + "\u001B[0m");

	        // Assert 4: Email validation
	        reviewEmailID.clear();
	        String actualMessage3 = validationMessageForReviewEmailID.getText().trim();
	        Assert.assertEquals("Please enter a valid email address.", actualMessage3); // adjust this message based on actual UI
	        System.out.println("\u001B[32mThe validation message displayed: " + actualMessage3 + "\u001B[0m");

	        Common.waitForElement(2);
	    }
	}


	public void scrollUsingJSWindow() {

		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript("window.scrollTo(0, 800);");

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
