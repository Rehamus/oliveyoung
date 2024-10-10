package coupang;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static coupang.CoupangCategoryUrlCrawler.getAllProductUrlsInCategory;

public class MultiCrawlerVersion2 {
    //카테고리 url 을 직접 받아서 상품 URL을 CSV로 저장 또는 상품평 크롤링(멀티 ver)
    public static void main(String[] args) {

        List<String> hardUrl = getStrings();
        ExecutorService executor = Executors.newFixedThreadPool(8);
        String[] names = {/*"토리든","코스알엑스",*/"넘버즈인",/*"소울코스메틱","(주)김정문알로에","달바","온그리디언츠","조선미녀"*/};

        for (int i = 0; i < hardUrl.size(); i++) {
            String urls = hardUrl.get(i);
            int currentIndex = i; // 배열 인덱스를 안전하게 전달

            List<String> categoryProductUrls = getAllProductUrlsInCategory(urls);
            for (String url : categoryProductUrls) {
                executor.submit(() -> {
                    try {
                        String threadName = Thread.currentThread().getName();
                        System.out.println("현재 Thread: " + threadName);

                        WebDriver driver = WebDriverSetting.driverSetting();
                        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

                        // names 배열 범위를 벗어나는지 확인
                        if (currentIndex < names.length) {
                            CoupangCrawler.pageCrawler_v2(url, driver, wait,names[currentIndex]);
                        } else {
                            System.out.println("names 배열 인덱스 범위를 벗어났습니다: " + currentIndex);
                        }

                    } catch (Exception e) {
                        System.out.println("오류 발생: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        }

        // 작업이 모두 완료될 때까지 기다림
        executor.shutdown(); // 더 이상 새로운 작업을 제출하지 않음

        try {
            if (!executor.awaitTermination(60, TimeUnit.MINUTES)) {
                executor.shutdownNow(); // 60분 안에 끝나지 않으면 강제 종료
                System.out.println("강제로 스레드를 종료했습니다.");
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("카테고리내 모든 상품의 크롤링이 완료되었습니다.");
    }


    private static List<String> getStrings() {
        List<String> hardUrl = new ArrayList<>();
        // 카태고리
//        hardUrl.add("https://www.coupang.com/np/products/brand-shop?brandName=%ED%86%A0%EB%A6%AC%EB%93%A0");//토리든
//        hardUrl.add("https://www.coupang.com/np/products/brand-shop?brandName=%EC%BD%94%EC%8A%A4%EC%95%8C%EC%97%91%EC%8A%A4");//코스알엑스
        hardUrl.add("https://www.coupang.com/np/products/brand-shop?brandName=%EB%84%98%EB%B2%84%EC%A6%88%EC%9D%B8");//넘버즈인
//        hardUrl.add("https://www.coupang.com/np/products/brand-shop?brandName=%EC%86%8C%EC%9A%B8%EC%BD%94%EC%8A%A4%EB%A9%94%ED%8B%B1");//소울

        //무한 스크롤
       /* hardUrl.add("https://shop.coupang.com/A00293111?source=brandstore_sdp_atf_topbadge&pid=6595109989&viid=82136881944&platform=p&locale=ko_KR");//김정문
        hardUrl.add("https://shop.coupang.com/dalba?source=brandstore_sdp_atf_topbadge&pid=6333299320&viid=80470406836&platform=p&locale=ko_KR");//달바
        hardUrl.add("https://shop.coupang.com/ongrendients?source=brandstore_sdp_atf_topbadge&pid=8218486369&viid=90638714326&platform=p&locale=ko_KR");//온그리디언츠
        hardUrl.add("https://shop.coupang.com/beautyofjoseon?source=brandstore_sdp_atf&pid=8295695198&viid=90949336893&platform=p&locale=ko_KR");//조선미녀*/

        //보류
//        hardUrl.add("https://shop.coupang.com/A00597188/186284?locale=ko_KR&platform=p&source=brandstore_sdp_atf_topbadge&pid=8051227805&viid=89624070209");//아모레 퍼시픽 > 설화수(보류)


        return hardUrl;
    }
}
