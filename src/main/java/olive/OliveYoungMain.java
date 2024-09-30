package olive;

import java.util.List;

public class OliveYoungMain {
    public static void main(String[] args) {
        // 카테고리 페이지에서 URL 리스트를 가져옴
        List<String> productUrls = OliveYoungCategoryCrawler.getProductUrls(
                "https://www.oliveyoung.co.kr/store/display/getMCategoryList.do?dispCatNo=1000001000100130001&t_page=%EC%83%81%ED%92%88%EC%83%81%EC%84%B8&t_click=%EC%86%8C%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC_%EC%9D%B4%EB%8F%99&t_goods_name=%5B9%EC%9B%94%20%EC%98%AC%EC%98%81%ED%94%BD%2F%ED%86%A0%EB%84%88%20250ml%EC%A6%9D%EC%A0%95%5D%EB%B0%94%EC%9D%B4%EC%98%A4%EB%8D%94%EB%A7%88%20%ED%95%98%EC%9D%B4%EB%93%9C%EB%9D%BC%EB%B9%84%EC%98%A4%20%ED%86%A0%EB%84%88%20500ml%20%EA%B8%B0%ED%9A%8D(%2B%ED%86%A0%EB%84%88%20250ml%20%EC%A6%9D%EC%A0%95)&t_goods_no=A000000188933"
                ,"src/driver/chromedriver.exe");

        // OliveYoungCrawler를 사용하여 각 상품 페이지를 크롤링
        OliveYoungCrawler.olive(productUrls.toArray(new String[0]));

        // 검색패이지는 검색할 수 있게 만들어도 될거같다 결국 주소에 검색에가 들어가니 '주소 + 검색어 ' 가 가능하니
    }
}
