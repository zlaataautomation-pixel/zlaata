package objectRepo;

import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import basePage.BasePage;

public abstract  class AdminPanelObjRepo extends BasePage {
	
	@FindBy(name = "email")
	protected WebElement adminEmail;
	
	@FindBy(id = "password")
	protected WebElement adminPassword;
	
	@FindBy(xpath = "//button[@type='submit']")
	protected WebElement adminLogin;
	
	@FindBy(id = "menuSearch")
	protected WebElement SearchAdmin;
	
	@FindBy(xpath = "//i[@class='la la-plus']")
	protected WebElement addHomePageBanner;
	
	@FindBy(name = "title")
	protected WebElement bannerTitle;	
	
	
	@FindBy(xpath = "//input[@class='file-limitation']")
	protected WebElement uploadImageInput;
	
	@FindBy(xpath = "//span[@data-value='save_and_back']")
	protected WebElement uploadButton;
	
	@FindBy(xpath = "(//input[@type='number'])[1]")
	protected WebElement sortBy;
	
	@FindBy(xpath = "(//i[@class='las la-check-circle btn-success'])[1]")
	protected WebElement sortBySave;
	
	
	@FindBy(xpath = "//li[@filter-name='banner_type']")
	protected WebElement homePageBannerDropDown;
	
	@FindBy(xpath = "//li[@class='select2-results__option select2-results__option--highlighted']")
	protected List <WebElement> selectHomePageValue;
	
	@FindBy(xpath = "//li[@filter-name='status']")
	protected WebElement status;
	
	@FindBy(xpath = "//li[@class='select2-results__option select2-results__option--highlighted']")
	protected List <WebElement> statusFilterSelect;
	
	
	@FindBy(xpath = "//tr[@class='odd']")
	protected List <WebElement> bannerRows;
	
	@FindBy(xpath = ".//label[@class='custom-control-label']")
	protected  WebElement disableButton;
	
	@FindBy(id = "remove_filters_button")
	protected  WebElement removeFilters;
	
	
	

}



