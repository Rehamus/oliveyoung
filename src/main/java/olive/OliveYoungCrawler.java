package olive;

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

    public static void olive(String[] urls) {
        System.setProperty("webdriver.chrome.driver", "src/driver/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(org.openqa.selenium.PageLoadStrategy.EAGER);
//        options.addArguments("--headless");

        WebDriver driver = new ChromeDriver(options);

        for (String url : urls) {
            crawler(driver, url);
        }

        driver.quit();
    }

    private static void crawler(WebDriver driver, String url) {
        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            Thread.sleep(5000); // 10초 대기
            WebElement reviewTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("reviewInfo")));
            reviewTab.click();
        } catch (Exception e) {
            System.out.println("리뷰 탭을 클릭하는 중 오류 발생: " + e.getMessage());
        }

        List<String[]> reviewData = new ArrayList<>();
        String productName = extractProductName(wait);
        if (productName == null) return;

        boolean skipSkinType = false; // Skin Type 추출을 건너뛰는 플래그

        int totalPages = 2;
        for (int currentPage = 1; currentPage <= totalPages; currentPage++) {
            skipSkinType = extractReviewsFromPage(driver, wait, reviewData, currentPage, skipSkinType);
            navigateToNextPage(driver, currentPage);
        }

        String safeFileName = makeSafeFileName(productName + ".csv");
        CsvWriter.saveToCSV(reviewData, safeFileName);
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

    private static boolean extractReviewsFromPage(WebDriver driver, WebDriverWait wait, List<String[]> reviewData, int page, boolean skipSkinType) {
        for (int i = 1; i <= 10; i++) {
            try {
                // skipSkinType 플래그를 전달하여 리뷰 추출
                String[] review = extractSingleReview(wait, i, skipSkinType);
                if (review != null) {
                    reviewData.add(review);
                    if (review[3].isEmpty()) { // review[3]은 skinType 값
                        skipSkinType = true; // 한번이라도 Skin Type 정보가 없으면 추출을 중단
                    }
                }
            } catch (Exception e) {
                System.out.println("Error while extracting review: " + e.getMessage());
            }
        }
        System.out.println(page + " 페이지 크롤링 완료");
        return skipSkinType; // skipSkinType 값을 계속 유지
    }

    private static String[] extractSingleReview(WebDriverWait wait, int reviewIndex, boolean skipSkinType) {
        try {
            WebElement dateElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + reviewIndex + ") > div.review_cont > div.score_area > span.date")));
            WebElement idElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + reviewIndex + ") > div.info > div > p.info_user > a.id")));
            WebElement rateElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + reviewIndex + ") > div.review_cont > div.score_area > span.review_point > span")));
            WebElement txtElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gdasList > li:nth-child(" + reviewIndex + ") > div.review_cont > div.txt_inner")));

            String skinType = "", select1Title = "", select1Content = "", select2Title = "", select2Content = "";

            if (!skipSkinType) {
                // skinType 정보 추출
                skinType = extractText(wait, "#gdasList > li:nth-child(" + reviewIndex + ") > div.review_cont > div.poll_sample > dl:nth-child(1) > dd > span", "Skin Type 정보가 없음.");
                select1Title = extractText(wait, "#gdasList > li:nth-child(" + reviewIndex + ") > div.review_cont > div.poll_sample > dl:nth-child(1) > dt > span", "Select 1 Title 정보가 없음.");
                select1Content = extractText(wait, "#gdasList > li:nth-child(" + reviewIndex + ") > div.review_cont > div.poll_sample > dl:nth-child(1) > dd > span", "Select 1 Content 정보가 없음.");
                select2Title = extractText(wait, "#gdasList > li:nth-child(" + reviewIndex + ") > div.review_cont > div.poll_sample > dl:nth-child(2) > dt > span", "Select 2 Title 정보가 없음.");
                select2Content = extractText(wait, "#gdasList > li:nth-child(" + reviewIndex + ") > div.review_cont > div.poll_sample > dl:nth-child(2) > dd > span", "Select 2 Content 정보가 없음.");
            }
            return new String[]{
                    dateElement.getText(),
                    rateElement.getText(),
                    idElement.getText(),
                    skinType,
                    select1Title,
                    select1Content,
                    select2Title,
                    select2Content,
                    txtElement.getText()
            };
        } catch (Exception e) {
            System.out.println("리뷰 추출 중 오류 발생: " + e.getMessage());
            return null;
        }
    }

    private static void navigateToNextPage(WebDriver driver, int currentPage) {
        try {
            // 다음 페이지 버튼을 찾음, currentPage에 1을 더한 페이지 번호로 클릭
            WebElement nextPageButton = driver.findElement(By.cssSelector("a[data-page-no='" + (currentPage + 1) + "']"));

            // 페이지 버튼이 존재하고 클릭할 수 있는 상태인지 확인
            if (nextPageButton.isDisplayed() && nextPageButton.isEnabled()) {
                nextPageButton.click();  // 다음 페이지 클릭
                currentPage++;  // 페이지 번호 증가
                Thread.sleep(2000);  // 페이지 로드를 기다림
            } else {
                System.out.println("더 이상 페이지를 넘길 수 없습니다. (마지막 페이지)");
            }

        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.out.println("더 이상 페이지가 없습니다. 크롤링을 종료합니다.");
        } catch (Exception e) {
            System.out.println("페이지 이동 중 오류 발생: " + e.getMessage());
        }
    }



    public static String makeSafeFileName(String fileName) {
        return fileName.replaceAll("[\\\\/:*?\"<>|]", "");
    }
}
