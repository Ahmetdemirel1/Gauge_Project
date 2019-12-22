package com.testinium.gauge.Util;


import com.testinium.gauge.base.BaseTest;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class ContextPage extends BaseTest {

    private final Logger logger = Logger.getLogger(ContextPage.class);
    private static final int DEFAULT_WAIT = 30;
    private static final int MIN_WAIT = 10;

    static void checkPage(By locator) {
        boolean isExist = false;
        if (locator != null) {
            try {
                waitForElement(DEFAULT_WAIT, locator);
                isExist = true;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        assertTrue(locator + " bulunamadı.", isExist);
    }

    public static void isButtonDisabled(By locator) {
        // boolean enableAttr = waitForElement(MIN_WAIT, id).isEnabled()
        assertTrue(locator + " aktif göründü", !waitForElement(MIN_WAIT, locator).isEnabled());
    }

    static WebElement waitForElement(int seconds, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, seconds, 3000);
        return wait.until( ExpectedConditions.presenceOfElementLocated(locator));
    }

    public void iSeePageCommon(By by) {
        checkPage(by);
    }

    static void iSeePageSpesific(By locator) {
        checkPage(locator);
    }

    private void waitSeconds() {
        try {
            TimeUnit.SECONDS.sleep( 1 );
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }

    private void iclickCommon(By locator) {
        clickAndWaitForElement(locator);
    }

    private void iclickIfExistCommon(By locator) {
        if (isexist(locator, 3)) {
            clickAndWaitForElement(locator);
        }
    }

    public void iclickSpesific(By locator) {
        clickAndWaitForElement(locator);
    }


    private static void iSelect(By locator, String text) {
        selectWithGivenData(locator, text);
    }

    private static void selectWithGivenData(By by, String text) {
        List<WebElement> list = waitForElements(DEFAULT_WAIT, by);
        for (WebElement element : list) {
            if (element.getText().contains(text)) {
                element.click();
                break;
            }
        }
    }

    private void fillWithGivenData(By locator, String text) {
        log.info("Doldurulan metin : " + text);
        WebElement element = getElement(locator);
        // focusAndClear(element)
        waitSeconds();
        element.sendKeys(text);
    }

    private WebElement getElement(By locator) {
        WebElement element = null;
        if (locator != null) {
            WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT);
            element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        }
        return element;
    }

    private WebElement getElementFromList(By locator, int index) {
        List<WebElement> elementList = null;
        if (locator != null) {
            WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT, 1000);
            elementList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        }
        if (elementList != null) {
            return elementList.get(index);
        }
        return null;
    }


    public void iclickSpesificIndex(By locator, int index) {
        waitForElements(DEFAULT_WAIT, locator).get(index).click();
    }

    private static List<WebElement> waitForElements(int seconds, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, seconds, 1000);
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    private void clickAndWaitForElement(By locator) {
        if (locator != null) {
            WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT);
            wait.until(ExpectedConditions.presenceOfElementLocated(locator)).click();
        }
    }

    public void iFillSpesific(By by, String text) {
        fillWithGivenData(by, text);
    }

    private void getTextContains(By locator, String text) {
        logger.info("CONTROL DATA : " + text);
        assertTrue("Aranan metin : \"" + text + "\" bulunamadı.",
                waitForElement(DEFAULT_WAIT, locator).getText().contains(text));
    }

    public void getTextContainsIndex(By locator, String text, int index) {
        Assert.assertTrue("Aranan metin : \"" + text + "\" bulunamadı.",
                waitForElements(DEFAULT_WAIT, locator).get(index).getText().contains(text));
    }

    private boolean isExistByLabel(String text, int seconds) {
        int counter = 0;
        boolean isExist = false;
        while (counter < seconds) {
            log.info(driver.getPageSource());
            if (driver.getPageSource().contains(text)) {
                isExist = true;
                break;
            }
            waitSeconds();
            counter++;
        }
        return isExist;
    }

    public boolean isexist(By locator, int seconds) {
        try {
            waitForElement(seconds, locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void isButtonEnabled(By locator) {
        boolean enableAttr = waitForElement(MIN_WAIT, locator).isEnabled();
        assertTrue(locator + " aktif göründü", enableAttr);
    }

    public static void wait(int seconds) {
        int count = 0;
        while (count < seconds) {
            try {
                TimeUnit.SECONDS.sleep((seconds));
                if (count == 20) {
                    log.info(driver.getPageSource());
                }
                count++;
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                Thread.currentThread().interrupt();

            }
        }
    }

    public void iclickIfExistSpesific(By locator) {
        clickAndWaitForElement(locator);
    }

    public static void isPage(By by) {
        //iSeePageSpesific(by);
        waitForElement(20,by);
    }

    public void clickAndWaitForElements(By by) {
        iclickCommon(by);
    }


    public static void selectFromList(By by, String text) {
        iSelect(by, text);
    }

    public void clickIfExist(By by) {
        iclickIfExistCommon(by);
    }


    public void getTextIsContains(By locator, String text) {
        getTextContains(locator, text);
    }

    public void isExistElement(int seconds, By by) {
        assertTrue(by + " elementi bulunamadı.", isexist(by, seconds));
    }


    private void waitForElementDisappear(int seconds, By locator) {
        int count = 0;
        while (isexist(locator, 1)) {
            if (count == seconds) {
                Assert.assertTrue(locator + " belirtilen süre içinde kaybolmadı.", false);
                break;
            }
            waitSeconds();
            count++;
        }
    }


}
