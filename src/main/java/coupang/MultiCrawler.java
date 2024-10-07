package coupang;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiCrawler {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("카테고리 url을 입력해주세요: ");
        String categoryUrl = scanner.nextLine();
        List<String> productUrls = CoupangCategoryUrlCrawler.getProductUrls(categoryUrl);

//         스레드 풀을 생성: 최대 5개의 스레드를 동시에 실행 가능
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (String url : productUrls) {
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
//        for (String url : productUrls) {
//           CoupangCrawler.pageCrawler(url);
//        }

        // 작업이 모두 완료될 때까지 기다림
        executor.shutdown(); // 더 이상 새로운 작업을 제출하지 않음

        System.out.println("카테고리내 모든 상품의 크롤링이 완료되었습니다.");
    }


}
