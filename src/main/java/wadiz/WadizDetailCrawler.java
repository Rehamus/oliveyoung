package wadiz;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

    public static void wadiz(String[] urls, String file, String name) {

        for (String url : urls) {
            crawler(url, file, name);
        }

    }

    private static void crawler(String url, String file, String name) {

        System.setProperty("webdriver.chrome.driver", "src/driver/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
//        options.setPageLoadStrategy(org.openqa.selenium.PageLoadStrategy.EAGER);
//        options.addArguments("--headless"); // 브라우저를 숨기고 실행하려면 주석을 해제하세요

        WebDriver driver = new ChromeDriver(options);


        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        scrollToEndOfPage(driver);

        List<String[]> reviewData = new ArrayList<>();
        String baseXPath = "/html/body/div[1]/main/div/div/div/div/div/div/div[1]/div[1]/div[";

        int i = 1;

        while (true) {
            try {
                // 동적으로 XPath를 생성하여 각 div 요소를 찾기
                String dynamicXPath = baseXPath + i + "]";
                WebElement commentItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath)));

                System.out.printf("요소 %d번\n", i);

                // 닉네임 추출 (태그 구조와 위치를 기준으로 접근)
                WebElement nicknameElement = commentItem.findElement(By.xpath(".//div/div[1]/a/div/div"));
                String nickname = nicknameElement.getText();

                // 리뷰 내용 추출 (태그 구조와 위치를 기준으로 접근)
                WebElement reviewElement = commentItem.findElement(By.xpath(".//div[contains(@class, 'CommentContent_contentWrapper')]/p"));
                String review = reviewElement.getText();

                System.out.println("닉네임: " + nickname);
                System.out.println("리뷰: " + review);

                // 데이터 배열을 리스트에 추가
                reviewData.add(new String[]{nickname, review});

                i++; // 다음 요소로 이동
            } catch (Exception e) {
                System.out.println("더 이상 요소가 없습니다. 종료합니다.");
                break;
            }
        }

        // 안전한 파일명 생성 및 CSV 저장
        CsvWriter.saveToCSV(reviewData, name, file);
        driver.quit();

    }

    public static String makeSafeFileName(String fileName) {
        return fileName.replaceAll("[\\\\/:*?\"<>|]", "");
    }

    public static void scrollToEndOfPage(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        int scrollAttempts = 0;
        long lastHeight = (long) js.executeScript("return document.body.scrollHeight");

        while (true) {
            // 페이지 끝으로 스크롤
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

            // 3초 대기
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 새로운 높이 가져오기
            long newHeight = (long) js.executeScript("return document.body.scrollHeight");

            // 스크롤할 수 없는 경우 (높이에 변화가 없을 경우)
            if (newHeight == lastHeight) {
                scrollAttempts++;
                System.out.println("더 이상 스크롤할 수 없음. 현재 시도 횟수: " + scrollAttempts);

                // 3번 반복했으면 종료
                if (scrollAttempts >= 3) {
                    System.out.println("페이지의 끝까지 3번 스크롤 시도 완료. 종료합니다.");
                    break;
                }

                // 페이지 맨 위로 이동
                js.executeScript("window.scrollTo(0, 0);");

                // 3초 대기 후 다시 시도
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                // 높이에 변화가 있을 경우 스크롤 시도 횟수 초기화
                scrollAttempts = 0;
                lastHeight = newHeight;
            }
        }
    }
}
