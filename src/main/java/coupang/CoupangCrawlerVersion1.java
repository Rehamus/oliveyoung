package coupang;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Scanner;

public class CoupangCrawlerVersion1 {
    //상품 URL 단건에 대한 크롤링
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("url을 입력해주세요: ");
        String url = scanner.nextLine();

        WebDriver driver = WebDriverSetting.driverSetting();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        CoupangCrawler.pageCrawler(url, driver, wait);
        System.out.println("종료");
        System.exit(0);

    }
}
