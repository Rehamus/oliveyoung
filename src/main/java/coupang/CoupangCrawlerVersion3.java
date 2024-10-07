package coupang;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.time.Duration;
import java.util.List;
import java.util.Scanner;

public class CoupangCrawlerVersion3 {

    //CSV 파일의 링크로 크롤링
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("파일이 저장되어 있는 경로를 입력하세요: ");
        String baseFilePath = scanner.nextLine();
        System.out.println("url이 저장된 파일명을 입력하세요(확장자 제외): ");
        String fileName = scanner.nextLine();

        String csvFilePath = baseFilePath + "\\" + fileName + ".csv";

        // CSV 파일에서 URL 읽기
        List<String> urls = CSVReader.readCsvFile(csvFilePath);
        for (int i = 0; i < urls.size(); i++) {
            WebDriver driver = WebDriverSetting.driverSetting();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            String url = urls.get(i); // i번째 URL 가져오기
            CoupangCrawler.pageCrawler(url, driver, wait); // 크롤링 수행
            System.out.println((i + 1) + "번째 URL에 대한 상품평 크롤링 완료");
        }
    }
}
