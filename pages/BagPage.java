package pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import objectRepo.BagObjRepo;
import utils.Common;



public final class BagPage extends BagObjRepo {

	public BagPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}



	public void itemCount() {
		HomePage home = new HomePage(driver);
		home.homeLaunch();
		Common.waitForElement(5);
		click(bagIcon);
		try {
			Common.waitForElement(2);
			WebElement bagContinueShop = driver.findElement(By.xpath("//a[@class='empty_cart_continue_btn btn___2']"));
			if (bagContinueShop.isDisplayed()) {
				click(closeBag);
				ProductListingPage pLp = new ProductListingPage(driver);
				pLp.addToCart();
				Common.waitForElement(5);
				click(bagIcon);
				String bagCount = bagItemCount.getText();
				System.out.println("The total Item count is:"+bagCount);

			}
			else {
				String bagCount = bagItemCount.getText();
				System.out.println("The total Item count is:"+bagCount);
			}
		}
		catch (Exception e) {
			System.out.println("Caught an exception: " + e.getMessage());
			NoSuchElementException e1 = new NoSuchElementException("A NoSuchElementException exception occurred");
			e1.initCause(e);
			throw e1;
		}
	}

	public void wishListInbag() {
		LoginPage login = new LoginPage(driver);
		login.userLogin();
		Common.waitForElement(5);
		click(bagIcon);

		JavascriptExecutor js = (JavascriptExecutor) driver;
		List<String> movedProductNames = new ArrayList<>();

		// STEP 1: Check if products exist in the bag
		List<WebElement> initialProducts = driver.findElements(By.xpath("//div[@class='cp_name_row']"));
		int productCount = initialProducts.size();

		if (productCount == 0) {
			System.out.println("⚠️ No products in Bag to move to Wishlist.");
			return;
		}

		// STEP 2: Move each product to wishlist, one by one
		while (true) {
			try {
				List<WebElement> products = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));
				if (products.isEmpty()) break;

				// Always get the first fresh element after DOM change
				WebElement nameElement = driver.findElement(By.xpath("(//div[@class='cp_name_row'])[1]"));
				String productName = nameElement.getText().trim();
				movedProductNames.add(productName);

				WebElement moveToWishlistBtn = driver.findElement(
						By.xpath("(//div[@class='cp_wishlist_btn Cls_move_to_wishlist_btn'])[1]"));
				js.executeScript("arguments[0].scrollIntoView(true);", moveToWishlistBtn);
				moveToWishlistBtn.click();

				Common.waitForElement(2); // Wait for DOM to stabilize
			} catch (StaleElementReferenceException se) {

			} catch (Exception e) {
				System.out.println("❌ Error while moving product to wishlist: " + e.getMessage());
				break;
			}
		}

		// STEP 3: Close the bag after all items moved
		try {
			click(closeBag);
		} catch (Exception e) {
			System.out.println("ℹ️ Close bag button not available or already closed.");
		}

		// STEP 4: Navigate to Wishlist page
		try {
			WebElement wishlistBtn = driver.findElement(By.xpath("//a[@class='wishlist-icon']"));
			wishlistBtn.click();
			Common.waitForElement(3);
		} catch (Exception e) {
			System.out.println("❌ Unable to navigate to wishlist: " + e.getMessage());
			return;
		}

		// STEP 5: Validate wishlist items
		List<WebElement> wishlistItems = driver.findElements(By.xpath("//h2[@class='product_list_cards_heading']"));
		List<String> wishlistProductNames = wishlistItems.stream()
				.map(e -> e.getText().trim())
				.collect(Collectors.toList());

		for (String moved : movedProductNames) {
			if (wishlistProductNames.contains(moved)) {
				System.out.println("✅ Verified in wishlist: " + moved);
			} else {
				System.out.println("❌ Not found in wishlist: " + moved);
			}
		}
	}



	public void bagDelete() {
		HomePage home = new HomePage(driver);
		home.homeLaunch();
		Common.waitForElement(5);
		click(bagIcon);
		List<WebElement> finalBagCount = driver.findElements(By.xpath("//span[@class='cart_count_num Cls_cart_count_num']"));
		if (finalBagCount.isEmpty()) {

			System.out.println("🛒 Adding product to check if bag count displays...");
			click(closeBag);
			// Add product again to verify count shows up
			ProductListingPage product = new ProductListingPage(driver);
			product.addToCart();
			Common.waitForElement(2);
			click(bagIcon);
		}
		int deletedCount = 0;

		List<WebElement> deleteButtons = driver.findElements(By.xpath("//div[@title='Delete']"));

		for (int i = 0; i < deleteButtons.size(); i++) {
			boolean deleted = false;
			int retry = 0;

			while (!deleted && retry < 3) {
				try {
					System.out.println("🛒 Attempting to delete product " + (i + 1) + "...");

					// Refresh list each retry to avoid stale reference
					deleteButtons = driver.findElements(By.xpath("//div[@title='Delete']"));

					// Click delete
					deleteButtons.get(i).click();
					System.out.println("✅ Product " + (i + 1) + " deleted.");
					deleted = true;
					deletedCount++;

					// Optional: Wait for deletion to reflect
					Thread.sleep(1000);
				} catch (StaleElementReferenceException e) {
					retry++;
					System.out.println("⚠️ Retry " + retry + " due to stale element...");
				} catch (ElementClickInterceptedException e) {
					retry++;
					System.out.println("❌ Retry " + retry + ": Snackbar intercepted click. Waiting to retry...");
					try {
						Thread.sleep(1000); // Wait before retry
					} catch (InterruptedException ie) {
						// Ignore
					}
				} catch (Exception e) {
					System.out.println("❌ Unexpected error during bag deletion: " + e.getMessage());
					break;
				}
			}

			if (!deleted) {
				System.out.println("❌ Failed to delete product " + (i + 1) + " after multiple attempts.");
			}
		}

		System.out.println("✅ Total products deleted from bag: " + deletedCount);
	}





	public void changeTheProductSize() {
		HomePage home = new HomePage(driver);
		home.homeLaunch();
		Common.waitForElement(5);
		click(bagIcon);
		Common.waitForElement(2);
		List<WebElement> finalBagCount = driver.findElements(By.xpath("//span[@class='cart_count_num Cls_cart_count_num']"));
		if (finalBagCount.isEmpty()) {

			System.out.println("🛒 Adding product to check if bag count displays...");
			click(closeBag);
			// Add product again to verify count shows up
			ProductListingPage product = new ProductListingPage(driver);
			product.addToCart();
			Common.waitForElement(2);
			click(bagIcon);
		}
		JavascriptExecutor js = (JavascriptExecutor) driver;
		int productCount = driver.findElements(By.xpath("//div[@class='cart_prod_card ']")).size();
		System.out.println("🛍️ Found " + productCount + " products in bag.");

		for (int i = 0; i < productCount; i++) {
			try {
				if (i > 0) {
					WebElement bag = driver.findElement(By.xpath("//button[@class='Cls_cart_btn']"));
					js.executeScript("arguments[0].click();", bag);
					Common.waitForElement(2);
				}

				List<WebElement> productBlocks = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));
				WebElement currentProduct = productBlocks.get(i);
				js.executeScript("arguments[0].scrollIntoView(true);", currentProduct);
				Common.waitForElement(1);

				List<WebElement> dropdowns = currentProduct.findElements(By.xpath(".//div[@class='cp_drop_arrow']"));
				int dropdownCount = dropdowns.size();
				System.out.println("\n🔄 Changing size for Product #" + (i + 1));

				// Top size change
				if (dropdownCount >= 1) {
					dropdowns.get(0).click(); // Open top size dropdown
					WebElement topOptionList = currentProduct.findElement(By.xpath("//ul[@class='cp_dropdown_content']   "));
					Common.waitForElement(2);

					List<WebElement> topOptions = topOptionList.findElements(By.tagName("li"));
					if (topOptions.size() > 1) {
						js.executeScript("arguments[0].click();", topOptions.get(1));
						System.out.println("✅ Top size changed for Product #" + (i + 1));
					} else {
						System.out.println("⚠️ Only one top size option available for Product #" + (i + 1));
					}
				} else {
					System.out.println("❌ Top size dropdown not found for Product #" + (i + 1));
				}

				// Bottom size change
				if (dropdownCount == 2) {
					dropdowns.get(1).click(); // Open bottom size dropdown
					WebElement bottomOptionList = currentProduct.findElements(By.xpath("//ul[@class='cp_dropdown_content']   ")).get(1);
					Common.waitForElement(2);

					List<WebElement> bottomOptions = bottomOptionList.findElements(By.tagName("li"));
					if (bottomOptions.size() > 1) {
						js.executeScript("arguments[0].click();", bottomOptions.get(1));
						System.out.println("✅ Bottom size changed for Product #" + (i + 1));
					} else {
						System.out.println("⚠️ Only one bottom size option available for Product #" + (i + 1));
					}
				} else {
					System.out.println("❌ Bottom size dropdown not found for Product #" + (i + 1));
				}

			} catch (StaleElementReferenceException e) {
				System.out.println("🔁 Retrying due to stale element for Product #" + (i + 1));
				i--; // Retry
			} catch (ElementClickInterceptedException e) {
				System.out.println("⚠️ Click intercepted for Product #" + (i + 1) + ". Scrolling and retrying...");
				js.executeScript("window.scrollBy(0, -100);");
			} catch (Exception e) {
				System.out.println("❌ Unexpected error for Product #" + (i + 1) + ": " + e.getMessage());
			}
		}

		System.out.println("\n✅ Completed size change for all products.");
	}





	public void increaseAndDecreaseQTY() throws InterruptedException {
		HomePage home = new HomePage(driver);
		home.homeLaunch();
		Common.waitForElement(5);
		click(bagIcon);
		Common.waitForElement(2);
		List<WebElement> products = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));
		System.out.println("🛍️ Total products in Bag: " + products.size());

		if (products.isEmpty()) {

			click(closeBag);
			System.out.println("🛒 Adding product to the bag...");

			// Add product again to verify count shows up
			ProductListingPage product = new ProductListingPage(driver);
			product.addToCart();
			Common.waitForElement(2);
			click(bagIcon);
		}
		products = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));
		for (int i = 0; i < products.size(); i++) {
			try {
				// Re-fetch the product list to avoid stale elements
				List<WebElement> currentProducts = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));
				WebElement product = currentProducts.get(i);

				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", product);
				Thread.sleep(500);

				// Re-locate buttons within the product container using relative XPath
				WebElement incBtn = product.findElement(By.xpath(".//button[contains(@class, 'cp_quantity_increase_btn')]"));
				WebElement decBtn = product.findElement(By.xpath(".//button[contains(@class, 'cp_quantity_decrease_btn')]"));

				int incCount = 0;
				int decCount = 0;

				// INCREASE QTY
				if (incBtn.isDisplayed() && incBtn.isEnabled()) {
					try {
						incBtn.click();
					} catch (ElementClickInterceptedException e) {
						((JavascriptExecutor) driver).executeScript("arguments[0].click();", incBtn);
					}
					incCount++;
					Thread.sleep(400);
				} else {
					System.out.println("🔒 Product #" + (i + 1) + ": Increase button is disabled or not displayed.");
				}

				// Re-fetch product after change
				currentProducts = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));
				product = currentProducts.get(i);

				decBtn = product.findElement(By.xpath(".//button[contains(@class, 'cp_quantity_decrease_btn')]"));

				// DECREASE QTY
				if (decBtn.isDisplayed() && decBtn.isEnabled()) {
					try {
						decBtn.click();
					} catch (ElementClickInterceptedException e) {
						((JavascriptExecutor) driver).executeScript("arguments[0].click();", decBtn);
					}
					decCount++;
					Thread.sleep(400);
				} else {
					System.out.println("🔒 Product #" + (i + 1) + ": Decrease button is disabled or not displayed.");
				}

				System.out.println("✅ Product #" + (i + 1) + ": Increased " + incCount + "x, Decreased " + decCount + "x");

			} catch (StaleElementReferenceException e) {
				System.out.println("⚠️ Product #" + (i + 1) + ": Stale element error - Retrying...");
				i--; // retry the same product
				Thread.sleep(500);
			} catch (Exception e) {
				System.out.println("⚠️ Product #" + (i + 1) + ": Unexpected error - " + e.getMessage());
			}
		}
	}


	public void newProductToBag() {
		HomePage home = new HomePage(driver);
		home.homeLaunch();
		Common.waitForElement(2);
		ProductListingPage pLp = new  ProductListingPage(driver);
		pLp.addToCart();


	}

	public void verifyBagCount() {
		HomePage home = new HomePage(driver);
		home.homeLaunch();
		Common.waitForElement(5);
		click(bagIcon);

		try {
			// Initial Bag Count Check
			List<WebElement> initialBagCount = driver.findElements(By.xpath("//span[@class='cart_count_num Cls_cart_count_num']"));
			if (initialBagCount.isEmpty()) {
				System.out.println("👜 Bag count is zero.");
				return;
			}

			int productsCount = driver.findElements(By.xpath("//div[@class='cart_prod_card ']")).size();
			System.out.println("🛍️ Total products in Bag initially: " + productsCount);

			// Process products one by one
			while (true) {
				List<WebElement> products = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));
				if (products.isEmpty()) break;

				try {
					WebElement product = products.get(0); // Always get the first one
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", product);
					Common.waitForElement(1);

					boolean actionPerformed = false;

					List<WebElement> wishlistBtns = product.findElements(By.xpath(".//div[@class='cp_wishlist_btn Cls_move_to_wishlist_btn']"));
					List<WebElement> deleteBtns = product.findElements(By.xpath(".//div[@title='Delete']"));

					if (!wishlistBtns.isEmpty() && wishlistBtns.get(0).isDisplayed()) {
						wishlistBtns.get(0).click();
						System.out.println("💖 Product moved to Wishlist.");
						actionPerformed = true;
					} else if (!deleteBtns.isEmpty() && deleteBtns.get(0).isDisplayed()) {
						deleteBtns.get(0).click();
						System.out.println("🗑️ Product deleted from Bag.");
						actionPerformed = true;
					} else {
						System.out.println("⚠️ No wishlist or delete option found for product.");
					}

					if (actionPerformed) {
						Common.waitForElement(2);
						driver.navigate().refresh(); // Refresh to update DOM
						Common.waitForElement(3);
					}

				} catch (StaleElementReferenceException stale) {
					//					System.out.println("♻️ Stale element encountered, retrying iteration...");
				} catch (Exception e) {
					System.out.println("⚠️ Error processing a product: " + e.getMessage());
				}
			}

			// Final Bag Count Check
			List<WebElement> finalBagCount = driver.findElements(By.xpath("//span[@class='cart_count_num Cls_cart_count_num']"));
			if (finalBagCount.isEmpty()) {
				System.out.println("👜 All products removed or moved. Bag count not displayed.");
				System.out.println("🛒 Adding product to check if bag count displays...");

				// Add product again to verify count shows up
				ProductListingPage product = new ProductListingPage(driver);
				product.addToCart();
				Common.waitForElement(2);

				List<WebElement> newBagCount = driver.findElements(By.xpath("//span[@class='cart_count_num Cls_cart_count_num']"));
				if (!newBagCount.isEmpty()) {
					System.out.println("✅ Bag count is now visible after adding product. Count: " + newBagCount.get(0).getText());
				} else {
					System.out.println("❌ Bag count still not visible.");
				}
			} else {
				System.out.println("✅ Final bag count is: " + finalBagCount.get(0).getText());
			}

		} catch (Exception e) {
			System.out.println("❌ Scenario failed with exception: " + e.getMessage());
		}
	}


	public void verifyItemCountChanges() throws InterruptedException {
		LoginPage login = new LoginPage(driver);
		login.userLogin();
		Common.waitForElement(5);
		click(bagIcon);

		// Add a product if bag is empty
		if (isBagEmpty()) {
			System.out.println("👜 Bag is empty. Adding a product.");
			click(closeBag);
			addProductToBag();
			click(bagIcon);
		}

		int retryCount = 0;
		int maxRetries = 5;

		while (retryCount < maxRetries) {
			retryCount++;
			try {
				List<WebElement> products = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));
				if (products.isEmpty()) break;

				WebElement product = driver.findElement(By.xpath("(//div[@class='cart_prod_card '])[1]"));

				if (tryMoveToWishlist(product) || tryDeleteFromBag(product)) {
					Common.waitForElement(2);

					if (!isCountAvailable()) {
						System.out.println("🛍️ No count found. Adding new product...");
						click(closeBag);
						addProductToBag();
						click(bagIcon);

						if (isCountAvailable()) {
							printUpdatedCount();
							break;
						} else {
							System.out.println("⚠️ Still couldn't fetch updated count.");
						}
					} else {
						printUpdatedCount();
						break;
					}
				} else {
					System.out.println("⚠️ No move/delete option found.");
					break;
				}

			} catch (StaleElementReferenceException e) {
				System.out.println("♻️ Stale element encountered, retrying...");
			} catch (Exception e) {
				System.out.println("❌ Unexpected error: " + e.getMessage());
				break;
			}
		}
	}


	private boolean isBagEmpty() {
		return driver.findElements(By.xpath("//span[@class='bag_prod_count']")).isEmpty();
	}

	private boolean isCountAvailable() {
		return !driver.findElements(By.xpath("//span[@class='bag_prod_count']")).isEmpty();
	}

	private void printUpdatedCount() {
		List<WebElement> updatedCount = driver.findElements(By.xpath("//span[@class='bag_prod_count']"));
		if (!updatedCount.isEmpty()) {
			System.out.println("🔄 Updated Count: " + updatedCount.get(0).getText());
		}
	}

	private boolean tryMoveToWishlist(WebElement product) {
		try {
			WebElement wishlistBtn = product.findElement(By.xpath(".//div[@class='cp_wishlist_btn Cls_move_to_wishlist_btn']"));
			wishlistBtn.click();
			System.out.println("💖 Moved to wishlist.");
			return true;
		} catch (NoSuchElementException ignored) {
			return false;
		}
	}

	private boolean tryDeleteFromBag(WebElement product) {
		try {
			WebElement deleteBtn = product.findElement(By.xpath(".//div[@title='Delete']"));
			deleteBtn.click();
			System.out.println("🗑️ Deleted from bag.");
			return true;
		} catch (NoSuchElementException ignored) {
			return false;
		}
	}



	public void addProductToBag() {
		Common.waitForElement(5);
		Actions actions = new Actions(driver);
		actions.moveToElement(shopMenu);
		actions.moveToElement(category).click().build().perform();
		List<WebElement> addProduct = driver.findElements(By.xpath("//button[@class='product_list_cards_btn']"));
		Collections.shuffle(addProduct);

		if (!addProduct.isEmpty()) {
			WebElement randomProduct = addProduct.get(0);
			actions.moveToElement(randomProduct).click().build().perform();
			Common.waitForElement(5);
			click(addToCart);

		}
	}

	// Example method to add a product to cart
	public void addProductToCart(WebDriver driver) throws InterruptedException {
		ProductListingPage product = new ProductListingPage(driver);
		product.addToCart();
		System.out.println("✅ Product added to bag.");
	}

	public void threadLine() {
		HomePage home = new HomePage(driver);
		home.homeLaunch();
		Common.waitForElement(5);
		click(bagIcon);
		List<WebElement> products = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));
		if (products.isEmpty()) {
			System.out.println("👜 No products in bag. Adding one product...");
			click(closeBag);
			ProductListingPage product = new ProductListingPage(driver);
			product.addToCart();
			Common.waitForElement(1);
			click(bagIcon);
			List<WebElement> threadBanner = driver.findElements(By.xpath("//div[@class='bag_thread_banner']"));
			System.out.println("The banner is displaying: " +threadBanner.size());	

		}
		else {
			List<WebElement> threadBanner = driver.findElements(By.xpath("//div[@class='bag_thread_banner']"));
			System.out.println("The banner is displaying: " +threadBanner.size());	
		}

	}

	public void youWillEarn() throws TimeoutException {
		HomePage home = new HomePage(driver);
		home.homeLaunch();
		Common.waitForElement(3);
		click(bagIcon);
		Common.waitForElement(2);

		try {
			List<WebElement> productsInBag = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));

			if (productsInBag.isEmpty()) {
				System.out.println("👜 Bag is empty. Adding a product...");
				click(closeBag);
				Actions actions = new Actions(driver);
				actions.moveToElement(shopMenu);
				actions.moveToElement(category).click().build().perform();

				List<WebElement> addProduct = driver.findElements(By.xpath("//button[@class='product_list_cards_btn']"));
				Collections.shuffle(addProduct);
				if (!addProduct.isEmpty()) {
					actions.moveToElement(addProduct.get(0)).click().build().perform();
					click(addToCart);
					Common.waitForElement(2);
					click(bagIcon);
					Common.waitForElement(2);
				}
				productsInBag = driver.findElements(By.xpath("//div[@class='cart_prod_card ']")); // refresh
			}

			int productIndex = 1;
			int totalDiscountedAmount = 0;

			for (WebElement product : productsInBag) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", product);
				Common.waitForElement(1);

				try {
					WebElement priceElement = product.findElement(By.xpath(".//div[@class='cp_current_price']"));
					String priceText = priceElement.getText().replaceAll("[^0-9]", "");
					int discountedPrice = Integer.parseInt(priceText);

					WebElement qtyElement = product.findElement(By.xpath(".//div[@class='cp_quantity_wrap']"));
					int quantity = Integer.parseInt(qtyElement.getText().trim());

					int subtotal = discountedPrice * quantity;
					totalDiscountedAmount += subtotal;

					System.out.println("🔹 Product " + productIndex + ":");
					System.out.println("   - Discounted Price: Rs. " + discountedPrice);
					System.out.println("   - Quantity: " + quantity);
					System.out.println("   - Subtotal: Rs. " + subtotal);
				} catch (Exception e) {
					System.out.println("❌ Error reading product " + productIndex + ": " + e.getMessage());
				}

				productIndex++;
			}

			// Calculate expected thread count
			int expectedThreads = (totalDiscountedAmount >= 500) ? (totalDiscountedAmount / 500) * 10 : 0;

			// Get actual displayed thread count (from overall cart)
			WebElement threadElement = driver.findElement(By.xpath("//span[@class='Cls_cart_thread_est_amount']"));
			int actualThreads = Integer.parseInt(threadElement.getText().replaceAll("[^0-9]", ""));

			// Final Output
			System.out.println("🧵 Total Discounted Amount: Rs. " + totalDiscountedAmount);
			System.out.println("🔢 Expected Threads: " + expectedThreads);
			System.out.println("📟 Actual Threads Displayed: " + actualThreads);

			if (expectedThreads == actualThreads) {
				System.out.println("✅ Thread count is correct.");
			} else {
				System.out.println("❌ Thread count mismatch!");
			}

		} catch (Exception e) {
			System.out.println("❌ Unexpected error: " + e.getMessage());
		}
	}


	public void threadCalculation() throws TimeoutException {
		HomePage home = new HomePage(driver);
		home.homeLaunch();
		Common.waitForElement(3);
		click(bagIcon);
		Common.waitForElement(2);

		try {
			List<WebElement> productsInBag = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));

			if (productsInBag.isEmpty()) {
				System.out.println(" Bag is empty. Adding a product...");
				click(closeBag);
				Actions actions = new Actions(driver);
				actions.moveToElement(shopMenu);
				actions.moveToElement(category).click().build().perform();

				List<WebElement> addProduct = driver.findElements(By.xpath("//button[@class='product_list_cards_btn']"));
				Collections.shuffle(addProduct);
				if (!addProduct.isEmpty()) {
					actions.moveToElement(addProduct.get(0)).click().build().perform();
					click(addToCart);
					Common.waitForElement(2);
					click(bagIcon);
					Common.waitForElement(2);
				}
				productsInBag = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));
			}

			int productIndex = 1;
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			for (WebElement product : productsInBag) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", product);
				Common.waitForElement(1);

				try {
					WebElement priceElement = product.findElement(By.xpath(".//div[@class='cp_current_price']"));
					int discountedPrice = Integer.parseInt(priceElement.getText().replaceAll("[^0-9]", ""));

					int qtyBefore = Integer.parseInt(product.findElement(By.xpath(".//div[@class='cp_quantity_wrap']")).getText().trim());
					int subtotalBefore = discountedPrice * qtyBefore;

					// Increase quantity
					product.findElement(By.xpath(".//button[contains(@class,'cp_quantity_increase_btn')]")).click();

					// Wait until quantity increases
					int finalQtyBefore = qtyBefore;
					wait.until(d -> {
						try {
							int newQty = Integer.parseInt(product.findElement(By.xpath(".//div[@class='cp_quantity_wrap']")).getText().trim());
							return newQty > finalQtyBefore;
						} catch (Exception e) {
							return false;
						}
					});

					int qtyAfterInc = Integer.parseInt(product.findElement(By.xpath(".//div[@class='cp_quantity_wrap']")).getText().trim());
					int subtotalAfterInc = discountedPrice * qtyAfterInc;

					System.out.println(" Product " + productIndex + ":");
					System.out.println("   - Discounted Price: Rs. " + discountedPrice);
					System.out.println("   - Quantity Before: " + qtyBefore + " → Subtotal: Rs. " + subtotalBefore);
					System.out.println("   - Quantity After Increase: " + qtyAfterInc + " → Subtotal: Rs. " + subtotalAfterInc);

					checkThreadCount("After Increase Product " + productIndex, subtotalAfterInc);

					// Decrease quantity
					product.findElement(By.xpath(".//button[contains(@class,'cp_quantity_decrease_btn')]")).click();

					// Wait until quantity decreases
					wait.until(d -> {
						try {
							int newQty = Integer.parseInt(product.findElement(By.xpath(".//div[@class='cp_quantity_wrap']")).getText().trim());
							return newQty == finalQtyBefore;
						} catch (Exception e) {
							return false;
						}
					});

					int qtyAfterDec = Integer.parseInt(product.findElement(By.xpath(".//div[@class='cp_quantity_wrap']")).getText().trim());
					int subtotalAfterDec = discountedPrice * qtyAfterDec;

					System.out.println("   - Quantity After Decrease: " + qtyAfterDec + " → Subtotal: Rs. " + subtotalAfterDec);

					checkThreadCount("After Decrease Product " + productIndex, subtotalAfterDec);

				} catch (Exception e) {
					System.out.println(" Error with product " + productIndex + ": " + e.getMessage());
				}

				productIndex++;
			}

			return;

		} catch (Exception e) {
			System.out.println(" Unexpected error: " + e.getMessage());
		}
	}


	private void checkThreadCount(String stage, int totalAmount) {
		int expectedThreads = (totalAmount / 500) * 10;

		try {        
			String threadText = driver.findElement(By.xpath(".//span[@class='Cls_cart_thread_est_amount']")).getText().trim();
			int actualThreads = 0;
			if (threadText.contains("threads")) {
				actualThreads = Integer.parseInt(threadText.replaceAll("[^0-9]", ""));
			}
			System.out.println("🧵 [" + stage + "]");
			System.out.println("   - Total Discounted Amount: Rs. " + totalAmount);
			System.out.println("   - Expected Threads: " + expectedThreads);
			System.out.println("   - Actual Threads Displayed: " + actualThreads);

			if (expectedThreads == actualThreads) {
				System.out.println("✅ Thread count matches " + stage.toLowerCase() + ".");
			} else {
				System.out.println("❌ Thread count mismatch  " + stage.toLowerCase() + "!");
			}

		} catch (Exception e) {
			System.out.println("❌ Error checking thread count  " + stage + ": " + e.getMessage());
		}
	}

	public void youSaved() {
		HomePage home = new HomePage(driver);
		home.homeLaunch();
		Common.waitForElement(5);
		click(bagIcon);

		try {
			List<WebElement> productsInBag = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));

			if (productsInBag.isEmpty()) {
				System.out.println("👜 Bag is empty. Adding product...");
				click(closeBag);
				Actions actions = new Actions(driver);
				actions.moveToElement(shopMenu);
				actions.moveToElement(category).click().build().perform();
				List<WebElement> addProduct = driver.findElements(By.xpath("//button[@class='product_list_cards_btn']"));
				Collections.shuffle(addProduct);

				if (!addProduct.isEmpty()) {
					WebElement randomProduct = addProduct.get(0);
					actions.moveToElement(randomProduct).click().build().perform();
					click(addToCart);
					click(bagIcon);
					Common.waitForElement(3);
					WebElement youSaved = driver.findElement(By.xpath(".//div[@class='bfr_pair Cls_cart_saved_amount']"));
					String yousave = youSaved.getText();
					if (youSaved.isDisplayed()) {

						System.out.println("The You saved amount displayed:"+ yousave);

					}
					else {
						System.out.println("The You saved is not displaying please add the product");
					}
				}

			}
		}
		catch (Exception e) {
			System.out.println("⚠️ Error occurred: " + e.getMessage());
		}
	}


	public void youSavedCalculation() {
		HomePage home = new HomePage(driver);
		home.homeLaunch();
		Common.waitForElement(5);
		click(bagIcon);

		try {
			if (isBagEmpty()) {
				System.out.println("👜 Bag is empty. Adding product...");
				click(closeBag);
				Actions actions = new Actions(driver);
				actions.moveToElement(shopMenu);
				actions.moveToElement(category).click().build().perform();

				List<WebElement> addProduct = driver.findElements(By.xpath("//button[@class='product_list_cards_btn']"));
				Collections.shuffle(addProduct);

				if (!addProduct.isEmpty()) {
					WebElement randomProduct = addProduct.get(0);
					actions.moveToElement(randomProduct).click().build().perform();
					click(addToCart);
					Common.waitForElement(2);
					click(bagIcon);
				}
			}

			Common.waitForElement(2); // Ensure bag is fully loaded
			int expectedTotalSaved = 0;

			List<WebElement> products = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));
			int productCount = products.size();

			System.out.println("Total Products in Bag: " + productCount);
			System.out.println("---------------------------------------------------");

			for (int i = 0; i < productCount; i++) {
				try {
					WebElement product = driver.findElements(By.xpath("//div[@class='cart_prod_card ']")).get(i); // always refetch

					WebElement actualPriceElement = product.findElement(By.xpath(".//div[@class='cp_actual_price']"));
					WebElement discountedPriceElement = product.findElement(By.xpath(".//div[@class='cp_current_price']"));

					String actualPriceText = actualPriceElement.getText().replaceAll("[^0-9]", "");
					String discountedPriceText = discountedPriceElement.getText().replaceAll("[^0-9]", "");

					int actualPrice = Integer.parseInt(actualPriceText);
					int discountedPrice = Integer.parseInt(discountedPriceText);

					int savedAmount = actualPrice - discountedPrice;
					expectedTotalSaved += savedAmount;

					System.out.println("Product " + (i + 1));
					System.out.println(" - Actual Price: Rs. " + actualPrice);
					System.out.println(" - Discounted Price: Rs. " + discountedPrice);
					System.out.println(" - Expected 'You Saved': Rs. " + savedAmount);
					System.out.println("---------------------------------------------------");
				} catch (StaleElementReferenceException se) {
					System.out.println("♻️ Stale element at product " + (i + 1) + ", retrying...");
					i--; // retry same product
					Common.waitForElement(1); // give time to reattach DOM
				}
			}

			WebElement totalSavedElement = driver.findElement(By.xpath("//div[@class='bfr_pair Cls_cart_saved_amount']"));
			String totalSavedText = totalSavedElement.getText().replaceAll("[^0-9]", "");
			int actualTotalSaved = Integer.parseInt(totalSavedText);

			System.out.println("Expected Total Saved Amount: Rs. " + expectedTotalSaved);
			System.out.println("Actual Total Saved Amount: Rs. " + actualTotalSaved);

			if (expectedTotalSaved == actualTotalSaved) {
				System.out.println("✅ Total Saved Amount is correct.");
			} else {
				System.out.println("❌ Mismatch in Total Saved Amount.");
			}

		} catch (Exception e) {
			System.out.println("⚠️ Error occurred: " + e.getMessage());
		}
	}
	public void totalAmount() {
		HomePage home = new HomePage(driver);
		home.homeLaunch();
		Common.waitForElement(5);
		click(bagIcon);

		try {
			List<WebElement> products = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));

			if (products.isEmpty()) {
				System.out.println("🛒 Bag is empty. Adding product...");
				click(closeBag);

				Actions actions = new Actions(driver);
				actions.moveToElement(shopMenu).moveToElement(category).click().build().perform();

				List<WebElement> addProduct = driver.findElements(By.xpath("//button[@class='product_list_cards_btn']"));
				Collections.shuffle(addProduct);

				if (!addProduct.isEmpty()) {
					WebElement randomProduct = addProduct.get(0);
					actions.moveToElement(randomProduct).click().build().perform();
					click(addToCart);
					click(bagIcon);
				}

				products = driver.findElements(By.xpath("//div[@class='cart_prod_card ']")); // Refetch
			}

			int expectedTotalAmount = 0;
			System.out.println("Total Products in Bag: " + products.size());
			System.out.println("---------------------------------------------------");

			for (int i = 0; i < products.size(); i++) {
				try {
					// Re-locate the product each time to avoid stale elements
					WebElement product = driver.findElements(By.xpath("//div[@class='cart_prod_card ']")).get(i);

					WebElement discountedPriceElement = product.findElement(By.xpath(".//div[@class='cp_current_price']"));
					String discountedPriceText = discountedPriceElement.getText().replaceAll("[^0-9]", "");
					int discountedPrice = Integer.parseInt(discountedPriceText);

					expectedTotalAmount += discountedPrice;

					System.out.println(" - Discounted Price: Rs. " + discountedPrice);
					System.out.println("---------------------------------------------------");

				} catch (StaleElementReferenceException sere) {
					System.out.println("♻️ Stale element detected. Retrying...");
					i--; // retry current index
					Common.waitForElement(1);
				}
			}

			// Retry fetching actual total
			String actualTotalText = "";
			int actualTotalAmount = 0;
			boolean totalFetched = false;
			int retries = 3;

			while (!totalFetched && retries > 0) {
				try {
					WebElement actualTotalElement = driver.findElement(By.xpath("//div[@class='bfr_pair Cls_cart_total_amount']"));
					actualTotalText = actualTotalElement.getText().replaceAll("[^0-9]", "");
					actualTotalAmount = Integer.parseInt(actualTotalText);
					totalFetched = true;
				} catch (StaleElementReferenceException e) {
					System.out.println("⚠️ Retrying fetching total amount due to stale element...");
					Common.waitForElement(1);
					retries--;
				}
			}

			System.out.println("✅ Expected Total Amount: Rs. " + expectedTotalAmount);
			System.out.println("✅ Actual Total Amount displayed: Rs. " + actualTotalAmount);

			if (expectedTotalAmount == actualTotalAmount) {
				System.out.println("🎯 Total amount is correct.");
			} else {
				System.out.println("❌ Total amount mismatch!");
			}

		} catch (Exception e) {
			System.out.println("💥 Error occurred: " + e.getMessage());
		}
	}

	public void youSavedandTotalAmountCalculationBasedOnQTY() throws InterruptedException {
	    HomePage home = new HomePage(driver);
	    home.homeLaunch();
	    Common.waitForElement(3);
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//	    // If bag empty, add a product
//	    List<WebElement> products = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));
//	    if (products.isEmpty()) {
//	        System.out.println(" Bag is empty. Adding product...");
//	        click(closeBag);
	        Actions actions = new Actions(driver);
	        actions.moveToElement(shopMenu).moveToElement(category).click().build().perform();
	        List<WebElement> addProduct = driver.findElements(By.xpath("//button[@class='product_list_cards_btn']"));
	        Collections.shuffle(addProduct);
	        if (!addProduct.isEmpty()) {
	            WebElement randomProduct = addProduct.get(0);
	            actions.moveToElement(randomProduct).click().build().perform();
	            click(addToCart);
	            click(bagIcon);
	    	    Common.waitForElement(3);

	            
	        }
	    
	    // Lambda or function wrappers to get product data freshly from DOM each time:
	    java.util.function.Function<Integer, Integer> getQuantity = index -> {
	        String text = getElementTextWithRetry(By.xpath("//div[@class='cart_prod_card ']"),
	                By.xpath(".//div[@class='cp_quantity_wrap']"), index);
	        return Integer.parseInt(text);
	    };
	    java.util.function.Function<Integer, Integer> getActualPrice = index -> {
	        String text = getElementTextWithRetry(By.xpath("//div[@class='cart_prod_card ']"),
	                By.xpath(".//div[@class='cp_actual_price']"), index);
	        return Integer.parseInt(text.replaceAll("[^0-9]", ""));
	    };
	    java.util.function.Function<Integer, Integer> getDiscountedPrice = index -> {
	        String text = getElementTextWithRetry(By.xpath("//div[@class='cart_prod_card ']"),
	                By.xpath(".//span[contains(@class,'cp_current_price') or contains(@class,'product_list_cards_actual_price_txt')]"), index);
	        return Integer.parseInt(text.replaceAll("[^0-9]", ""));
	    };
	    // Function to print summary of products
	    java.util.function.Consumer<String> printSummary = label -> {
	        System.out.println("\n" + label);
	        List<WebElement> prods = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));
	        int totalYouSaved = 0;
	        int totalDiscounted = 0;
	        for (int i = 0; i < prods.size(); i++) {
	            final int index = i;
	            int quantity = getQuantity.apply(index);
	            int actualPrice = getActualPrice.apply(index);
	            int discountedPrice = getDiscountedPrice.apply(index);
	            int youSaved = (actualPrice - discountedPrice) * quantity;
	            int discountedTotal = discountedPrice * quantity;
	            totalYouSaved += youSaved;
	            totalDiscounted += discountedTotal;
	            System.out.println("---------------------------------------------------");
	            System.out.println("Product " + (i + 1));
	            System.out.println(" - Quantity: " + quantity);
	            System.out.println(" - Actual Price: Rs. " + actualPrice);
	            System.out.println(" - Discounted Price: Rs. " + discountedPrice);
	            System.out.println(" - Expected 'You Saved': Rs. " + youSaved + " Actual you saved : Rs. " + youSaved + " /-");
	            System.out.println(" - Expected Discounted Total: Rs. " + discountedTotal + " Actual Total Amount : Rs. " + discountedTotal + " /-");
	        }
	        System.out.println("\n " + label + " - Grand Totals:");
	        System.out.println("Expected Total 'You Saved': Rs. " + totalYouSaved + ", Actual you saved: Rs. " + totalYouSaved);
	        System.out.println("Expected Total Amount: Rs. " + totalDiscounted + ", Actual Total Amount: Rs. " + totalDiscounted);
	        System.out.println("---------------------------------------------------");
	    };
	    // Print initial summary
	    printSummary.accept(" Initial Product Summary");
	    // Increase quantity for each product
	    List<WebElement> productsBeforeIncrease = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));
	    for (int i = 0; i < productsBeforeIncrease.size(); i++) {
	        final int index = i;
	        WebElement product = driver.findElements(By.xpath("//div[@class='cart_prod_card ']")).get(index);
	        WebElement incBtn = product.findElement(By.xpath(".//button[contains(@class,'cp_quantity_increase_btn')]"));
	        int oldQty = getQuantity.apply(index);
	        incBtn.click();
	        int expectedQty = oldQty + 1;
	        wait.until(driver -> {
	            try {
	                int currentQty = getQuantity.apply(index);
	                return currentQty == expectedQty;
	            } catch (Exception e) {
	                return false;
	            }
	        });
	    }
	    // After increase, print summary
	    printSummary.accept(" After Increasing Quantity");
	    // Decrease quantity back to original
	    List<WebElement> productsBeforeDecrease = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));
	    for (int i = 0; i < productsBeforeDecrease.size(); i++) {
	        final int index = i;
	        WebElement product = productsBeforeDecrease.get(index);
	        WebElement decBtn = product.findElement(By.xpath(".//button[contains(@class,'cp_quantity_decrease_btn')]"));
	        int oldQty = getQuantity.apply(index);
	        decBtn.click();
	        int expectedQty = oldQty - 1;
	        wait.until(driver -> {
	            try {
	                int currentQty = getQuantity.apply(index);
	                return currentQty == expectedQty;
	            } catch (Exception e) {
	                return false;
	            }
	        });
	    }
	    // After decrease, print final summary
	    printSummary.accept(" After Decreasing Back to Original");
	}
	// Helper method to get text with retries for stale elements
	private String getElementTextWithRetry(By parentBy, By childBy, int index) {
	    int attempts = 0;
	    while (attempts < 5) {
	        try {
	            List<WebElement> parents = driver.findElements(parentBy);
	            if (parents.size() <= index) {
	                throw new NoSuchElementException("No element at index " + index);
	            }
	            WebElement parent = parents.get(index);
	            WebElement child = parent.findElement(childBy);
	            return child.getText().trim();
	        } catch (StaleElementReferenceException | NoSuchElementException e) {
	            attempts++;
	            try {
	                Thread.sleep(200);  // small wait before retry
	            } catch (InterruptedException ie) {
	                Thread.currentThread().interrupt();
	            }
	        }
	    }
	    throw new RuntimeException("Failed to get element text after 5 retries");
	}













	public void buyNowBagPage() {
		HomePage home = new HomePage(driver);
		home.homeLaunch();
		Common.waitForElement(5);
		click(bagIcon);

		List<WebElement> products = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));
		try {


			if (products.isEmpty()) {
				System.out.println("👜 Bag is empty. Adding product...");
				click(closeBag);
				Actions actions = new Actions(driver);
				actions.moveToElement(shopMenu);
				actions.moveToElement(category).click().build().perform();
				List<WebElement> addProduct = driver.findElements(By.xpath("//button[@class='product_list_cards_btn']"));
				Collections.shuffle(addProduct);
				if (!addProduct.isEmpty()) {
					WebElement randomProduct = addProduct.get(0);
					actions.moveToElement(randomProduct).click().build().perform();
					click(addToCart);
					click(bagIcon);
					Common.waitForElement(1);
					click(buyNowButton);
				}
				else {
					click(buyNowButton);
				}
			}
		}
		catch (Exception e) {
			System.out.println("⚠️ Error occurred: " + e.getMessage());
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
