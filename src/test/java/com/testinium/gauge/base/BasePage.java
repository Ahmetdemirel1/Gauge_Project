package com.testinium.gauge.base;

import org.apache.commons.lang.RandomStringUtils;
import org.openqa.selenium.WebDriver;

import java.util.Random;

public class BasePage extends BasePageUtil {
    public BasePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Desired length random string
     *
     * @param size
     * @return
     */
    protected String createRandomString(int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }

    /**
     * The desirable range random int
     *
     * @param start
     * @param end
     * @return
     */
    protected int createRandomInteger(int start, int end) {
        int randomNumber;
        if (start > end) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        } else {
            Random random = new Random();
            randomNumber = random.nextInt((end - start) + 1) + start;
        }
        return randomNumber;
    }


}
