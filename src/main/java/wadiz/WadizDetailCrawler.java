package wadiz;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class WadizDetailCrawler {

    public static void wadiz(String[] urls, String file ,String name) {
        System.setProperty("webdriver.chrome.driver", "src/driver/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(org.openqa.selenium.PageLoadStrategy.EAGER);
        options.addArguments("--headless");

        WebDriver driver = new ChromeDriver(options);

        for (String url : urls) {
            crawler(driver, url, file);
        }

        driver.quit();
    }

    private static void crawler(WebDriver driver, String url, String file) {
        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            Thread.sleep(3000); // 3초 대기
            WebElement reviewTab = wait.until(ExpectedConditions.elementToBeClickable(By.className("SignatureTitle_title__1IkIV SignatureTitle_titleMobile__2pm3w")));
            reviewTab.click();
        } catch (Exception e) {
            System.out.println("리뷰 탭을 클릭하는 중 오류 발생: " + e.getMessage());
        }

        List<String[]> reviewData = new ArrayList<>();
        String productName = extractProductName(wait);

        // 만족도 리뷰 텍스트 추출
        String satisfactionReview = extractSatisfactionReview(wait);
        if (satisfactionReview != null) {
            System.out.println("만족도 리뷰: " + satisfactionReview);
        }

        String safeFileName = makeSafeFileName(productName + ".csv");
        CsvWriter.saveToCSV(reviewData, safeFileName, file);
    }

    // 헬퍼 메서드: 주어진 CSS 선택자를 사용해 텍스트를 추출하는 메서드
    private static String extractText(WebDriverWait wait, String cssSelector, String errorMessage) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cssSelector)));
            return element.getText();
        } catch (Exception e) {
            System.out.println(errorMessage);
            return "";
        }
    }

    private static String extractProductName(WebDriverWait wait) {
        try {
            WebElement productNameElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("p.prd_name")));
            return productNameElement.getText();
        } catch (Exception e) {
            System.out.println("상품명을 추출하는 중 오류 발생: " + e.getMessage());
            return null;
        }
    }

    private static String extractSatisfactionReview(WebDriverWait wait) {
        try {
            WebElement satisfactionReviewElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.SatisfactionTitle_title__3E9lb.SatisfactionTitle_titleMobile__1RqOT")));
            return satisfactionReviewElement.getText();
        } catch (Exception e) {
            System.out.println("만족도 리뷰를 추출하는 중 오류 발생: " + e.getMessage());
            return null;
        }
    }

    private static String[] extractSingleReview(WebDriverWait wait, int reviewIndex, boolean skipSkinType) {
        try {
            WebElement dateElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + reviewIndex + ") > div.review_cont > div.score_area > span.date")));
            WebElement idElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + reviewIndex + ") > div.info > div > p.info_user > a.id")));
            WebElement rateElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + reviewIndex + ") > div.review_cont > div.score_area > span.review_point > span")));
            WebElement txtElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + reviewIndex + ") > div.review_cont > div.txt_inner")));

            return new String[]{
                    dateElement.getText(),
                    rateElement.getText(),
                    idElement.getText(),
                    txtElement.getText()
            };
        } catch (Exception e) {
            System.out.println("리뷰 추출 중 오류 발생: " + e.getMessage());
            return null;
        }
    }

    public static String makeSafeFileName(String fileName) {
        return fileName.replaceAll("[\\\\/:*?\"<>|]", "");
    }
}
