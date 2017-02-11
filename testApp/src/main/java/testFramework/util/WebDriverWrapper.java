package testFramework.util;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;

/**
 * Designed to hold and control the instance of the WebDriver
 */

public enum WebDriverWrapper {

    INSTANCE;

    private WebDriver webDriver;
    private WebDriverWait webDriverWaitManual;
    private WebDriverWait webDriverWaitShort;
    private WebDriverWait webDriverWaitMedium;
    private WebDriverWait webDriverWaitLong;

    public void clearWebDriver() {
        webDriver = null;
        webDriverWaitMedium = null;
        webDriverWaitShort = null;
    }

    public WebDriver getWebDriver() {
        if (webDriver == null) {
            webDriver = newDriverChrome();
            webDriver.manage().window().maximize();
        }
        return webDriver;
    }

    public WebDriverWait getWebDriverWait(int wait) {
        this.webDriverWaitManual = new WebDriverWait(getWebDriver(), wait);

        return webDriverWaitManual;
    }

    public WebDriverWait getWebDriverWaitlong() {
        if (webDriverWaitLong == null) {
            webDriverWaitLong = new WebDriverWait(getWebDriver(), 120);
        }
        return webDriverWaitLong;
    }

    public WebDriverWait getWebDriverWaitMedium() {
        if (webDriverWaitMedium == null) {
            webDriverWaitMedium = new WebDriverWait(getWebDriver(), 30);
        }
        return webDriverWaitMedium;
    }

    public WebDriverWait getWebDriverWaitShort() {
        if (webDriverWaitShort == null) {
            webDriverWaitShort = new WebDriverWait(getWebDriver(), 20);
        }
        return webDriverWaitShort;
    }

    public WebDriverWait getWebDriverWaitVeryShort() {
        if (webDriverWaitShort == null) {
            webDriverWaitShort = new WebDriverWait(getWebDriver(), 10);
        }
        return webDriverWaitShort;
    }

    private ChromeDriver newDriverChrome() {
        String driverFile = "chromedriver_linux-2.27";
        if(SystemUtils.IS_OS_WINDOWS){
            driverFile = "chromedriver_win-2.27.exe";
        }else if(SystemUtils.IS_OS_MAC){
            driverFile = "chromedriver_mac-2.27";
        }

        String driverPath = getClass().getResource("/web-drivers/chrome/" + driverFile).getPath();
        System.out.println("************ DRIVERPATH: " + driverPath);
        File driverExecutable = new File(driverPath);
        if (!driverExecutable.canExecute())
            driverExecutable.setExecutable(true);
        ChromeDriverService.Builder serviceBuilder = new ChromeDriverService.Builder();
        serviceBuilder.usingDriverExecutable(new File(driverPath));

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-web-security"); // Allows for running in localhost
        options.addArguments("disable-popup-blocking");
        if(SystemUtils.IS_OS_LINUX) {
            ClassLoader classLoader = getClass().getClassLoader();
            File flashPath = new File(classLoader.getResource("linux-flash/libpepflashplayer.so").getFile());
            options.addArguments("--ppapi-flash-path=" + flashPath);
        }

        return new ChromeDriver(serviceBuilder.build(), options);
    }
}
