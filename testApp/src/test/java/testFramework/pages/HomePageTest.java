package testFramework.pages;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class HomePageTest extends TestBase {
	public static final Logger LOGGER = Logger.getLogger(String.valueOf(HomePageTest.class));

	HomePage homepage;

	@Parameters({ "path", "url" })
	@BeforeClass
	public void testInit(String path, String url) {

		LOGGER.info("initializing Class with url: " + url);
		// Load the page in the browser
		webDriver.get(url + path);
		homepage = PageFactory.initElements(webDriver, HomePage.class);
	}

	@Test
	public void testH1Existing() throws InterruptedException {
		LOGGER.info("Assert H1 exists");
		Assert.assertTrue(homepage.getH1() != null);
	}

	@Test
	public void test2() throws InterruptedException {
		LOGGER.info("Assert nothings");
		Assert.assertTrue(true);
	}
}
