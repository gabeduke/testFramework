package testFramework.util;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import testFramework.paths.SeleniumPath;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


/**
 * Wraps and controls all access to the WebDriver. Designed to be a static
 * import and used by PageObjects
 */
public class WebDriverUtil {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_DDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_MMDDYYYY = new SimpleDateFormat("MM/dd/yyyy");
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_DDMMYYYY_HMA = new SimpleDateFormat("dd/MM/yyyy H:m a");
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_MMDDYYYY_HMA = new SimpleDateFormat("MM/dd/yyyy H:m a");
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_MMDDYYYY_HMSA = new SimpleDateFormat("MM/dd/yyyy H:m:s a");
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_DDMMYYYY_HMSA = new SimpleDateFormat("dd/MM/yyyy H:m:s a");
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_EEEE_MMMDYYYY_HMA = new SimpleDateFormat("EEEE, MMM d, yyyy h:m a");
    public static final Logger LOGGER = Logger.getLogger(String.valueOf(WebDriverUtil.class));
    static Actions actions = null;

    public static Actions getActions() {
        if(actions == null) {
            actions = new Actions(getWebDriver());
        }
        return actions;
    }

    public static void clearActions() {
        actions = null;
    }

    public static Boolean isAlertPresent() {   	
    	WebDriverWait wait = new WebDriverWait(getWebDriver(), 10);
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (TimeoutException eTO) {
            return false;
        }
    }

    public static void acceptAlert() {
        WebDriverWait confirmPopup = new WebDriverWait(getWebDriver(), 10);
        Alert alert = confirmPopup.until(ExpectedConditions.alertIsPresent());
        if(alert != null) {
        	alert = getWebDriver().switchTo().alert();
        	alert.accept();
            switchToDefaultContent();
        }
    }

    public static String getActiveElementId() {
        WebElement element = getWebDriver().switchTo().activeElement();
        String elementId = element.getAttribute("id");
        return elementId;
    }
    
    public static String getAlertText() {
        WebDriverWait confirmPopup = new WebDriverWait(getWebDriver(), 10);
        Alert alert = confirmPopup.until(ExpectedConditions.alertIsPresent());
        if(alert != null) {
            alert = getWebDriver().switchTo().alert();
            return alert.getText();
        } else {
            return "";
        }
    }

    private static void clearAll(By by) {
        waitForPresenceOfElement(by);
        clearAll(findElement(by));
    }

    public static void clearAll(SeleniumPath seleniumPath) {
        clearAll(seleniumPath.getBy());
    }

    private static void clearAll(WebElement webElement) {
        webElement.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        webElement.sendKeys(Keys.BACK_SPACE);
    }

    public static void selectAll() {
        actions.sendKeys(Keys.chord(Keys.CONTROL, "a")).perform();
    }

    public static void cut(){
        actions.sendKeys(Keys.chord(Keys.CONTROL, "x")).perform();
    }

    public static void paste(){
        actions.sendKeys(Keys.chord(Keys.CONTROL, "v")).perform();
    }

    public static Object getContentsOfTheClipboard(){
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        try {
            return clipboard.getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void clickAll(SeleniumPath seleniumPath) {
        clickAll(seleniumPath.getBy());
    }

    private static void clickAll(By by) {
        clickAll(findElements(by));
    }

    private static void clickAll(List<WebElement> webElements) {
        for(WebElement e : webElements) {
            click(e);
        }
    }

    public static void clearText(SeleniumPath seleniumPath) {
        clearText(seleniumPath.getBy());
    }

    private static void clearText(By by) {
        clearText(findElement(by));
    }

    public static void clearText(WebElement webElement) {
        webElement.clear();
    }

    public static void clearTextByBackspace(SeleniumPath seleniumPath, int numberOfBackspaces) {
        clearTextByBackspace(seleniumPath.getBy(), numberOfBackspaces);
    }

    private static void clearTextByBackspace(By by, int numberOfBackspaces) {
        clearTextByBackspace(findElement(by), numberOfBackspaces);
    }

    public static void clearTextByBackspace(WebElement webElement, int numberOfBackspaces) {
        for (int i = 0; i < numberOfBackspaces; i++) {
            webElement.sendKeys(Keys.BACK_SPACE);
        }
    }

    public static void clearTextAndSendKeys(SeleniumPath seleniumPath, CharSequence keys) {
        By by = seleniumPath.getBy();
        clearText(by);
        sendKeys(by, keys);
    }

    private static void click(By by) {
        WebElement element = getWebElement(by);
        click(element);
    }

    public static void click(SeleniumPath seleniumPath) {
        waitForElementToBeClickable(seleniumPath);
        click(seleniumPath.getBy());
    }

    public static void click(WebElement webElement) {
        waitForElementToBeClickable(webElement);
        webElement.click();
    }

    public static void click(List<WebElement> webElements) {
        webElements.forEach(org.openqa.selenium.WebElement::click);
    }
    
    public static void click(SeleniumPath seleniumPath, int xOffset, int yOffset) {
        click(seleniumPath.getBy(), xOffset, yOffset);
    }
    
    private static void click(By by, int xOffset, int yOffset) {
        click(findElement(by), xOffset, yOffset);
    }
    
    private static void click(WebElement webElement, int xOffset, int yOffset){
        getActions().moveToElement(webElement, xOffset, yOffset).click().build().perform();
    }

    public static void hitEnter(){
        actions.sendKeys(Keys.ENTER).perform();
    }

    public static void tab(){
        actions.sendKeys(Keys.TAB).perform();
    }

    public static void shiftTab(){
        actions.sendKeys(Keys.chord(Keys.SHIFT, Keys.TAB)).perform();
    }

    public static void openTab() {
        findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"t");
        switchToTab(1);
    }

    public static void clickLink(WebElement webElement) {
        waitForElementToBeClickable(webElement);
        webElement.click();
    }

    @Deprecated
    public static void clickByLinkText(String text) {
        click(By.linkText(text));
    }

    public static void clickChildByLinkText(SeleniumPath seleniumPath, String text) {
        click(findElement(seleniumPath.getBy(), By.partialLinkText(text)));
    }

    public static void clickByText(SeleniumPath seleniumPath, String field) {
        clickOnText(seleniumPath.getBy(), field);
    }

    public static void submit(SeleniumPath seleniumPath) {
        submit(seleniumPath.getBy());
    }

    private static void submit(By by) {
        WebElement element = getWebElement(by);
        submit(element);
    }

    private static WebElement getWebElement(By by) {
        waitForPresenceOfElement(by);
        WebElement element = findElement(by);
        getActions().moveToElement(element).build().perform();
        waitForVisibilityOfElement(by);
        waitForElementToBeClickable(by);
        return element;
    }

    public static void submit(WebElement webElement) {
        waitForElementToBeClickable(webElement);
        webElement.submit();
    }

    public static void clickOnText(SeleniumPath seleniumPath, String text) {
        clickOnText(seleniumPath.getBy(), text);
    }
    
    private static void clickOnText(By by, String text) {
        click(findElement(by, text));
    }

    public static void clickReverseOffset(SeleniumPath seleniumPath, int xReverseOffset, int yReverseOffset) {
        clickReverseOffset(seleniumPath.getBy(), xReverseOffset, yReverseOffset);
    }
    
    private static void clickReverseOffset(By by, int xReverseOffset, int yReverseOffset) {
        int xOffset = getAttributeWidth(findElement(by)) - xReverseOffset;
        int yOffset = getAttributeHeight(findElement(by)) - yReverseOffset;
        click(findElement(by), xOffset, yOffset);
    }
    
	public static void closeBrowser() {
        clearActions();
        getWebDriver().quit();
    }

    public static void declineAlert() {
        WebDriverWait confirmPopup = new WebDriverWait(getWebDriver(), 10);
        confirmPopup.until(ExpectedConditions.alertIsPresent());
        Alert alert = getWebDriver().switchTo().alert();
        alert.dismiss();
    }

    public static void dragAndDrop(SeleniumPath itemToDrag, SeleniumPath target) {
        JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
        String itemToDragCSS = itemToDrag.getBy().toString().replace("By.selector: ", "");
        String targetCSS = target.getBy().toString().replace("By.selector: ", "");
        js.executeScript("var summary = $('" + itemToDragCSS + "').detach(); $('" + targetCSS + "').before(summary);");
    }

    public static String getCSSValue(SeleniumPath seleniumPath, String cssProp) {
        return getCSSValue(seleniumPath.getBy(), cssProp);
    }
    
    private static String getCSSValue(By by, String cssProp) {
        return getCSSValue(findElement(by), cssProp);
    }
    
    private static String getCSSValue(WebElement webElement, String cssProp) {
        return webElement.getCssValue(cssProp);
    }

    public static List<String> getCSSValueOfAllElements(SeleniumPath seleniumPath, String cssProp) {
        return getCSSValueOfAllElements(seleniumPath.getBy(), cssProp);
    }
    
    private static List<String> getCSSValueOfAllElements(By by, String cssProp) {
        List<String> cssValues = new ArrayList<String>();
        for(WebElement webElement : findElements(by)) {
            cssValues.add(getCSSValue(webElement, cssProp));
        }
        return cssValues;
    }
    
    @Deprecated
	public static String getCSSValueOfMonthForCalendarWidget(String month){
   	int calendarWidgetMonth = Integer.parseInt(month) - 1;
   	return Integer.toString(calendarWidgetMonth);
   }

    private static WebElement findElement(SeleniumPath seleniumPath) {
        return findElement(seleniumPath.getBy());
    }

    public static WebElement findElement(By by) {
        return getWebDriver().findElement(by);
    }

    public static boolean findElementEnabled(SeleniumPath path) {
        return findElement(path).isEnabled();
    }

    private static WebElement findElement(By by, String text) {
        List<WebElement> webElements = findElements(by);
        for(WebElement current : webElements) {
            if(getText(current).equals(text)) {
                return current;
            }
        }
        throw new NoSuchElementException("No Element Found with Text [" + text + "].");
        
    }

    private static WebElement findElement(By parent, By child) {
        return findElement(parent).findElement(child);
    }

    public static List<WebElement> findElements(By by) {
        return getWebDriver().findElements(by);
    }

    public static List<WebElement> findElements(SeleniumPath seleniumPath) {
        return findElements(seleniumPath.getBy());
    }

    public static int getAmountOfElements(SeleniumPath seleniumPath) {
        return getAmountOfElements(seleniumPath.getBy());
    }
    
    private static int getAmountOfElements(By by) {
        return findElements(by).size();
    }

    public static String getAttributeTitle(SeleniumPath seleniumPath) {
        return getAttribute(seleniumPath.getBy(), "title");
    }

    public static String getAttributeBlackListData(SeleniumPath seleniumPath) {
        return getAttribute(seleniumPath.getBy(), "data-autoaddblacklistdata");
    }

    public static String getAttributeWhiteListData(SeleniumPath seleniumPath) {
        return getAttribute(seleniumPath.getBy(), "data-autoaddwhitelistdata");
    }

    public static String getAttributeIds(SeleniumPath seleniumPath) {
        return getAttribute(seleniumPath.getBy(), "Id");
    }
    
    
    public static List<String> getAttributesId(SeleniumPath seleniumPath) {
        return getAttributeId(seleniumPath.getBy(), "Id");
    }
    
    private static List<String> getAttributeId(By by, String string) {
        return getAttributeId(findElements(by));
    }
    private static List<String> getAttributeId(By by) {
        return getAttributesValues(findElements(by));
    }

    public static List<String> getAttributeId(SeleniumPath seleniumPath) {
        return getAttributeId(seleniumPath.getBy());
    }

    private static List<String> getAttributeId(List<WebElement> webElements) {
        List<String> values = new ArrayList<String>();
        for(WebElement e : webElements) {
            values.add(getAttributeId(e));
        }
        return values;
    }
    
    private static String getAttributeId(WebElement webElement) {
        return webElement.getAttribute("Id");
    }
    
    
    
    
    
    
    public static String getAttributeTooltip(SeleniumPath seleniumPath) {
        return getAttribute(seleniumPath.getBy(), "tooltip");
    }

    public static String getAttributeName(SeleniumPath seleniumPath) {
        return getAttribute(seleniumPath.getBy(), "name");
    }
    public static String getAttributeFormat(SeleniumPath seleniumPath) {
        return getAttribute(seleniumPath.getBy(), "format");
    }
    
    public static String getAttributeClass(SeleniumPath seleniumPath) {
        return getAttribute(seleniumPath.getBy(), "class");
    }

    public static String getAttributeOnClick(SeleniumPath seleniumPath) {
        return getAttribute(seleniumPath.getBy(), "onclick");
    }

    public static String getAttributeHref(SeleniumPath seleniumPath) {
        return getAttribute(seleniumPath.getBy(), "href");
    }

    public static String getAttributeSrc(SeleniumPath seleniumPath) {
        return getAttribute(seleniumPath.getBy(), "src");
    }

    private static String getAttribute(By by, String attribute) {
        return getAttribute(findElement(by), attribute);
    }

    private static String getAttribute(WebElement webElement, String attribute) {
        return webElement.getAttribute(attribute);
    }

    private static String getAttributeValue(By by) {
        return getAttributeValue(findElement(by));
    }

    public static String getAttributeValue(SeleniumPath seleniumPath) {
        return getAttributeValue(seleniumPath.getBy());
    }

    public static String getAttribute(String id, String attribute) {
        WebElement element = getWebElement(By.id(id));
        return getAttribute(element, attribute);
    }

    private static String getAttributeValue(WebElement webElement) {
        return webElement.getAttribute("value");
    }

    public static String getAttributeClass(WebElement webElement) {
        return webElement.getAttribute("class");
    }

    private static List<String> getAttributesValues(By by) {
        return getAttributesValues(findElements(by));
    }

    public static List<String> getAttributesValues(SeleniumPath seleniumPath) {
        return getAttributesValues(seleniumPath.getBy());
    }

    private static List<String> getAttributesValues(List<WebElement> webElements) {
        List<String> values = new ArrayList<String>();
        for(WebElement e : webElements) {
            values.add(getAttributeValue(e));
        }
        return values;
    }
    
    private static List<String> getAttributesClasses(By by) {
        return getAttributesClasses(findElements(by));
    }

    public static List<String> getAttributesClasses(SeleniumPath seleniumPath) {
        return getAttributesClasses(seleniumPath.getBy());
    }
    
    private static List<String> getAttributesClasses(List<WebElement> webElements) {
    	List<String> values = new ArrayList<String>();
        for(WebElement e : webElements) {
            values.add(getAttributeClass(e));
        }
        return values;
    }
    
    public static int getAttributeWidth(SeleniumPath seleniumPath) {
        return getAttributeWidth(seleniumPath.getBy());
    }
    
    private static int getAttributeWidth(By by) {
        return getAttributeWidth(findElement(by));
    }
    
    private static int getAttributeWidth(WebElement webElement) {
        return webElement.getSize().width;
    }
    
    public static int getAttributeHeight(SeleniumPath seleniumPath) {
        return getAttributeHeight(seleniumPath.getBy());
    }
    
    private static int getAttributeHeight(By by) {
        return getAttributeHeight(findElement(by));
    }
    
    private static int getAttributeHeight(WebElement webElement) {
        return webElement.getSize().height;
    }

    public static String getCurrentUrl() {
        return getWebDriver().getCurrentUrl();
    }

    private static String removeSeconds(String date) {
        if (date.matches("\\d+\\/\\d+\\/\\d{2} \\d+:\\d+:\\d+ \\w{2}")) {
            StringBuilder d = new StringBuilder(date);
            d.delete(date.length() - 6, date.length() - 3);
            return d.toString();
        } else {
            return date;
        }
    }

    public static List<Double> getDoubleElementsFromString(SeleniumPath seleniumPath) {
        return getDoubleElementsFromString(seleniumPath.getBy());
    }
    
    private static List<Double> getDoubleElementsFromString(By by) {
        return getDoubleElementsFromString(findElements(by));
    }
    
    private static List<Double> getDoubleElementsFromString(List<WebElement> webElements){
        List<Double> doubleElements = new ArrayList<Double>();
        for(WebElement current : webElements) {
            String temp = current.getText().trim();
            double test = Double.parseDouble(temp.replaceAll("[^.0-9]", ""));
            doubleElements.add(test);
        }
        return doubleElements;
    }

    public static Integer getInteger(SeleniumPath seleniumPath) {
        return Integer.parseInt(getText(seleniumPath));
    }
    
    public static List<Integer> getIntegerElements(SeleniumPath seleniumPath) {
        List<Integer> integerElements = new ArrayList<Integer>();
        for(String current : getTextElements(seleniumPath.getBy())) {
            integerElements.add(Integer.parseInt(current));
        }
        return integerElements;
    }

    public static String getDropdownSelectedValue(String id) {
        Select selectList = new Select(getWebDriver().findElement(By.id(id)));
        String selectedValue = selectList.getFirstSelectedOption().getText();
        return selectedValue;
    }

    /*public static String getDropdownSelectedValue(String id) {
        Select selectList = new Select(getWebDriver().findElement(By.id(id)));
        String selectedValue = selectList.getFirstSelectedOption().getText();
        return selectedValue;
    }*/

    private static String getText(By by) {
        return getText(findElement(by));
    }

    public static String getText(SeleniumPath seleniumPath) {
        return getText(seleniumPath.getBy());
    }
    
    private static String getText(WebElement webElement) {
        String text = webElement.getText().trim();
        if ( text == null) {
            text = "";
        }
        return text;
    }

    public static List<String> getTextDelimitedByComma(SeleniumPath seleniumPath) {
        String text = getText(seleniumPath.getBy());
        return new ArrayList<String>(Arrays.asList(text.split(",")));
    }

    public static List<String> getTextDelimitedByNewLine(SeleniumPath seleniumPath) {
        String text = getText(seleniumPath.getBy());
        return new ArrayList<String>(Arrays.asList(text.split("\n")));
    }

    public static List<String> getTextDelimitedByDash(SeleniumPath seleniumPath) {
        String text = getText(seleniumPath.getBy()).trim();
        List<String> list = new ArrayList<String>(Arrays.asList(text.split("\\s*-\\s*")));
        list.remove(0);
        return list;
    }

    private static List<String> getTextElements(By by) {
        return getTextElements(findElements(by));
    }

    public static List<String> getTextElements(List<WebElement> webElements) {
        List<String> textElements = new ArrayList<>();
        for(WebElement e : webElements) {
            textElements.add(e.getText().trim());
        }
        return textElements;
    }

    public static List<String> getTextElements(SeleniumPath seleniumPath) {
        return getTextElements(seleniumPath.getBy());
    }

    public static String getTextInsideOfParenthesis(SeleniumPath seleniumPath) {
        return getText(seleniumPath.getBy()).replace("(", "").replace(")", "");
    }

    public static WebDriver getWebDriver() {
        return WebDriverWrapper.INSTANCE.getWebDriver();
    }

    private static WebDriverWait getWebDriverWaitManual(int wait) {
        return WebDriverWrapper.INSTANCE.getWebDriverWait(wait);
    }

    private static WebDriverWait getWebDriverWaitLong() {
        return WebDriverWrapper.INSTANCE.getWebDriverWaitlong();
    }

    public static WebDriverWait getWebDriverWaitMedium() {
        return WebDriverWrapper.INSTANCE.getWebDriverWaitMedium();
    }

    private static WebDriverWait getWebDriverWaitVeryShort() {
        return WebDriverWrapper.INSTANCE.getWebDriverWaitVeryShort();
    }
    
    private static WebDriverWait getWebDriverWaitShort() {
        return WebDriverWrapper.INSTANCE.getWebDriverWaitShort();
    }

    private static void hoverOverElement(By by) {
        hoverOverElement(findElement(by));
    }

    public static void hoverOverElement(SeleniumPath seleniumPath) {
        hoverOverElement(seleniumPath.getBy());
    }

    private static void hoverOverElement(WebElement webElement) {
        getActions().moveToElement(webElement).build().perform();
    }

    public static boolean isAbsent(SeleniumPath SeleniumPath) {
        return !isPresent(SeleniumPath.getBy());
    }

    private static boolean isDisplayed(By by) {
        return isDisplayed(findElement(by));
    }

    public static boolean isDisplayed(SeleniumPath seleniumPath) {
        return isDisplayed(seleniumPath.getBy());
    }

    public static boolean isPresentAndDisplayed(SeleniumPath seleniumPath){
    	return isPresent(seleniumPath) && isDisplayed(seleniumPath);
    }

    private static boolean isDisplayed(WebElement findElement) {
        return findElement.isDisplayed();
    }
    
    public static boolean isNotDisplayed(SeleniumPath seleniumPath) {
        return !isDisplayed(seleniumPath.getBy());
    }

    private static boolean isPresent(By by) {
        return !getWebDriver().findElements(by).isEmpty();
    }

    public static boolean isPresent(SeleniumPath seleniumPath) {
        return isPresent(seleniumPath.getBy());
    }

    private static boolean isSelected(WebElement findElement) {
        return findElement.isSelected();
    }
    
    private static boolean isSelected(By by) {
    	return isSelected(findElement(by));
    }
    
    public static boolean isSelected(SeleniumPath seleniumPath) {
    	return isSelected(seleniumPath.getBy());
    }
    
    public static void openURL(String url) {
        getWebDriver().get(url);
    }

    public static void selectOption(SeleniumPath seleniumPath, String optionValue) {
        click(seleniumPath.getBy());
        click(findElement(seleniumPath.getBy(), By.cssSelector("option[value='" + optionValue + "']")));
    }

    public static void clickNthElement(SeleniumPath seleniumPath, int n) {
        List<WebElement> webElements = findElements(seleniumPath.getBy());
        if(webElements.size() <= n) {
            throw new IndexOutOfBoundsException("Error selecting the " + n + " element of " + seleniumPath);
        }
        WebElement clickableElement = webElements.get(n);
        waitForElementToBeClickable(clickableElement);
        click(clickableElement);
    }

    private static void sendKeys(By by, CharSequence keys) {
        sendKeys(findElement(by), keys);
    }

    private static void sendKeys(By by, Keys keys) {
        sendKeys(findElement(by), keys);
    }

    public static void sendKeys(SeleniumPath seleniumPath, CharSequence keys) {
        waitForVisibilityOfElement(seleniumPath.getBy());
        sendKeys(seleniumPath.getBy(), keys);
    }

    public static void sendStringList(SeleniumPath seleniumPath, List<String> stringList) {
        waitForVisibilityOfElement(seleniumPath.getBy());
        for (String keys : stringList) {
            sendKeys(seleniumPath.getBy(), keys + (stringList.indexOf(keys) == stringList.size() - 1 ? "" : ","));
        }
    }

    public static void sendKeysToInvisibleElement(SeleniumPath seleniumPath, CharSequence keys){
        sendKeys(seleniumPath.getBy(), keys);
    }

    public static void sendKeys(SeleniumPath seleniumPath, Keys keys) {
        waitForVisibilityOfElement(seleniumPath.getBy());
        sendKeys(seleniumPath.getBy(), keys);
    }

    public static void sendKeys(WebElement webElement, CharSequence keys) {
        webElement.sendKeys(keys);
    }

    private static void sendKeys(WebElement webElement, Keys keys) {
        webElement.sendKeys(keys);
    }

    public static void setValueOfHiddenInput(SeleniumPath seleniumPath, String value) {
        WebElement input = findElement(seleniumPath.getBy());
        JavascriptExecutor js = (JavascriptExecutor)getWebDriver();
        js.executeScript("document.getElementsByName('" + input.getAttribute("name") + "')[0].setAttribute('value', '" + value + "');");
    }

    public static void setBodyInnerHTML(String text) {
        String pElement = "<p>" + text + "<br></p>";
        String jsCode = "document.body.innerHTML = '<div spellcheck=\"true\" aria-label=\"Message body\" id=\"MicrosoftOWAEditorRegion\" class=\"ms-rtestate-write\" useinlinestyle=\"true\" contenteditable=\"true\" role=\"textbox\" style=\"height:100%;border-style:hidden; outline:none;font-size:12pt;color:#000000;background-color:#FFFFFF;font-family:Calibri,Arial,Helvetica,sans-serif;\" rtedirty=\"true\">" + pElement + "</div>'";
        ((JavascriptExecutor) getWebDriver()).executeScript(jsCode);
    }

    public static void switchToDefaultContent() {
        getWebDriver().switchTo().defaultContent();
    }

	private static void switchToFrame(WebElement webElement) {
        getWebDriver().switchTo().frame(webElement);
    }

    private static void switchToFrameWhenPresent(By frameBy) {
        switchToFrame(waitForPresenceOfElement(frameBy));
    }

    public static void switchToFrameWhenPresent(SeleniumPath seleniumPath) {
        switchToDefaultContent();
        switchToFrameWhenPresent(seleniumPath.getBy());
    }

    public static boolean isElementPresent(SeleniumPath seleniumPath) {
        try {
            findElement(seleniumPath);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public static boolean isElementVisible(String cssLocator){
        return findElement(By.cssSelector(cssLocator)).isDisplayed();
    }

    private static void switchToInnerFrame(By frameBy) {
        switchToFrame(waitForPresenceOfElement(frameBy));
    }

    public static void switchToInnerFrame(SeleniumPath seleniumPath) {
        switchToInnerFrame(seleniumPath.getBy());
    }

    public static void switchToTab(int tabNumber) {
        ArrayList<String> tabs = new ArrayList<String>(getWebDriver().getWindowHandles());
        getWebDriver().switchTo().window(tabs.get(tabNumber));
    }

    public static void switchToOtherWindow() {
        waitTwoSeconds();
        String currentWindow = getWebDriver().getWindowHandle();
        ArrayList<String> allWindows = new ArrayList<String>(getWebDriver().getWindowHandles());
        for (String aWindow : allWindows) {
            if (!currentWindow.equals(aWindow)) {
                getWebDriver().switchTo().window(aWindow);
                break;
            }
        }
    }

    public static void closeTab(int tabNumber) {
        switchToTab(tabNumber);
        getWebDriver().close();
    }

    public static boolean urlContains(String value) {
        return getCurrentUrl().contains(value);
    }

    private static void wait(int millis) {
        try {
            Thread.sleep(millis);
        } catch(InterruptedException e) {
        }
    }

    private static WebElement waitForElementToBeClickable(By by) {
        return getWebDriverWaitMedium().until(ExpectedConditions.elementToBeClickable(by));
    }

    private static WebElement waitForElementToBeClickable(WebElement webElement) {
        return getWebDriverWaitMedium().until(ExpectedConditions.elementToBeClickable(webElement));
    }

    private static WebElement waitForElementToBeClickableShort(By by) {
        return getWebDriverWaitShort().until(ExpectedConditions.elementToBeClickable(by));
    }

    private static WebElement waitForElementToBeClickableShort(WebElement webElement) {
        return getWebDriverWaitShort().until(ExpectedConditions.elementToBeClickable(webElement));
    }

    public static WebElement waitForElementToBeClickable(SeleniumPath seleniumPath) {
        return waitForElementToBeClickable(seleniumPath.getBy());
    }

    private static void waitForInvisibilityOfElement(By by, int wait) {
        getWebDriverWaitManual(wait).until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    private static void waitForInvisibilityOfElement(By by) {
        getWebDriverWaitMedium().until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    private static void waitForInvisibilityOfElementLong(By by) {
        getWebDriverWaitLong().until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    private static void waitForInvisibilityOfElementShort(By by) {
        getWebDriverWaitShort().until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public static void waitForInvisibilityOfElement(SeleniumPath seleniumPath) {
        waitForInvisibilityOfElement(seleniumPath.getBy());
    }

    public static void waitForInvisibilityOfElementShort(SeleniumPath seleniumPath) {
        waitForInvisibilityOfElementShort(seleniumPath.getBy());
    }

    public static void waitForElementToContainText(SeleniumPath seleniumPath, String text) {
        waitForElementToContainText(seleniumPath.getBy(), text);
    }

    private static void waitForElementToContainText(By by, String text) {
        getWebDriverWaitMedium().until(ExpectedConditions.textToBePresentInElementLocated(by, text));
    }

    private static void waitForElementToContainTextShort(By by, String text) {
        getWebDriverWaitShort().until(ExpectedConditions.textToBePresentInElementLocated(by, text));
    }

    private static List<WebElement> waitForPresenceOfAllElements(By by) {
        return getWebDriverWaitMedium().until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }

    private static List<WebElement> waitForVisibilityOfAllElements(By by) {
        return getWebDriverWaitMedium().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
    }

    public static List<WebElement> waitForVisibilityOfAllElements(SeleniumPath seleniumPath) {
        return waitForVisibilityOfAllElements(seleniumPath.getBy());
    }

    public static List<WebElement> waitForPresenceOfAllElements(SeleniumPath seleniumPath) {
        return waitForPresenceOfAllElements(seleniumPath.getBy());
    }

    private static WebElement waitForPresenceOfElement(By by) {
        return getWebDriverWaitMedium().until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private static WebElement waitForPresenceOfElement(By by, int wait) {
        return getWebDriverWaitManual(wait).until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private static WebElement waitForPresenceOfElementShort(By by) {
        return getWebDriverWaitShort().until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public static WebElement waitForPresenceOfElement(SeleniumPath seleniumPath) {
        return waitForPresenceOfElement(seleniumPath.getBy());
    }

    public static WebElement waitForPresenceOfElementShort(SeleniumPath seleniumPath) {
        return waitForPresenceOfElementShort(seleniumPath.getBy());
    }

    private static WebElement waitForVisibilityOfElement(By by) {
        return getWebDriverWaitMedium().until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    private static WebElement waitForVisibilityOfElementShort(By by) {
        return getWebDriverWaitShort().until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    private static WebElement waitForVisibilityOfElementVeryShort(By by) {
        return getWebDriverWaitVeryShort().until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static WebElement waitForVisibilityOfElementShort(SeleniumPath seleniumPath) {
        return waitForVisibilityOfElementShort(seleniumPath.getBy());
    }

    public static WebElement waitForVisibilityOfElementVeryShort(SeleniumPath seleniumPath) {
        return waitForVisibilityOfElementVeryShort(seleniumPath.getBy());
    }

    public static void waitForAbsenceOfElement(SeleniumPath seleniumPath) {
        if (isPresent(seleniumPath)) {
            waitForAbsenceOfElement(seleniumPath.getBy());
        }
    }

    private static void waitForAbsenceOfElement(By by) {
        if (isPresent(by)) {
            waitForAbsenceOfElement(findElement(by));
        }
    }

    public static List<String> getOptions(SeleniumPath options){
    	List<String> result = new ArrayList<>();
    	List<WebElement> values = findElements(options);
		for (WebElement e: values)
			result.add(e.getAttribute("value").trim());
		return result;
    }

    private static void waitForAbsenceOfElement(WebElement webElement) {
    	WebDriverWait wait = new WebDriverWait(getWebDriver(), 10);
        if (webElement.isDisplayed()) {
            wait.until(ExpectedConditions.stalenessOf(webElement));
        }
    }

    public static void waitOneTenthOfASecond() {
        wait(100);
    }

    // Do not use, need to dynamically wait for elements if possible
    public static void waitHalfSecond() {
        wait(500);
    }

    // Do not use, need to dynamically wait for elements if possible
    public static void waitOneSecond() {
        wait(1000);
    }

    // Do not use, need to dynamically wait for elements if possible
    public static void waitTwoSeconds() {wait(2000);}

    public static void waitThreeSeconds() {wait(3000);}

    // Do not use, need to dynamically wait for elements if possible
    public static void waitSeconds(int numSeconds) {
        wait(numSeconds * 1000);
    }

    private WebDriverUtil() {
        throw new IllegalAccessError("Cannot instantiate utility class.");
    }

    public static void refreshBrowserAndAcceptAlert() {
        getWebDriver().navigate().refresh();
        acceptAlert();
    }

    public static void refreshBrowser() {
        getWebDriver().navigate().refresh();
    }

    private static WebElement findVisibleElement(By by){
        List<WebElement> elements = waitForPresenceOfAllElements(by);
        for (WebElement e: elements){
            if (e.isDisplayed())
                return e;
        }
        throw new NoSuchElementException("Could not find a visible element located by " + by);
    }
    public static WebElement waitForVisibilityOfAnyElement(By by){
        return getWebDriverWaitMedium().until(new ExpectedCondition<WebElement>(){
            @Override
            public WebElement apply(WebDriver input) {
                return findVisibleElement(by);
            }});
    }

    public static <T> T waitUntil(ExpectedCondition<T> expectedCondition) {
        return getWebDriverWaitMedium().until(expectedCondition);
    }

    // For items that toggle the presence of another element
    // (such as the "menu" button, which both opens and closes the main menu)
    // clicking the item after something else has changed the state
    // may have the opposite of the intended effect
    // this allows you to test whether the element the toggle controls is present
    // before clicking it and toggling the element's presence in the wrong direction
    public static boolean checkForPresenceOfElement(SeleniumPath seleniumPath) {
        return isPresent(seleniumPath);
    }

    public static void writeScreenshot(String path) {
        File f = ((TakesScreenshot)getWebDriver()).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(f, new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void moveTo(SeleniumPath seleniumPath) {
        moveTo(seleniumPath.getBy());
    }

    private static void moveTo(By by) {
        waitForPresenceOfElement(by);
        WebElement element = findElement(by);
        getActions().moveToElement(element).build().perform();
    }

    public static void selectDropdownOption(SeleniumPath seleniumPath, String optionText) {
        Select selectDropdown = new Select(findElement(seleniumPath.getBy()));
        selectDropdown.selectByVisibleText(optionText);
    }

    public static void selectDropdownOptionByValue(SeleniumPath seleniumPath, String optionText) {
        Select selectDropdown = new Select(findElement(seleniumPath.getBy()));
        selectDropdown.selectByValue(optionText);
    }

    public static String isChecked(SeleniumPath seleniumPath) {
        JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
        String elementPath = seleniumPath.getBy().toString().replace("By.selector: ","");
        return (js.executeScript("var bo = $(" + "\"" + elementPath + "\"" + ").is(':checked'); return bo;").toString());
    }

    public static String getAngularCheckboxValue(String elementPath) {
        JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
        return (js.executeScript("var bo = $(" + "\"" + elementPath + "\"" + ").is(':checked'); return bo;").toString());
    }

	public static Object jScript(String str) {
		return jScript(str, true);
	}

    public static Object jScript(String str, Boolean doLog) {
        if(doLog) {
            LOGGER.info("|| Javascript Command: " + str);
        }
        return ((JavascriptExecutor) getWebDriver()).executeScript(str);
    }

    public static Boolean getJscriptIsTrue(String str) {
        return jScript(str, false).toString() == "true";
    }

    public static void triggerClickEvent(String css) {
        jScript("var evt = document.createEvent(\"HTMLEvents\");" + "evt.initEvent(\"click\", false, true);"+"$('" + css + "')[0].dispatchEvent(evt)");
    }

    public static void waitUntilJavascriptIsFinished() {
        new WebDriverWait(getWebDriver(), 10).until(
                (Object input)->getJscriptIsTrue("try {\n" +
                        "  if (document.readyState !== 'complete') {\n" +
                        "    return false; // Page not loaded yet\n" +
                        "  }\n" +
                        "  if (window.jQuery) {\n" +
                        "    if (window.jQuery.active) {\n" +
                        "      return false;\n" +
                        "    } else if (window.jQuery.ajax && window.jQuery.ajax.active) {\n" +
                        "      return false;\n" +
                        "    }\n" +
                        "  }\n" +
                        "  if (window.angular) {\n" +
                        "    if (!window.qa) {\n" +
                        "      // Used to track the render cycle finish after loading is complete\n" +
                        "      window.qa = {\n" +
                        "        doneRendering: false\n" +
                        "      };\n" +
                        "    }\n" +
                        "    // Get the angular injector for this app (change element if necessary)\n" +
                        "    var injector = window.angular.element(document.body).injector();\n" +
                        "    // Store providers to use for these checks\n" +
                        "    var $rootScope = injector.get('$rootScope');\n" +
                        "    var $http = injector.get('$http');\n" +
                        "    var $timeout = injector.get('$timeout');\n" +
                        "    // Check if digest\n" +
                        "    if ($rootScope.$$phase === '$apply' || $rootScope.$$phase === '$digest' || $http.pendingRequests.length !== 0) {\n" +
                        "      window.qa.doneRendering = false;\n" +
                        "      return false; // Angular digesting or loading data\n" +
                        "    }\n" +
                        "    if (!window.qa.doneRendering) {\n" +
                        "      // Set timeout to mark angular rendering as finished\n" +
                        "      $timeout(function() {\n" +
                        "        window.qa.doneRendering = true;\n" +
                        "      }, 0);\n" +
                        "      return false;\n" +
                        "    }\n" +
                        "  }\n" +
                        "  return true;\n" +
                        "} catch (ex) {\n" +
                        "  return false;\n" +
                        "}")
        );
    }


    public static boolean waitUntil(boolean expectedCondition) {
        WebDriverUtil.waitUntil(expectedCondition);
        return true;
    }

    public static WebElement getPageElementByCssSelector(String selector) {
        return findElement(new By.ByCssSelector(selector));
    }
}
