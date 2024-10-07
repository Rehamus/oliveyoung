package coupang;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static coupang.WebDriverSetting.*;

public class CoupangCategoryUrlCrawler {

    //카테고리 내 상품들의 URL 을 중복 없이 크롤링(저장 및 URL 을 반환)
    static List<String> getAllProductUrlsInCategory(String categoryUrl) {

        WebDriver driver = driverSetting();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get(categoryUrl);
        handlePopupIfPresent(driver);
        afterUrl(driver, wait);

        WebElement categoryTitleElement = driver.findElement(By.cssSelector("div.newcx-main > h1"));
        String categoryTitle = categoryTitleElement.getText(); //CSV 이름으로 사용할 것
        categoryTitle = categoryTitle.replaceAll("[\\\\/:*?\"<>|]", "");

        String baseUrl = "https://www.coupang.com/vp/products/";
        Set<String> indexesInCategory = getProductIndexesInCategory(driver);

        List<String> allProductUrlsInCategory = new ArrayList<>();
        for (String index : indexesInCategory) {
            String productUrl = baseUrl + index;
            allProductUrlsInCategory.add(productUrl);
        }

        CsvWriter.saveUrlsToCSV(allProductUrlsInCategory, categoryTitle +"카테고리 상품 Urls.csv");

        return allProductUrlsInCategory;
    }
    //카테고리 내 상품들의 URL 을 중복 없이 크롤링(저장만, 반환값 없음)
    static void getCSVofAllProductUrlsInCategory(String categoryUrl) {

        WebDriver driver = driverSetting();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get(categoryUrl);
        handlePopupIfPresent(driver);
        afterUrl(driver, wait);

        WebElement categoryTitleElement = driver.findElement(By.cssSelector("div.newcx-main > h1"));
        String categoryTitle = categoryTitleElement.getText(); //CSV 이름으로 사용할 것
        categoryTitle = categoryTitle.replaceAll("[\\\\/:*?\"<>|]", "");

        String baseUrl = "https://www.coupang.com/vp/products/";
        Set<String> indexesInCategory = getProductIndexesInCategory(driver);

        List<String> allProductUrlsInCategory = new ArrayList<>();
        for (String index : indexesInCategory) {
            String productUrl = baseUrl + index;
            allProductUrlsInCategory.add(productUrl);
        }

        CsvWriter.saveUrlsToCSV(allProductUrlsInCategory, categoryTitle + " 카테고리 상품 Urls.csv");
    }

    static Set<String> getProductIndexesInCategory(WebDriver driver) {

        Set<String> allProductIndexesInCategory = new HashSet<>();

        int currentPage = 1;
        boolean flag = true;
        while (flag) {
            Set<String> currentPageProductIndexes = getProductIndexesInPage(driver);
            allProductIndexesInCategory.addAll(currentPageProductIndexes);

            //페이지 넘기기
            int nextPage = currentPage + 1;

            String nextPageLocation = "a.icon.next-page";
            List<WebElement> nextPageElements = driver.findElements(By.cssSelector(nextPageLocation));

            if (!nextPageElements.isEmpty()) {
                WebElement nextPageElement = nextPageElements.get(0);
                String nextIconHref = nextPageElement.getAttribute("href");
                driver.get(nextIconHref);
                System.out.println("load next page: " + nextPage);
                currentPage++;
                randomTimeSleep();
            } else {
                System.out.println("더 이상 페이지가 없습니다. 크롤링을 종료합니다.");
                driver.quit();
                flag = false;
            }
        }

        return allProductIndexesInCategory;

    }

    static Set<String> getProductIndexesInPage(WebDriver driver) {
        Set<String> productIndexes = new HashSet<>();
        WebElement productList = driver.findElement(By.id("productList"));
        String dataProducts = productList.getAttribute("data-products");

        try {
            // indexes 추출하기
            int startIndex = dataProducts.indexOf("\"indexes\":[") + "\"indexes\":[".length();
            int endIndex = dataProducts.indexOf("]", startIndex);
            String indexesString = dataProducts.substring(startIndex, endIndex);

            // 가져온 문자열을 Set에 저장
            String[] indexesArray = indexesString.split(",");

            // 배열의 각 요소를 리스트에 추가
            for (String index : indexesArray) {
                productIndexes.add(index.trim()); // 문자열을 정수로 변환하여 리스트에 추가
            }

            // 결과 출력
            System.out.println("추출한 인덱스들: " + productIndexes);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("데이터 추출 중 오류 발생: " + e.getMessage());
        }

        return productIndexes;

    }

    public static List<String> getProductUrls(String categoryUrl) {
        WebDriver driver = driverSetting();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get(categoryUrl);
        handlePopupIfPresent(driver);
        afterUrl(driver, wait);

        List<String> urls = new ArrayList<>();

        boolean flag = true;
        int currentPage = 1;
        while (flag) {

            System.out.println("현재 페이지: " + currentPage);
            List<WebElement> elements = driver.findElements(By.cssSelector("li.baby-product.renew-badge a.baby-product-link"));

            for (WebElement element : elements) {
                // 각 a 태그의 href 속성 추출
                String href = element.getAttribute("href");
                urls.add(href);
            }

            System.out.println(currentPage + "페이지 링크 추출 완료");

            try {
                //페이지 넘기는 메서드
                int nextPage = currentPage + 1;
                String NextButtonLocation = " a[data-page='" + nextPage + "']";
                List<WebElement> nextPageElements = driver.findElements(By.cssSelector(NextButtonLocation));

                if (!nextPageElements.isEmpty()) {
                    WebElement nextPageElement = nextPageElements.get(0);
                    String href = nextPageElement.getAttribute("href");
                    driver.get(href);
                    System.out.println("load next page: " + nextPage);
                    currentPage++;
                    randomTimeSleep();
                } else {
                    //없는 경우 icon next-page 클래스의 href 로드
                    String nextTenPageElementLocation = "a.icon.next-page";
                    List<WebElement> nextIconPageElements = driver.findElements(By.cssSelector(nextTenPageElementLocation));

                    if (!nextIconPageElements.isEmpty()) {
                        WebElement nextTenPageElement = nextPageElements.get(0);
                        String nextIconHref = nextTenPageElement.getAttribute("href");
                        driver.get(nextIconHref);
                        System.out.println("load next ten page");
                        currentPage++;
                        randomTimeSleep();
                    } else {
                        System.out.println("더 이상 페이지가 없습니다. 크롤링을 종료합니다.");
                        driver.quit();
                        flag = false;
                    }
                }
            } catch (Exception e) {
                System.out.println("다음 페이지로 이동하는 중 오류 발생: " + e.getMessage());
                driver.quit();
                flag = false;
            }
        }

        for (int i = 0; i < urls.size(); i++) {
            System.out.println((i + 1) + "번째: " + urls.get(i));
        }

//        driver.quit();

        return urls;

    }
}
