package coupang;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static coupang.CoupangCategoryUrlCrawler.getAllProductUrlsInCategory;

import static coupang.CoupangCategoryUrlCrawler.getCSVofAllProductUrlsInCategory;
import static coupang.WebDriverSetting.*;

public class MultiCrawlerVersion2 {
    //카테고리 url 을 직접 받아서 상품 URL을 CSV로 저장 또는 상품평 크롤링(멀티 ver)
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

        ExecutorService executor = Executors.newFixedThreadPool(4);

        if (option.equals("a")) {
            getCSVofAllProductUrlsInCategory(categoryUrl);
        } else if (option.equals("b")) {
            List<String> categoryProductUrls = getAllProductUrlsInCategory(categoryUrl);

            for (String url : categoryProductUrls) {
                //멀티
                String finalUrl = url;
                executor.submit(() -> {
                    try {
                        String threadName = Thread.currentThread().getName();
                        System.out.println("현재 Thread: " + threadName);
                        WebDriver driver = WebDriverSetting.driverSetting();
                        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
                        CoupangCrawler.pageCrawler(finalUrl, driver, wait);
                    } catch (Exception e) {
                        System.out.println("오류 발생: " + e.getMessage());
                        e.printStackTrace();
                    }
                });

            }

            // 작업이 모두 완료될 때까지 기다림
            executor.shutdown(); // 더 이상 새로운 작업을 제출하지 않음

            System.out.println("카테고리내 모든 상품의 크롤링이 완료되었습니다.");
        }

    }
}
