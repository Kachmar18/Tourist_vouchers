package tourist_vouchers.v17_tourist_vouchers.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tourist_vouchers.v17_tourist_vouchers.model.ClientChoice;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReportUtil {

    public static void exportClientsToExcel(List<ClientChoice> clients, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Клієнти з турами");

            // Заголовок
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Ім’я", "Телефон", "Дата бронювання", "ID туру"};

            // Стиль заголовка
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Стиль звичайних клітинок
            CellStyle bodyStyle = workbook.createCellStyle();
            bodyStyle.setWrapText(true);
            bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Дані
            int rowNum = 1;
            for (ClientChoice c : clients) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(c.getId_client());
                row.createCell(1).setCellValue(c.getName());
                row.createCell(2).setCellValue(c.getPhone());
                row.createCell(3).setCellValue(c.getBookingDate().toString());
                row.createCell(4).setCellValue(c.getId_selectedTour());
            }

            // Автоширина
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Запис у файл
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



