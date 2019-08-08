package com.testinium.gauge.Util;

import com.testinium.gauge.base.BaseTest;
import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Browser {
    protected final Logger logger = Logger.getLogger(Browser.class);
    private DesiredCapabilities capabilities;


    public void createLocalDriver(String url){
        // setSetUrl("URL");
        ChromeOptions option = new ChromeOptions();
        capabilities = DesiredCapabilities.chrome();
        option.addArguments("test-type");
        option.addArguments("disable-popup-blocking");
        option.addArguments("ignore-certificate-errors");
        option.addArguments("disable-translate");
        option.addArguments("--start-maximized");
        option.addArguments("disable-automatic-password-saving");
        option.addArguments("allow-silent-push");
        option.addArguments("disable-infobars");
		option.addArguments("--kiosk");
        capabilities.setCapability(ChromeOptions.CAPABILITY, option);
        capabilities.setBrowserName("chrome");
        capabilities.setPlatform(Platform.getCurrent());
        selectPath(capabilities.getPlatform());
        BaseTest.setDriver(new ChromeDriver(capabilities));
        BaseTest.getDriver().get(url);
    }

    public WebDriver getDriver() {
        return BaseTest.getDriver();
    }

    protected void selectPath( Platform platform ) {
        String browser;
        if ("CHROME".equalsIgnoreCase(capabilities.getBrowserName())) {
            browser = "webdriver.chrome.driver";
            switch (platform) {
                case MAC:
                    System.setProperty(browser, "properties/driver/chromedrivermac");
                    break;
                case WIN10:
                case WIN8:
                case WIN8_1:
                case WINDOWS:
                    System.setProperty(browser, "properties/driver/chromedriverwin.exe");
                    break;
                case LINUX:
                    System.setProperty(browser, "properties/driver/chromedriverlinux64.exe");
                    break;
                default:
                    logger.info("PLATFORM DOES NOT EXISTS");
                    break;
            }
        }
    }
}
