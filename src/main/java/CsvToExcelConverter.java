import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CsvToExcelConverter {
    public static void main(String[] args) {
        String folderPath = "D:\\DD\\정리\\oliveyoung\\review\\쿠팡\\클렌징&필링\\클렌징오일"; // CSV 파일들이 있는 폴더 경로

        try {
            convertEachCsvToExcel(folderPath);
            System.out.println("모든 CSV 파일이 각기 다른 엑셀 파일로 변환되었습니다.");
        } catch (IOException e) {
            System.err.println("파일 변환 중 오류 발생: " + e.getMessage());
        }
    }

    public static void convertEachCsvToExcel(String folderPath) throws IOException {
        Files.walk(Paths.get(folderPath))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".csv"))
                .forEach(path -> {
                    String csvFilePath = path.toString();
                    String excelFilePath = csvFilePath.replace(".csv", ".xlsx");

                    try {
                        convertCsvToExcel(csvFilePath, excelFilePath);
                        System.out.println(csvFilePath + " 파일이 " + excelFilePath + " 파일로 변환되었습니다.");
                    } catch (IOException e) {
                        System.err.println("파일 처리 중 오류 발생: " + csvFilePath + " - " + e.getMessage());
                    }
                });
    }

    public static void convertCsvToExcel(String csvFilePath, String excelFilePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
             Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Sheet1");
            String line;
            int rowNum = 0;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < values.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(values[i]);
                }
            }

            try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
                workbook.write(fileOut);
            }
        }
    }
}
