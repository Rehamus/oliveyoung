package coupang;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    public static List<String> readCsvFile(String filePath) {
        List<String> urls = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                urls.add(line.trim()); // 각 줄을 리스트에 추가
            }
        } catch (IOException e) {
            System.out.println("CSV 파일 읽기 중 오류 발생: " + e.getMessage());
        }

        return urls;
    }
}
