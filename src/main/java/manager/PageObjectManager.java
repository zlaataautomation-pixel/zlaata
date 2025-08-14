package manager;


import context.TestContext;
import pages.*;
import stepDef.Hooks;


import org.openqa.selenium.WebDriver;

public class PageObjectManager {

    private WebDriver driver;
    private Hooks hooks;
    private NegativeSignupPages negsignups;
    private MyOrdersPage myorders;
    private LoginPage login;
    private HomePage home;
    private Menus menu;
    private ProductListingPage pLp;
    private BagPage bag;
    private CheckoutPage cOp;
    private SaleOffer50Percentage sale;
    private SaleBuy1Get1 saleB1G1;
    private OrdersPage order;
    private AddressPage address;
    private FooterPage footer;
    private ProductDetailsPage pDP;
    private SearchSectionPage search;
   
   

    public PageObjectManager(WebDriver driver) {
        this.driver = driver;
    }
    public AddressPage getAddressPage() {
        return (address == null) ? address = new AddressPage(driver) : address;
    }
    
    public FooterPage getFooterPage() {
        return (footer == null) ? footer = new FooterPage(driver) : footer;
    }
    
    public ProductDetailsPage getProductDetailsPage() {
        return (pDP == null) ? pDP = new ProductDetailsPage(driver) : pDP;
    }
    public SearchSectionPage getSearchSectionPage() {
        return (search == null) ? search = new SearchSectionPage(driver) : search;
    }
    
    public NegativeSignupPages getNegativeSignupPages() {
        return (negsignups == null) ? negsignups = new NegativeSignupPages(driver) : negsignups;
    }
	public Hooks getHooks() {
        TestContext testContext = null;
		return (hooks == null) ? hooks = new Hooks(testContext) : hooks;
    }
    
	public MyOrdersPage getMyOrdersPage() {
        return (myorders == null) ? myorders = new MyOrdersPage(driver) : myorders;
    
	}
    
	public LoginPage getLoginPage() {
        return (login == null) ? login = new LoginPage(driver) : login;
    
	}
	
	public HomePage getHomePage() {
        return (home == null) ? home = new HomePage(driver) : home;
    
	}
    
	public Menus getMenus() {
        return (menu == null) ? menu = new Menus(driver) : menu;
    
	}
    
	public ProductListingPage getProductListingPage() {
        return (pLp == null) ? pLp = new ProductListingPage(driver) : pLp;
    
	}
	public BagPage getBagPage() {
        return (bag == null) ? bag = new BagPage(driver) : bag;
    
	}
	public CheckoutPage getCheckoutPage() {
        return (cOp == null) ? cOp = new CheckoutPage(driver) : cOp;
    
	}
    
	public SaleOffer50Percentage getSaleOffer50Percentage() {
        return (sale == null) ? sale = new SaleOffer50Percentage(driver) : sale;
    
	}
	public SaleBuy1Get1 getSaleBuy1Get1() {
        return (saleB1G1 == null) ? saleB1G1 = new SaleBuy1Get1(driver) : saleB1G1;
    
	}
	public OrdersPage getOrdersPage() {
        return (order == null) ? order = new OrdersPage(driver) : order;
    
	}
	
	
}