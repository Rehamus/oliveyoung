import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class OliveYoungCrawler {
    public static void main(String[] args) {
        // WebDriver 경로 설정 (본인의 ChromeDriver 경로로 변경 필요)
        System.setProperty("webdriver.chrome.driver", "src/driver/chromedriver.exe");

/*        // Chrome 옵션 설정 (브라우저 창을 숨길 수 있음)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // 브라우저를 숨기고 실행할 때*/

        WebDriver driver = new ChromeDriver(/*options*/);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // 명시적 대기

        // 접속할 URL
        String url = "https://www.oliveyoung.co.kr/store/goods/getGoodsDetail.do?goodsNo=A000000188933&dispCatNo=10000010001&trackingCd=Cat10000010001_Prodrank&t_page=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EA%B4%80&t_click=%EB%9E%AD%ED%82%B9BEST%EC%83%81%ED%92%88%EB%B8%8C%EB%9E%9C%EB%93%9C_%EC%83%81%ED%92%88%EC%83%81%EC%84%B8&t_number=1";
        driver.get(url);

        // 10초 대기 후 리뷰 탭 클릭
        try {
            Thread.sleep(5000); // 10초 대기
            WebElement reviewTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("reviewInfo")));
            reviewTab.click(); // 리뷰 탭 클릭
        } catch (Exception e) {
            System.out.println("리뷰 탭을 클릭하는 중 오류 발생: " + e.getMessage());
        }

        // 데이터 저장할 리스트
        List<String[]> reviewData = new ArrayList<>();

        // 리뷰 크롤링 함수
        int totalPages = 10; // 예시로 10페이지만 크롤링
        for (int currentPage = 1; currentPage <= totalPages; currentPage++) {
            for (int i = 1; i <= 10; i++) {  // 한 페이지에서 최대 10개의 리뷰 크롤링
                try {
                    // 명시적 대기를 사용하여 요소가 존재할 때까지 대기
                    WebElement dateElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + i + ") > div.review_cont > div.score_area > span.date")));
                    WebElement rateElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + i + ") > div.review_cont > div.score_area > span.review_point > span")));
                    WebElement idElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + i + ") > div.info > div > p.info_user > a.id")));
                    WebElement skinTypeElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + i + ") > div.review_cont > div.poll_sample > dl:nth-child(1) > dd > span")));
                    WebElement select1TitleElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + i + ") > div.review_cont > div.poll_sample > dl:nth-child(1) > dt > span")));
                    WebElement select1ContentElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + i + ") > div.review_cont > div.poll_sample > dl:nth-child(1) > dd > span")));
                    WebElement select2TitleElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + i + ") > div.review_cont > div.poll_sample > dl:nth-child(2) > dt > span")));
                    WebElement select2ContentElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + i + ") > div.review_cont > div.poll_sample > dl:nth-child(2) > dd > span")));
                    WebElement txtElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + i + ") > div.review_cont > div.txt_inner")));

                    // 각 요소에서 텍스트를 추출
                    String date = dateElement.getText();
                    String rate = rateElement.getText();
                    String id = idElement.getText();
                    String skinType = skinTypeElement.getText();
                    String select1Title = select1TitleElement.getText();
                    String select1Content = select1ContentElement.getText();
                    String select2Title = select2TitleElement.getText();
                    String select2Content = select2ContentElement.getText();
                    String txt = txtElement.getText();

                    // 크롤링된 데이터를 리스트에 저장
                    reviewData.add(new String[]{date, rate, id, skinType, select1Title, select1Content, select2Title, select2Content, txt});
                } catch (Exception e) {
                    // 에러 발생 시 계속 진행
                    System.out.println("Error while extracting review: " + e.getMessage());
                }
            }

            try {
                // 페이지 넘김 처리 (현재 페이지가 10의 배수가 아닐 때)
                if (currentPage % 10 != 0) {
                    WebElement nextPageButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#gdasContentsArea > div > div.pageing > a:nth-child(" + (currentPage % 10 + 1) + ")")));
                    nextPageButton.click();
                } else { // 현재 페이지가 10의 배수일 때
                    WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#gdasContentsArea > div > div.pageing > a.next")));
                    nextButton.click();
                }
                // 페이지 넘김 후 기다리기
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println("Error while navigating pages: " + e.getMessage());
            }

            System.out.println(currentPage + " 페이지 크롤링 완료");
        }

        // WebDriver 종료
        driver.quit();

        // 결과 출력
        for (String[] review : reviewData) {
            System.out.println(String.join(", ", review));
        }

        // 데이터를 CSV 파일로 저장
        CsvWriter.saveToCSV(reviewData, "reviews.csv");
    }
}
