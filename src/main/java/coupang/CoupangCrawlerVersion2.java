package coupang;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static coupang.CoupangCategoryUrlCrawler.*;
import static coupang.WebDriverSetting.*;

public class CoupangCrawlerVersion2 {
    //카테고리 url 을 직접 받아서 상품 URL을 CSV로 저장 또는 상품평 크롤링
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("카테고리 URL을 입력해 주세요: ");
        String categoryUrl = scanner.nextLine();

        String option;
        while (true) {
            System.out.println("a: 상품 URL 저장만, b: 상품 URL 저장과 크롤링");
            option = scanner.nextLine();

            if (!option.equals("a") && !option.equals("b")) {
                System.out.println("a나 b중에 입력해주세요.");;
            } else {
                break;
            }
        }

//        WebDriver driver = driverSetting();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
//        driver.get(categoryUrl);
//        handlePopupIfPresent(driver);
//        afterUrl(driver, wait);

        List<String> categoryProductUrls = new ArrayList<>();

        if (option.equals("a")) {
            getCSVofAllProductUrlsInCategory(categoryUrl);
        } else if (option.equals("b")) {
            categoryProductUrls = getAllProductUrlsInCategory(categoryUrl);
            for (String url : categoryProductUrls) {
                WebDriver driver = driverSetting();
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
                driver.get(categoryUrl);
                handlePopupIfPresent(driver);
                afterUrl(driver, wait);

                CoupangCrawler.pageCrawler(url, driver, wait);
            }
        }

//        System.out.println(categoryProductUrls);
//        driver.quit();

    }
}
