import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by mycola on 07.05.2018.
 */
public class Excel {

    private static int globalRow = 1;
    private static XSSFWorkbook book = new XSSFWorkbook();
    private static XSSFSheet sheet = book.createSheet("Тестовые данные и результаты");
    private static List<String>[] groups = new ArrayList[4];


    @SuppressWarnings("deprecation")
    public static void writeReportIntoExcel(String file) {

        // Создаем заголовок
        createTitle();
        globalRow = globalRow + 2;

        //Отбираем группы
        selectGroups();

        for (int n=0; n<4; n++) {
            for (int g = 0; g < groups[n].size(); g++) {
                // Создаем группу
                createGroup(g, n);
                globalRow++;
                // Создаем записи отчета
                int m = globalRow;
                for (int i = 0; i < Values.runs.size(); i++) {
                    createRecord(i, g, n, m);
                }
            }
        }

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

    private static void createTitle() {
        // Обьединяем ячейки в заголовке
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$B$2"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$C$1:$C$2"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$D$1:$F$1"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$G$1:$G$2"));
        // Создаем стили для заголовка
        XSSFCellStyle style1 = createStyleForTitle1();
        XSSFCellStyle style2 = createStyleForTitle2();
        // Создаем строки заголовка
        XSSFRow titleRow1 = sheet.createRow(globalRow-1);
        XSSFRow titleRow2 = sheet.createRow(globalRow);
        // Создаем ячейки заголовка
        XSSFCell с0 = titleRow1.createCell(0);
        с0.setCellValue("№");
        с0.setCellStyle(style1);
        XSSFCell c1 = titleRow1.createCell(2);
        c1.setCellValue("Тестовый сценарий");
        c1.setCellStyle(style1);
        XSSFCell c2 = titleRow1.createCell(3);
        c2.setCellValue("Тестовые данные");
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
        sheet.setColumnWidth(0, 5*256); //6 characters wide
        sheet.setColumnWidth(1, 7*256); //5 characters wide
        sheet.setColumnWidth(2, 45*256); //40 characters wide
        sheet.setColumnWidth(3, 20*256); //15 characters wide
        sheet.setColumnWidth(4, 25*256); //20 characters wide
        sheet.setColumnWidth(5, 55*256); //50 characters wide
        sheet.setColumnWidth(6, 25*256); //20 characters wide
    }

    private static void createGroup(int g, int n) {
        int i;
        for(i=0; i<Values.runs.size(); i++) {
            String group = Values.runs.get(i).getPart()+Values.runs.get(i).getBrowser()+Values.runs.get(i).getResolution();
            if (group.equals(groups[n].get(g))) {
                break;
            }
        }
        // Обьединяем ячейки в группе
        sheet.addMergedRegion(CellRangeAddress.valueOf("$B$" + globalRow + ":$G$" + globalRow));
        // Создаем стиль для группы
        XSSFCellStyle style = createStyleForGroup();
        // Создаем строку группы
        XSSFRow groupRow = sheet.createRow(globalRow-1);
        // Создаем ячейки группы
        XSSFCell с0 = groupRow.createCell(0);
        с0.setCellValue((n+1) + "." + (g+1));
        с0.setCellStyle(style);
        XSSFCell с1 = groupRow.createCell(1);
        с1.setCellValue("Группа автотестов - " + Values.runs.get(i).getDescription() +
                        ". Браузер - "         + Values.runs.get(i).getBrowser() +
                        ", Разрешение - "      + Values.runs.get(i).getResolution());
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

    private static void selectGroups() {
        List<String> groups1 = new ArrayList<>();
        List<String> groups2 = new ArrayList<>();
        List<String> groups3 = new ArrayList<>();
        List<String> groups4 = new ArrayList<>();

        for(int i=0; i<Values.runs.size(); i++) {
            String newGroup = Values.runs.get(i).getPart()+Values.runs.get(i).getBrowser()+Values.runs.get(i).getResolution();
            switch (Values.runs.get(i).getPart()) {
                case "1": {
                    groups1 = addNewGroup(groups1,newGroup);
                } break;
                case "2": {
                    groups2 = addNewGroup(groups2,newGroup);
                } break;
                case "3": {
                    groups3 = addNewGroup(groups3,newGroup);
                } break;
                case "4": {
                    groups4 = addNewGroup(groups4,newGroup);
                }
            }
        }
        groups[0] = groups1;
        groups[1] = groups2;
        groups[2] = groups3;
        groups[3] = groups4;

        System.out.println("1я группа = " + groups[0].toString());
        System.out.println("2я группа = " + groups[1].toString());
        System.out.println("3я группа = " + groups[2].toString());
        System.out.println("4я группа = " + groups[3].toString());
    }

    private static List<String> addNewGroup(List<String> group, String newGroup){
        boolean found = false;
        for(int j=0; j<group.size(); j++) {
            if (group.get(j).equals(newGroup)) {
                found = true;
                break;
            }
        }
        if (!found) group.add(newGroup);
        return group;
    }

    private static void createRecord(int i, int g, int n, int m) {
        String group = Values.runs.get(i).getPart()+Values.runs.get(i).getBrowser()+Values.runs.get(i).getResolution();
        if (group.equals(groups[n].get(g))) {
            // Создаем стиль для записи
            XSSFCellStyle style1 = createStyle1ForRecord();
            XSSFCellStyle style2 = createStyle2ForRecord();
            // Создаем строку записи
            XSSFRow groupRow = sheet.createRow(globalRow - 1);
            // Создаем ячейки записи
            XSSFCell c0 = groupRow.createCell(0);
            c0.setCellStyle(style1);
            XSSFCell c1 = groupRow.createCell(1);
            c1.setCellValue((n+1) + "." + (g+1) + "." + (globalRow-m+1));//номер по порядку
            c1.setCellStyle(style1);
            XSSFCell c2 = groupRow.createCell(2);
            c2.setCellValue(getRussianLanguage(Values.runs.get(i).getLanguage()) +
                    " язык интерфейса, оплата в " + Values.runs.get(i).getCurrency());//язык и валюта
            c2.setCellStyle(style1);
            XSSFCell c3 = groupRow.createCell(3);
            c3.setCellValue(Values.runs.get(i).getPnr());//PNR
            c3.setCellStyle(style1);
            XSSFCell c4 = groupRow.createCell(4);
            c4.setCellValue(Values.runs.get(i).getCard());//номер кредитной карты
            c4.setCellStyle(style1);
            XSSFCell c5 = groupRow.createCell(5);
            c5.setCellValue(Values.runs.get(i).getDocumens());//номера документов
            c5.setCellStyle(style1);
            XSSFCell c6 = groupRow.createCell(6);
            String status = Values.runs.get(i).getStatus();
            c6.setCellValue(getRussianStatus(status));//статус
            if (status.equals("passed"))
                style2.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
            if (status.equals("failed")) style2.setFillForegroundColor(IndexedColors.RED.index);
            if (status.equals("broken"))
                style2.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
            if (status.equals("skipped")) style2.setFillForegroundColor(IndexedColors.WHITE.index);
            c6.setCellStyle(style2);
            globalRow++;
        }
    }

    private static XSSFCellStyle createStyleForTitle1() {
        XSSFFont font = book.createFont();
        font.setBold(true);
        //font.setItalic(true);
        //font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle style = book.createCellStyle();
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

    private static XSSFCellStyle createStyleForTitle2() {
        XSSFFont font = book.createFont();
        font.setItalic(true);
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.BLACK.index);
        XSSFCellStyle style = book.createCellStyle();
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

    private static XSSFCellStyle createStyleForGroup() {
        XSSFFont font = book.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.DARK_BLUE.index);

        XSSFCellStyle style = book.createCellStyle();
        //style.setWrapText(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.PALE_BLUE.index);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    private static XSSFCellStyle createStyle1ForRecord() {
        XSSFCellStyle style = book.createCellStyle();
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.WHITE.index);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private static XSSFCellStyle createStyle2ForRecord() {
        XSSFCellStyle style = book.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private static String getRussianLanguage(String lang) {
        switch (lang) {
            case "French" : return "Французский";
            case "Spanish" : return "Испанский";
            case "Italian" : return "Итальянский";
            case "Japanese" : return "Японский";
            case "Chinese" : return "Китайский";
            case "English" : return "Английский";
            case "Russian" : return "Русский";
            case "Korean" : return "Корейский";
            case "German" : return "Немецкий";
        }
        return "Неизвестный";
    }

    private static String getRussianStatus(String status) {
        switch (status) {
            case "passed" : return "Пройдено";
            case "failed" : return "Не пройдено";
            case "broken" : return "Сломано";
            case "skipped" : return "Пропущено";
        }
        return "Неизвестно";
    }
}
