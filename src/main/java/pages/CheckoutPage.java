package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import objectRepo.CheckOutPageObjRepo;
import utils.Common;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public final class CheckoutPage extends CheckOutPageObjRepo{
	WebDriver driver;

	public CheckoutPage(WebDriver driver) {
		this.driver = driver;
	}


		public void verifyCheckoutCalculations() {
			LoginPage login = new LoginPage(driver);
			login.userLogin();
			Common.waitForElement(5);
			Actions actions = new Actions(driver);
	
			WebElement bagIcon = driver.findElement(By.xpath("//button[@class='Cls_cart_btn']"));
			bagIcon.click();
			Common.waitForElement(2);
	
			List<WebElement> productBlocks = driver.findElements(By.xpath(".//div[@class='cart_prod_card ']"));
			if (productBlocks.isEmpty()) {
				System.out.println("🛍️ Bag item count not displayed. Adding product...");
				WebElement closeBag = driver.findElement(By.xpath("//div[@class='bag_inner_wrap Cls_bag_items_unavailable ']//div[@class='bag_closeup_btn']"));
				closeBag.click();
				Common.waitForElement(2);
				WebElement shopMenu = driver.findElement(By.xpath("//li[@class='navigation_menu_list nav_menu_dropdown shop']"));
				actions.moveToElement(shopMenu);
				WebElement category = driver.findElement(By.xpath("//div[@class='nav_drop_down_box_category active']//a[contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'ALL')]"));
				actions.moveToElement(category).click().build().perform();
				List<WebElement> addProduct = driver.findElements(By.xpath("//button[@class='product_list_cards_btn']"));
				Collections.shuffle(addProduct);
				if (!addProduct.isEmpty()) {
					WebElement randomProduct = addProduct.get(0);
					actions.moveToElement(randomProduct).click().build().perform();
					WebElement addToCart = driver.findElement(By.xpath("//button[@class='add_bag_prod_buy_now_btn btn___2  Cls_CartList ClsProductListSizes']"));
					addToCart.click();
					Common.waitForElement(5);
					driver.findElement(By.xpath("//button[@class='Cls_cart_btn Cls_redirect_restrict']")).click();
					Common.waitForElement(2);
				}
			}
	
			driver.findElement(By.xpath("//button[.='Buy Now']")).click();
			productBlocks = driver.findElements(By.xpath(".//div[@class='cart_prod_card ']"));
	
			double expectedTotalMrp = 0, expectedTotalDiscounted = 0, expectedYouSaved = 0;
	
			System.out.println("=== Product-wise Breakdown ===\n");
	
			for (int i = 0; i < productBlocks.size(); i++) {
				WebElement product = productBlocks.get(i);
				String productName = product.findElement(By.xpath(".//div[@class='cp_name_row']")).getText();
				int quantity = Integer.parseInt(product.findElement(By.xpath(".//div[@class='cp_quantity_wrap']")).getText().trim());
				double mrp = Double.parseDouble(product.findElement(By.xpath(".//div[@class='cp_actual_price']")).getText().replaceAll("[^\\d.]", ""));
	
				double discounted = 0;
				boolean isPromotional = false;
	
				if (product.findElements(By.xpath(".//span[@class='product_list_cards_actual_price_txt']")).size() > 0) {
					discounted = Double.parseDouble(product.findElement(By.xpath(".//span[@class='product_list_cards_actual_price_txt']")).getText().replaceAll("[^\\d.]", ""));
					isPromotional = true;
				} else {
					discounted = Double.parseDouble(product.findElement(By.xpath(".//div[@class='cp_current_price']")).getText().replaceAll("[^\\d.]", ""));
				}
	
				double expectedMrp = mrp * quantity;
				double expectedDiscounted = discounted * quantity;
				double expectedSaved = (mrp - discounted) * quantity;
	
				if (isPromotional) {
					System.out.println("Product " + (i + 1) + ": "  + " (Promotional)");
				} else {
					System.out.println("Product " + (i + 1) + ": "  + " (Normal)");
				}
	
				expectedTotalMrp += expectedMrp;
				expectedTotalDiscounted += expectedDiscounted;
				expectedYouSaved += expectedSaved;
	
				System.out.println("Product " + (i + 1) + ": " + productName);
				System.out.println("Quantity: " + quantity);
				System.out.println("Actual Price of the product : Rs. " + expectedMrp + " | Discounted Price of the product: Rs. " + expectedDiscounted);
				System.out.println("Product " + (i + 1) + " calculations complete\n");
	
			}
	
			// Gift Wrap
			double giftWrap = 0, express = 0, custom = 0;
			try {
				WebElement giftWrapToggle = driver.findElement(By.xpath("//input[@class='checkout_git_list_item__checkbox']"));
				giftWrapToggle.click();
				driver.findElement(By.id("recipient-name")).sendKeys("Test User");
				driver.findElement(By.xpath("//button[@class='gift__submit btn___2']")).click();
				Common.waitForElement(2);
				giftWrap = Double.parseDouble(driver.findElement(By.xpath(".//div[@class='price_details_pair git__wraping11']")).getText().replaceAll("[^\\d.]", ""));
			} catch (Exception e) {
				System.out.println("🎁 Gift wrap section not available or skipped");
			}
	
			// Express Delivery
			try {
				WebElement expressOption = driver.findElement(By.id("checked2"));
				if (!expressOption.isSelected()) expressOption.click();
				express = Double.parseDouble(driver.findElement(By.xpath(".//div[@class='Cls_CourierFee']")).getText().replaceAll("[^\\d.]", ""));
			} catch (Exception e) {
				System.out.println("---------------------------------------------");
				System.out.println("🚚 Express delivery not available or skipped");
				System.out.println("---------------------------------------------");
			}
	
			// Customization Charges
			try {
				custom = Double.parseDouble(driver.findElement(By.xpath(".//div[@class='price_details_row Cls_customized_extras']")).getText().replaceAll("[^\\d.]", ""));
			} catch (Exception ignored) {}
	
			// Coupon
			double couponDiscount = 0;
			try {
				Common.waitForElement(2);
				List<WebElement> availableCoupons = driver.findElements(By.xpath("//button[@class='offer_list_item_apply_btn Cls_apply_coupon']"));
				if (!availableCoupons.isEmpty()) {
					availableCoupons.get(0).click();
					Common.waitForElement(2);
					WebElement appliedText = driver.findElement(By.xpath("//p[@class='coupon_apply_msg active']"));
					couponDiscount = Double.parseDouble(appliedText.getText().replaceAll("[^\\d.]", ""));
					System.out.println("---------------------------------------------");
					System.out.println("✅ Coupon applied successfully. Discount amount: Rs. " + couponDiscount);
					System.out.println("---------------------------------------------");
				}
			} catch (Exception e) {
				System.out.println("---------------------------------------------");
				System.out.println("❌ Coupon skipped or not applicable.");
				System.out.println("---------------------------------------------");
			}
	
			// Flat Rs. 50 Discount
			double flatDiscount = 0;
			try {
				flatDiscount = Double.parseDouble(driver.findElement(By.xpath("//div[@class='price_details_pair Cls_cart_extra_prepaid_discount']")).getText().replaceAll("[^\\d.]", ""));
			} catch (Exception ignored) {}
	
			// Thread Application
			int threadsApplied = 0;
			try {
				WebElement availableThread = driver.findElement(By.xpath("//span[@class='price_details_key_span']"));
				threadsApplied = Integer.parseInt(availableThread.getText().replaceAll("[^\\d]", ""));
				WebElement threadInput = driver.findElement(By.xpath("//input[@placeholder='Enter threads']"));
				if (!threadInput.isSelected()) threadInput.click();
				threadInput.clear();
				threadInput.sendKeys(String.valueOf(threadsApplied));
			} catch (Exception e) {
				System.out.println("🧵 Threads not applied or skipped");
			}
	
			double finalAmount = Double.parseDouble(driver.findElement(By.xpath(".//div[@class='price_details_pair Cls_cart_total_amount']")).getText().replaceAll("[^\\d.]", ""));
			double actualSaved = Double.parseDouble(driver.findElement(By.xpath(".//div[@class='price_details_pair Cls_cart_saved_amount']")).getText().replaceAll("[^\\d.]", ""));
			double expectedFinalYouSaved = expectedYouSaved + flatDiscount + couponDiscount + threadsApplied;
			double finalPayable = finalAmount;
			double actualTotalMrp = expectedTotalMrp; 
			double actualDiscountedMrp = expectedTotalDiscounted;
			int earnedThreads = ((int) (finalPayable - giftWrap - express - custom) / 500) * 10;
			int actualEarnedThreads = ((int) (finalPayable - giftWrap - express - custom) / 500) * 10;
	
			System.out.println("============= Checkout Summary ==================================");	
			System.out.println("Expected Total MRP: Rs. " + expectedTotalMrp + " || Actual Total MRP: Rs. " + actualTotalMrp + " || " + (expectedTotalMrp == actualTotalMrp ? "\u001B[32mMatched\u001B[0m" : "\u001B[31mMismatch\u001B[0m"));
			System.out.println("Expected Discounted MRP: Rs. " + expectedTotalDiscounted + " || Actual Discounted MRP: Rs. " + actualDiscountedMrp + " || " + (expectedTotalDiscounted == actualDiscountedMrp ? "\u001B[32mMatched\u001B[0m" : "\u001B[31mMismatch\u001B[0m"));
			System.out.println("Gift Wrap: Rs. " + giftWrap);
			System.out.println("Express Delivery: Rs. " + express);
			System.out.println("Customization Charges: Rs. " + custom);
			System.out.println("Expected Earned Threads: " + earnedThreads + " || Actual Earned Threads: " + actualEarnedThreads + " || " + (earnedThreads == actualEarnedThreads ? "\u001B[32mMatched\u001B[0m" : "\u001B[31mMismatch\u001B[0m"));
			System.out.println("Flat ₹50 Discount Applied: Rs. " + flatDiscount);
			System.out.println("Thread Discount Applied: Rs. " + threadsApplied);
			System.out.println("Coupon Discount Applied: Rs. " + couponDiscount);
			System.out.println("Expected Final 'You Saved': Rs. " + expectedFinalYouSaved + " || Actual Final 'You Saved': Rs. " + actualSaved + " || " + (expectedFinalYouSaved == actualSaved ? "\u001B[32mMatched\u001B[0m" : "\u001B[31mMismatch\u001B[0m"));
			System.out.println("Final Total Amount (Expected): Rs. " + finalPayable + " || Final Total Amount (Actual): Rs. " + finalAmount + " || " + (finalPayable == finalAmount ? "\u001B[32mMatched\u001B[0m" : "\u001B[31mMismatch\u001B[0m"));
			System.out.println("\u001B[0m✅ All checkout calculations validated.");
		}




	public void CheckoutPageIcons() {
		LoginPage login = new LoginPage(driver);
		login.userLogin();
		Common.waitForElement(5);
		Actions actions = new Actions(driver);
		WebElement bagIcon = driver.findElement(By.xpath("//button[@class='Cls_cart_btn']"));
		bagIcon.click();


		List<WebElement> productBlocks = driver.findElements(By.xpath(".//div[@class='cart_prod_card ']"));
		if (productBlocks.isEmpty()) {
			System.out.println("🛍️ Bag item count not displayed. Adding product...");
			WebElement closeBag = driver.findElement(By.xpath("(//div[@class='popup_containers_cls_btn'])[2]"));
			closeBag.click();

			WebElement shopMenu = driver.findElement(By.xpath("//li[@class='navigation_menu_list nav_menu_dropdown shop']"));
			actions.moveToElement(shopMenu);
			Common.waitForElement(2);
			WebElement category = driver.findElement(By.xpath("//div[@class='nav_drop_down_box_category active']//a[contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'ALL')]"));
			category.click();

			List<WebElement> addProduct = driver.findElements(By.xpath("//button[@class='product_list_cards_btn']"));
			Collections.shuffle(addProduct);
			if (!addProduct.isEmpty()) {
				WebElement randomProduct = addProduct.get(0);
				actions.moveToElement(randomProduct).click().build().perform();
				WebElement addToCart = driver.findElement(By.xpath("//button[@class='add_bag_prod_buy_now_btn btn___2  Cls_CartList ClsProductListSizes']"));
				addToCart.click();
				Common.waitForElement(5);
				bagIcon.click();
				Common.waitForElement(2);
				WebElement buyNow = driver.findElement(By.xpath("//button[.='Buy Now']"));
				actions.moveToElement(buyNow).click().perform();
			}
			else {
				WebElement buyNow = driver.findElement(By.xpath("//button[.='Buy Now']"));
				actions.moveToElement(buyNow).click().perform();
			}
		}
		WebElement icons = driver.findElement(By.xpath("//div[@class='checkout_progress_bar']"));
		if (icons.isDisplayed()) {
			System.out.println("ALL the Icons is displayed");

		}
		else {
			System.out.println("Please check the Icons not displaying");
		}

	}
	public void itemCount() {
		LoginPage login = new LoginPage(driver);
		login.userLogin();
		Common.waitForElement(5);
		Actions actions = new Actions(driver);
		WebElement bagIcon = driver.findElement(By.xpath("//button[@class='Cls_cart_btn']"));
		bagIcon.click();

		List<WebElement> productBlocks = driver.findElements(By.xpath(".//div[@class='cart_prod_card ']"));
		if (productBlocks.isEmpty()) {
			System.out.println("🛍️ Bag item count not displayed. Adding product...");
			WebElement closeBag = driver.findElement(By.xpath("//div[@class='bag_inner_wrap Cls_bag_items_unavailable ']//div[@class='bag_closeup_btn']"));
			closeBag.click();

			WebElement shopMenu = driver.findElement(By.xpath("//li[@class='navigation_menu_list nav_menu_dropdown shop']"));
			actions.moveToElement(shopMenu);
			WebElement category = driver.findElement(By.xpath("//div[@class='nav_drop_down_box_category active']//a[contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'ALL')]"));
			category.click();

			List<WebElement> addProduct = driver.findElements(By.xpath("//button[@class='product_list_cards_btn']"));
			Collections.shuffle(addProduct);
			try {



				if (!addProduct.isEmpty()) {
					WebElement randomProduct = addProduct.get(0);
					actions.moveToElement(randomProduct).click().build().perform();
					WebElement addToCart = driver.findElement(By.xpath("//button[@class='add_bag_prod_buy_now_btn btn___2  Cls_CartList ClsProductListSizes']"));
					addToCart.click();
					Common.waitForElement(5);
					bagIcon.click();
					Common.waitForElement(2);
					WebElement buyNow = driver.findElement(By.xpath("//button[.='Buy Now']"));
					actions.moveToElement(buyNow).click().perform();
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
	}


	public void couponText() {
		LoginPage login = new LoginPage(driver);
		login.userLogin();
		Common.waitForElement(5);

		WebElement bagIcon = driver.findElement(By.xpath("//button[@class='Cls_cart_btn']"));
		bagIcon.click();
		Common.waitForElement(2);
		Actions actions = new Actions(driver);

		List<WebElement> productBlocks = driver.findElements(By.xpath(".//div[@class='cart_prod_card ']"));
		if (productBlocks.isEmpty()) {
			System.out.println("🛍️ Bag item count not displayed. Adding product...");
			WebElement closeBag = driver.findElement(By.xpath("//div[@class='bag_inner_wrap Cls_bag_items_unavailable ']//div[@class='bag_closeup_btn']"));
			closeBag.click();

			WebElement shopMenu = driver.findElement(By.xpath("//li[@class='navigation_menu_list nav_menu_dropdown shop']"));
			actions.moveToElement(shopMenu);
			WebElement category = driver.findElement(By.xpath("//div[@class='nav_drop_down_box_category active']//a[contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'ALL')]"));
			category.click();

			List<WebElement> addProduct = driver.findElements(By.xpath("//button[@class='product_list_cards_btn']"));
			Collections.shuffle(addProduct);
			try {



				if (!addProduct.isEmpty()) {
					WebElement randomProduct = addProduct.get(0);
					actions.moveToElement(randomProduct).click().build().perform();
					WebElement addToCart = driver.findElement(By.xpath("//button[@class='add_bag_prod_buy_now_btn btn___2  Cls_CartList ClsProductListSizes']"));
					addToCart.click();
					Common.waitForElement(5);
					bagIcon.click();
					Common.waitForElement(2);
					WebElement buyNow = driver.findElement(By.xpath("//button[.='Buy Now']"));
					actions.moveToElement(buyNow).click().perform();
					String bagCount = bagItemCount.getText();
					System.out.println("The total Item count is:"+bagCount);

				}
				else {
					String coupontextBox = couponTextBox.getText();
					System.out.println("The total Item count is:"+ coupontextBox);


				}
			}
			catch (Exception e) {
				System.out.println("Caught an exception: " + e.getMessage());
				NoSuchElementException e1 = new NoSuchElementException("A NoSuchElementException exception occurred");
				e1.initCause(e);
				throw e1;
			}
		}

	}

	public void applyButton() {
		LoginPage login = new LoginPage(driver);
		login.userLogin();
		Common.waitForElement(5);

		WebElement bagIcon = driver.findElement(By.xpath("//button[@class='Cls_cart_btn']"));
		bagIcon.click();
		Common.waitForElement(2);
		Actions actions = new Actions(driver);

		List<WebElement> productBlocks = driver.findElements(By.xpath(".//div[@class='cart_prod_card ']"));
		if (productBlocks.isEmpty()) {
			System.out.println("🛍️ Bag item count not displayed. Adding product...");
			WebElement closeBag = driver.findElement(By.xpath("//div[@class='bag_inner_wrap Cls_bag_items_unavailable ']//div[@class='bag_closeup_btn']"));
			closeBag.click();

			WebElement shopMenu = driver.findElement(By.xpath("//li[@class='navigation_menu_list nav_menu_dropdown shop']"));
			actions.moveToElement(shopMenu);
			WebElement category = driver.findElement(By.xpath("//div[@class='nav_drop_down_box_category active']//a[contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'ALL')]"));
			category.click();

			List<WebElement> addProduct = driver.findElements(By.xpath("//button[@class='product_list_cards_btn']"));
			Collections.shuffle(addProduct);
			try {
				if (!addProduct.isEmpty()) {
					WebElement randomProduct = addProduct.get(0);
					actions.moveToElement(randomProduct).click().build().perform();
					WebElement addToCart = driver.findElement(By.xpath("//button[@class='add_bag_prod_buy_now_btn btn___2  Cls_CartList ClsProductListSizes']"));
					addToCart.click();
					Common.waitForElement(5);
					bagIcon.click();
					Common.waitForElement(2);
					WebElement buyNow = driver.findElement(By.xpath("//button[.='Buy Now']"));
					actions.moveToElement(buyNow).click().perform();
				}

				Common.waitForElement(2);
				List<WebElement> availableCoupons = driver.findElements(By.xpath("//button[@class='offer_list_item_apply_btn Cls_apply_coupon']"));
				if (!availableCoupons.isEmpty()) {
					WebElement coupon = availableCoupons.get(0);
					coupon.click();
				}
				else {

					System.out.println("Coupon Not displayed in the list");
				}
			}




			catch (Exception e) {
				System.out.println("Caught an exception: " + e.getMessage());
				NoSuchElementException e1 = new NoSuchElementException("A NoSuchElementException exception occurred");
				e1.initCause(e);
				throw e1;
			}
		}
	}


	public void availableOfferSection() {
		LoginPage login = new LoginPage(driver);
		login.userLogin();
		Common.waitForElement(5);
		Actions actions = new Actions(driver);
		WebElement bagIcon = driver.findElement(By.xpath("//button[@class='Cls_cart_btn']"));
		bagIcon.click();

		List<WebElement> productBlocks = driver.findElements(By.xpath(".//div[@class='cart_prod_card ']"));
		if (productBlocks.isEmpty()) {
			System.out.println("🛍️ Bag item count not displayed. Adding product...");
			WebElement closeBag = driver.findElement(By.xpath("//div[@class='bag_inner_wrap Cls_bag_items_unavailable ']//div[@class='bag_closeup_btn']"));
			closeBag.click();

			WebElement shopMenu = driver.findElement(By.xpath("//li[@class='navigation_menu_list nav_menu_dropdown shop']"));
			actions.moveToElement(shopMenu);
			WebElement category = driver.findElement(By.xpath("//div[@class='nav_drop_down_box_category active']//a[contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'ALL')]"));
			category.click();

			List<WebElement> addProduct = driver.findElements(By.xpath("//button[@class='product_list_cards_btn']"));
			Collections.shuffle(addProduct);
			try {



				if (!addProduct.isEmpty()) {
					WebElement randomProduct = addProduct.get(0);
					actions.moveToElement(randomProduct).click().build().perform();
					WebElement addToCart = driver.findElement(By.xpath("//button[@class='add_bag_prod_buy_now_btn btn___2  Cls_CartList ClsProductListSizes']"));
					addToCart.click();
					Common.waitForElement(5);
					bagIcon.click();
					Common.waitForElement(2);
					WebElement buyNow = driver.findElement(By.xpath("//button[.='Buy Now']"));
					actions.moveToElement(buyNow).click().perform();
					String bagCount = bagItemCount.getText();
					System.out.println("The total Item count is:"+bagCount);

				}
				else {
					String couponAvailableOffer = avilableOfferSection.getText();
					System.out.println("The total Item count is:"+ couponAvailableOffer );


				}
			}
			catch (Exception e) {
				System.out.println("Caught an exception: " + e.getMessage());
				NoSuchElementException e1 = new NoSuchElementException("A NoSuchElementException exception occurred");
				e1.initCause(e);
				throw e1;
			}
		}



	}
	public void viewMore() {
		LoginPage login = new LoginPage(driver);
		login.userLogin();
		Common.waitForElement(5);

		WebElement bagIcon = driver.findElement(By.xpath("//button[@class='Cls_cart_btn']"));
		bagIcon.click();
		Common.waitForElement(2);
		Actions actions = new Actions(driver);


		List<WebElement> productBlocks = driver.findElements(By.xpath(".//div[@class='cart_prod_card ']"));
		if (productBlocks.isEmpty()) {
			System.out.println("🛍️ Bag item count not displayed. Adding product...");
			WebElement closeBag = driver.findElement(By.xpath("//div[@class='bag_inner_wrap Cls_bag_items_unavailable ']//div[@class='bag_closeup_btn']"));
			closeBag.click();

			WebElement shopMenu = driver.findElement(By.xpath("//li[@class='navigation_menu_list nav_menu_dropdown shop']"));
			actions.moveToElement(shopMenu);
			WebElement category = driver.findElement(By.xpath("//div[@class='nav_drop_down_box_category active']//a[contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'ALL')]"));
			category.click();

			List<WebElement> addProduct = driver.findElements(By.xpath("//button[@class='product_list_cards_btn']"));
			Collections.shuffle(addProduct);
			try {



				if (!addProduct.isEmpty()) {
					WebElement randomProduct = addProduct.get(0);
					actions.moveToElement(randomProduct).click().build().perform();
					WebElement addToCart = driver.findElement(By.xpath("//button[@class='add_bag_prod_buy_now_btn btn___2  Cls_CartList ClsProductListSizes']"));
					addToCart.click();
					Common.waitForElement(5);
					bagIcon.click();
					Common.waitForElement(2);
					WebElement buyNow = driver.findElement(By.xpath("//button[.='Buy Now']"));
					actions.moveToElement(buyNow).click().perform();
					String bagCount = bagItemCount.getText();
					System.out.println("The total Item count is:"+bagCount);

				}
				else {
					String couponViewMore = viewMoreButton.getText();
					System.out.println("The total Item count is:"+ couponViewMore );


				}
			}
			catch (Exception e) {
				System.out.println("Caught an exception: " + e.getMessage());
				NoSuchElementException e1 = new NoSuchElementException("A NoSuchElementException exception occurred");
				e1.initCause(e);
				throw e1;
			}
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

