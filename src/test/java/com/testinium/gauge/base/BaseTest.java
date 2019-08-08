package com.testinium.gauge.base;

import com.testinium.gauge.Util.Browser;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;


public class BaseTest {

    public static WebDriver driver;
    public static final Logger log = Logger.getLogger(BaseTest.class);
    public  DesiredCapabilities capabilities;
    protected static Browser browser = new Browser();


    public static void setBrowser(String url) {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        PropertyConfigurator.configure("properties/log4j.properties");


        String key = null;
        if(StringUtils.isNotEmpty(key)){ // Testinium ayarlari
            log.info("Key info:" + key);

            capabilities.setCapability("key", key);
            try{
                capabilities.setCapability("takesScreenshot", true);
                capabilities.setPlatform(Platform.MAC);
                setDriver(new RemoteWebDriver(new URL("http://hub.testinium.io/wd/hub"), capabilities));
                BaseTest.getDriver().get(url);
            }
            catch (MalformedURLException e) {
                log.error(e.getMessage());
            }

        }
        else {//local driver
            browser.createLocalDriver(url);

        }
    }


    public static void tearDown(){
        BaseTest.getDriver().quit();
    }

    public static WebDriver getDriver(){
        return driver;
    }

    public static void setDriver(RemoteWebDriver driver){
        BaseTest.driver = driver;
    }


}
