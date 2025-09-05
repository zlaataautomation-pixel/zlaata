package stepDef;

import context.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import pages.AdminPanelPage;


public class AdminPanelStepDef {
	
	TestContext testContext;
	AdminPanelPage admin;


	public AdminPanelStepDef(TestContext context) {
		testContext = context;
		admin = testContext.getPageObjectManager().getAdminPanelPage();
		
	}
	

		@Given("I upload an image {string} in admin panel")
		public void i_upload_an_image_in_admin_panel(String imageName ) {
			String imagePath = System.getProperty("user.dir") + "/src/test/resources/images/" + imageName;
			admin.uploadImage(imagePath);
			
			
		}


			





					@When("I verify that the homepage first banner is {string}")
					public void i_verify_that_the_homepage_first_banner_is(String expectedBannerTitle ) {
						admin.verifyBannerOnHomePage("Home Page Automation Banner");
					}




}
