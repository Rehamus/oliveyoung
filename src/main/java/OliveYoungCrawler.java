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

public class OliveYoungCrawler {

    public static void main(String[] args) {
        // URL
        String[] urls = {
                "https://www.oliveyoung.co.kr/store/goods/getGoodsDetail.do?goodsNo=A000000209348&t_page=%ED%86%B5%ED%95%A9%EA%B2%80%EC%83%89%EA%B2%B0%EA%B3%BC%ED%8E%98%EC%9D%B4%EC%A7%80&t_click=%EA%B2%80%EC%83%89%EC%83%81%ED%92%88%EC%83%81%EC%84%B8&t_search_name=%EB%A9%94%EC%9D%B4%ED%81%AC%ED%9E%90&t_number=2&dispCatNo=1000001000600010005&trackingCd=Result_2",
                "https://www.oliveyoung.co.kr/store/goods/getGoodsDetail.do?goodsNo=A000000210341&t_page=%ED%86%B5%ED%95%A9%EA%B2%80%EC%83%89%EA%B2%B0%EA%B3%BC%ED%8E%98%EC%9D%B4%EC%A7%80&t_click=%EA%B2%80%EC%83%89%EC%83%81%ED%92%88%EC%83%81%EC%84%B8&t_search_name=%EB%A9%94%EC%9D%B4%ED%81%AC%ED%9E%90&t_number=3&dispCatNo=1000001000600010005&trackingCd=Result_3",
                "https://www.oliveyoung.co.kr/store/goods/getGoodsDetail.do?goodsNo=A000000209358&t_page=%ED%86%B5%ED%95%A9%EA%B2%80%EC%83%89%EA%B2%B0%EA%B3%BC%ED%8E%98%EC%9D%B4%EC%A7%80&t_click=%EA%B2%80%EC%83%89%EC%83%81%ED%92%88%EC%83%81%EC%84%B8&t_search_name=%EB%A9%94%EC%9D%B4%ED%81%AC%ED%9E%90&t_number=5&dispCatNo=1000001000600010005&trackingCd=Result_5"
        };

        olive(urls);
    }


    private static void olive(String[] urls) {
        // WebDriver 경로 설정 (본인의 ChromeDriver 경로로 변경 필요)
        System.setProperty("webdriver.chrome.driver", "src/driver/chromedriver.exe");

/*        // Chrome 옵션 설정 (브라우저 창을 숨길 수 있음)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // 브라우저를 숨기고 실행할 때*/

        // Chrome 옵션 설정 (브라우저가 완전히 로드될 때까지 기다리지 않도록 설정)
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(org.openqa.selenium.PageLoadStrategy.EAGER);  // 페이지의 중요한 부분이 로드되면 바로 작업을 시작함

        WebDriver driver = new ChromeDriver(options);


        // URL 순차적으로 크롤링
        for (String url : urls) {
            crawler(driver, url);
        }

        // WebDriver 종료
        driver.quit();
    }

    private static void crawler(WebDriver driver, String url) {
        driver.get(url);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // 명시적 대기


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

        String productName = null;

        try {
            // p.prd_name 요소가 나타날 때까지 기다림
            WebElement productNameElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("p.prd_name")));

            // 텍스트 추출
            productName = productNameElement.getText();
            System.out.println("상품명: " + productName);
        } catch (Exception e) {
            System.out.println("상품명을 추출하는 중 오류 발생: " + e.getMessage());
        }

        // 리뷰 크롤링 함수
        int totalPages = 5; // 예시로 5페이지 크롤링
        for (int currentPage = 1; currentPage <= totalPages; currentPage++) {
            for (int i = 1; i <= 10; i++) {  // 한 페이지에서 최대 10개의 리뷰 크롤링
                try {
                    // 각 요소를 찾는 동안 예외가 발생하면 빈 값으로 처리
                    String skinType = "", select1Title = "", select1Content = "", select2Title = "", select2Content = "";

                    WebElement dateElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + i + ") > div.review_cont > div.score_area > span.date")));
                    WebElement idElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + i + ") > div.info > div > p.info_user > a.id")));
                    WebElement rateElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + i + ") > div.review_cont > div.score_area > span.review_point > span")));
                    WebElement txtElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + i + ") > div.review_cont > div.txt_inner")));

                    String date = dateElement.getText();
                    String id = idElement.getText();
                    String rate = rateElement.getText();
                    String txt = txtElement.getText();

                    /*try {
                        WebElement skinTypeElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + i + ") > div.review_cont > div.poll_sample > dl:nth-child(1) > dd > span")));
                        skinType = skinTypeElement.getText();
                    } catch (Exception e) {
                        System.out.println("Skin Type 정보가 없음.");
                    }

                    try {
                        WebElement select1TitleElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + i + ") > div.review_cont > div.poll_sample > dl:nth-child(1) > dt > span")));
                        select1Title = select1TitleElement.getText();
                    } catch (Exception e) {
                        System.out.println("Select 1 Title 정보가 없음.");
                    }

                    try {
                        WebElement select1ContentElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + i + ") > div.review_cont > div.poll_sample > dl:nth-child(1) > dd > span")));
                        select1Content = select1ContentElement.getText();
                    } catch (Exception e) {
                        System.out.println("Select 1 Content 정보가 없음.");
                    }

                    try {
                        WebElement select2TitleElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + i + ") > div.review_cont > div.poll_sample > dl:nth-child(2) > dt > span")));
                        select2Title = select2TitleElement.getText();
                    } catch (Exception e) {
                        System.out.println("Select 2 Title 정보가 없음.");
                    }

                    try {
                        WebElement select2ContentElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + i + ") > div.review_cont > div.poll_sample > dl:nth-child(2) > dd > span")));
                        select2Content = select2ContentElement.getText();
                    } catch (Exception e) {
                        System.out.println("Select 2 Content 정보가 없음.");
                    }*/

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

        // 데이터를 CSV 파일로 저장
        // 파일명을 생성하기 전에 허용되지 않는 문자를 제거
        String safeFileName = makeSafeFileName(productName + "_reviews.csv");

        // 데이터를 CSV 파일로 저장
        CsvWriter.saveToCSV(reviewData, safeFileName);
    }

    public static String makeSafeFileName(String fileName) {
        // 파일명에서 특수문자 제거: 정규식을 사용해 허용되지 않는 문자 제거
        return fileName.replaceAll("[\\\\/:*?\"<>|]", "");
    }
}
