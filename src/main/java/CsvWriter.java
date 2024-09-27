import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CsvWriter {

    public static void saveToCSV(List<String[]> data, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            // CSV 파일의 헤더를 추가
            String[] header = {"Date", "Rate", "ID", "Skin Type", "Select 1 Title", "Select 1 Content", "Select 2 Title", "Select 2 Content", "Review Text"};
            writer.write(String.join(",", header));
            writer.newLine(); // 줄바꿈 추가

            // 리뷰 데이터를 작성
            for (String[] row : data) {
                // Review Text (마지막 요소)에서 줄바꿈을 제거하고 따옴표로 감싸기
                row[8] = "\"" + row[8].replace("\n", " ").replace("\r", " ") + "\""; // 줄바꿈을 공백으로 대체하고 텍스트를 따옴표로 감싸기

                String line = String.join(",", row); // CSV 포맷으로 변환
                writer.write(line);
                writer.newLine(); // 각 row마다 줄바꿈 추가
            }
            System.out.println("CSV 파일이 저장되었습니다: " + fileName);
        } catch (IOException e) {
            System.err.println("파일 저장 중 오류 발생: " + e.getMessage());
        }
    }
}
