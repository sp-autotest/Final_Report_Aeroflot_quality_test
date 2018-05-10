import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by mycola on 07.05.2018.
 */
public class Excel {


    @SuppressWarnings("deprecation")
    public static void writeIntoExcel(String file) {
        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet("Тестовые данные и результаты");
        // Создаем заголовок
        createTitle(book, sheet);
        // Создаем группу
        createGroup(3, book, sheet);

        // Записываем всё в файл
        try {
            book.write(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createTitle(XSSFWorkbook book, XSSFSheet sheet) {
        // Обьединяем ячейки в заголовке
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$B$2"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$C$1:$C$2"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$D$1:$F$1"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$G$1:$G$2"));
        // Создаем стили для заголовка
        XSSFCellStyle style1 = createStyleForTitle1(book);
        XSSFCellStyle style2 = createStyleForTitle2(book);
        // Создаем строки заголовка
        XSSFRow titleRow1 = sheet.createRow(0);
        XSSFRow titleRow2 = sheet.createRow(1);
        // Создаем ячейки заголовка
        XSSFCell с0 = titleRow1.createCell(0);
        с0.setCellValue("№");
        с0.setCellStyle(style1);
        XSSFCell c1 = titleRow1.createCell(2);
        c1.setCellValue("Тестовый сценарий");
        c1.setCellStyle(style1);
        XSSFCell c2 = titleRow1.createCell(3);
        c2.setCellValue("Тестовый данные");
        c2.setCellStyle(style1);
        XSSFCell c3 = titleRow2.createCell(3);
        c3.setCellValue("Бронь");
        c3.setCellStyle(style2);
        XSSFCell c4 = titleRow2.createCell(4);
        c4.setCellValue("Номер карты");
        c4.setCellStyle(style2);
        XSSFCell c5 = titleRow2.createCell(5);
        c5.setCellValue("Номера документов");
        c5.setCellStyle(style2);
        XSSFCell c6 = titleRow1.createCell(6);
        c6.setCellValue("Результат проверки");
        c6.setCellStyle(style1);
        XSSFCell c7 = titleRow2.createCell(6);
        c7.setCellStyle(style1);
        XSSFCell c8 = titleRow2.createCell(0);
        c8.setCellStyle(style1);
        XSSFCell c9 = titleRow2.createCell(1);
        c9.setCellStyle(style1);
        XSSFCell c10 = titleRow2.createCell(2);
        c10.setCellStyle(style1);
        // Меняем размер столбца
        sheet.setColumnWidth(0, 6*256); //6 characters wide
        sheet.setColumnWidth(1, 5*256); //5 characters wide
        sheet.setColumnWidth(2, 50*256); //40 characters wide
        sheet.setColumnWidth(3, 25*256); //15 characters wide
        sheet.setColumnWidth(4, 30*256); //20 characters wide
        sheet.setColumnWidth(5, 60*256); //50 characters wide
        sheet.setColumnWidth(6, 30*256); //20 characters wide
    }

    private static void createGroup(int row, XSSFWorkbook book, XSSFSheet sheet) {
        // Обьединяем ячейки в группе
        sheet.addMergedRegion(CellRangeAddress.valueOf("$B$" + row + ":$G$" + row));
        // Создаем стиль для группы
        XSSFCellStyle style = createStyleForGroup(book);
        // Создаем строку группы
        XSSFRow groupRow = sheet.createRow(row-1);
        // Создаем ячейки заголовка
        XSSFCell с0 = groupRow.createCell(0);
        с0.setCellValue("1.1");
        с0.setCellStyle(style);
        XSSFCell с1 = groupRow.createCell(1);
        с1.setCellValue("Группа автотестов  - покупка билета 1 взрослый, услуги: «Полетная страховка», «Медицинская страховка» (классическая),  «Аэроэкспресс», «Бронирование отеля». Браузер - хххх, Разрешение - ххх");
        с1.setCellStyle(style);
        XSSFCell c2 = groupRow.createCell(2);
        c2.setCellStyle(style);
        XSSFCell c3 = groupRow.createCell(3);
        c3.setCellStyle(style);
        XSSFCell c4 = groupRow.createCell(4);
        c4.setCellStyle(style);
        XSSFCell c5 = groupRow.createCell(5);
        c5.setCellStyle(style);
        XSSFCell c6 = groupRow.createCell(6);
        c6.setCellStyle(style);
    }

    private static XSSFCellStyle createStyleForTitle1(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        //font.setItalic(true);
        //font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);

        return style;
    }

    private static XSSFCellStyle createStyleForTitle2(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setItalic(true);
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.BLACK.index);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        return style;
    }

    private static XSSFCellStyle createStyleForGroup(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.DARK_BLUE.index);

        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.PALE_BLUE.index);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }
}
