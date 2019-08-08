package com.testinium.gauge.Util;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Waiters {
    WebDriver waDriver;
    public Waiters(WebDriver driver) {
        waDriver = driver;
    }

    JavascriptExecutor jsDriver = null;

    public void ajaxComplete() {

        jsDriver = (JavascriptExecutor)waDriver;
        jsDriver.executeScript("var callback = arguments[arguments.length - 1];"
                + "var xhr = new XMLHttpRequest();" + "xhr.open('GET', '/Ajax_call', true);"
                + "xhr.onreadystatechange = function() {" + "  if (xhr.readyState == 4) {"
                + "    callback(xhr.responseText);" + "  }" + "};" + "xhr.send();");
    }

    public void pageLoadComplete() {
        jsDriver = (JavascriptExecutor)waDriver;
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {

            public Boolean apply(WebDriver driver) {
                return jsDriver.executeScript("return document.readyState", true).toString().equals("complete");
            }
        };
        try {
            Thread.sleep(1000);
            WebDriverWait wait = new WebDriverWait(waDriver, 200);
            wait.until(expectation);
        } catch (Throwable error) {
        }
    }


    public void jQueryComplete() {

        jsDriver = (JavascriptExecutor)waDriver;
        if (jsDriver.executeScript("return jQuery.active").toString().equals("0")) {
            System.out.println("Page Is loaded.");
            return;
        }

        for (int i = 0; i < 25; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            if (jsDriver.executeScript("return jQuery.active").toString().equals("0")) {
                break;
            }
        }

    }
}
