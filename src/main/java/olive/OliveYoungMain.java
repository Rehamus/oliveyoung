package olive;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OliveYoungMain {
    public static void main(String[] args) {
        // 카테고리 페이지에서 URL 리스트를 가져옴
        List<String> productUrls = OliveYoungCategoryCrawler.getProductUrls(
//                "https://www.oliveyoung.co.kr/store/display/getMCategoryList.do?dispCatNo=1000001000600010005&fltDispCatNo=&prdSort=01&pageIdx=1&rowsPerPage=24&searchTypeSort=btn_thumb&plusButtonFlag=N&isLoginCnt=2&aShowCnt=0&bShowCnt=0&cShowCnt=0&trackingCd=Cat1000001000600010005_Small&amplitudePageGubun=SMALL_CATE&t_page=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EA%B4%80&t_click=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EC%83%81%EC%84%B8_%EC%86%8C%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC&midCategory=%EB%A9%94%EC%9D%B4%ED%81%AC%EC%97%85%EC%86%8C%ED%92%88&smallCategory=%EC%86%8C_%ED%8D%BC%ED%94%84"
//                "https://www.oliveyoung.co.kr/store/display/getMCategoryList.do?dispCatNo=1000001000600010006&fltDispCatNo=&prdSort=01&pageIdx=1&rowsPerPage=24&searchTypeSort=btn_thumb&plusButtonFlag=N&isLoginCnt=2&aShowCnt=0&bShowCnt=0&cShowCnt=0&trackingCd=Cat1000001000600010006_Small&amplitudePageGubun=SMALL_CATE&t_page=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EA%B4%80&t_click=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EC%83%81%EC%84%B8_%EC%86%8C%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC&midCategory=%ED%8D%BC%ED%94%84&smallCategory=%EC%86%8C_%EC%8A%A4%ED%8E%80%EC%A7%80&checkBrnds=&lastChkBrnd=&t_3rd_category_type=%EC%86%8C_%EC%8A%A4%ED%8E%80%EC%A7%80"
                "https://www.oliveyoung.co.kr/store/display/getMCategoryList.do?dispCatNo=100000100110006&fltDispCatNo=&prdSort=01&pageIdx=1&rowsPerPage=24&searchTypeSort=btn_thumb&plusButtonFlag=N&isLoginCnt=9&aShowCnt=0&bShowCnt=0&cShowCnt=0&trackingCd=Cat100000100110006_Small&amplitudePageGubun=&t_page=&t_click=&midCategory=%EC%84%A0%ED%81%AC%EB%A6%BC&smallCategory=%EC%A0%84%EC%B2%B4&checkBrnds=&lastChkBrnd="
                , "src/driver/chromedriver.exe");

        // 스레드 풀을 생성하여 최대 5개의 스레드를 동시에 실행
        ExecutorService executor = Executors.newFixedThreadPool(8);

        String file = "sunscreen";
//        String file = "sponge";

        // 각 상품 페이지에 대해 크롤러 작업을 스레드로 실행
        for (String url : productUrls) {
            executor.submit(() -> OliveYoungCrawler.olive(new String[]{url}, file));
        }

        // 작업이 모두 완료될 때까지 기다림
        executor.shutdown(); // 더 이상 새로운 작업을 제출하지 않음
        while (!executor.isTerminated()) {
            // 모든 작업이 끝날 때까지 대기
        }

        System.out.println("모든 페이지 크롤링이 완료되었습니다.");
    }
}
