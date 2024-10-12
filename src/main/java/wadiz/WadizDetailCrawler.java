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
        options.addArguments("--headless"); // 브라우저를 숨기고 실행하려면 주석을 해제하세요

        WebDriver driver = new ChromeDriver(options);

        for (String url : urls) {
            crawler(driver, url, file);
        }

        driver.quit();
    }

    private static void crawler(WebDriver driver, String url, String file) {
        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));


        List<String[]> reviewData = new ArrayList<>();

        // 리뷰 항목을 포함하는 최상위 div
        List<WebElement> commentItems = driver.findElements(By.xpath("//div[@id='main-app']//div[contains(@class, 'CommentItem')]"));

        for (WebElement commentItem : commentItems) {
            try {
                // 닉네임 추출
                String nickname = commentItem.findElement(By.xpath(".//a[1]/div/div")).getText();

                // 날짜 추출
                String date = commentItem.findElement(By.xpath(".//a[1]/div/span")).getText();

                // 평점 추출
                String rating = commentItem.findElement(By.xpath(".//div[1]/div[1]/div/div[2]//span")).getText();

                // 리뷰 내용 추출
                String review = commentItem.findElement(By.xpath(".//p")).getText();

                // 추출된 데이터 출력 및 CSV 저장을 위한 리스트에 추가
                System.out.println("닉네임: " + nickname);
                System.out.println("날짜: " + date);
                System.out.println("평점: " + rating);
                System.out.println("리뷰: " + review);

                // 데이터 배열을 리스트에 추가
                reviewData.add(new String[]{nickname, date, rating, review});
            } catch (Exception e) {
                System.out.println("리뷰 정보 추출 중 오류 발생: " + e.getMessage());
            }
        }

        // 안전한 파일명 생성 및 CSV 저장
        String safeFileName = makeSafeFileName(url + ".csv");
        CsvWriter.saveToCSV(reviewData, safeFileName, file);
    }

    public static String makeSafeFileName(String fileName) {
        return fileName.replaceAll("[\\\\/:*?\"<>|]", "");
    }
}
