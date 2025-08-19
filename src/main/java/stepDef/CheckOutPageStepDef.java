package stepDef;

import context.TestContext;
import pages.BagPage;
import pages.CheckoutPage;

import io.cucumber.java.en.Given;


public class CheckOutPageStepDef {

	TestContext testContext;
	CheckoutPage cOp;

	public  CheckOutPageStepDef(TestContext context) {
		testContext = context;
		cOp= testContext.getPageObjectManager().getCheckoutPage();

	}

	@Given("User Verifies Checkout page calculation")
	public void user_verifies_checkout_page_calculation() {
		cOp.verifyCheckoutCalculationsWithExcel();
		

	}



}
