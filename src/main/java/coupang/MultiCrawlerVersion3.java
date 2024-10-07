package coupang;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiCrawlerVersion3 {

    //CSV 파일의 링크들을 멀티쓰레드로 크롤링
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("파일이 저장되어 있는 경로를 입력하세요: ");
        String baseFilePath = scanner.nextLine();
        System.out.println("url이 저장된 파일명을 입력하세요(확장자 제외): ");
        String fileName = scanner.nextLine();

        String csvFilePath = baseFilePath + "\\" + fileName + ".csv";

        // CSV 파일에서 URL 읽기
        List<String> ProductUrls = CSVReader.readCsvFile(csvFilePath);

        ExecutorService executor = Executors.newFixedThreadPool(4);

        for (String url : ProductUrls) {
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
    }
}
