package com.testinium.gauge.test;

import com.testinium.gauge.base.BasePageUtil;
import com.testinium.gauge.base.BaseTest;
import com.testinium.gauge.base.GaugeBase;
import com.testinium.gauge.mapping.MapMethodType;
import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import com.thoughtworks.gauge.Step;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.testinium.gauge.mapping.Mapper.foundActivity;

public class StepImplementation extends BaseTest {

    protected Map<String, String> userVariables = new HashMap<String, String>();
    protected Map<String, String> saveEnv = new HashMap<String, String>();
    GaugeBase base;
    BasePageUtil bsp;

    @BeforeScenario
    public void before() {
        PropertyConfigurator.configure("properties/log4j.properties");

        log.info("********************************* TEST IS STARTING *********************************");

        setBrowser("https://www.gittigidiyor.com");
        base = new GaugeBase(driver);

    }



    @AfterScenario
    public void afterMethod() {
        tearDown();
    }


    @Step("<button> butonuna tıklanır")
    public void clickButton(String button) {

        base.Click(foundActivity(MapMethodType.CLICK_ELEMENT, button));


    }

    @Step("<sec> saniye süre ile beklenir")
    public void waitSec(int sec) {
        base.waitSec(sec);
    }

    @Step("<by> alanına <text> yazılır")
    public void sendKeys(String by, String text) {
        base.SendKeys(foundActivity(MapMethodType.INPUT_ELEMENT, by), isTextAParameter(text));
    }


    @Step("<by> alanı <text> değerine eşittir")
    public void isTextEquels(String by, String text) {
        base.isTextEqual(foundActivity(MapMethodType.IS_ELEMENT, by), isTextAParameter(text));
    }

    @Step("Ekrandaki <by> alanı <text> değişkenine kaydedilir")
    public void ekrankiAlanDegiskeneKaydedilir(String by, String userVar) {
        String newEnv = null;
        newEnv = base.getAttributeValue(foundActivity(MapMethodType.IS_ELEMENT, by));
        if (newEnv == null || newEnv.trim().length() == 0) {
            newEnv = base.getElementText(foundActivity(MapMethodType.IS_ELEMENT, by));
            log.info(newEnv);
        }
        //  bsp.assertionFalse(by + " alanındaki değer null geldi", newEnv == null || newEnv.trim().length() == 0);
        userVariables.put(userVar, newEnv);

    }

    @Step("<by> urun listesinden rastgele bir urun tıklanır")
    public void clickRandomlyFromList(String by){
        int getListSize;
        getListSize = base.getList(foundActivity(MapMethodType.IS_ELEMENT, by)).size();
        log.info("liste boyutu" + getListSize);
        if(getListSize == 0){
            bsp.assertFail("Liste boş olarak geldi.");
        }
        try {
            Random rand = new Random();
            int rnd = rand.nextInt(getListSize + 1);
            base.Click(By.xpath(".//ul[@class='catalog-view clearfix products-container']//li[" + rnd + "]//a"));
        }
        catch (Exception e) {
            log.error("ERROR :", e);
            bsp.assertFail("Element Tıklanamadı :" + e.getMessage());
        }
    }



    public String isTextAParameter(String text) {
        String typo = text;
        if (text.startsWith("@")) {
            typo = replaceSQL(text);
            log.info("Typo:" + typo);
        }
        return typo;
    }


    protected String replaceSQL(String sql) {
        String replacedSQL = new String();

        for (Map.Entry<String, String> entry : userVariables.entrySet()) {
            replacedSQL = sql.replace(entry.getKey(), entry.getValue());

        }
        return replacedSQL;
    }
}
