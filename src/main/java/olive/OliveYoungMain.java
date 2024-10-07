package olive;

import olive.Crawler.OliveYoungBrendCrawler;
import olive.Crawler.OliveYoungCategoryCrawler;
import olive.Crawler.OliveYoungSearchCrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OliveYoungMain {
    public static void main(String[] args) {

        List<String[]> crawlerUrls = new ArrayList<>();
        // 완료
       /* crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getMCategoryList.do?dispCatNo=1000001000900020001&fltDispCatNo=&prdSort=01&pageIdx=1&rowsPerPage=24&searchTypeSort=btn_thumb&plusButtonFlag=N&isLoginCnt=6&aShowCnt=0&bShowCnt=0&cShowCnt=0&trackingCd=Cat1000001000900020001_Small&amplitudePageGubun=SMALL_CATE&t_page=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EA%B4%80&t_click=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EC%83%81%EC%84%B8_%EC%86%8C%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC&midCategory=%ED%8E%98%EC%9D%B4%EC%85%9C%ED%8C%A9&smallCategory=%EC%86%8C_%EC%9B%8C%EC%8B%9C%EC%98%A4%ED%94%84%ED%8C%A9&checkBrnds=&lastChkBrnd=&t_3rd_category_type=%EC%86%8C_%EC%9B%8C%EC%8B%9C%EC%98%A4%ED%94%84%ED%8C%A9"
                , "마스크팩/페이셜팩/워시오프팩"
                , "카테고리"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getMCategoryList.do?dispCatNo=1000001000900020003&fltDispCatNo=&prdSort=01&pageIdx=1&rowsPerPage=24&searchTypeSort=btn_thumb&plusButtonFlag=N&isLoginCnt=6&aShowCnt=0&bShowCnt=0&cShowCnt=0&trackingCd=Cat1000001000900020003_Small&amplitudePageGubun=SMALL_CATE&t_page=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EA%B4%80&t_click=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EC%83%81%EC%84%B8_%EC%86%8C%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC&midCategory=%EC%9B%8C%EC%8B%9C%EC%98%A4%ED%94%84%ED%8C%A9&smallCategory=%EC%86%8C_%EB%AA%A8%EB%8D%B8%EB%A7%81%ED%8C%A9%2F%ED%95%84%EC%98%A4%ED%94%84%ED%8C%A9&checkBrnds=&lastChkBrnd=&t_3rd_category_type=%EC%86%8C_%EB%AA%A8%EB%8D%B8%EB%A7%81%ED%8C%A9%2F%ED%95%84%EC%98%A4%ED%94%84%ED%8C%A9"
                , "마스크팩/페이셜팩/모델링팩&필오프팩"
                , "카테고리"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getMCategoryList.do?dispCatNo=1000001000900020002&fltDispCatNo=&prdSort=01&pageIdx=1&rowsPerPage=24&searchTypeSort=btn_thumb&plusButtonFlag=N&isLoginCnt=6&aShowCnt=0&bShowCnt=0&cShowCnt=0&trackingCd=Cat1000001000900020002_Small&amplitudePageGubun=SMALL_CATE&t_page=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EA%B4%80&t_click=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EC%83%81%EC%84%B8_%EC%86%8C%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC&midCategory=%EB%AA%A8%EB%8D%B8%EB%A7%81%ED%8C%A9%2F%ED%95%84%EC%98%A4%ED%94%84%ED%8C%A9&smallCategory=%EC%86%8C_%EC%8A%AC%EB%A6%AC%ED%95%91%ED%8C%A9&checkBrnds=&lastChkBrnd=&t_3rd_category_type=%EC%86%8C_%EC%8A%AC%EB%A6%AC%ED%95%91%ED%8C%A9"
                , "마스크팩/페이셜팩/슬리핑팩"
                , "카테고리"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getMCategoryList.do?dispCatNo=100000100010013&isLoginCnt=2&aShowCnt=0&bShowCnt=0&cShowCnt=0&gateCd=Drawer&trackingCd=Cat100000100010013_MID&trackingCd=Cat100000100010013_MID&t_page=%EB%93%9C%EB%A1%9C%EC%9A%B0_%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC&t_click=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%ED%83%AD_%EC%A4%91%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC&t_2nd_category_type=%EC%A4%91_%EC%8A%A4%ED%82%A8%2F%ED%86%A0%EB%84%88"
                , "스킨케어/스킨&토너"
                , "카테고리"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getMCategoryList.do?dispCatNo=100000100010014&isLoginCnt=3&aShowCnt=0&bShowCnt=0&cShowCnt=0&gateCd=Drawer&trackingCd=Cat100000100010014_MID&trackingCd=Cat100000100010014_MID&t_page=%EB%93%9C%EB%A1%9C%EC%9A%B0_%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC&t_click=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%ED%83%AD_%EC%A4%91%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC&t_2nd_category_type=%EC%A4%91_%EC%97%90%EC%84%BC%EC%8A%A4%2F%EC%84%B8%EB%9F%BC%2F%EC%95%B0%ED%94%8C"
                , "에센스&세럼&앰플"
                , "카테고리"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getMCategoryList.do?dispCatNo=1000001000100150001&fltDispCatNo=&prdSort=01&pageIdx=1&rowsPerPage=24&searchTypeSort=btn_thumb&plusButtonFlag=N&isLoginCnt=4&aShowCnt=0&bShowCnt=0&cShowCnt=0&trackingCd=Cat1000001000100150001_Small&amplitudePageGubun=SMALL_CATE&t_page=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EA%B4%80&t_click=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EC%83%81%EC%84%B8_%EC%86%8C%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC&midCategory=%ED%81%AC%EB%A6%BC&smallCategory=%EC%86%8C_%ED%81%AC%EB%A6%BC&checkBrnds=&lastChkBrnd=&t_3rd_category_type=%EC%86%8C_%ED%81%AC%EB%A6%BC"
                , "스킨케어/크림"
                , "카테고리"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getMCategoryList.do?dispCatNo=1000001001000040002&fltDispCatNo=&prdSort=01&pageIdx=1&rowsPerPage=24&searchTypeSort=btn_thumb&plusButtonFlag=N&isLoginCnt=0&aShowCnt=0&bShowCnt=0&cShowCnt=0&trackingCd=Cat1000001001000040002_Small&amplitudePageGubun=SMALL_CATE&t_page=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EA%B4%80&t_click=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EC%83%81%EC%84%B8_%EC%86%8C%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC&midCategory=%ED%81%B4%EB%A0%8C%EC%A7%95%EB%B0%A4&smallCategory=%EC%86%8C_%ED%81%B4%EB%A0%8C%EC%A7%95%EB%B0%A4&checkBrnds=&lastChkBrnd=&t_3rd_category_type=%EC%86%8C_%ED%81%B4%EB%A0%8C%EC%A7%95%EB%B0%A4"
                , "클렌징/오일&밤/클렌징밤"
                , "카테고리"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getMCategoryList.do?dispCatNo=1000001000300080004&fltDispCatNo=&prdSort=01&pageIdx=1&rowsPerPage=24&searchTypeSort=btn_thumb&plusButtonFlag=N&isLoginCnt=5&aShowCnt=0&bShowCnt=0&cShowCnt=0&trackingCd=Cat1000001000300080004_Small&amplitudePageGubun=SMALL_CATE&t_page=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EA%B4%80&t_click=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EC%83%81%EC%84%B8_%EC%86%8C%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC&midCategory=%EB%A6%BD%EC%BC%80%EC%96%B4&smallCategory=%EC%86%8C_%EB%A6%BD%EB%B0%A4&checkBrnds=&lastChkBrnd=&t_3rd_category_type=%EC%86%8C_%EB%A6%BD%EB%B0%A4"
                , "바디케어/립밤"
                , "카테고리"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getMCategoryList.do?dispCatNo=1000001000300080006&fltDispCatNo=&prdSort=01&pageIdx=1&rowsPerPage=24&searchTypeSort=btn_thumb&plusButtonFlag=N&isLoginCnt=5&aShowCnt=0&bShowCnt=0&cShowCnt=0&trackingCd=Cat1000001000300080006_Small&amplitudePageGubun=SMALL_CATE&t_page=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EA%B4%80&t_click=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EC%83%81%EC%84%B8_%EC%86%8C%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC&midCategory=%EB%A6%BD%EC%98%A4%EC%9D%BC%2F%ED%94%8C%EB%9F%BC%ED%8D%BC&smallCategory=%EC%86%8C_%EB%A6%BD%EB%A7%88%EC%8A%A4%ED%81%AC%2F%EC%8A%A4%ED%81%AC%EB%9F%BD&checkBrnds=&lastChkBrnd=&t_3rd_category_type=%EC%86%8C_%EB%A6%BD%EB%A7%88%EC%8A%A4%ED%81%AC%2F%EC%8A%A4%ED%81%AC%EB%9F%BD"
                , "바디케어/립마스크&스크럽"
                , "카테고리"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getBrandShopDetail.do?onlBrndCd=A002924&t_page=%ED%86%B5%ED%95%A9%EA%B2%80%EC%83%89%EA%B2%B0%EA%B3%BC%ED%8E%98%EC%9D%B4%EC%A7%80&t_click=%EB%B8%8C%EB%9E%9C%EB%93%9C%EA%B4%80%EB%B0%94%EB%A1%9C%EA%B0%80%EA%B8%B0&t_brand_name=%EB%8D%94%EB%9E%A9%EB%B0%94%EC%9D%B4%EB%B8%94%EB%9E%91%EB%91%90"
                , "브렌드/더랩바이블랑두"
                , "브렌드"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getBrandShopDetail.do?onlBrndCd=A003585&t_page=%ED%86%B5%ED%95%A9%EA%B2%80%EC%83%89%EA%B2%B0%EA%B3%BC%ED%8E%98%EC%9D%B4%EC%A7%80&t_click=%EB%B8%8C%EB%9E%9C%EB%93%9C%EA%B4%80%EB%B0%94%EB%A1%9C%EA%B0%80%EA%B8%B0&t_brand_name=%EA%B0%80%ED%9E%88"
                , "브렌드/가히"
                , "브렌드"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getBrandShopDetail.do?onlBrndCd=A002820&t_page=%ED%86%B5%ED%95%A9%EA%B2%80%EC%83%89%EA%B2%B0%EA%B3%BC%ED%8E%98%EC%9D%B4%EC%A7%80&t_click=%EB%B8%8C%EB%9E%9C%EB%93%9C%EA%B4%80%EB%B0%94%EB%A1%9C%EA%B0%80%EA%B8%B0&t_brand_name=%ED%86%A0%EB%A6%AC%EB%93%A0"
                , "브렌드/토리든"
                , "브렌드"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getBrandShopDetail.do?onlBrndCd=A001465&t_page=%ED%86%B5%ED%95%A9%EA%B2%80%EC%83%89%EA%B2%B0%EA%B3%BC%ED%8E%98%EC%9D%B4%EC%A7%80&t_click=%EB%B8%8C%EB%9E%9C%EB%93%9C%EA%B4%80%EB%B0%94%EB%A1%9C%EA%B0%80%EA%B8%B0&t_brand_name=%EC%BD%94%EC%8A%A4%EC%95%8C%EC%97%91%EC%8A%A4"
                , "브렌드/코스알엑스"
                , "브렌드"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getBrandShopDetail.do?onlBrndCd=A003477&t_page=%ED%86%B5%ED%95%A9%EA%B2%80%EC%83%89%EA%B2%B0%EA%B3%BC%ED%8E%98%EC%9D%B4%EC%A7%80&t_click=%EB%B8%8C%EB%9E%9C%EB%93%9C%EA%B4%80%EB%B0%94%EB%A1%9C%EA%B0%80%EA%B8%B0&t_brand_name=%EB%84%98%EB%B2%84%EC%A6%88%EC%9D%B8"
                , "브렌드/넘버즈인"
                , "브렌드"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getBrandShopDetail.do?onlBrndCd=A003923&t_page=%ED%86%B5%ED%95%A9%EA%B2%80%EC%83%89%EA%B2%B0%EA%B3%BC%ED%8E%98%EC%9D%B4%EC%A7%80&t_click=%EB%B8%8C%EB%9E%9C%EB%93%9C%EA%B4%80%EB%B0%94%EB%A1%9C%EA%B0%80%EA%B8%B0&t_brand_name=%EC%A1%B0%EC%84%A0%EB%AF%B8%EB%85%80"
                , "브렌드/조선미녀"
                , "브렌드"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getBrandShopDetail.do?onlBrndCd=A012973&t_page=%ED%86%B5%ED%95%A9%EA%B2%80%EC%83%89%EA%B2%B0%EA%B3%BC%ED%8E%98%EC%9D%B4%EC%A7%80&t_click=%EB%B8%8C%EB%9E%9C%EB%93%9C%EA%B4%80%EB%B0%94%EB%A1%9C%EA%B0%80%EA%B8%B0&t_brand_name=%EC%84%A4%ED%99%94%EC%88%98"
                , "브렌드/설화수"
                , "브렌드"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getBrandShopDetail.do?onlBrndCd=A003829&t_page=%ED%86%B5%ED%95%A9%EA%B2%80%EC%83%89%EA%B2%B0%EA%B3%BC%ED%8E%98%EC%9D%B4%EC%A7%80&t_click=%EB%B8%8C%EB%9E%9C%EB%93%9C%EA%B4%80%EB%B0%94%EB%A1%9C%EA%B0%80%EA%B8%B0&t_brand_name=%EC%98%A8%EA%B7%B8%EB%A6%AC%EB%94%94%EC%96%B8%EC%B8%A0"
                , "브렌드/온그리디언츠"
                , "브렌드"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getBrandShopDetail.do?onlBrndCd=A012875&t_page=%ED%86%B5%ED%95%A9%EA%B2%80%EC%83%89%EA%B2%B0%EA%B3%BC%ED%8E%98%EC%9D%B4%EC%A7%80&t_click=%EB%B8%8C%EB%9E%9C%EB%93%9C%EA%B4%80%EB%B0%94%EB%A1%9C%EA%B0%80%EA%B8%B0&t_brand_name=%EB%82%98%EB%A5%B4%EC%B9%B4"
                , "브렌드/나르카"
                , "브렌드"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getBrandShopDetail.do?onlBrndCd=A002758&t_page=%ED%86%B5%ED%95%A9%EA%B2%80%EC%83%89%EA%B2%B0%EA%B3%BC%ED%8E%98%EC%9D%B4%EC%A7%80&t_click=%EB%B8%8C%EB%9E%9C%EB%93%9C%EA%B4%80%EB%B0%94%EB%A1%9C%EA%B0%80%EA%B8%B0&t_brand_name=%EB%8B%AC%EB%B0%94"
                , "브렌드/달바"
                , "브렌드"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getBrandShopDetail.do?onlBrndCd=A009151&t_page=%ED%86%B5%ED%95%A9%EA%B2%80%EC%83%89%EA%B2%B0%EA%B3%BC%ED%8E%98%EC%9D%B4%EC%A7%80&t_click=%EB%B8%8C%EB%9E%9C%EB%93%9C%EA%B4%80%EB%B0%94%EB%A1%9C%EA%B0%80%EA%B8%B0&t_brand_name=%EC%86%8C%EC%9A%B8"
                , "브렌드/소울"
                , "브렌드"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getBrandShopDetail.do?onlBrndCd=A002369&t_page=%EC%83%81%ED%92%88%EC%83%81%EC%84%B8&t_click=%EB%B8%8C%EB%9E%9C%EB%93%9C%EA%B4%80_%ED%95%98%EB%8B%A8&t_brand_name=%ED%81%90%EC%96%B4"
                , "브렌드/큐어(김정문&알로에)"
                , "브렌드"});*/

        // 부족
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getMCategoryList.do?dispCatNo=1000001001000040001&fltDispCatNo=&prdSort=01&pageIdx=1&rowsPerPage=24&searchTypeSort=btn_thumb&plusButtonFlag=N&isLoginCnt=2&aShowCnt=0&bShowCnt=0&cShowCnt=0&trackingCd=Cat1000001001000040001_Small&amplitudePageGubun=SMALL_CATE&t_page=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EA%B4%80&t_click=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EC%83%81%EC%84%B8_%EC%86%8C%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC&midCategory=%EC%98%A4%EC%9D%BC%2F%EB%B0%A4&smallCategory=%EC%86%8C_%ED%81%B4%EB%A0%8C%EC%A7%95%EC%98%A4%EC%9D%BC&checkBrnds=&lastChkBrnd=&t_3rd_category_type=%EC%86%8C_%ED%81%B4%EB%A0%8C%EC%A7%95%EC%98%A4%EC%9D%BC"
                , "클렌징/오일&밤/클렌징오일"
                , "카테고리"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getMCategoryList.do?dispCatNo=1000001001000010001&fltDispCatNo=&prdSort=01&pageIdx=1&rowsPerPage=24&searchTypeSort=btn_thumb&plusButtonFlag=N&isLoginCnt=1&aShowCnt=0&bShowCnt=0&cShowCnt=0&trackingCd=Cat1000001001000010001_Small&amplitudePageGubun=SMALL_CATE&t_page=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EA%B4%80&t_click=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EC%83%81%EC%84%B8_%EC%86%8C%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC&midCategory=%ED%81%B4%EB%A0%8C%EC%A7%95%ED%8F%BC%2F%EC%A0%A4&smallCategory=%EC%86%8C_%ED%81%B4%EB%A0%8C%EC%A7%95%ED%8F%BC%2F%EC%A0%A4&checkBrnds=&lastChkBrnd=&t_3rd_category_type=%EC%86%8C_%ED%81%B4%EB%A0%8C%EC%A7%95%ED%8F%BC%2F%EC%A0%A4"
                , "클렌징/클렌징폼%젤"
                , "카테고리"});
        crawlerUrls.add(new String[]{
                "https://www.oliveyoung.co.kr/store/display/getMCategoryList.do?dispCatNo=1000001000300080005&fltDispCatNo=&prdSort=01&pageIdx=1&rowsPerPage=24&searchTypeSort=btn_thumb&plusButtonFlag=N&isLoginCnt=5&aShowCnt=0&bShowCnt=0&cShowCnt=0&trackingCd=Cat1000001000300080005_Small&amplitudePageGubun=SMALL_CATE&t_page=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EA%B4%80&t_click=%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%EC%83%81%EC%84%B8_%EC%86%8C%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC&midCategory=%EB%A6%BD%EB%B0%A4&smallCategory=%EC%86%8C_%EB%A6%BD%EC%98%A4%EC%9D%BC%2F%ED%94%8C%EB%9F%BC%ED%8D%BC&checkBrnds=&lastChkBrnd=&t_3rd_category_type=%EC%86%8C_%EB%A6%BD%EC%98%A4%EC%9D%BC%2F%ED%94%8C%EB%9F%BC%ED%8D%BC"
                , "바디케어/립오일&플럼퍼"
                , "카테고리"});


        ExecutorService executor = Executors.newFixedThreadPool(12);

        for (String[] data : crawlerUrls) {
            maker(executor, data[0], data[1], data[2]);
        }

        executor.shutdown();

        System.out.println("모든 페이지 크롤링이 완료되었습니다.");
    }

    private static void maker(ExecutorService executor, String mainUrl, String file, String map) {
        List<String> productUrls;

        switch (map) {
            case "검색" -> productUrls = OliveYoungSearchCrawler.getProductUrls(mainUrl);
            case "브렌드" -> productUrls = OliveYoungBrendCrawler.getProductUrls(mainUrl);
            default -> productUrls = OliveYoungCategoryCrawler.getProductUrls(mainUrl);
        }

        for (String url : productUrls) {
            executor.submit(() -> OliveYoungDetailCrawler.olive(new String[]{url}, file));
        }

    }
}
