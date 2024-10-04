package olive;

import olive.Crawler.OliveYoungBrendCrawler;
import olive.Crawler.OliveYoungSearchCrawler;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OliveYoungMain {
    public static void main(String[] args) {
/*        // 카테고리 페이지에서 URL 리스트를 가져옴
        List<String> productUrls = OliveYoungCategoryCrawler.getProductUrls(
                "https://www.oliveyoung.co.kr/store/display/getMCategoryList.do?dispCatNo=1000001000900020001&fltDispCatNo=&prdSort=01&pageIdx=1&rowsPerPage=24&searchTypeSort=btn_thumb&plusButtonFlag=N&isLoginCnt=6&aShowCnt=0&bShowCnt=0&cShowCnt=0&trackingCd=Cat1000001000900020001_Small&amplitudePageGubun=SMALL_CATE&t_page=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EA%B4%80&t_click=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EC%83%81%EC%84%B8_%EC%86%8C%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC&midCategory=%ED%8E%98%EC%9D%B4%EC%85%9C%ED%8C%A9&smallCategory=%EC%86%8C_%EC%9B%8C%EC%8B%9C%EC%98%A4%ED%94%84%ED%8C%A9&checkBrnds=&lastChkBrnd=&t_3rd_category_type=%EC%86%8C_%EC%9B%8C%EC%8B%9C%EC%98%A4%ED%94%84%ED%8C%A9"
       );*/


       /* // 브렌드 페이지에서 URL 리스트를 가져옴
        List<String> productUrls = OliveYoungBrendCrawler.getProductUrls(
                "https://www.oliveyoung.co.kr/store/display/getBrandShopDetail.do?onlBrndCd=A002820&t_page=%ED%86%B5%ED%95%A9%EA%B2%80%EC%83%89%EA%B2%B0%EA%B3%BC%ED%8E%98%EC%9D%B4%EC%A7%80&t_click=%EB%B8%8C%EB%9E%9C%EB%93%9C%EA%B4%80%EB%B0%94%EB%A1%9C%EA%B0%80%EA%B8%B0&t_brand_name=%ED%86%A0%EB%A6%AC%EB%93%A0"
        );*/


        // 검색 페이지에서 URL 리스트를 가져옴
        List<String> productUrls = OliveYoungSearchCrawler.getProductUrls(
                "https://www.oliveyoung.co.kr/store/search/getSearchMain.do?query=%EC%B4%88%EC%BD%94&giftYn=N&t_page=%EC%9D%B4%EB%94%94%EC%95%84%EA%B2%80%EC%83%89&t_click=%EA%B2%80%EC%83%89%EC%B0%BD&t_search_name=%EC%B4%88%EC%BD%94"
        );

        // 스레드 풀을 생성하여 최대 5개의 스레드를 동시에 실행
        ExecutorService executor = Executors.newFixedThreadPool(4);

        String file = "mask/Wash-off";

        // 각 상품 페이지에 대해 크롤러 작업을 스레드로 실행
/*        for (String url : productUrls) {
            executor.submit(() -> OliveYoungCrawler.olive(new String[]{url}, file));
        }*/

        // 작업이 모두 완료될 때까지 기다림
        executor.shutdown(); // 더 이상 새로운 작업을 제출하지 않음

        System.out.println("모든 페이지 크롤링이 완료되었습니다.");
    }
}
