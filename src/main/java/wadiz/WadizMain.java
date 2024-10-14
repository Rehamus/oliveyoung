package wadiz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WadizMain {
    public static void main(String[] args) {

        List<String[]> crawlerUrls = new ArrayList<>();

        crawlerUrls.add(new String[]{
                "https://www.wadiz.kr/web/campaign/detail/qa/245521"
                , "와디즈/1"
        });
        crawlerUrls.add(new String[]{
                "https://www.wadiz.kr/web/campaign/detail/qa/134880"
                , "와디즈/2"
        });
        crawlerUrls.add(new String[]{
                "https://www.wadiz.kr/web/campaign/detail/qa/169943"
                , "와디즈/3"
        });
        crawlerUrls.add(new String[]{
                "https://www.wadiz.kr/web/campaign/detail/qa/188953"
                , "와디즈/4"
        });
        crawlerUrls.add(new String[]{
                "https://www.wadiz.kr/web/campaign/detail/qa/169551"
                , "와디즈/5"
        });
        crawlerUrls.add(new String[]{
                "https://www.wadiz.kr/web/campaign/detail/qa/251969"
                , "와디즈/6"
        });

        for (String[] data : crawlerUrls) {
            wadizmaker(data[0], data[1]);
        }


    }

    private static void wadizmaker(String mainUrl, String file) {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        String[] map = {"comment", "satisfaction", "signature"};

        for (int i = 0; i < map.length; i++) {
            int finalI = i;
            executor.submit(
                    () -> WadizDetailCrawler.wadiz(new String[]{mainUrl + "/" + map[finalI]}, file, map[finalI])

            );
            System.out.println( map[finalI] +" 페이지 크롤링이 완료되었습니다.");

        }

        executor.shutdown();


    }
}
