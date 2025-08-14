package objectRepo;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import basePage.BasePage;

public abstract class BagObjRepo extends BasePage
{
	@FindBy(xpath ="//button[@class='Cls_cart_btn Cls_redirect_restrict']")
	protected WebElement bagIcon;
	
	@FindBy(xpath ="//span[@class='bag_prod_count']")
	protected WebElement bagItemCount;
	
	@FindBy(xpath ="//a[@class='empty_cart_continue_btn btn___2']")
	protected WebElement bagContinueShop;
	
	@FindBy(xpath ="(//header[@class='popup_containers_header']//div[@class='popup_containers_cls_btn'])[2]")
	protected WebElement closeBag;
	
	@FindBy(xpath ="//div[@class='cp_wishlist_btn Cls_move_to_wishlist_btn']")
	protected WebElement moveToWishlistButton;
	
	@FindBy(xpath ="//a[@class='wishlist-icon']")
	protected WebElement wishListIcon;
	
	
	
	@FindBy(xpath ="//div[@title='Delete']")
	protected WebElement moveToDeleteButton;

	@FindBy(xpath ="//div[@class='cp_drop_arrow']")
	protected WebElement clickOnDownArrow;
	
	@FindBy(xpath ="//button[@class='cp_quantity_increase_btn  Cls_cp_quantity_increase_btn ']")
	protected WebElement increaseTheProductQunatity;
	
	@FindBy(xpath ="//button[@class='cp_quantity_decrease_btn Cls_cp_quantity_decrease_btn']")
	protected WebElement decreaseTheProductQunatity;

	@FindBy(xpath ="//button[normalize-space()='Add to cart']")
	protected WebElement addToCartButton;

	protected WebElement bagCount;
	
	@FindBy(xpath ="//div[@class='bag_thread_banner']")
	protected WebElement threadbanner;
	
	@FindBy(xpath = "//div[@class='cp_current_price']")
	protected WebElement currentPrice;
	
	@FindBy(xpath = "//div[@class='cp_actual_price']")
	protected WebElement actualPrice;
	
	@FindBy(xpath = "//span[@class='Cls_cart_thread_est_amount']")
	protected WebElement threadAmount;
	
	@FindBy(xpath ="//div[@class='bfr_pair Cls_cart_saved_amount']")
	protected WebElement youSavedAmount;
	
	@FindBy(xpath = "//div[@class='bfr_pair Cls_cart_total_amount']")
	protected WebElement totalAmount;
	
	@FindBy(xpath ="//button[.='Buy Now']")
	protected WebElement buyNowButton;
	
	@FindBy(xpath = "//li[@class='navigation_menu_list nav_menu_dropdown shop']")
	protected WebElement shopMenu;
	
	@FindBy(xpath = "//div[@class='nav_drop_down_box_category active']//a[contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'ALL')]")
	protected WebElement category;
	
	@FindBy(xpath = "//button[@class='add_bag_prod_buy_now_btn btn___2  Cls_CartList ClsProductListSizes']")
	protected WebElement addToCart;
	
	@FindBy(xpath = "//div[@class='cart_prod_card ']")
	protected WebElement products;
	

}
