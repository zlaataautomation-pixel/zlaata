package objectRepo;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import basePage.BasePage;

public abstract class CheckOutPageObjRepo extends BasePage {

	@FindBy(xpath ="//button[@class='Cls_cart_btn Cls_redirect_restrict']")
	protected WebElement bagIcon;

	@FindBy(xpath ="//div[@class='bag_inner_wrap Cls_bag_items_unavailable ']//div[@class='bag_closeup_btn']")
	protected WebElement closeBag;

	@FindBy(xpath ="//button[.='Buy Now']")
	protected WebElement buyNowButton;

	@FindBy(xpath = "//li[@class='navigation_menu_list nav_menu_dropdown shop']")
	protected WebElement shopMenu;

	@FindBy(xpath = "//div[@class='nav_drop_down_box_category active']//a[contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'ALL')]")
	protected WebElement category;

	@FindBy(xpath = "//button[@class='add_bag_prod_buy_now_btn btn___2  Cls_CartList ClsProductListSizes']")
	protected WebElement addToCart;

	@FindBy(xpath = "//div[@class='cart_prod_card ']")
	protected List <WebElement> products;


	@FindBy(xpath ="//div[@class='checkout_progress_bar']")
	protected WebElement bagAddresspaymentsIcon;


	@FindBy(xpath ="//span[@class='checkout_prod_count Cls_checkout_prod_count']")
	protected WebElement bagItemCount;


	@FindBy(xpath ="cp_wishlist_btn Cls_move_to_wishlist_btn")
	protected WebElement wishlistPage;


	@FindBy(xpath ="//div[@title='Delete']")
	protected WebElement deleteButton;

	@FindBy(xpath ="//div[@class='cp_drop_arrow']")
	protected WebElement changeTheProductSize;

	@FindBy(xpath ="//button[@class='cp_quantity_increase_btn  Cls_cp_quantity_increase_btn ']")
	protected WebElement increaseTheProductQunatity;

	@FindBy(xpath ="//button[@class='cp_quantity_decrease_btn Cls_cp_quantity_decrease_btn']")
	protected WebElement decreaseTheProductQunatity;

	@FindBy(xpath ="//button[normalize-space()='Add to cart']")
	protected WebElement addToCartButton;

	@FindBy(xpath = "//div[@class='coupon_input_card']")
	protected WebElement couponTextBox;

	@FindBy(xpath = "//button[@class='coupon_apply_btn Cls_coupon_apply_rmv_btn']")
	protected WebElement applyButton;

	@FindBy(xpath = "//div[@class='available_offer_card']")
	protected WebElement avilableOfferSection;

	@FindBy(xpath = "//button[@class='view_more_coupon_btn Cls_viewmore']")
	protected WebElement viewMoreButton;

	@FindBy(xpath = "//div[@class='offer_list_item_details_wrap']")//List WebElement
	protected WebElement listOfAvilableCoupon;

	@FindBy(xpath = "//button[@class='offer_list_item_apply_btn Cls_apply_coupon']")
	protected WebElement clickApplyButtonOnCouponTextBox;

	@FindBy(xpath = "//button[@class='coupon_apply_btn Cls_coupon_apply_rmv_btn']")
	protected WebElement clickRemoveButtonOnCouponTextBox;

	@FindBy(xpath = "//button[@class='offer_list_item_apply_btn Cls_apply_coupon']")
	protected WebElement clikApplyuButtonOnListOfCoupon;	

	@FindBy(xpath = "//p[@class='coupon_apply_msg active']")
	protected  WebElement appliedCouponAmount;

	@FindBy(xpath = "//div[@class='offer_list_item_heading']")
	protected WebElement copyTheCouponCode;

	@FindBy(xpath = "//input[@class='coupon_input Cls_coupon_input']")
	protected WebElement tryToEnterTheCouponCode;

	@FindBy(xpath = "//h5[@class='delivery_type_heading']")
	protected WebElement deliveryTypeHeading;

	@FindBy(xpath = "//div[@class='extra_coupon_details']")
	protected WebElement getFaltRs50Coupon;

	@FindBy(xpath = "//div[@class='checkout_git_list_item']")
	protected WebElement giftWrapSection;

	@FindBy(xpath = "//div[@class='gift_card_box_heading']")
	protected WebElement giftCardSection;

	@FindBy(xpath = "//div[@class='eligible_for_returns']")
	protected WebElement  eligibleForReturns;

	@FindBy(xpath = "//h5[.='Price Details  ']")
	protected WebElement priceDetailsSection;


	@FindBy(xpath = "//div[@class='price_details_card']")
	protected WebElement calculation;


	@FindBy(xpath = "//div[@class='price_details_row actual_mrp Cls_cart_total_mrp_row']")
	protected WebElement totalMRP;

	@FindBy(xpath = "//div[@class='price_details_row discount_mrp Cls_cart_discounted_mrp_row']")
	protected WebElement discountAmount;

	@FindBy(xpath = "//div[@class='price_details_row extra_prepaid_discount Cls_cart_extra_prepaid_discount_row']")
	protected WebElement prepaidcouponAmount;

	@FindBy(xpath = "//div[@class='price_details_row coupon_discount Cls_cart_coupon_discount_row']")
	protected WebElement coupondiscountLine;

	@FindBy(xpath = "//div[@class='price_details_row total__earn__wrpr']")
	protected WebElement youEarnThreadAmount;


	@FindBy(xpath = "//div[@class='price_details_row saved_mrp Cls_cart_saved_amount_row']")
	protected WebElement youSavedAmount;

	@FindBy(xpath = "//div[@class='price_details_row total_mrp Cls_cart_total_amount_row']")
	protected WebElement totalAmount;

	@FindBy(xpath = "//div[@class='price_details_key row_price_details_inclusive']")
	protected WebElement   inclusiveOfAllTaxesLine;

	@FindBy(xpath = "//button[@class='place_order_btn Cls_place_order btn___2 ']")
	protected WebElement placeOrderButton;
}
