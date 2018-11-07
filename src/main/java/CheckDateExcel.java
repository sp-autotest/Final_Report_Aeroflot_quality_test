import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mycola on 06.11.2018.
 */
public class CheckDateExcel {
    private static int globalRow = 1;
    private static XSSFWorkbook book = new XSSFWorkbook();
    private static XSSFSheet sheet = book.createSheet("Дата,время перелета и трансфера");


    public static void writeReportIntoExcel(String file) {
        createTitle();// Создаем заголовок
        List<Run> sort = new ArrayList<>();
        for (int i = 0; i < Values.runs.size(); i++) {
            if (Values.runs.get(i).getPart().equals("9")) {
                sort.add(Values.runs.get(i));
            }
        }
        for (Integer i = 1; i<4; i++) {
            for (Integer j = 1; j<9; j++) {
                for (Run s : sort) {
                    if (s.getSector().equals(i.toString()) && s.getIteration().equals(j.toString())) {
                        createRecord(s, createStyle1ForRecord(), createStyle1ForRecord());// Создаем строку отчета
                    }
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

    private static void createRecord(Run run, XSSFCellStyle style1, XSSFCellStyle style2) {
        // Создаем запись отчета
        XSSFRow row = sheet.createRow(globalRow);
        if (run.getSector().equals("2")) style1.setFillForegroundColor(new XSSFColor(new java.awt.Color(255,255,240)));
        else style1.setFillForegroundColor(IndexedColors.WHITE.index);
        createCellAndSetValue(row, 0, run.getSector(), style1); //номер временного отрезка
        createCellAndSetValue(row, 1, run.getIteration(), style1); //номер кейса
        createCellAndSetValue(row, 2, run.getDocumens(), style1); //документы
        createCellAndSetValue(row, 3, run.getPnr(), style1); //PNR
        String critical = getCritical(run);
        createCellAndSetValue(row, 4, critical, getStyleByStatus(critical, style2)); //критичность дефекта
        globalRow++;
    }

    private static XSSFCell createCellAndSetValue(XSSFRow row, int cellNumber, String value, XSSFCellStyle cellStyle) {
        XSSFCell cell = row.createCell(cellNumber);
        cell.setCellValue(value);
        if ( null != cellStyle ) cell.setCellStyle(cellStyle);
        return cell;
    }

    private static void createTitle() {
        // Создаем стили для заголовка
        XSSFCellStyle titleStyle = createStyleForTitle();
        XSSFRow rowHeader = sheet.createRow(0);
        createCellAndSetValue(rowHeader, 0, "Отрезок", titleStyle);
        createCellAndSetValue(rowHeader, 1, "№ кейса", titleStyle);
        createCellAndSetValue(rowHeader, 2, "Рейс", titleStyle);
        createCellAndSetValue(rowHeader, 3, "PNR", titleStyle);
        createCellAndSetValue(rowHeader, 4, "Результат", titleStyle);
        sheet.setColumnWidth(0, 10*256);
        sheet.setColumnWidth(1, 10*256);
        sheet.setColumnWidth(2, 70*256);
        sheet.setColumnWidth(3, 10*256);
        sheet.setColumnWidth(4, 25*256);
    }

    private static XSSFCellStyle createStyleForTitle() {
        XSSFFont font = book.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.BLACK.index);
        XSSFCellStyle style = book.createCellStyle();
        style.setFont(font);
        style.setWrapText(true);
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


    private static String getCritical(Run run) {
        switch (run.getStatus()) {
            case "failed" : {
                if (run.getMessage().contains("условию, отсутствует")) return "Перелет отсутствует";
                else return "Ошибка";
            }
            case "broken" : {
                return "Сломано";
            }
            case "passed" : {
                return "Пройдено";
            }
        }
        return "не определено";
    }

    private static XSSFCellStyle getStyleByStatus(String critical, XSSFCellStyle st) {
        switch (critical) {
            case "Ошибка" : {
                st.setFillForegroundColor(IndexedColors.RED.index);
                break;
            }
            case "Перелет отсутствует" : {
                st.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
                break;
            }
            case "Сломано" : {
                st.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
                break;
            }
            case "Пройдено" : {
                st.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
                break;
            }
            case "не определено" : {
                st.setFillForegroundColor(IndexedColors.WHITE.index);
            }
        }
        return st;
    }

}
