package coupang;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class WebDriverSetting {

    static JavascriptExecutor js;
//    static WebDriverWait wait;

    public static WebDriver driverSetting() {
        // WebDriver 경로 설정 (본인의 ChromeDriver 경로로 변경 필요)
        System.setProperty("webdriver.chrome.driver", "src/driver/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();

        // 언어설정
        options.addArguments("--header='accept-language=ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7'");
        // Chrome의 Blink 엔진에서 자동화 감지 기능을 비활성화하는 옵션 설정
        options.addArguments("--disable-blink-features=AutomationControlled");
        // 실제 브라우저처럼 보이게 하기 위해 User-Agent 설정
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");

        WebDriver driver = new ChromeDriver(options);
//        wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // 명시적 대기 - 최대 대기시간

        return driver;
    }


    static void afterUrl(WebDriver driver, WebDriverWait wait) {
        js = (JavascriptExecutor) driver;
        js.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");

        wait.until(webDriver -> (JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete");

        //쿠키 및 로컬 스토리지 삭제
        driver.manage().deleteAllCookies();
        js.executeScript("window.localStorage.clear();");
    }

    // 팝업 감지 및 자동 닫기
    public static void handlePopupIfPresent(WebDriver driver) {
        try {
            // 대기 시간 설정 (필요 시 wait 사용 가능)
            WebDriverWait popWait = new WebDriverWait(driver, Duration.ofSeconds(4));
            popWait.until(ExpectedConditions.alertIsPresent());

            // Alert 감지 및 닫기
            Alert alert = driver.switchTo().alert();
            System.out.println("Alert: " + alert.getText());  // 팝업 창 내용
            alert.accept();  // 팝업을 확인(닫기)
            System.out.println("Alert closed successfully.");

        } catch (NoAlertPresentException e) {
            System.out.println("No alert detected.");
        } catch (TimeoutException e) {
            System.out.println("Alert did not appear within the expected time.");
        }
    }

    static void randomTimeSleep() {
        try {
            int randomSleepTime = ThreadLocalRandom.current().nextInt(1000, 4001);
            System.out.println("Sleeping for " + randomSleepTime + " milliseconds");

            Thread.sleep(randomSleepTime);  // 랜덤한 시간 동안 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static String getTextOfElementIfExist(String location, String type, WebDriver driver, WebDriverWait wait) {
        String text = "";
        boolean existLocation = isExistLocation(location, type, driver);
        if (existLocation) {
            text = existTextOfElement(location, type, wait);
        } else {
            text = type + "없음";
        }
        return text;
    }

    static boolean isExistLocation(String location, String type, WebDriver driver) {
        List<WebElement> elements = driver.findElements(By.cssSelector(location));
        if (elements.size() == 1) {
            return true;
        } else {
            if (!(elements.isEmpty())) {
                System.out.println(type + "이 0개나 1개가 아닙니다. 확인요망");
            }
            return false;
        }
    }

    private static String existTextOfElement(String location, String type, WebDriverWait wait) {
        String textOfElement;
        try {
            textOfElement = getTextOfElement(location, wait);
        } catch (Exception e) {
            textOfElement = "No" + type;
        }
        return textOfElement;
    }

    static String getTextOfElement(String Location, WebDriverWait wait) {
        try {
//            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(location))); //가시성도 기다림
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(Location)));
            String text = "\"" + element.getText().replace(",", " ").replace("\n", " ").replace("\r", " ") + "\"";
//            System.out.println("extracted text: " + text);

            return text;
        } catch (Exception e) {
            return "text 없음";
        }
    }

    static String getTextInTagOfElement(String Location, String tagName, WebDriverWait wait) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(Location)));
        String text = element.getAttribute(tagName);
//        System.out.println("extracted text: " + text);
        return text;
    }

}
