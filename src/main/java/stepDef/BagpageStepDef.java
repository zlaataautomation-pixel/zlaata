package stepDef;



import java.util.concurrent.TimeoutException;

import context.TestContext;
import pages.BagPage;
import io.cucumber.java.en.Given;


public class BagpageStepDef {	

	TestContext testContext;
	BagPage bag;
	
	public BagpageStepDef(TestContext context) {
		testContext = context;
		bag = testContext.getPageObjectManager().getBagPage();
	}


	@Given("User Verifies Bag Item Count")
	public void user_verifies_bag_item_count(){
		bag.itemCount();
	}

	@Given("User Verifies Display of Wishlist Button")
	public void user_verifies_display_of_wishlist_button() throws TimeoutException {
		bag.wishListInbag();
	}

	@Given("User Verifies Display of Delete Button")
	public void user_verifies_display_of_delete_button() {
		try {
	        bag.bagDelete();
	    } catch (Exception e) {
	        System.out.println("💥 Step failed: " + e.getMessage());
	        e.printStackTrace();
	    }
		
			
	}


	@Given("User Verifies That User Can Change Product Size")
	public void user_verifies_that_user_can_change_product_size() throws InterruptedException {
		bag.changeTheProductSize();
	}


	@Given("User Verifies User Can Increase or Decrease Product Quantity")
	public void user_verifies_user_can_increase_or_decrease_product_quantity() throws InterruptedException {
		bag.increaseAndDecreaseQTY();
	}


	@Given("User Verifies User Can Add New Product")
	public void user_verifies_user_can_add_new_product() {
		bag.newProductToBag();
	}

	@Given("User Verifies That Adding Product to Bag Count is Displaying or Not Above Bag Icon")
	public void user_verifies_that_adding_product_to_bag_count_is_displaying_or_not_above_bag_icon() {
		bag.verifyBagCount();
	}

	@Given("User Verifies That Adding New Product or Deleting Product Count Increases or Decreases")
	public void user_verifies_that_adding_new_product_or_deleting_product_count_increases_or_decreases() throws InterruptedException {
		
		try {
			bag.verifyItemCountChanges();
	    } catch (Exception e) {
	        System.out.println("💥 Step failed: " + e.getMessage());
	        e.printStackTrace();
	    }
	}


	@Given("User Verifies {string} Line Display")
	public void user_verifies_line_display(String string) {
		
		try {
			bag.threadLine();
	    } catch (Exception e) {
	        System.out.println("💥 Step failed: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	


	@Given("User Verifies {string} Thread Amount Calculation Based on Discount Amount")
	public void user_verifies_thread_amount_calculation_based_on_discount_amount(String string) {
		try {
			bag.youWillEarn();
		} catch (TimeoutException e) {
			System.out.println("💥 Step failed: " + e.getMessage());
	        e.printStackTrace();
		}
	}

	@Given("User Verifies Increasing\\/Decreasing\\/Adding Product's {string} Thread Amount Based on Change")
	public void user_verifies_increasing_decreasing_adding_product_s_thread_amount_based_on_change(String string) throws InterruptedException {
		try {
			bag.threadCalculation();
		} catch (TimeoutException e) {
			System.out.println("💥 Step failed: " + e.getMessage());
	        e.printStackTrace();
		}
	}

	@Given("User Verifies {string} and Total Amount Line Display")
	public void user_verifies_and_total_amount_line_display(String string) {
		bag.youSaved();
	}


	@Given("User Verifies {string} Amount Calculation Based on Original Price Minus Discount Amount")
	public void user_verifies_amount_calculation_based_on_original_price_minus_discount_amount(String string) {
		bag.youSavedCalculation();
	}

	@Given("User Verifies {string} Calculating Based on Discounted Amount")
	public void user_verifies_calculating_based_on_discounted_amount(String string) {
		bag.totalAmount();
	}


	@Given("User Verifies Increasing\\/Decreasing\\/Adding Product's {string} & Total Amount Based on Change")
	public void user_verifies_increasing_decreasing_adding_product_s_total_amount_based_on_change(String string) throws InterruptedException {
		bag.youSavedandTotalAmountCalculationBasedOnQTY();
	}

	@Given("User Verifies {string} Button Functionality")
	public void user_verifies_button_functionality(String string) {
		bag.buyNowBagPage();
	}


















}
