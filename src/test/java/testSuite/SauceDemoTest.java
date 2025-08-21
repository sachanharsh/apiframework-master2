package testSuite;

import java.time.Duration;

import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

class RetryAnalyzer implements IRetryAnalyzer{
	private int retry=0;
	private static final int MAX_RETRIES=3;
	
	@Override
	public boolean retry(ITestResult result)  {
		if(retry<MAX_RETRIES) {
			retry++;
			return true;
		}
		return false;
	}
}

class MyWebDriverManager{
	private static MyWebDriverManager instance;
	private static final ThreadLocal<WebDriver> tlDriver=new ThreadLocal<>();
	
	
	private MyWebDriverManager() {
		//getDriver();
	}
	
	public static synchronized MyWebDriverManager getInstance() {
		if(instance==null)
			instance=new MyWebDriverManager();
		return instance;
	}
	
	public WebDriver getDriver() {
		if (tlDriver.get() == null) {
            //WebDriverManager.chromedriver().setup();
            //tlDriver.set(createDriver());
            tlDriver.set(new ChromeDriver());
            // driver.manage().timeouts().implicitlyWait(...); // if you want
            // driver.manage().window().maximize();
        }
        return tlDriver.get();
	}
	
    public void quitDriver() {
        if (tlDriver.get() != null) {
        	tlDriver.get().quit();
        	tlDriver.remove();
        }
    }
    
    public WebDriver createDriver() {
    	String browser = System.getProperty("browser", "error").toLowerCase();
        switch (browser) {
            case "chrome": {
                ChromeOptions opts = new ChromeOptions();
                //if (Boolean.getBoolean("headless")) opts.addArguments("--headless=new");
                // opts.addArguments("--start-maximized"); // if you want
                // With Selenium 4.11+, no need to call WebDriverManager
                return new ChromeDriver(opts);
            }
            case "firefox": {
                FirefoxOptions opts = new FirefoxOptions();
                //if (Boolean.getBoolean("headless")) opts.addArguments("-headless");
                return new FirefoxDriver(opts);
            }
            // add edge/safari/remote as needed
            default: throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }
}

abstract class BaseTest3{
	protected WebDriver driver;
	
	@BeforeClass
	public void setup() {
		driver=MyWebDriverManager.getInstance().getDriver();
	}
	@AfterClass
	public void tearDown() {
		if(driver!=null)
			MyWebDriverManager.getInstance().quitDriver();
	}
}

abstract class BasePage {
    protected final WebDriver driver;
    private static final Duration TIMEOUT       = Duration.ofSeconds(10);
    private static final Duration POLL_INTERVAL = Duration.ofMillis(500);
    private static final int CLICK_RETRIES      = 3;

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    protected WebElement safeFind(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        wait.pollingEvery(POLL_INTERVAL)
            .ignoring(NoSuchElementException.class);
        return wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(locator)));
    }

    protected void clickWhenReady(By locator) {
        int attempts = 0;
        while (true) {
            try {
                safeFind(locator).click();
                return;
            } catch (ElementClickInterceptedException | TimeoutException e) {
            	if (++attempts >= CLICK_RETRIES) {
                    throw new RuntimeException(
                        "Failed to type into " + locator + " after " + CLICK_RETRIES + " attempts", e);
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    protected void typeWhenReady(By locator, String text) {
        int attempts = 0;
        while (true) {
            try {
                WebElement el = safeFind(locator);
                el.clear();
                el.sendKeys(text);
                return;
            } catch (StaleElementReferenceException e) {
                if (++attempts >= CLICK_RETRIES) {
                    throw new RuntimeException(
                        "Failed to type into " + locator + " after " + CLICK_RETRIES + " attempts", e);
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

//    /** Simple back-off between retries */
//    private void backOff() {
//        try {
//            Thread.sleep(200);
//        } catch (InterruptedException ignored) {
//            Thread.currentThread().interrupt();
//        }
//    }
}

class LoginPage extends BasePage{
	private By userId=By.id("user-name");
	private By pass=By.id("password");
	private By submit=By.id("login-button");
	private By errorBanner = By.cssSelector(".error-message-container.error");
	
	public LoginPage(WebDriver driver) {
		super(driver);
	}
	
	public ProductsPage getLogin() {
		driver.findElement(userId).sendKeys("standard_user");
		driver.findElement(pass).sendKeys("secret_sauce");
		driver.findElement(submit).click();
		return new ProductsPage(driver);
		
	}
	
	public boolean hasError() {
		return !driver.findElements(errorBanner).isEmpty();
	}
	
	public boolean isAt() {
		return !driver.findElements(submit).isEmpty();
	}
}

class ProductsPage extends BasePage{
	private By firstProduct=By.xpath("//div[@class='inventory_list'][1]");
	
	private By firstProductName=By.xpath("//div[@class='inventory_item_description']/div[@class='inventory_item_label']/a");
	private By firstProductPrice=By.xpath("//div[@class='inventory_item_description']/div[@class='pricebar']/div");
	private By firstProductAddToCart=By.xpath("//div[@class='inventory_item_description']/div[@class='pricebar']/button[@id='add-to-cart-sauce-labs-backpack']");
	private By shoppingCart=By.className("shopping_cart_link");
	
	public ProductsPage(WebDriver driver) {
		super(driver);
//		  if (!driver.getTitle().equals("Swag Labs")) {
//		    throw new IllegalStateException("Expected Products page, but was: " + driver.getTitle());
//		  }
	}
	
	public boolean isAt() {
		return driver.getTitle().equals("Swag Labs");
	}
	
	public Pair<String,String> getFirstProductDetails() {
		String name=driver.findElement(firstProduct).findElement(firstProductName).getText();
		String price=driver.findElement(firstProduct).findElement(firstProductPrice).getText();
		driver.findElement(firstProductAddToCart).click();
		driver.findElement(shoppingCart).click();
		return Pair.of(name, price);
	}
}

class CartPage extends BasePage{
	private By firstProductCartName=By.xpath("//div[@class='inventory_item_name']");
	private By firstProductCartPrice=By.xpath("//div[@class='inventory_item_price']");
	private By menu=By.className("bm-burger-button");
	private By logout=By.id("logout_sidebar_link");
	public CartPage(WebDriver driver) {
		super(driver);
	}
	
	public Pair<String,String> getFirstProductDetailsFromCart() {
		String name=driver.findElement(firstProductCartName).getText();
		String price=driver.findElement(firstProductCartPrice).getText();
		return Pair.of(name, price);
		
	}
	
	public void doLogout() {
		driver.findElement(menu).click();
		//driver.findElement(logout).click();
		clickWhenReady(logout);
	}
}
public class SauceDemoTest extends BaseTest3 {
	
	
	@Test
	public void fullFlowTest() {
		
		driver.get("https://www.saucedemo.com");
		LoginPage login=new LoginPage(driver);
		ProductsPage ppage=login.getLogin();
		Assert.assertTrue(ppage.isAt(), "Should land on Products page");
		Pair<String,String> expected=ppage.getFirstProductDetails();
		CartPage cpage=new CartPage(driver);
		Pair<String,String> actual=cpage.getFirstProductDetailsFromCart();
		Assert.assertEquals(expected.getLeft(), actual.getLeft(),"Product name is not same");
		Assert.assertEquals(expected.getRight(), actual.getRight(),"Product price is not same");
		cpage.doLogout();
//		LoginPage loginPageAfterLogout = new LoginPage(driver);
//		Assert.assertTrue(loginPageAfterLogout.isAt(),"Should land on login page");
	}

}
