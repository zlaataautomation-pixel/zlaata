package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import objectRepo.CheckOutPageObjRepo;
import utils.Common;
import utils.ExcelReportUtil;
import static org.junit.Assert.assertEquals;
import java.time.Duration;
import java.util.Collections;
import java.util.List;


public final class CheckoutPage extends CheckOutPageObjRepo{
	WebDriver driver;

	public CheckoutPage(WebDriver driver) {
		this.driver = driver;
	}


	public void verifyCheckoutCalculationsWithExcel() {
	    // 1️⃣ Navigate to checkout page
		LoginPage login = new LoginPage(driver);
		login.userLogin();
		Common.waitForElement(5);
		Actions actions = new Actions(driver);

		WebElement bagIcon = driver.findElement(By.xpath("//button[@class='Cls_cart_btn Cls_redirect_restrict']"));
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
				driver.findElement(By.xpath("//button[@class='Cls_cart_btn']")).click();
				Common.waitForElement(2);
			}
		}

		driver.findElement(By.xpath("//button[.='Buy Now']")).click();

	    // 2️⃣ Stage 1 - Initial checkout values
	    performCheckoutCalculationAndExcelLog("Initial");

	    // 3️⃣ Stage 2 - Increase quantity for each product
	    List<WebElement> products = driver.findElements(By.xpath("//div[@class='cart_prod_card ']"));
	    for (WebElement product : products) {
	        try {
	            WebElement plusBtn = product.findElement(By.xpath(".//button[contains(@class,'cp_quantity_increase_btn')]"));
	            new Actions(driver).moveToElement(plusBtn).click().perform();
	            Common.waitForElement(2);
	        } catch (Exception e) {
	            System.out.println("Could not increase quantity for product: " +
	                    safeGetText(product, ".//div[@class='cp_product_name']"));
	        }
	    }
	    Common.waitForElement(2);
	    performCheckoutCalculationAndExcelLog("After Increase");

	    // 4️⃣ Stage 3 - Decrease quantity back
	    for (WebElement product : products) {
	        try {
	            WebElement minusBtn = product.findElement(By.xpath(".//button[contains(@class,'cp_quantity_decrease_btn')]"));
	            new Actions(driver).moveToElement(minusBtn).click().perform();
	            Common.waitForElement(2);
	        } catch (Exception e) {
	            System.out.println("Could not decrease quantity for product: " +
	                    safeGetText(product, ".//div[@class='cp_product_name']"));
	        }
	    }
	    Common.waitForElement(2);
	    performCheckoutCalculationAndExcelLog("After Decrease");

	    // 5️⃣ Save Excel report at end
	    ExcelReportUtil.generateExcelReport();
	}
	
	private String safeGetTextDriver(String xpath) {
	    try {
	        return driver.findElement(By.xpath(xpath)).getText().trim();
	    } catch (Exception e) {
	        return "";
	    }
	}
	private double safeParseDouble(String s) {
	    try {
	        if (s == null || s.isEmpty()) return 0.0;
	        return Double.parseDouble(s.replaceAll("[^\\d.]", ""));
	    } catch (Exception e) {
	        return 0.0;
	    }
	}
	private String safeGetText(WebElement parent, String childXpath) {
	    try {
	        return parent.findElement(By.xpath(childXpath)).getText().trim();
	    } catch (Exception e) {
	        return "";
	    }
	}
	

	private boolean couponApplied = false;
	private boolean giftWrapApplied = false;
	private boolean expressApplied = false;
	private boolean threadsAppliedOnce = false;

	private double couponDiscount = 0;
	private double threadDiscount = 0;
	private double giftWrapCharge = 0;
	private double expressCharge = 0;
	private static final String RESET = "\u001B[0m";
	private static final String BLUE = "\u001B[34m";
	private static final String GREEN = "\u001B[32m";
	private static final String YELLOW = "\u001B[33m";
	private static final String RED = "\u001B[31m";
	private static final String CYAN = "\u001B[36m";
	private static final String MAGENTA = "\u001B[35m";
	
	private double getGiftWrapCharge() {
	    try {
	        if (!giftWrapApplied) {
	            WebElement giftWrapToggle = driver.findElement(By.xpath("//input[@class='checkout_git_list_item__checkbox']"));
	            giftWrapToggle.click();
	            driver.findElement(By.id("recipient-name")).sendKeys("Test User");
	            driver.findElement(By.xpath("//button[@class='gift__submit btn___2']")).click();
	            Common.waitForElement(2);
	            giftWrapCharge = safeParseDouble(safeGetTextDriver("//div[@class='price_details_pair git__wraping11']"));
	            giftWrapApplied = true;
	        } else {
	            giftWrapCharge = safeParseDouble(safeGetTextDriver("//div[@class='price_details_pair git__wraping11']"));
	        }
	    } catch (Exception ignored) {}
	    return giftWrapCharge;
	}

	private double getExpressCharge() {
	    try {
	        if (!expressApplied) {
	            WebElement expressOption = driver.findElement(By.name("delivery_speed"));
	            if (!expressOption.isSelected()) expressOption.click();
	            expressCharge = safeParseDouble(safeGetTextDriver("//div[@class='Cls_CourierFee']"));
	            expressApplied = true;
	        } else {
	            expressCharge = safeParseDouble(safeGetTextDriver("//div[@class='Cls_CourierFee']"));
	        }
	    } catch (Exception ignored) {}
	    return expressCharge;
	}

	private double getCustomizationCharge() {
	    try {
	        return safeParseDouble(safeGetTextDriver("//div[@class='price_details_row Cls_customized_extras']"));
	    } catch (Exception ignored) {}
	    return 0;
	}

	private double getFlatDiscount() {
	    try {
	        return safeParseDouble(safeGetTextDriver("//div[@class='price_details_pair Cls_cart_extra_prepaid_discount']"));
	    } catch (Exception ignored) {}
	    return 0;
	}

	private double applyCouponIfNotApplied() {
	    if (!couponApplied) {
	        try {
	            if (driver.findElements(By.xpath("//button[@class='checkout_details_sub_heading viewCouponBtn']")).size() > 0) {
	                driver.findElement(By.xpath("//button[@class='checkout_details_sub_heading viewCouponBtn']")).click();
	                Common.waitForElement(2);
	                List<WebElement> coupons = driver.findElements(By.xpath("//button[@class='coupon_apply_btn Cls_apply_coupon']"));
	                if (!coupons.isEmpty()) {
	                    coupons.get(0).click();
	                    Common.waitForElement(2);
	                    driver.findElement(By.xpath("//div[@class='coupon_popup popup_containers active']//button[@class='coupon_input_apply_btn']")).click();
	                    Common.waitForElement(2);
	                    couponDiscount = safeParseDouble(safeGetTextDriver("//p[@class='acc_details_status']"));
	                    couponApplied = true;
	                }
	            }
	        } catch (Exception ignored) {}
	    }
	    return couponDiscount;
	}

	private double applyThreadsIfNotApplied() {
	    if (!threadsAppliedOnce) {
	        try {
	            int availableThreads = (int) safeParseDouble(safeGetTextDriver("//span[@class='price_details_key_span']"));
	            if (availableThreads > 0) {
	                WebElement threadInput = driver.findElement(By.xpath("//input[@placeholder='Enter threads']"));
	                threadInput.clear();
	                threadInput.sendKeys(String.valueOf(availableThreads));
	                threadDiscount = availableThreads; // 1 thread = Rs.1
	                threadsAppliedOnce = true;
	            }
	        } catch (Exception ignored) {}
	    }
	    return threadDiscount;
	}
	private int getQuantity(WebElement product) {
	    try {
	        // First try from text
	        WebElement qtyElement = product.findElement(By.xpath(".//div[@class='cp_quantity_wrap']"));
	        String qtyText = qtyElement.getText().trim();
	        if (!qtyText.isEmpty()) {
	            return Integer.parseInt(qtyText);
	        }

	        // Try input value fallback
	        List<WebElement> qtyInputs = product.findElements(By.xpath(".//input[contains(@class,'cp_quantity_input')]"));
	        if (!qtyInputs.isEmpty()) {
	            String inputVal = qtyInputs.get(0).getAttribute("value").trim();
	            if (!inputVal.isEmpty()) {
	                return Integer.parseInt(inputVal);
	            }
	        }

	    } catch (Exception e) {
	        System.out.println(RED + "⚠️ Failed to retrieve quantity — defaulting to 1" + RESET);
	    }
	    return 1; // Default fallback
	}
	

	public void performCheckoutCalculationAndExcelLog(String stage) {
	    System.out.println(MAGENTA + "\n=== CHECKOUT CALCULATION STAGE: " + stage + " ===" + RESET);
	    List<WebElement> productBlocks = driver.findElements(By.xpath(".//div[@class='cart_prod_card ']"));
	    boolean skipStage = false;

	    // 🔹 Step 0 — Handle Quantity Change Logic Before Calculation
	    for (WebElement product : productBlocks) {
	        String productName = safeGetText(product, ".//a[@class='cp_name']");
	        int quantity = getQuantity(product);
	        int maxQtyAllowed = 2;
	        int minQtyAllowed = 1;

	        if ("AFTER_INCREASE".equalsIgnoreCase(stage)) {
	            if (quantity >= maxQtyAllowed) {
	                System.out.println(YELLOW + "⚠️ Max quantity reached for '" + productName + "' (" + quantity + ") — skipping increase stage" + RESET);
	                skipStage = true;
	            } else {
	                System.out.println(GREEN + "✅ Increasing quantity for '" + productName + "'" + RESET);
	                WebElement plusBtn = product.findElement(By.xpath(".//button[@class='cp_plus_btn']"));
	                plusBtn.click();
	                waitForQtyUpdate(product, quantity + 1);
	            }
	        }

	        if ("AFTER_DECREASE".equalsIgnoreCase(stage)) {
	            if (quantity <= minQtyAllowed) {
	                System.out.println(YELLOW + "⚠️ Min quantity reached for '" + productName + "' (" + quantity + ") — skipping decrease stage" + RESET);
	                skipStage = true;
	            } else {
	                System.out.println(GREEN + "✅ Decreasing quantity for '" + productName + "'" + RESET);
	                WebElement minusBtn = product.findElement(By.xpath(".//button[@class='cp_minus_btn']"));
	                minusBtn.click();
	                waitForQtyUpdate(product, quantity - 1);
	            }
	        }
	    }

	    if (skipStage) {
	        System.out.println(CYAN + "ℹ Stage '" + stage + "' skipped — No valid quantity change possible" + RESET);
	        return;
	    }

	    // 🔹 Step 1 — Product details
	    double totalMrp = 0, totalDiscounted = 0, totalYouSaved = 0;
	    for (WebElement product : productBlocks) {
	        int quantity = getQuantity(product);
	        double mrp = safeParseDouble(safeGetText(product, ".//div[@class='cp_actual_price']"));
	        double discounted;
	        if (product.findElements(By.xpath(".//span[@class='product_list_cards_actual_price_txt']")).size() > 0) {
	            discounted = safeParseDouble(safeGetText(product, ".//span[@class='product_list_cards_actual_price_txt']"));
	        } else {
	            discounted = safeParseDouble(safeGetText(product, ".//div[@class='cp_current_price']"));
	        }

	        totalMrp += mrp * quantity;
	        totalDiscounted += discounted * quantity;
	        totalYouSaved += (mrp - discounted) * quantity;

	        String productName = safeGetText(product, ".//a[@class='cp_name']");
	        System.out.printf(CYAN + "%-25s | Qty: %d | MRP: %.2f | Disc: %.2f | Line MRP: %.2f | Line Disc: %.2f | Saved: %.2f%n" + RESET,
	                productName, quantity, mrp, discounted, mrp * quantity, discounted * quantity, (mrp - discounted) * quantity);
	    }

	    // --- Charges / Discounts ---
	    double giftWrap = getGiftWrapCharge();
	    double express = getExpressCharge();
	    double customization = getCustomizationCharge();
	    double flatDiscount = getFlatDiscount();
	    double coupon = applyCouponIfNotApplied();
	    double threads = applyThreadsIfNotApplied();

	    // 🔹 Step 2 — Price breakdown
	    double finalSaved = totalYouSaved + flatDiscount + coupon + threads;
	    double finalAmount = totalDiscounted + giftWrap + express + customization - flatDiscount - coupon - threads;

	    System.out.println(YELLOW + "\n------ PRICE BREAKDOWN ------" + RESET);
	    System.out.printf("Base Discounted Price : %.2f%n", totalDiscounted);
	    System.out.printf(RED + "+ Gift Wrap : %.2f%n" + RESET, giftWrap);
	    System.out.printf(RED + "+ Express Delivery : %.2f%n" + RESET, express);
	    System.out.printf(RED + "+ Customization : %.2f%n" + RESET, customization);
	    System.out.printf(GREEN + "- Flat Discount : %.2f%n" + RESET, flatDiscount);
	    System.out.printf(GREEN + "- Coupon Discount : %.2f%n" + RESET, coupon);
	    System.out.printf(GREEN + "- Threads Discount : %.2f%n" + RESET, threads);
	    System.out.println(YELLOW + "-----------------------------" + RESET);
	    System.out.printf(GREEN + "Final Amount = %.2f%n" + RESET, finalAmount);

	    System.out.println("\n" + CYAN + "💰 SAVINGS BREAKDOWN" + RESET);
	    System.out.printf("Product Savings : %.2f%n", totalYouSaved);
	    System.out.printf("+ Flat Discount : %.2f%n", flatDiscount);
	    System.out.printf("+ Coupon Discount : %.2f%n", coupon);
	    System.out.printf("+ Threads Discount : %.2f%n", threads);
	    System.out.println(YELLOW + "-----------------------------" + RESET);
	    System.out.printf(GREEN + "Total You Saved = %.2f%n" + RESET, finalSaved);

	    // 🔹 Step 3 — UI values
	    double actualFinalAmount = safeParseDouble(safeGetTextDriver("//div[@class='price_details_pair Cls_cart_total_amount']"));
	    double actualSavedAmount = safeParseDouble(safeGetTextDriver("//div[@class='price_details_pair Cls_cart_saved_amount']"));

	    // 🔹 Step 4 — UI vs Expected comparison
	    System.out.println(MAGENTA + "\n=== UI vs EXPECTED CHECK ===" + RESET);
	    System.out.printf(BLUE + "UI Final Amount : %.2f" + RESET + " | " + GREEN + "Expected Final Amount : %.2f%n" + RESET,
	            actualFinalAmount, finalAmount);
	    System.out.printf(BLUE + "UI You Saved : %.2f" + RESET + " | " + GREEN + "Expected You Saved : %.2f%n" + RESET,
	            actualSavedAmount, finalSaved);

	    // 🔹 Step 5 — Pass/Fail logic
	    boolean pass = true;
	    String failReason = "";

	    if (Math.abs(finalSaved - actualSavedAmount) > 0.01) {
	        pass = false;
	        failReason += String.format("You Saved mismatch! Expected: %.2f, Found: %.2f. ", finalSaved, actualSavedAmount);
	    }

	    if (Math.abs(finalAmount - actualFinalAmount) > 0.01) {
	        pass = false;
	        failReason += String.format("Final Amount mismatch! Expected: %.2f, Found: %.2f. ", finalAmount, actualFinalAmount);
	    }

	    // 🔹 Step 6 — Assertions
	    assertEquals("'You Saved' mismatch!", finalSaved, actualSavedAmount, 0.01);
	    assertEquals("Final Amount mismatch!", finalAmount, actualFinalAmount, 0.01);

	    // 🔹 Step 7 — Excel report (only FINAL stage)
	    if ("FINAL".equalsIgnoreCase(stage)) {
	        String tcId = "TC_UI_Zlaata_COP_25";
	        String tcDescription = tcId + " | Verify That Calculation Functionality is Working | \"TD_UI_Zlaata_COP_25\"";
	        ExcelReportUtil.results.add(new ExcelReportUtil.TestResult(
	                tcId,
	                tcDescription,
	                0,
	                System.getProperty("user.name"),
	                pass ? "Pass" : "Fail",
	                failReason,
	                ExcelReportUtil.captureScreenshot(driver, tcId),
	                "Checkout Page"
	        ));
	    }
	}

	// ✅ Wait until quantity updates
	private void waitForQtyUpdate(WebElement productBlock, int expectedQty) {
	    new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.textToBe(
	        By.xpath(".//div[@class='cp_quantity_wrap']"),
	        String.valueOf(expectedQty)
	    ));
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

