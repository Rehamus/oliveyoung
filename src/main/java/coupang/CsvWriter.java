package coupang;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CsvWriter {

    public static void saveToCSVForCoupang(List<List<String>> data, List<String> headers, String folder, String fileName) {
        // 저장 경로를 포함한 파일 경로를 설정
        String saveDirectory = "./review/" + folder;
        Path directoryPath = Paths.get(saveDirectory);
        String fullPath = Paths.get(saveDirectory, fileName).toString();

        // 디렉토리가 없으면 생성
        try {
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
                System.out.println("폴더가 생성되었습니다: " + directoryPath);
            }

            // 파일 쓰기 시작
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fullPath), StandardCharsets.UTF_8))) {
                writer.write("\uFEFF"); // UTF-8 BOM 추가

                // CSV 파일의 헤더를 추가
                writer.write(String.join(",", headers));
                writer.newLine();

                // 리뷰 데이터를 작성
                for (List<String> row : data) {
                    while (row.size() < headers.size()) {
                        row.add(""); // 빈 셀로 채우기
                    }

                    if (row.size() > 5) {
                        row.set(5, "\"" + row.get(5).replace("\n", " ").replace("\r", " ") + "\"");
                    }

                    String line = String.join(",", row);
                    writer.write(line);
                    writer.newLine();
                }
                System.out.println("CSV 파일이 저장되었습니다: " + fullPath);
            }
        } catch (IOException e) {
            System.err.println("파일 저장 중 오류 발생: " + e.getMessage());
        }
    }

    public static void saveUrlsToCSV(List<String> urls, String folder, String fileName) {
        // 저장 경로를 포함한 파일 경로를 설정
        String saveDirectory = "./review/" + folder;
        Path directoryPath = Paths.get(saveDirectory);
        String fullPath = Paths.get(saveDirectory, fileName).toString();

        // 디렉토리가 없으면 생성
        try {
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
                System.out.println("폴더가 생성되었습니다: " + directoryPath);
            }

            // 파일 쓰기 시작
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fullPath), StandardCharsets.UTF_8))) {
                for (String url : urls) {
                    writer.write(url);
                    writer.newLine();
                }
                System.out.println("CSV 파일 저장 완료: " + fullPath);
            }
        } catch (IOException e) {
            System.err.println("파일 저장 중 오류 발생: " + e.getMessage());
        }
    }
}
