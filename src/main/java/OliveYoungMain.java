import java.util.List;

public class OliveYoungMain {
    public static void main(String[] args) {
        // 카테고리 페이지에서 URL 리스트를 가져옴
        List<String> productUrls = OliveYoungCategoryCrawler.getProductUrls("src/driver/chromedriver.exe");

        // OliveYoungCrawler를 사용하여 각 상품 페이지를 크롤링
        OliveYoungCrawler.olive(productUrls.toArray(new String[0]));

        // 검색패이지는 검색할 수 있게 만들어도 될거같다 결국 주소에 검색에가 들어가니 '주소 + 검색어 ' 가 가능하니
    }
}
