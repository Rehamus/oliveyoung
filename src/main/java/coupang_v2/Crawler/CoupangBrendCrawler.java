package coupang_v2.Crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class CoupangBrendCrawler {

    public static void main(String[] args) {
        getProductUrls("https://www.coupang.com/np/products/brand-shop?brandName=%EB%8D%94%EB%9E%A9%EB%B0%94%EC%9D%B4%EB%B8%94%EB%9E%91%EB%91%90");
    }
    // 상품 URL을 크롤링하여 리스트로 반환하는 메서드
    public static List<String> getProductUrls(String url) {
        System.setProperty("webdriver.chrome.driver", "src/driver/chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        JavascriptExecutor js = (JavascriptExecutor) driver;

        List<String> productUrls = new ArrayList<>(); // 상품 URL 리스트

        try {
            // 페이지 로드
            driver.get(url);

            boolean hasNextPage = true;
            int currentPage = 1;

            while (hasNextPage) {
                // 모든 ul.cate_prd_list.gtm_cate_list 블록을 가져옴
                List<WebElement> productLists = driver.findElements(By.cssSelector("ul#productList"));

                // 각 ul 블록을 순차적으로 처리
                for (WebElement productList : productLists) {
                    // 각 ul 블록 내의 모든 li 태그를 가져옴 (data-index가 있는)
                    List<WebElement> productItems = productList.findElements(By.cssSelector("li"));

                    // 각 li 항목의 상품명과 href 추출
                    for (WebElement item : productItems) {
                        try {
                            // 상품명 추출
                            WebElement nameElement = item.findElement(By.cssSelector(".name"));
                            String productName = nameElement.getText();

                            // 링크 추출
                            WebElement linkElement = item.findElement(By.cssSelector("a"));
                            String productLink = linkElement.getAttribute("href");

                            // 추출한 값 출력
                            System.out.println("상품명: " + productName);
                            System.out.println("상품 링크: " + productLink);
                            System.out.println("---------------------------------------");

                            // URL 리스트에 추가
                            productUrls.add(productLink);

                        } catch (Exception e) {
                            System.out.println("데이터 추출 중 오류 발생: " + e.getMessage());
                        }
                    }
                }
                // 다음 페이지가 있는지 확인
                try {
                    Random random = new Random();

                    js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                    Thread.sleep((random.nextInt(2) + 2) * 800);

                    WebElement nextPageButton = driver.findElement(By.cssSelector("#product-list-paging a[data-page='" + (currentPage + 1) + "']"));
                    System.out.println("다음 페이지: " + nextPageButton.getText());
                    nextPageButton.click();
                    currentPage++;

                    Thread.sleep((random.nextInt(5) + 2) * 1000);
                } catch (NoSuchElementException e) {
                    System.out.println("다음 페이지 버튼이 없습니다. 크롤링을 종료합니다.");
                    hasNextPage = false;
                } catch (Exception e) {
                    System.out.println("페이지 이동 중 오류 발생: " + e.getMessage());
                    hasNextPage = false;
                }
            }

        } catch (Exception e) {
            System.out.println("크롤링 중 오류 발생: " + e.getMessage());
        } finally {
            // WebDriver 종료
            driver.quit();
        }

        return productUrls; // URL 리스트 반환
    }
}
