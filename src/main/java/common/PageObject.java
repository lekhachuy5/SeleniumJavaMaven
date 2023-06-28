package common;

import org.apache.logging.log4j.LogManager;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;
import org.apache.logging.log4j.core.Logger;
import org.junit.Assert;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class PageObject {
    private static final Logger logger = (Logger)LogManager.getLogger();
    public static String MethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
    public static String ClassName = Thread.currentThread().getStackTrace()[2].getClassName();
    public static WebDriver edriver;
    private static WebDriver jsWaitDriver;
    private static WebDriverWait jsWait;
    private static JavascriptExecutor jsExec;
    public PageObject() {
    }

    public static void setDriver(WebDriver driver) {
        edriver = driver;
        jsWaitDriver = driver;
        jsWait = new WebDriverWait(jsWaitDriver, Duration.ofSeconds(20L));
        jsExec = (JavascriptExecutor)jsWaitDriver;
    }

    public static void waitUntilElementAttributeContainsValue(final WebElement element, final String attribute, final String value) throws Exception {
        waitJQueryAngular();
        int attempt = 0;

        while(attempt < 2) {
            try {
                (new WebDriverWait(edriver, Duration.ofSeconds(20L))).until(new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver d) {
                        return StringUtils.contains(element.getAttribute(attribute), value);
                    }
                });
                break;
            } catch (StaleElementReferenceException var5) {
                ++attempt;
                if (attempt == 2) {
                    throw var5;
                }
            }
        }

    }

    public static void waitUntilElementIsVisible(WebElement element) throws Exception {
        waitJQueryAngular();
        waitWhileBrowserIsBusy();
        int attempt = 0;

        while(attempt < 2) {
            try {
                (new WebDriverWait(edriver, Duration.ofSeconds(20L))).until(ExpectedConditions.visibilityOf(element));
                break;
            } catch (StaleElementReferenceException var3) {
                ++attempt;
                if (attempt == 2) {
                    throw var3;
                }
            }
        }

    }

    public static void waitUntilElementIsVisible(WebElement element, long timeout) throws Exception {
        waitWhileBrowserIsBusy();
        int attempt = 0;

        while(attempt < 2) {
            try {
                (new WebDriverWait(edriver, Duration.ofSeconds(timeout))).until(ExpectedConditions.visibilityOf(element));
                break;
            } catch (StaleElementReferenceException var5) {
                ++attempt;
                if (attempt == 2) {
                    throw var5;
                }
            }
        }

    }

    public static void waitUntilElementIsVisible(String path) throws Exception {
        waitJQueryAngular();
        waitWhileBrowserIsBusy();
        int attempt = 0;

        while(attempt < 2) {
            try {
                WebElement element = edriver.findElement(By.xpath(path));
                (new WebDriverWait(edriver, Duration.ofSeconds(20))).until(ExpectedConditions.visibilityOf(element));
                break;
            } catch (StaleElementReferenceException var3) {
                ++attempt;
                if (attempt == 2) {
                    throw var3;
                }
            }
        }

    }

    public static void waitUntilElementIsInVisible(WebElement element, long timeout) throws Exception {
        waitWhileBrowserIsBusy();

        try {
            (new WebDriverWait(edriver, Duration.ofSeconds(20L))).until(ExpectedConditions.invisibilityOf(element));
        } catch (Exception var4) {
        }

    }

    public static void waitUntilElementIsInVisible(String xpath, long timeout) throws Exception {
        waitJQueryAngular();
        waitWhileBrowserIsBusy();

        try {
            (new WebDriverWait(edriver, Duration.ofSeconds(timeout))).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
        } catch (Exception var4) {
        }

    }

    public static void waitUntilElementIsInVisible(WebElement element) throws Exception {
        waitWhileBrowserIsBusy();

        try {
            (new WebDriverWait(edriver, Duration.ofSeconds(20L))).until(ExpectedConditions.invisibilityOf(element));
        } catch (Exception var2) {
        }

    }

    public static void waitUntilElementIsInVisible(String xpath) throws Exception {
        waitJQueryAngular();
        waitWhileBrowserIsBusy();

        try {
            (new WebDriverWait(edriver, Duration.ofSeconds(20L))).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
        } catch (Exception var2) {
        }

    }

    public static void waitUntilElementIsClickable(WebElement element) throws Exception {
        waitWhileBrowserIsBusy();
        waitJQueryAngular();
        int attempt = 0;

        while(attempt < 2) {
            try {
                (new WebDriverWait(edriver, Duration.ofSeconds(20L))).until(ExpectedConditions.elementToBeClickable(element));
                break;
            } catch (StaleElementReferenceException var3) {
                ++attempt;
                if (attempt == 2) {
                    throw var3;
                }
            }
        }

    }

    public static void waitUntilElementContainsValue(final WebElement element, final String value) throws Exception {
        waitJQueryAngular();
        int attempt = 0;

        while(attempt < 2) {
            try {
                (new WebDriverWait(edriver, Duration.ofSeconds(20L))).until(new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver d) {
                        if (StringUtils.equals(element.getAttribute("value"), value)) {
                            PageObject.getFocusOut(element);
                        }

                        return StringUtils.equals(element.getAttribute("value"), value);
                    }
                });
                break;
            } catch (StaleElementReferenceException var4) {
                ++attempt;
                if (attempt == 2) {
                    throw var4;
                }
            }
        }

    }

    public static void waitUntilElementContainsText(final WebElement element, final String text) throws Exception {
        waitJQueryAngular();
        int attempt = 0;

        while(attempt < 2) {
            try {
                (new WebDriverWait(edriver, Duration.ofSeconds(20L))).until(new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver d) {
                        if (StringUtils.equals(element.getText(), text)) {
                            PageObject.getFocusOut(element);
                        }

                        return StringUtils.equals(element.getText(), text);
                    }
                });
                break;
            } catch (StaleElementReferenceException var4) {
                ++attempt;
                if (attempt == 2) {
                    throw var4;
                }
            }
        }

    }

    public static void waitUntilListContainsValueAtIndex(final List collection, final int index) throws Exception {
        waitJQueryAngular();
        (new WebDriverWait(edriver, Duration.ofSeconds(20L))).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return collection != null && collection.size() > index;
            }
        });
    }

    public static void waitUntilListContainsValueAtIndex(final String xpath, final int index) throws Exception {
        int attempt = 0;

        while(attempt < 2) {
            try {
                waitJQueryAngular();
                (new WebDriverWait(edriver, Duration.ofSeconds(20L))).until(new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver d) {
                        return PageObject.edriver.findElements(By.xpath(xpath)) != null && PageObject.edriver.findElements(By.xpath(xpath)).size() > index;
                    }
                });
                break;
            } catch (StaleElementReferenceException var4) {
                ++attempt;
                if (attempt == 2) {
                    throw var4;
                }
            }
        }

    }

    public static void waitUntilValuesAreEqual(final WebElement ele, final String actualValue, final String expectedValue) throws Exception {
        waitJQueryAngular();
        int attempt = 0;

        while(attempt < 2) {
            try {
                (new WebDriverWait(edriver, Duration.ofSeconds(20L))).until(new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver d) {
                        if (StringUtils.equals(actualValue, expectedValue)) {
                            PageObject.getFocusOut(ele);
                        }

                        return StringUtils.equals(actualValue, expectedValue);
                    }
                });
                break;
            } catch (StaleElementReferenceException var5) {
                ++attempt;
                if (attempt == 2) {
                    throw var5;
                }
            }
        }

    }

    public static void waitUntilElementIsNotEmpty(WebElement element) throws Exception {
        waitJQueryAngular();
        int attempt = 0;

        while(attempt < 2) {
            try {
                (new WebDriverWait(edriver, Duration.ofSeconds(20L))).until(ExpectedConditions.attributeToBeNotEmpty(element, "value"));
                break;
            } catch (StaleElementReferenceException var3) {
                ++attempt;
                if (attempt == 2) {
                    throw var3;
                }
            }
        }

    }

    public static void waitUntilElementIsEmpty(WebElement element) throws Exception {
        waitJQueryAngular();
        int attempt = 0;

        while(attempt < 2) {
            try {
                (new WebDriverWait(edriver, Duration.ofSeconds(20L))).until(ExpectedConditions.attributeToBe(element, "value", ""));
                break;
            } catch (StaleElementReferenceException var3) {
                ++attempt;
                if (attempt == 2) {
                    throw var3;
                }
            }
        }

    }

    public static void waitUntilElementIsDisabled(WebElement element) throws Exception {
        waitJQueryAngular();
        int attempt = 0;

        while(attempt < 2) {
            try {
                (new WebDriverWait(edriver, Duration.ofSeconds(20L))).until(ExpectedConditions.attributeToBe(element, "disabled", "true"));
                break;
            } catch (StaleElementReferenceException var3) {
                ++attempt;
                if (attempt == 2) {
                    throw var3;
                }
            }
        }

    }

    public static void waitUntilSelectDropdownHasValueAtIndex(final Select element, final int index) throws Exception {
        waitJQueryAngular();
        int attempt = 0;

        while(attempt < 2) {
            try {
                (new WebDriverWait(edriver, Duration.ofSeconds(20L))).until(new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver d) {
                        return element.getOptions() != null && element.getOptions().size() > index;
                    }
                });
                break;
            } catch (StaleElementReferenceException var4) {
                ++attempt;
                if (attempt == 2) {
                    throw var4;
                }
            }
        }

    }

    public static void waitWhileBrowserIsBusy() throws Exception {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                JavascriptExecutor executor = (JavascriptExecutor)driver;
                return executor.executeScript("return document.readyState", new Object[0]).equals("complete");
            }
        };
        WebDriverWait wait = new WebDriverWait(edriver, Duration.ofSeconds(20L));

        try {
            wait.until(expectation);
        } catch (Throwable var3) {
            logger.error("Browser did not responded in 20 sec(s)");
        }

    }

    public static void waitWhileAngularAjaxCallsAreActive() {
        Wait<WebDriver> wait = new WebDriverWait(edriver, Duration.ofSeconds(20L));
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                Boolean ajaxInactive = true;
                JavascriptExecutor executor = (JavascriptExecutor)driver;
                if (Boolean.valueOf(executor.executeScript("return (window.angular != undefined)", new Object[0]).toString())) {
                    ajaxInactive = Boolean.valueOf(executor.executeScript("return angular.element('.ng-scope').injector().get('$http').pendingRequests.length == 0", new Object[0]).toString());
                }

                return ajaxInactive;
            }
        });
    }

    public static void getFocusOut(WebElement element) {
        ((JavascriptExecutor)edriver).executeScript("arguments[0].blur();", new Object[]{element});
    }

    public static void focus(WebElement element) throws Exception {
        ((JavascriptExecutor)edriver).executeScript("arguments[0].focus();", new Object[]{element});
    }

    public static void doubleClick(WebElement element) throws Exception {
        waitUntilElementIsClickable(element);
        ((JavascriptExecutor)edriver).executeScript("var evt = document.createEvent('MouseEvents');evt.initMouseEvent('dblclick',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);arguments[0].dispatchEvent(evt);", new Object[]{element});
    }

    public static void doubleClick(String xpath) throws Exception {
        WebElement element = edriver.findElement(By.xpath(xpath));
        waitUntilElementIsClickable(element);
        ((JavascriptExecutor)edriver).executeScript("var evt = document.createEvent('MouseEvents');evt.initMouseEvent('dblclick',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);arguments[0].dispatchEvent(evt);", new Object[]{element});
    }

    public static void focusWindowAndClick(WebElement element) throws Exception {
        try {
            (new WebDriverWait(edriver, Duration.ofSeconds(1L))).until(ExpectedConditions.elementToBeClickable(element));
        } catch (StaleElementReferenceException var2) {
            Thread.sleep(5000L);
        } catch (Exception var3) {
        }

        ((JavascriptExecutor)edriver).executeScript("window.focus();", new Object[0]);
        ((JavascriptExecutor)edriver).executeScript("arguments[0].click();", new Object[]{element});
    }

    public static void scroll() {
        ((JavascriptExecutor)edriver).executeScript("window.scrollBy(0,200)", new Object[0]);
    }

    public static void scrollDown() {
        ((JavascriptExecutor)edriver).executeScript("window.scrollBy(0,250)", new Object[0]);
    }

    public static void scrollToElement(WebElement element) {
        ((JavascriptExecutor)edriver).executeScript("window.scrollBy(0,-100)", new Object[0]);
        ((JavascriptExecutor)edriver).executeScript("arguments[0].scrollIntoView(true);", new Object[]{element});
    }

    public static void waitForJQueryLoad() {
        setDriver(edriver);
        ExpectedCondition<Boolean> jQueryLoad = (driver1) -> {
            return (Long)((JavascriptExecutor)jsWaitDriver).executeScript("return jQuery.active", new Object[0]) == 0L;
        };
        boolean jqueryReady = (Boolean)jsExec.executeScript("return jQuery.active==0", new Object[0]);
        if (!jqueryReady) {
            logger.info("JQuery is NOT Ready!");
            jsWait.until(jQueryLoad);
        }

    }

    public static void waitForAngularLoad() {
        WebDriverWait wait = new WebDriverWait(jsWaitDriver, Duration.ofSeconds(15L));
        JavascriptExecutor jsExec = (JavascriptExecutor)jsWaitDriver;
        String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";
        ExpectedCondition<Boolean> angularLoad = (driver1) -> {
            return Boolean.valueOf(((JavascriptExecutor)edriver).executeScript(angularReadyScript, new Object[0]).toString());
        };
        boolean angularReady = Boolean.valueOf(jsExec.executeScript(angularReadyScript, new Object[0]).toString());
        if (!angularReady) {
            logger.info("ANGULAR is NOT Ready!");
            wait.until(angularLoad);
        }

    }

    public static void waitUntilJSReady() {
        WebDriverWait wait = new WebDriverWait(jsWaitDriver, Duration.ofSeconds(20L));
        JavascriptExecutor jsExec = (JavascriptExecutor)jsWaitDriver;
        ExpectedCondition<Boolean> jsLoad = (driver1) -> {
            return ((JavascriptExecutor)jsWaitDriver).executeScript("return document.readyState", new Object[0]).toString().equals("complete");
        };
        boolean jsReady = Boolean.valueOf(jsExec.executeScript("return document.readyState", new Object[0]).toString().equals("complete"));
        if (!jsReady) {
            logger.info("JS in NOT Ready!");
            wait.until(jsLoad);
        }

    }

    public static void waitUntilJQueryReady() {
        JavascriptExecutor jsExec = (JavascriptExecutor)jsWaitDriver;
        Boolean jQueryDefined = (Boolean)jsExec.executeScript("return typeof jQuery != 'undefined'", new Object[0]);
        if (jQueryDefined) {
            sleep(20);
            waitForJQueryLoad();
            waitUntilJSReady();
            sleep(20);
        }

    }

    public static void waitUntilAngularReady() {
        JavascriptExecutor jsExec = (JavascriptExecutor)jsWaitDriver;
        Boolean angularUnDefined = (Boolean)jsExec.executeScript("return window.angular === undefined", new Object[0]);
        if (!angularUnDefined) {
            Boolean angularInjectorUnDefined = (Boolean)jsExec.executeScript("return angular.element(document).injector() === undefined", new Object[0]);
            if (!angularInjectorUnDefined) {
                sleep(20);
                waitForAngularLoad();
                waitUntilJSReady();
                sleep(20);
            }
        }

    }

    public static void waitJQueryAngular() {
        waitUntilAngularReady();
    }

    public static void sleep(Integer seconds) {
        long secondsLong = (long)seconds;

        try {
            Thread.sleep(secondsLong);
        } catch (InterruptedException var4) {
            var4.printStackTrace();
        }

    }

    public static void switchToNewWindow(int windowNumber) throws InterruptedException {
        Thread.sleep(12000L);
        Set<String> s = edriver.getWindowHandles();
        Iterator<String> ite = s.iterator();

        for(int i = 1; ite.hasNext() && i < 10; ++i) {
            String popupHandle = ((String)ite.next()).toString();
            edriver.switchTo().window(popupHandle);
            edriver.manage().window().maximize();
            edriver.manage().timeouts().implicitlyWait(20L, TimeUnit.SECONDS);
            if (i == windowNumber) {
                break;
            }
        }

    }

    public boolean isElementExist(By locator) throws Exception {
        waitWhileBrowserIsBusy();

        try {
            return edriver.findElement(locator) != null;
        } catch (Exception var3) {
            return false;
        }
    }

    public boolean isElementExist(WebElement ele) throws Exception {
        waitWhileBrowserIsBusy();

        try {
            return ele.isDisplayed();
        } catch (Exception var3) {
            return false;
        }
    }

    public boolean isElementExistWithMinimumImplicitWait(WebElement ele, int timeoutInMilliSeconds) throws Exception {
        waitWhileBrowserIsBusy();
        this.turnOffImplicitWait((long)timeoutInMilliSeconds);

        try {
            waitUntilElementIsVisible(ele, TimeUnit.MILLISECONDS.toSeconds((long)timeoutInMilliSeconds));
            if (ele.isDisplayed()) {
                this.turnOnImplicitWait();
                return true;
            } else {
                this.turnOnImplicitWait();
                return false;
            }
        } catch (Exception var4) {
            this.turnOnImplicitWait();
            return false;
        }
    }

    public boolean isElementExistWithMinimumImplicitWait(String xpath, int timeoutInMilliSeconds) throws Exception {
        waitWhileBrowserIsBusy();
        this.turnOffImplicitWait((long)timeoutInMilliSeconds);

        try {
            if (edriver.findElement(By.xpath(xpath)) != null) {
                this.turnOnImplicitWait();
                return true;
            } else {
                this.turnOnImplicitWait();
                return false;
            }
        } catch (Exception var4) {
            this.turnOnImplicitWait();
            return false;
        }
    }

    public void waitUntilElementIsInVisibleWithMinimumImplicitWait(WebElement ele, int timeoutInMilliSeconds) throws Exception {
        waitWhileBrowserIsBusy();
        this.turnOffImplicitWait((long)timeoutInMilliSeconds);

        try {
            waitUntilElementIsInVisible(ele, TimeUnit.MILLISECONDS.toSeconds((long)timeoutInMilliSeconds));
            this.turnOnImplicitWait();
        } catch (Exception var4) {
            this.turnOnImplicitWait();
        }

    }

    public void waitUntilElementIsInVisibleWithMinimumImplicitWait(String xpath, int timeoutInMilliSeconds) throws Exception {
        waitWhileBrowserIsBusy();
        this.turnOffImplicitWait((long)timeoutInMilliSeconds);

        try {
            waitUntilElementIsInVisible(xpath, TimeUnit.MILLISECONDS.toSeconds((long)timeoutInMilliSeconds));
            this.turnOnImplicitWait();
        } catch (Exception var4) {
            this.turnOnImplicitWait();
        }

    }

    public boolean isElementEnabled(WebElement ele) throws Exception {
        waitWhileAngularAjaxCallsAreActive();
        waitUntilJQueryReady();
        waitWhileBrowserIsBusy();

        try {
            return ele.isEnabled();
        } catch (NoSuchElementException var3) {
            return false;
        }
    }

    protected void switchToFrame(By locator) throws Exception {
        try {
            edriver.switchTo().defaultContent();
            WebElement iFrame = (WebElement)(new WebDriverWait(edriver, Duration.ofSeconds(20L))).until(ExpectedConditions.visibilityOfElementLocated(locator));
            edriver.switchTo().frame(iFrame);
        } catch (Exception var3) {
            logger.error("frame not found, searched by By [" + locator.toString() + "]", var3);
            throw var3;
        }
    }

    public void compareInputAndUIValue(String fieldName, WebElement ele, String expectedValue) throws Exception {
        if (StringUtils.isNotEmpty(expectedValue) && this.isElementExistWithMinimumImplicitWait((WebElement)ele, 1000)) {
            Assert.assertEquals(fieldName + " mismatched - inputValue: " + expectedValue + " and UI value: " + ele.getAttribute("value"), expectedValue, ele.getAttribute("value"));
        }

    }

    public void compareIfUIElementIsSelected(String fieldName, WebElement ele, String inputValue) throws Exception {
        if (StringUtils.isNotEmpty(inputValue) && this.isElementExistWithMinimumImplicitWait((WebElement)ele, 1000)) {
            Assert.assertTrue(fieldName + " is not selected - UI value: " + ele.isSelected(), ele.isSelected());
        }

    }

    public void compareInputAndUISelectedValue(String fieldName, WebElement ele, String inputValue) throws Exception {
        if (StringUtils.isNotEmpty(inputValue) && this.isElementExistWithMinimumImplicitWait((WebElement)ele, 1000)) {
            Assert.assertEquals(fieldName + " mismatched - inputValue: " + inputValue + " and UI value: " + (new Select(ele)).getFirstSelectedOption().getText().trim(), inputValue, (new Select(ele)).getFirstSelectedOption().getText().trim());
        }

    }
    public void focusClick(WebElement element) throws Exception {
        try {
            (new WebDriverWait(edriver, Duration.ofSeconds(20L))).until(ExpectedConditions.elementToBeClickable(element));
        } catch (StaleElementReferenceException var3) {
            Thread.sleep(5000L);
        } catch (Exception var4) {
        }

        waitUntilElementIsVisible(element);
        scrollToElement(element);
        ((JavascriptExecutor)edriver).executeScript("arguments[0].focus();", new Object[]{element});
        waitUntilElementIsClickable(element);
        waitWhileBrowserIsBusy();
        ((JavascriptExecutor)edriver).executeScript("arguments[0].click();", new Object[]{element});
    }

    public void focusDoubleClick(WebElement element) throws Exception {
        try {
            (new WebDriverWait(edriver, Duration.ofSeconds(20L))).until(ExpectedConditions.elementToBeClickable(element));
        } catch (StaleElementReferenceException var3) {
            Thread.sleep(5000L);
        } catch (Exception var4) {
        }

        waitWhileBrowserIsBusy();
        waitUntilElementIsVisible(element);
        focus(element);
        doubleClick(element);
    }

    public void focusDoubleClick(String xpath) throws Exception {
        WebElement element = edriver.findElement(By.xpath(xpath));
        this.focusDoubleClick(element);
    }

    public void focusClick(String xpath) throws Exception {
        WebElement element = edriver.findElement(By.xpath(xpath));
        scrollToElement(element);
        this.focusClick(element);
    }

    public void click(WebElement element) throws Exception {
        waitWhileAngularAjaxCallsAreActive();
        waitJQueryAngular();

        try {
            (new WebDriverWait(edriver, Duration.ofSeconds(20L))).until(ExpectedConditions.elementToBeClickable(element));
        } catch (StaleElementReferenceException var3) {
            Thread.sleep(5000L);
            PageFactory.initElements(edriver, this);
        } catch (Exception var4) {
        }

        waitUntilElementIsVisible(element);
        waitUntilElementIsClickable(element);
        scrollToElement(element);
        element.click();
        waitWhileBrowserIsBusy();
    }

    public void click(String xpath) throws Exception {
        WebElement element = edriver.findElement(By.xpath(xpath));
        this.click(element);
    }

    public void sendKeys(WebElement element, String text) throws Exception {
        this.focusClick(element);
        element.clear();
        if (StringUtils.isNotEmpty(element.getAttribute("value"))) {
            for(int i = element.getAttribute("value").length(); i > 0; --i) {
                element.sendKeys(new CharSequence[]{Keys.BACK_SPACE});
            }
        }

        waitUntilElementIsEmpty(element);
        element.sendKeys(new CharSequence[]{text});
    }

    public void sendKeys(By by, String text) throws Exception {
        WebElement element = edriver.findElement(by);
        this.focusClick(element);
        element.clear();
        element = edriver.findElement(by);
        if (StringUtils.isNotEmpty(element.getAttribute("value"))) {
            for(int i = element.getAttribute("value").length(); i > 0; --i) {
                element.sendKeys(new CharSequence[]{Keys.BACK_SPACE});
            }
        }

        waitUntilElementIsEmpty(element);
        element.sendKeys(new CharSequence[]{text});
    }

    public void sendKeys(String xpath, String text) throws Exception {
        WebElement element = edriver.findElement(By.xpath(xpath));
        this.sendKeys(element, text);
    }

    public void sendKeysBy(String xpath, String text) throws Exception {
        this.sendKeys(By.xpath(xpath), text);
    }

    public void enterTextUsingJavaScript(WebElement element, String text) throws Exception {
        ((JavascriptExecutor)edriver).executeScript("arguments[0].setAttribute('value',arguments[1]);", new Object[]{element, text});
    }

    public void selectByValue(WebElement element, String value) throws Exception {
        this.focusClick(element);
        Select select = new Select(element);
        select.selectByValue(value);
    }

    public void selectByValue(String xpath, String value) throws Exception {
        WebElement element = edriver.findElement(By.xpath(xpath));
        this.selectByValue(element, value);
    }

    public void selectByVisibleText(WebElement element, String text) throws Exception {
        this.focusClick(element);
        Select select = new Select(element);
        select.selectByVisibleText(text);
    }

    public void selectByVisibleText(String xpath, String text) throws Exception {
        WebElement element = edriver.findElement(By.xpath(xpath));
        this.selectByVisibleText(element, text);
    }

    public void selectByIndex(WebElement element, int index) throws Exception {
        this.focusClick(element);
        Select select = new Select(element);
        waitUntilSelectDropdownHasValueAtIndex(select, index);
        select.selectByIndex(index);
    }

    public String getText(WebElement element) throws Exception {
        try {
            return element.getText();
        } catch (Exception var3) {
            return "";
        }
    }

    public String getText(String xpath) throws Exception {
        WebElement element = edriver.findElement(By.xpath(xpath));
        return this.getText(element);
    }

    public List<WebElement> getElements(String xpath) throws Exception {
        int attempt = 0;

        while(attempt < 2) {
            try {
                if (edriver.findElements(By.xpath(xpath)) != null) {
                    ((WebElement)edriver.findElements(By.xpath(xpath)).get(0)).getTagName();
                }

                return edriver.findElements(By.xpath(xpath));
            } catch (StaleElementReferenceException var4) {
                ++attempt;
            }
        }

        return null;
    }

    public WebElement getElement(String xpath) throws Exception {
        int attempt = 0;

        while(attempt < 2) {
            try {
                edriver.findElement(By.xpath(xpath)).getTagName();
                return edriver.findElement(By.xpath(xpath));
            } catch (StaleElementReferenceException var4) {
                ++attempt;
            }
        }

        return null;
    }

    public void navigateToNextPage() throws Exception {
        (new WebDriverWait(edriver, Duration.ofSeconds(40L))).until((edriver) -> {
            return edriver.findElement(By.xpath("//button[contains(@id,'navigation_button')]")).isDisplayed();
        });
        (new WebDriverWait(edriver, Duration.ofSeconds(40L))).until((edriver) -> {
            return edriver.findElement(By.xpath("//button[contains(@id,'navigation_button')]")).isEnabled();
        });
        this.focusClick(edriver.findElement(By.xpath("//button[contains(@id,'navigation_button')]")));
    }

    public void turnOffImplicitWait(long timeout) throws Exception {
        edriver.manage().timeouts().implicitlyWait(timeout, TimeUnit.MILLISECONDS);
    }

    public void turnOnImplicitWait() throws Exception {
        edriver.manage().timeouts().implicitlyWait(15L, TimeUnit.SECONDS);
    }

    public String retrieveElementValueBasedOnType(WebElement webElement) throws Exception {
        if (StringUtils.contains(webElement.getAttribute("type"), "checkbox")) {
            return webElement.isSelected() ? "Y" : "N";
        } else if (StringUtils.contains(webElement.getAttribute("type"), "select")) {
            return (new Select(webElement)).getFirstSelectedOption().getText().trim();
        } else {
            return !StringUtils.contains(webElement.getAttribute("type"), "text") && !StringUtils.contains(webElement.getTagName(), "input") ? this.getText(webElement) : webElement.getAttribute("value");
        }
    }

    public String retrieveGlyphiconValue(WebElement webElement) throws Exception {
        if (StringUtils.contains(webElement.getAttribute("class"), "glyphicon")) {
            return StringUtils.contains(webElement.getAttribute("class"), "glyphicon-remove") ? "N" : "Y";
        } else {
            return null;
        }
    }

    public String retrieveElementValueBasedOnType(String xpath) throws Exception {
        return this.retrieveElementValueBasedOnType(edriver.findElement(By.xpath(xpath)));
    }

    public void enterNonEmptyValue(WebElement webElement, String value) throws Exception {
        if (StringUtils.isNotEmpty(value)) {
            this.sendKeys(webElement, value);
        }

    }

    public void enterNonEmptyValue(String xpath, String value) throws Exception {
        if (StringUtils.isNotEmpty(value)) {
            this.sendKeys(edriver.findElement(By.xpath(xpath)), value);
        }

    }

    public void enterNonEmptyValueUsingBy(String xpath, String value) throws Exception {
        if (StringUtils.isNotEmpty(value)) {
            this.sendKeysBy(xpath, value);
        }

    }

    public void enterValue(WebElement webElement, String value) throws Exception {
        if (StringUtils.isNotEmpty(value)) {
            this.sendKeys(webElement, value);
        } else if (StringUtils.isEmpty(value)) {
            this.sendKeys(webElement, "");
        }

    }

    public void enterValue(String xpath, String value) throws Exception {
        this.enterValue(this.getElement(xpath), value);
    }

    public void selectNonEmptyValueByVisibleText(WebElement webElement, String text) throws Exception {
        if (StringUtils.isNotEmpty(text)) {
            this.selectByVisibleText(webElement, text);
        }

    }

    public void selectNonEmptyValueByVisibleText(String xpath, String text) throws Exception {
        this.selectNonEmptyValueByVisibleText(this.getElement(xpath), text);
    }

    public void selectNonEmptyValueByValue(WebElement webElement, String text) throws Exception {
        if (StringUtils.isNotEmpty(text)) {
            this.selectByValue(webElement, text);
        }

    }

    public void selectNonEmptyValueByValue(String xpath, String text) throws Exception {
        if (StringUtils.isNotEmpty(text)) {
            this.selectByValue(this.getElement(xpath), text);
        }

    }

    public void clickCheckBox(WebElement webElement, String text) throws Exception {
        if ((StringUtils.equalsIgnoreCase(text, "True") || StringUtils.equalsIgnoreCase(text, "Y") || StringUtils.equalsIgnoreCase(text, "Yes")) && !webElement.isSelected()) {
            this.click(webElement);
        } else if ((StringUtils.equalsIgnoreCase(text, "false") || StringUtils.equalsIgnoreCase(text, "N") || StringUtils.equalsIgnoreCase(text, "No")) && webElement.isSelected()) {
            this.click(webElement);
        }

    }

    public void clickCheckBox(String xpath, String text) throws Exception {
        this.clickCheckBox(edriver.findElement(By.xpath(xpath)), text);
    }

    public void selectRadioOption(WebElement webElement, String expected, String actual) throws Exception {
        if (StringUtils.equalsIgnoreCase(expected, actual)) {
            webElement.click();
        }

    }

    public void clickIfValueIsNotEmpty(WebElement webElement, String text) throws Exception {
        if (StringUtils.isNotEmpty(text)) {
            this.click(webElement);
        }

    }

    public void clickIfValueIsNotEmpty(String xpath, String text) throws Exception {
        if (StringUtils.isNotEmpty(text)) {
            this.click(edriver.findElement(By.xpath(xpath)));
        }

    }

    public void clickIfValueEquals(WebElement webElement, String expected, String actual) throws Exception {
        if (StringUtils.equals(expected, actual)) {
            this.click(webElement);
        }

    }

    public void clickIfValueEquals(String xpath, String expected, String actual) throws Exception {
        if (StringUtils.equals(expected, actual)) {
            this.click(edriver.findElement(By.xpath(xpath)));
        }

    }

    public void selectByValueForReact(String xpath, String value) throws Exception {
        this.selectByValueForReact(this.getElement(xpath), value);
    }

    public void selectByValueForReact(WebElement element, String value) throws Exception {
        String xpath = "./ancestor-or-self::div[contains(@class,'SelectControl')]/descendant::div[contains(@class,'gel__menu')]/descendant::div[@data-value='<value>']";
        String gelIndicatorXpath = "./ancestor-or-self::div[contains(@class,'SelectControl')]/descendant::div[contains(@class,'gel__indicators')]";

        try {
            this.click(element);
        } catch (ElementClickInterceptedException var6) {
            this.click(element.findElement(By.xpath(gelIndicatorXpath)));
        }

        WebElement dropDownValue = element.findElement(By.xpath(xpath.replace("<value>", value)));
        this.click(dropDownValue);
    }
}