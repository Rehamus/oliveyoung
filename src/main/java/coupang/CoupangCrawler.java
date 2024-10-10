package coupang;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static coupang.WebDriverSetting.*;

public class CoupangCrawler {

//    static JavascriptExecutor js;
//    static WebDriverWait wait;

    //각 상품의 상품평을 크롤링
    public static void pageCrawler(String url, WebDriver driver, WebDriverWait wait) {
        String productId = extractProductId(url);

        driver.get(url);
        handlePopupIfPresent(driver);
        afterUrl(driver, wait);

        String productNameLocation = ".prod-buy-header__title";
        String productName = getTextOfElement(productNameLocation, wait)
                .replaceAll("[\\\\/:*?\"<>|]", "") + productId;

        List<List<String>> reviewData = new ArrayList<>();
        List<String> headers = new ArrayList<>(Arrays.asList("Date", "Rate", "Nickname", "Option", "Title", "Content"));

        int checkedCount = checkReviewCount();
        if (checkedCount <= 0) {
            List<String> emptyRow = new ArrayList<>(Arrays.asList(checkedCount + "개", "", "", "", ""));
            reviewData.add(emptyRow);

            System.out.println("등록된 리뷰가 없어 driver를 종료합니다.");
            driver.quit();
            return;
//            System.exit(0);
        } else {
            try {
                WebElement reviewsLink = driver.findElement(By.cssSelector("a.moveAnchor#prod-review-nav-link .count"));
                reviewsLink.click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.sdp-review__article.js_reviewArticleContainer > section.js_reviewArticleListContainer")));
            } catch (Exception e) {
                System.out.println("리뷰 클릭중 오류 발생: " + e.getMessage());
            }

            boolean flag = true;
            int currentPage = 1;
            while (flag) {
                System.out.println("현재 페이지: " + currentPage);

                String reviewContainerLocation = "div.sdp-review__article.js_reviewArticleContainer > section.js_reviewArticleListContainer";

                try {
                    WebElement reviewContainer = driver.findElement(By.cssSelector(reviewContainerLocation));
                    List<WebElement> reviewArticles = reviewContainer.findElements(By.cssSelector("article"));
                    if (!reviewArticles.isEmpty()) {
                        String reviewLocation = "div.sdp-review__article.js_reviewArticleContainer > section.js_reviewArticleListContainer > article";
                        int reviewCount = countInCurrentPage(reviewLocation, driver);
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(reviewLocation)));
//                        System.out.println("현재 페이지 리뷰: " + reviewCount + "개");

                        for (int i = 1; i <= reviewCount; i++) {
                            try {
                                String baseLocation = "div.sdp-review__article.js_reviewArticleContainer > section.js_reviewArticleListContainer > article:nth-child(" + (2 + i) + ")";
//                                System.out.println(currentPage + "페이지의" + i + "번 상품평");

                                String dateLocation = baseLocation + " > div.sdp-review__article__list__info > div.sdp-review__article__list__info__product-info > div.sdp-review__article__list__info__product-info__reg-date";
                                String date = getTextOfElement(dateLocation, wait);

                                String rateLocation = baseLocation + " > div.sdp-review__article__list__info > div.sdp-review__article__list__info__product-info > div.sdp-review__article__list__info__product-info__star-gray > div.sdp-review__article__list__info__product-info__star-orange.js_reviewArticleRatingValue";
                                String tagName = "data-rating";
                                String rate = getTextInTagOfElement(rateLocation, tagName, wait);

                                String nicknameLocation = baseLocation + " > div.sdp-review__article__list__info > div.sdp-review__article__list__info__user > span";
                                String nickname = getTextOfElement(nicknameLocation, wait);

                                String optionLocation = baseLocation + "> div.sdp-review__article__list__info > div.sdp-review__article__list__info__product-info__name";
                                String option = getTextOfElement(optionLocation, wait);

                                String titleLocation = baseLocation + " > div.sdp-review__article__list__headline";
                                String reviewTitle = getTextOfElementIfExist(titleLocation, "title", driver, wait);

                                String reviewContentLocation = baseLocation + " > div.sdp-review__article__list__review.js_reviewArticleContentContainer > div";
                                String reviewContent = getTextOfElementIfExist(reviewContentLocation, "content", driver, wait);

                                // rowData를 headers 크기만큼 미리 생성
                                List<String> rowData = new ArrayList<>(Arrays.asList(date, rate, nickname, option, reviewTitle, reviewContent));
                                while (rowData.size() < headers.size()) {
                                    rowData.add(""); // 빈 셀로 채우기
                                }

                                rowData = extractSurveyDataIfExist(baseLocation, headers, rowData, driver, wait);
                                reviewData.add(rowData);

                            } catch (Exception e) {
                                System.out.println("크롤링 중 예외발생: " + e.getMessage());
                                System.out.println("상품평이 없습니다.");
                                break;
                            }
                        }
                        System.out.println(currentPage + " 페이지 크롤링 완료");

                        flag = goToNextPage(currentPage, driver, wait, reviewLocation);
                        if (flag) {
                            currentPage++;
                        } else {
                            // 상품평이 없는 경우 처리
                            System.out.println("더 이상 상품평이 없습니다.");
                        }

                    } else {
                        System.out.println(currentPage + "페이지에 등록된 상품평이 없습니다.");
                        flag = false;
                    }

                } catch (NoSuchElementException e) {
                    System.out.println("리뷰 컨테이너를 찾지 못했습니다: " + e.getMessage());
                    System.out.println(currentPage + "에 등록된 상품평이 없습니다");
                    break;
                }

            }
            randomTimeSleep();
//            CsvWriter.saveToCSVForCoupang(reviewData, headers, productName + ".csv");
        }
        driver.quit();
    }

    public static void pageCrawler_v2(String url, WebDriver driver, WebDriverWait wait ,String fileName) {
        String productId = extractProductId(url);

        driver.get(url);
        handlePopupIfPresent(driver);
        afterUrl(driver, wait);

        String productNameLocation = ".prod-buy-header__title";
        String productName = getTextOfElement(productNameLocation, wait)
                .replaceAll("[\\\\/:*?\"<>|]", "") + productId;

        List<List<String>> reviewData = new ArrayList<>();
        List<String> headers = new ArrayList<>(Arrays.asList("Date", "Rate", "Nickname", "Option", "Title", "Content"));

        int checkedCount = checkReviewCount();
        if (checkedCount <= 0) {
            List<String> emptyRow = new ArrayList<>(Arrays.asList(checkedCount + "개", "", "", "", ""));
            reviewData.add(emptyRow);

            System.out.println("등록된 리뷰가 없어 driver를 종료합니다.");
            driver.quit();
            return;
        } else {
            try {
                WebElement reviewsLink = driver.findElement(By.cssSelector("a.moveAnchor#prod-review-nav-link .count"));
                reviewsLink.click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.sdp-review__article.js_reviewArticleContainer > section.js_reviewArticleListContainer")));
            } catch (Exception e) {
                System.out.println("리뷰 클릭중 오류 발생: " + e.getMessage());
            }

            boolean flag = true;
            int currentPage = 1;
            while (flag) {
                System.out.println("현재 페이지: " + currentPage);

                String reviewContainerLocation = "div.sdp-review__article.js_reviewArticleContainer > section.js_reviewArticleListContainer";

                try {
                    WebElement reviewContainer = driver.findElement(By.cssSelector(reviewContainerLocation));
                    List<WebElement> reviewArticles = reviewContainer.findElements(By.cssSelector("article"));
                    if (!reviewArticles.isEmpty()) {
                        String reviewLocation = "div.sdp-review__article.js_reviewArticleContainer > section.js_reviewArticleListContainer > article";
                        int reviewCount = countInCurrentPage(reviewLocation, driver);
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(reviewLocation)));
//                        System.out.println("현재 페이지 리뷰: " + reviewCount + "개");

                        for (int i = 1; i <= reviewCount; i++) {
                            try {
                                String baseLocation = "div.sdp-review__article.js_reviewArticleContainer > section.js_reviewArticleListContainer > article:nth-child(" + (2 + i) + ")";
//                                System.out.println(currentPage + "페이지의" + i + "번 상품평");

                                String dateLocation = baseLocation + " > div.sdp-review__article__list__info > div.sdp-review__article__list__info__product-info > div.sdp-review__article__list__info__product-info__reg-date";
                                String date = getTextOfElement(dateLocation, wait);

                                String rateLocation = baseLocation + " > div.sdp-review__article__list__info > div.sdp-review__article__list__info__product-info > div.sdp-review__article__list__info__product-info__star-gray > div.sdp-review__article__list__info__product-info__star-orange.js_reviewArticleRatingValue";
                                String tagName = "data-rating";
                                String rate = getTextInTagOfElement(rateLocation, tagName, wait);

                                String nicknameLocation = baseLocation + " > div.sdp-review__article__list__info > div.sdp-review__article__list__info__user > span";
                                String nickname = getTextOfElement(nicknameLocation, wait);

                                String optionLocation = baseLocation + "> div.sdp-review__article__list__info > div.sdp-review__article__list__info__product-info__name";
                                String option = getTextOfElement(optionLocation, wait);

                                String titleLocation = baseLocation + " > div.sdp-review__article__list__headline";
                                String reviewTitle = getTextOfElementIfExist(titleLocation, "title", driver, wait);

                                String reviewContentLocation = baseLocation + " > div.sdp-review__article__list__review.js_reviewArticleContentContainer > div";
                                String reviewContent = getTextOfElementIfExist(reviewContentLocation, "content", driver, wait);

                                // rowData를 headers 크기만큼 미리 생성
                                List<String> rowData = new ArrayList<>(Arrays.asList(date, rate, nickname, option, reviewTitle, reviewContent));
                                while (rowData.size() < headers.size()) {
                                    rowData.add(""); // 빈 셀로 채우기
                                }

                                rowData = extractSurveyDataIfExist(baseLocation, headers, rowData, driver, wait);
                                reviewData.add(rowData);

                            } catch (Exception e) {
                                System.out.println("크롤링 중 예외발생: " + e.getMessage());
                                System.out.println("상품평이 없습니다.");
                                break;
                            }
                        }
                        System.out.println(currentPage + " 페이지 크롤링 완료");

                        flag = goToNextPage(currentPage, driver, wait, reviewLocation);
                        if (flag) {
                            currentPage++;
                        } else {
                            // 상품평이 없는 경우 처리
                            System.out.println("더 이상 상품평이 없습니다.");
                        }

                    } else {
                        System.out.println(currentPage + "페이지에 등록된 상품평이 없습니다.");
                        flag = false;
                    }

                } catch (NoSuchElementException e) {
                    System.out.println("리뷰 컨테이너를 찾지 못했습니다: " + e.getMessage());
                    System.out.println(currentPage + "에 등록된 상품평이 없습니다");
                    break;
                }

            }
            randomTimeSleep();
            CsvWriter.saveToCSVForCoupang(reviewData, headers, fileName,productName + ".csv");
        }
        driver.quit();
    }

    private static String extractProductId(String url) {

        // 정규식: 'products/' 다음에 오는 숫자를 찾음
        String regex = "products/(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        // 패턴이 매칭되면 첫 번째 그룹(숫자)을 반환
        if (matcher.find()) {
            return matcher.group(1); // 매칭된 숫자 부분
        }

        // 매칭되지 않으면 null 반환
        return null;

    }

    private static boolean goToNextPage(int currentPage, WebDriver driver, WebDriverWait wait, String reviewLocation) {
        try {
            // 다음 페이지 번호 버튼이 있는지 확인 후 있으면 클릭
            int nextPage = currentPage + 1;
            String selectedNextPageButton = "button.sdp-review__article__page__num.js_reviewArticlePageBtn[data-page='" + nextPage + "']";
            List<WebElement> pageButtons = driver.findElements(By.cssSelector(selectedNextPageButton));

            if (!pageButtons.isEmpty()) {

                WebElement nextPageButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selectedNextPageButton)));
                nextPageButton.click();
                System.out.println("clicked: " + nextPage);
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(reviewLocation)));
                randomTimeSleep();
                return true;
            } else {
                // 페이지 번호 버튼이 없으면 '다음 페이지' 버튼이 있는지 확인
                String nextButtonSelector = "button.sdp-review__article__page__next.sdp-review__article__page__next--active.js_reviewArticlePageNextBtn";
                List<WebElement> nextButtons = driver.findElements(By.cssSelector(nextButtonSelector));

                if (!nextButtons.isEmpty()) {
                    // '다음 페이지' 버튼이 있으면 클릭
                    WebElement nextPageButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(nextButtonSelector)));
                    nextPageButton.click();
                    System.out.println("clicked next page");

                    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(reviewLocation)));

                    randomTimeSleep();
                    return true;
                } else {
                    // 더 이상 클릭할 버튼이 없으면 크롤링 종료
                    System.out.println("더 이상 페이지가 없습니다. 크롤링을 종료합니다.");
//                    driver.quit();
                    return false;
                }
            }

        } catch (Exception e) {

            System.out.println("페이지를 넘기는 중 오류 발생: " + e.getMessage());
            driver.quit();
            return false;

        }
    }

    private static List<String> extractSurveyDataIfExist(String baseLocation, List<String> headers, List<String> rowData, WebDriver driver, WebDriverWait wait) {
        WebElement review = driver.findElement(By.cssSelector(baseLocation));
        String surveyQuestionSelector = "span.sdp-review__article__list__survey__row__question";
        String surveyAnswerSelector = "span.sdp-review__article__list__survey__row__answer";
        List<WebElement> surveyQuestions = review.findElements(By.cssSelector(surveyQuestionSelector));
        int surveyCount = surveyQuestions.size();
//        System.out.println("설문: " + surveyCount + "개");

        if (surveyCount > 0) {
            for (int j = 1; j <= surveyCount; j++) {
                String surveyQuestionLocation = baseLocation + "> div.sdp-review__article__list__survey > div:nth-child(" + j + ") > " + surveyQuestionSelector;
                String surveyAnswerLocation = baseLocation + "> div.sdp-review__article__list__survey > div:nth-child(" + j + ") > " + surveyAnswerSelector;

                String questionText = getTextOfElement(surveyQuestionLocation, wait);
                String answerText = getTextOfElement(surveyAnswerLocation, wait);

                // 동일한 questionText가 이미 존재하는지 확인
                if (headers.contains(questionText)) {// 이미 있는 경우, 해당 컬럼 위치에 answerText 저장
                    int index = headers.indexOf(questionText);
                    while (rowData.size() <= index) {
                        rowData.add(""); // 빈 셀로 채우기
                    }
                    rowData.set(index, answerText);
                } else {// 없는 경우, 새로운 컬럼 추가
                    headers.add(questionText);
                    int index = headers.indexOf(questionText);
                    while (rowData.size() <= index) {
                        rowData.add(""); // 빈 셀로 채우기
                    }
                    rowData.set(index, answerText);
                }
            }
        }
        return rowData;
    }

    private static void pageDownMultipleTimes(int n, WebDriver driver) {
        for (int i = 0; i < n; i++) {
            randomTimeSleep();
            pageDown(i, driver);
        }
    }

    private static int checkReviewCount() {
        String countedReviewLocation = "#prod-review-nav-link > .count";
        String countText = getTextOfElementWithJS(countedReviewLocation);
        String number = countText.replace("개 상품평", "").replace(",", "").trim();
        int checkedCount = Integer.parseInt(number);
        System.out.println("총 리뷰 개수: " + checkedCount);
        return checkedCount;
    }

    private static String getTextOfElementWithJS(String countedReviewLocation) {

        try {
            // 요소가 나타날 때까지 대기
            String countText = (String) js.executeScript("return document.querySelector(arguments[0]).innerText;", countedReviewLocation);
            System.out.println("extracted text: " + countText);
            return countText;
        } catch (Exception e) {
            System.out.println("JavaScript를 사용하여 텍스트 추출 중 오류 발생: " + e.getMessage());
            return null;
        }
    }

    // 페이지 다운
    private static void pageDown(int i, WebDriver driver) {
        WebElement body = driver.findElement(By.tagName("body"));
        body.sendKeys(Keys.PAGE_DOWN);
        System.out.println("Page down count: " + (i + 1));
    }

    private static int countInCurrentPage(String location, WebDriver driver) {
        List<WebElement> elements = driver.findElements(By.cssSelector(location));
        return elements.size();
    }

    // 완전히 로드될 때까지 대기 후 리뷰 탭 클릭
    private static void clickReviewTab(WebDriverWait wait) throws Exception {
        try {
            wait.until(webDriver -> (JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete");
            randomTimeSleep();
            WebElement reviewTab = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("ul.tab-titles > li[name='review']")));
            randomTimeSleep();

            reviewTab.click(); // 리뷰 탭 클릭
            System.out.println("clicked reviewTab");
            randomTimeSleep();

        } catch (Exception e) {
            System.out.println("리뷰 탭 클릭 중 오류 발생: " + e.getMessage());
            throw e;
        }
    }

    private static void randomTimeSleep() {
        try {
            int randomSleepTime = ThreadLocalRandom.current().nextInt(4510, 6400);
            System.out.println("Sleeping for " + randomSleepTime + " milliseconds");

            Thread.sleep(randomSleepTime);  // 랜덤한 시간 동안 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
