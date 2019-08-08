package com.testinium.gauge.mapping;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

import static org.junit.Assert.fail;

public class Mapper {

    private static final String BASE_PATH = "mapJSON" + "/";
    private static final String BASE_EXTENSION = ".json";
    protected static final Logger log = Logger.getLogger(Mapper.class);

    /**
     * Read json file and map
     *
     * @param
     * @return Json mapped hash map
     * @throws IOException
     */
    private static JsonObject readJSON(MapMethodType eventActivity, String elementFound) {
        Gson gson = new Gson();
        JsonElement jsonObject = null;
        FileReader reader = null;
        JsonObject jsonElement = null;
        JsonObject foundElement = null;
        foundElement = null;
        log.info("******* BASE PATH :" + BASE_PATH);
        try {
            log.info("EVENT ACTIVITY TYPE :" + eventActivity.getValue());
            reader = new FileReader(BASE_PATH + eventActivity.getValue() + BASE_EXTENSION);
            jsonObject = gson.fromJson(reader, JsonElement.class);
            jsonElement = jsonObject.getAsJsonObject();
            foundElement = jsonElement.get(clearTurkishCharsAndUpperCase(elementFound)).getAsJsonObject();
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        log.info("******* Map :" + foundElement);
        return foundElement;
    }

    /**
     * Reads map value from json and clear turkish chars
     *
     * @param elementFound Sentence with turkish chars
     * @return Map value
     * @throws IOException
     */
    public static By foundActivity(MapMethodType eventActivity, String elementFound) {
        Set<Entry<String, JsonElement>> entries = null;
        try {
            entries = readJSON(eventActivity, elementFound).entrySet();
        } catch (NullPointerException e) {
            log.error(elementFound + "is not found in JSON file");
            fail(elementFound + "is not found in JSON file");
        }
        By by = null;
        for (Entry<String, JsonElement> entry : entries) {
            log.info("BY GENERATE :" + entry.getKey() + "-" + entry.getValue());
            by = generateByElement(entry.getKey(), entry.getValue().getAsString());
        }
        log.info("BY :" + by);
        return by;
    }

    /**
     * @param byType
     * @param byValue
     * @return
     */
    static By generateByElement(String byType, String byValue) {
        By byElement = null;
        if (byType.contains(MapValue.ID.getText())) {
            byElement = By.id(byValue);
        } else if (byType.contains(MapValue.CLASSNAME.getText())) {
            byElement = By.className(byValue);
        } else if (byType.contains(MapValue.CSSSELECTOR.getText())) {
            byElement = By.cssSelector(byValue);
        } else if (byType.contains(MapValue.XPATH.getText())) {
            byElement = By.xpath(byValue);
        } else if (byType.contains(MapValue.LINKTEXT.getText())) {
            byElement = By.linkText(byValue);
        } else if (byType.contains(MapValue.CONTAINS.getText())) {
            byElement = By.xpath("//*[contains(@id, '" + byValue + "')]");
        } else {
            fail("No such selector.");
        }
        return byElement;
    }

    /**
     * Clear turkish chars And string to upper case
     *
     * @param str contains turkish chars
     * @return cleared turkish chars
     */
    private static String clearTurkishCharsAndUpperCase(String str) {
        String returnStr = str;
        char[] turkishChars = new char[]{0x131, 0x130, 0xFC, 0xDC, 0xF6, 0xD6, 0x15F, 0x15E, 0xE7, 0xC7, 0x11F,
                0x11E};
        char[] englishChars = new char[]{'i', 'I', 'u', 'U', 'o', 'O', 's', 'S', 'c', 'C', 'g', 'G'};
        for (int i = 0; i < turkishChars.length; i++) {
            returnStr = returnStr.replaceAll(new String(new char[]{turkishChars[i]}),
                    new String(new char[]{englishChars[i]}));
        }
        return returnStr.toUpperCase(Locale.ENGLISH);
    }
}
