package com.testinium.gauge.base;

import com.sun.deploy.util.Waiter;
import com.testinium.gauge.Util.Waiters;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class BasePageUtil {

    protected WebDriver driver;
    protected final Logger log = Logger.getLogger(getClass());
    protected WebDriverWait wait;
    protected Waiters wa;


    public BasePageUtil(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 30);
        this.wa = new Waiters(this.driver);
    }

    protected JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) driver;
    }

    /**
     * @param jsStmt
     * @param wait
     * @return
     */
    protected Object executeJS(String jsStmt, boolean wait) {
        return wait ? getJSExecutor().executeScript(jsStmt, "") : getJSExecutor().executeAsyncScript(jsStmt, "");
    }

    /**
     * Element null exception
     *
     * @param by
     * @param index
     */
    protected void nullElementException(By by, int... index) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ELEMENT (");
        stringBuilder.append(by);
        stringBuilder.append(",");
        stringBuilder.append(index.length > 0 ? index[0] : "");
        stringBuilder.append(") NOT EXISTS; AUTOMATION DATAS MAY BE INVALID!");
        throw new NullPointerException(stringBuilder.toString());
    }

    protected void waitAllRequest() {
        wa.pageLoadComplete();
        wa.ajaxComplete();
        //wa.jQueryComplete();

    }

    protected WebElement findElement(By by, int... index) {
        waitAllRequest();
        try {
            if (index.length == 0) {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            } else if (index[0] >= 0) {
                List<WebElement> elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
                if (!elements.isEmpty() && index[0] <= elements.size()) {
                    return elements.get(index[0]);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * find WebElement parent
     *
     * @param parent
     * @param by
     * @param index
     * @return
     */
    protected WebElement findElement(WebElement parent, By by, int... index) {
        waitAllRequest();
        if (index.length == 0) {
            return parent.findElement(by);
        } else if (index[0] >= 0) {
            List<WebElement> elements = parent.findElements(by);
            if (!elements.isEmpty() && index[0] <= elements.size()) {
                return elements.get(index[0]);
            }
        }
        return null;
    }

    /**
     * findElement parent by, by
     *
     * @param parent
     * @param child
     * @param index
     * @return
     */
    protected WebElement findElement(By parent, By child, int... index) {
        return findElement(findElement(parent), child, index);
    }

    /**
     * @param by
     * @return list
     */
    protected List<WebElement> findElements(By by) {
        waitAllRequest();
        List<WebElement> listOfElements = null;
        try {

            listOfElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
        } catch (Exception e) {
            log.error("Element null");
        }
        return listOfElements;

    }

    /**
     * @param x
     * @param y
     */
    protected void scrollTo(int x, int y) {
        String jsStmt = String.format("window.scrollTo(%d, %d);", x, y);
        executeJS(jsStmt, true);
    }

    /**
     * @param element
     */
    protected void scrollToElement(WebElement element) {
        if (element != null) {
            scrollTo(element.getLocation().getX(), element.getLocation().getY());
        }
    }

    protected void scrollToElement(By by, int... index) {
        scrollToElement(findElement(by, index));
    }

    /**
     * scrollTo Page End
     */
    protected void scrollToPageEnd() {
        executeJS("window.scrollTo(0, document.body.scrollHeight)", true);
    }

    /**
     * scrollTo Page Up
     */
    protected void scrollToPageUp() {
        executeJS("window.scrollTo(document.body.scrollHeight, 0)", true);
    }


    /**
     * Sleep Seconds
     *
     * @param sleepTime
     * @throws InterruptedException
     */
    protected void waitSeconds(int sleepTime) {
        try {
            TimeUnit.SECONDS.sleep(sleepTime);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * Switch to frame
     *
     * @param by
     */
    protected void switchToFrame(By by, int... index) {
        driver.switchTo().defaultContent();
        WebElement switchElement = findElement(by, index);
        driver.switchTo().frame(switchElement);
    }

    /**
     * Alert popup handle
     *
     * @param acceptAndDismiss
     * @throws InterruptedException
     */
    protected void alertPopup(boolean acceptAndDismiss) {
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        if (acceptAndDismiss) {
            alert.accept();
        } else {
            alert.dismiss();
        }
    }

    /**
     * Mouse Hover and Click
     *
     * @param by
     * @param click
     * @param index
     */
    protected void hoverElement(By by, boolean click, int... index) {
        hoverElement(findElement(by, index));
        if (click) {
            clickElement(by, index);
        }
    }

    /**
     * Mouse Hover and Click
     *
     * @param by
     * @param index
     */
    protected void hoverElementAndClick(By by, int... index) {
        hoverElement(by, true, index);
    }

    /**
     * Mouse Hover
     *
     * @param by
     * @param index
     */
    protected void hoverElement(By by, int... index) {
        hoverElement(by, false, index);
    }


    /**
     * Mouse Hover
     *
     * @param by
     * @param index
     */
    protected void hoverElementJS(By by, int... index) {
        hoverElementJavasript(findElement(by, index));
    }

    /**
     * Mouse Hover
     *
     * @param element
     */
    protected void hoverElement(WebElement element) {
        Actions action = new Actions(driver);
        action.moveToElement(element).build().perform();
    }

    /**
     * Mouse Hover
     *
     * @param element
     */
    protected void hoverElementJavasript(WebElement element) {
        /*String strJavaScript = "var element = arguments[0];"
                + "var mouseEventObj = document.createEvent('MouseEvents');"
                + "mouseEventObj.initEvent( 'mouseover', true, true );" + "element.dispatchEvent(mouseEventObj);";
        executeJS(strJavaScript, element);*/
    }

    /**
     * Mouse Hover List
     *
     * @param click ? true(click) : only hover
     */
    protected void hoverNClickAllElement(By by, boolean... click) {
        for (WebElement webElement : findElements(by)) {
            hoverElement(webElement);
            if (click != null && click[0]) {
                webElement.click();
            }
        }
    }

    /**
     * Drag and Drop Element
     *
     * @param by
     * @param xOffset
     * @param yOffset
     * @throws InterruptedException
     */
    protected void dragElement(By by, int xOffset, int yOffset) {
        Actions a = new Actions(driver);
        a.dragAndDropBy(findElement(by), xOffset, yOffset).build().perform();
        waitSeconds(2);
    }

    /**
     * Find Select Menu
     *
     * @param by
     * @return
     */
    protected Select selectOption(By by) {
        return new Select(findElement(by));
    }

    protected Select selectOption(WebElement element) {
        return new Select(element);
    }


    /**
     * Select menu with text click.
     *
     * @param by    = id olması tercih edilir.
     * @param value = Görünen text'i
     */
    protected void selectOptionClick(By by, String value) {
        selectOption(by).selectByVisibleText(value);
    }

    protected void selectOptionClick(WebElement element, String value) {
        selectOption(element).selectByVisibleText(value);
    }

    /**
     * Sleep Minute
     *
     * @param sleepTime
     * @throws InterruptedException
     */
    protected void waitMinute(int sleepTime) {
        try {
            TimeUnit.MINUTES.sleep(sleepTime);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Select menu list get.
     *
     * @param by
     * @return
     */
    protected List<WebElement> selectOptionList(By by) {
        return selectOption(by).getOptions();
    }

    /**
     * Select menu first select get
     *
     * @param by
     * @return
     */
    protected WebElement selectOptionFirstSelect(By by) {
        return selectOption(by).getFirstSelectedOption();
    }

    /**
     * Select menu get selected text
     *
     * @param by
     * @return
     */
    protected String selectOptionSelectedText(By by) {
        return selectOption(by).getFirstSelectedOption().getText();
    }

    /**
     * Assertion Control(True)
     *
     * @param message
     * @param condition
     */
    public void assertionTrue(String message, boolean condition) {
        assertTrue(message, condition);
    }

    /**
     * Assertion Control(True)
     *
     * @param condition
     */
    public void assertionTrue(boolean condition) {
        assertTrue(condition);
    }

    /**
     * Assertion Control(False)
     *
     * @param message
     * @param condition
     */
    public void assertionFalse(String message, boolean condition) {
        assertFalse(message, condition);
    }

    /**
     * Assertion Control(False)
     *
     * @param condition
     */
    public void assertionFalse(boolean condition) {
        assertFalse(condition);
    }

    /**
     * Assertion Control(Equals)
     *
     * @param message
     * @param expected
     * @param actual
     */
    public void assertionEquals(String message, Object expected, Object actual) {
        assertEquals(message, expected, actual);
    }

    /**
     * Assertion Fail
     *
     * @param message
     */
    public void assertFail(String message) {
        fail(message);
    }


    /**
     * Click web element (By by, int index(getIndex)) waitforAjax
     *
     * @param by
     * @param index
     */
    protected void clickElement(By by, int... index) {
        WebElement element = null;
        try {
            element = findElement(by, index);
        } catch (Exception e) {
            log.error("ERROR :", e);
            assertFail("Element Not Found :" + e.getMessage());
        }
        if (element == null) {
            nullElementException(by, index);
        } else {
            if (!isElementDisplayed(by, index)) {
                //scrollTo(element.getLocation().getX(), element.getLocation().getY());
                scrollToElement(element);
            }
            untilElementClickable(element);
            log.info("Element Clicked :" + element);
            element.click();
        }
    }

    /**
     * Click web element parent (By parent, By by, int index(getIndex))
     * waitforAjax
     *
     * @param parent
     * @param child
     * @param index
     */
    protected void clickElement(By parent, By child, int... index) {
        WebElement element = findElement(findElement(parent, index[0]), child, index[1]);
        if (element == null) {
            nullElementException(child, index);
        } else {
            if (!element.isDisplayed()) {
                scrollTo(element.getLocation().getX(), element.getLocation().getY());
            }
            untilElementClickable(element);
            log.info("Element Clicked :" + element);
            element.click();

        }
    }

    protected void clickElement(WebElement element, boolean waitForAjax) {
        WebElement foundedElement = element;
        if (foundedElement == null) {
            // nullElementException(by, index)
        } else {
            if (!foundedElement.isDisplayed()) {
                scrollTo(foundedElement.getLocation().getX(), foundedElement.getLocation().getY());
            }
            untilElementClickable(foundedElement);
            foundedElement.click();
        }
    }


    /**
     * Click web element with JavaScript
     *
     * @param by
     * @param index
     */
    protected void clickElementJS(By by, int... index) {
        getJSExecutor().executeScript("arguments[0].click();", findElement(by, index));
    }

    /**
     * Provide data input(By by, String text, int index)
     *
     * @param by
     * @param text
     * @param index
     */
    protected void sendKeys(By by, String text, int... index) {
        sendKeys(by, text, false, index);
    }

    /**
     * Provide data input(By by, String text, int index) press enter
     *
     * @param by
     * @param text
     * @param pressEnter
     * @param index
     */
    protected void sendKeys(By by, String text, boolean pressEnter, int... index) {
        WebElement element = null;
        try {
            element = findElement(by, index);
        } catch (Exception e) {
            log.error("ERROR :", e);
            assertFail("Element Not Found :" + e.getMessage());
        }
        if (element == null) {
            nullElementException(by, index);
        } else if (element.isEnabled()) {
            untilElementClickable(element);
            log.info("Element Send Keys : " + text + "-" + element);
            element.clear();
            element.sendKeys(text);
            if (pressEnter) {
                element.sendKeys(Keys.ENTER);
            }
        }
    }

    /**
     * Qualification to control the visibility
     *
     * @param by
     * @param index
     * @return
     */
    public boolean isElementDisplayed(By by, int... index) {
        try {
            return findElement(by, index).isDisplayed();
        } catch (NoSuchElementException e) {
            log.debug("Element Is Not Displayed", e);
            return false;
        }
    }

    /**
     * Qualification to control the visibility
     *
     * @param element
     * @return
     */
    protected boolean isElementDisplayed(WebElement element) {
        if (element.isDisplayed()) {
            return true;
        }
        return false;
    }

    /**
     * Qualification to control the enabled
     *
     * @param by
     * @param index
     * @return
     */
    protected boolean isElementEnabled(By by, int... index) {
        try {
            return findElement(by, index).isEnabled();
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return false;
        }
    }

    /**
     * Navigate to url
     *
     * @param url
     */
    protected void navigateTo(String url) {
        driver.navigate().to(url);
    }

    /**
     * Refresh page
     */
    protected void refreshTo() {
        driver.navigate().refresh();
    }

    /**
     * Get Element Attribute
     *
     * @param by
     * @param attr
     * @param index
     * @return
     */
    protected String getAttribute(By by, String attr, int... index) {
        log.info("Feature Sought Element :" + attr + "-" + by);
        return findElement(by, index).getAttribute(attr);
    }

    /**
     * Get Element Attribute for Value
     *
     * @param by
     * @param index
     * @return
     */
    protected String getValueAttribute(By by, int... index) {
        log.info("Feature Sought Element : value -" + by);
        return getAttribute(by, "value", index);
    }

    /**
     * Get page url
     *
     * @return
     */
    protected String getCurrentUrl() {
        log.info("Found Url :" + driver.getCurrentUrl().trim());
        return driver.getCurrentUrl().trim();
    }

    /**
     * Get page source
     *
     * @return
     */
    protected String getPageSource() {
        return driver.getPageSource();
    }

    /**
     * Get page title
     *
     * @return
     */
    protected String getTitle() {
        log.info("Found Page Title :" + driver.getTitle());
        return driver.getTitle();
    }

    /**
     * Get element text
     *
     * @param by
     * @param index
     * @return
     */
    protected String getText(By by, int... index) {
        return findElement(by, index).getText();
    }

    /**
     * Get element list size
     *
     * @param by
     * @return
     */
    protected Integer getSize(By by) {
        return findElements(by).size();
    }

    /**
     * Get Lenght
     *
     * @param value
     * @return
     */
    protected Integer getLenght(String value) {
        return value.length();
    }

    /**
     * Go forward backs
     */
    public void goBack() {
        driver.navigate().back();
    }

    /**
     * Call the page
     *
     * @param page
     */
    protected void callPage(String page) {
        driver.get(getCurrentUrl() + page);
    }

    /**
     * Turkish character to convert
     *
     * @param string
     * @return
     */
    protected static String convertTurkishChar(String string) {
        String returnString = string;
        char[] turkishChars = new char[]{0x131, 0x130, 0xFC, 0xDC, 0xF6, 0xD6, 0x15F, 0x15E, 0xE7, 0xC7, 0x11F,
                0x11E};
        char[] englishChars = new char[]{'i', 'I', 'u', 'U', 'o', 'O', 's', 'S', 'c', 'C', 'g', 'G'};
        for (int i = 0; i < turkishChars.length; i++) {
            returnString = returnString.replaceAll(new String(new char[]{turkishChars[i]}),
                    new String(new char[]{englishChars[i]}));
        }
        return returnString;
    }

    /**
     * Balance for Integer
     *
     * @return
     */
    protected int getCurrencyAsInt(By balance) {
        String getTimes = getText(balance);
        getTimes = getTimes.replaceAll("\\.", "");
        getTimes = getTimes.replaceAll("TL", "");
        getTimes = getTimes.replaceAll(" ", "");
        return new Integer(getTimes.split(",")[0]);
    }

    /**
     * Text Contains Control
     *
     * @param by
     * @param text contains
     * @return
     */
    protected boolean isTextContains(By by, String text, int... index) {
        try {
            return getText(by, index).contains(text);
        } catch (NullPointerException e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            log.info("Found Text :" + getText(by, index));
            log.info("Searching Text :" + text);
        }
    }

    /**
     * Text Equals Control
     *
     * @param by
     * @param text equals
     * @return
     */
    protected boolean isTextEquals(By by, String text, int... index) {
        try {
            System.out.println("getText:" + getText(by) + "verilen text:" + text);
            System.out.println(getText(by, index).equals(text));
            return getText(by, index).equals(text);
        } catch (NullPointerException e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            log.info("Found Text :" + getText(by, index));
            log.info("Searching Text :" + text);
        }
    }


    /**
     * Text Present Page Source
     *
     * @param text
     * @return
     */
    protected boolean isTextPresent(String text) {
        try {
            return getPageSource().contains(text);
        } catch (NullPointerException e) {
            log.error("Does Not Exists Element", e);
            return false;
        }
    }

    /**
     * JavaScript Clicker
     *
     * @param driver
     * @param element
     */
    protected void javaScriptClicker(WebDriver driver, WebElement element) {
        waitAllRequest();
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("var evt = document.createEvent('MouseEvents');"
                + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
                + "arguments[0].dispatchEvent(evt);", element);
    }

    /**
     * Turkish Character Ranking
     *
     * @return
     */
    protected RuleBasedCollator getTurkishCollator() {
        String turkish = " < '.' < 0 < 1 < 2 < 3 < 4 < 5 < 6 < 7 < 8 < 9 < a, A < b, B < c, C < ç, Ç < d, D < e, E < f, F"
                + "< g, G < ğ, Ğ < h, H < ı, I < i, İ < j, J < k, K < l, L "
                + "< m, M < n, N < o, O < ö, Ö < p, P < q, Q < r, R < s, S"
                + "< ş, Ş < t, T < u, U < ü, Ü < v, V < w, W < x, X < y, Y < z, Z";
        try {
            return new RuleBasedCollator(turkish);
        } catch (ParseException e) {
            log.error("Does Not Exists Character", e);
        }
        return null;
    }

    /**
     * Delete cookie
     *
     * @param cookieName
     * @return
     */
    public BasePageUtil deleteCookie(String cookieName) {
        driver.manage().deleteCookieNamed(cookieName);
        return this;
    }

    /**
     * Move To Element
     *
     * @param element
     */
    protected void moveToElement(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.perform();
    }


    /**
     * Generate css Selector By
     *
     * @param cssSelector
     * @return
     */
    protected By generateCss(String cssSelector) {
        return By.cssSelector(cssSelector);
    }

    /**
     * Generate xpath By
     *
     * @param xPath
     * @return
     */
    protected By generateXpath(String xPath) {
        return By.xpath(xPath);
    }

    protected void untilElementClickable(WebElement element) {
        log.info("Waiting Actions Process Clickable");
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }


    protected void untilElementAppear(By by) {
        log.info("Waiting Actions Process Appear");
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected void untilElementAppear(By by, int... index) {
        log.info("Waiting Actions Process Element Appear");
        wait.until(ExpectedConditions.visibilityOf(findElement(by, index)));
    }

    protected void untilElementPresence(By by) {
        log.info("Waiting Actions Process Presence");
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }


    protected void waitForElementDissapear(By by, int timeOutInSeconds) {
        log.info("Waiting Actions Process Dissapear");
        try {
            WebDriverWait waitSeconds = (WebDriverWait) new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class).ignoring(NoSuchElementException.class);
            waitSeconds.until(ExpectedConditions.invisibilityOfElementLocated(by));
        } catch (NoSuchElementException noSuch) {
            log.debug(noSuch.getMessage(), noSuch);
        } catch (TimeoutException e) {
            log.error(e.getMessage(), e);
            assertFail("Element did not Disappear in Expected Time");
        }
    }

    /**
     * Expects the element disappears
     *
     * @param by
     */
    protected void untilElementDisappear(By by) {
        log.info("Waiting Actions Process Disappear");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    /**
     * Expects the element disappears return boolean
     *
     * @param by
     */
    protected boolean untilElementDisappearB(By by) {
        log.info("Waiting Actions Process Disappear");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    /**
     * wait for web element present
     *
     * @param by
     * @param timeOutInSeconds
     * @return
     */
    protected WebElement waitForElementPresent(By by, int timeOutInSeconds) {
        WebElement element = null;
        try {
            WebDriverWait waitSeconds = (WebDriverWait) new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(NoSuchElementException.class, StaleElementReferenceException.class);
            element = waitSeconds.until(ExpectedConditions.presenceOfElementLocated(by));
            return element;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return element;
    }

    /**
     * The expected qualification visibility
     *
     * @param by
     * @param waitTime
     * @return
     */
    protected boolean isElementDisplayedWait(By by, int waitTime) {
        try {
            return waitForElementPresent(by, waitTime).isDisplayed();
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return false;
        }
    }

}

