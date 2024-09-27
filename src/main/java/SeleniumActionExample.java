import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumActionExample {
    public static void main(String[] args) {
        // WebDriver 경로 설정 (본인의 ChromeDriver 경로로 변경 필요)
        System.setProperty("webdriver.chrome.driver", "src/driver/chromedriver.exe");

        // ChromeDriver 객체 생성
        WebDriver driver = new ChromeDriver();

        // 지정된 URL로 이동
        driver.get("https://www.oliveyoung.co.kr/store/goods/getGoodsDetail.do?goodsNo=A000000188933&dispCatNo=10000010001&trackingCd=Cat10000010001_Prodrank&t_page=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EA%B4%80&t_click=%EB%9E%AD%ED%82%B9BEST%EC%83%81%ED%92%88%EB%B8%8C%EB%9E%9C%EB%93%9C_%EC%83%81%ED%92%88%EC%83%81%EC%84%B8&t_number=1");

        // Actions 객체 생성 (WebDriver에 대해 액션을 수행하는 명령어)
//        Actions actions = new Actions(driver);

        // Actions 객체로 다양한 동작 수행 가능
        // 예시: actions.moveToElement(element).click().build().perform();
    }
}
