package com.testinium.gauge.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Locale;

public class GaugeBase extends BasePage {
    public GaugeBase(WebDriver driver) {
        super(driver);
    }


    /**
     * Click Element Method
     */
    public void Click(By by) {

        clickElement(by);
    }

    /**
     * Fill Input Method
     *
     */
    public void SendKeys(By by, String text) {
        sendKeys(by, text);
    }

    public List<WebElement> getList(By by){

        List<WebElement> listElement = null;
        listElement = findElements(by);
        return listElement;
    }

    /**
     * Time Unit
     *
     * @param sleepTime
     */
    public void timeUnitSeconds(int sleepTime){
        waitSeconds(sleepTime);
    }

    /**
     * Navigate to Page
     *
     * @param url
     */
    public void navigateSite(String url) {
        navigateTo(url);
    }

    /**
     * @param string
     * @return
     */
    public String converTurkishCharacter(String string) {
        return convertTurkishChar(string).toUpperCase(new Locale("en"));
    }

    /**
     * Until Element Appear Method
     *
     */
    public void untilElementAppear(By by) {
        untilElementAppear(by);
    }

    public void clickJS(By by, int... index){
        getJSExecutor().executeScript("arguments[0].click();", findElement(by, index));
    }

    public void waitSec(int sec){
        waitSeconds(sec);
    }

    public void isTextEqual(By by, String text){
        assertionTrue(by + " alanından gelen değer " +text + " değerine eşit değil.", isTextEquals(by,text));
        isTextEquals(by,text);
    }

    public String getAttributeValue(By by){
        return  getAttribute(by,"value");
    }


    public String getElementText(By by){
        return getText(by);
    }

}
