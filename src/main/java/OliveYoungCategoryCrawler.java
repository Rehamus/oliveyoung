import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

public class OliveYoungCategoryCrawler {

    // 상품 URL을 크롤링하여 리스트로 반환하는 메서드
    public static List<String> getProductUrls(String driverPath) {
        System.setProperty("webdriver.chrome.driver", driverPath);
        WebDriver driver = new ChromeDriver();

        List<String> productUrls = new ArrayList<>(); // 상품 URL 리스트

        try {
            // 페이지 로드
            driver.get("https://www.oliveyoung.co.kr/store/display/getMCategoryList.do?dispCatNo=100000100060006");

            // 모든 ul.cate_prd_list.gtm_cate_list 블록을 가져옴
            List<WebElement> productLists = driver.findElements(By.cssSelector(".cate_prd_list"));

            // 각 ul 블록을 순차적으로 처리
            for (WebElement productList : productLists) {
                // 각 ul 블록 내의 모든 li 태그를 가져옴 (data-index가 있는)
                List<WebElement> productItems = productList.findElements(By.cssSelector("li[data-index]"));

                // 각 li 항목의 상품명과 href 추출
                for (WebElement item : productItems) {
                    try {

                        // 상품명 추출
                        WebElement nameElement = item.findElement(By.cssSelector(".prd_name p"));
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
        } finally {
            // WebDriver 종료
            driver.quit();
        }

        return productUrls; // URL 리스트 반환
    }
}
