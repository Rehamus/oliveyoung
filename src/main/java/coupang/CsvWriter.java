package coupang;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CsvWriter {

    public static void saveToCSVForCoupang(List<List<String>> data, List<String> headers, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            writer.write("\uFEFF");

            // CSV 파일의 헤더를 추가
            writer.write(String.join(",", headers));
            writer.newLine(); // 줄바꿈 추가

            // 리뷰 데이터를 작성
            for (List<String> row : data) {

                // 행이 headers의 크기와 같지 않으면 오류가 발생할 수 있으므로
                while (row.size() < headers.size()) {
                    row.add(""); // 빈 셀로 채우기
                }


                if (row.size() > 5) { // row의 크기를 체크
                    row.set(5, "\"" + row.get(5).replace("\n", " ").replace("\r", " ") + "\"");
                }

                String line = String.join(",", row); // CSV 포맷으로 변환
                writer.write(line);
                writer.newLine(); // 각 row마다 줄바꿈 추가
            }
            System.out.println("CSV 파일이 저장되었습니다: " + fileName);
        } catch (IOException e) {
            System.err.println("파일 저장 중 오류 발생: " + e.getMessage());
        }
    }

    public static void saveUrlsToCSV(List<String> urls, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            // 각 URL을 한 줄씩 저장
            for (String url : urls) {
                writer.append(url);
                writer.append("\n");
            }
            System.out.println("CSV 파일 저장 완료: " + fileName);
        } catch (IOException e) {
            System.out.println("CSV 파일 저장 중 오류 발생: " + e.getMessage());
        }
    }
}
