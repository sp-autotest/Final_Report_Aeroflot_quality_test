import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by mycola on 06.07.2018.
 */
public class DeviationReportExcel {
    private static int globalRow = 1;
    private static XSSFWorkbook book = new XSSFWorkbook();
    private static XSSFSheet sheet = book.createSheet("Отклонения");

    public static void writeReportIntoExcel(String file) {
        createTitle();// Создаем заголовок
        for (int i = 0; i < Values.runs.size(); i++) {
            if (Values.runs.get(i).getPart().equals("9")) continue;
            if (!Values.runs.get(i).getStatus().equals("passed"))
                createRecord(i, createStyle1ForRecord(), createStyle2ForRecord());// Создаем строку отчета
            else if (null != Values.runs.get(i).getLastStep()) {
                if (Values.runs.get(i).getLastStep().equals("Неблокирующие ошибки")) {
                    createRecord(i, createStyle1ForRecord(), createStyle2ForRecord());// Создаем строку отчета
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

    private static void createRecord(int i, XSSFCellStyle style1, XSSFCellStyle style2) {
        // Создаем запись отчета
        XSSFRow row = sheet.createRow(globalRow);
        createCellAndSetValue(row, 0, ""+globalRow, style1); //№
        createCellAndSetValue(row, 1, Values.runs.get(i).getLastStep() + "\r\n" +
                                      Values.runs.get(i).getLastSubStep() + "\r\n" +
                                      Values.runs.get(i).getMessage(), style1); //описание отклонения
        createCellAndSetValue(row, 2, getScenario(i), style1); //сценарий
        createCellAndSetValue(row, 3, Values.runs.get(i).getPnr(), style1); //PNR
        createCellAndSetValue(row, 4, Values.runs.get(i).getCard(), style1); ////номер кредитной карты
        createCellAndSetValue(row, 5, Values.runs.get(i).getDocumens(), style1); //номера документов
        createCellAndSetValue(row, 6, Values.jenkinsPath + Values.runs.get(i).getBuild() +
                "/allure/#testresult/" + Values.runs.get(i).getUid(), style1); //ссылка на прохождение
        createCellAndSetValue(row, 7, Values.runs.get(i).getPeriodicity(), style1); //повторяемость дефекта
        String critical = getCritical(i);
        createCellAndSetValue(row, 8, critical, getStyleByStatus(critical, style2)); //критичность дефекта
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
        createCellAndSetValue(rowHeader, 0, "№", titleStyle);
        createCellAndSetValue(rowHeader, 1, "Описание отклонения в работе системы", titleStyle);
        createCellAndSetValue(rowHeader, 2, "Сценарий", titleStyle);
        createCellAndSetValue(rowHeader, 3, "PNR", titleStyle);
        createCellAndSetValue(rowHeader, 4, "Номер карты", titleStyle);
        createCellAndSetValue(rowHeader, 5, "Номера документов", titleStyle);
        createCellAndSetValue(rowHeader, 6, "Ссылка на прохождение", titleStyle);
        createCellAndSetValue(rowHeader, 7, "Повторя-емость", titleStyle);
        createCellAndSetValue(rowHeader, 8, "Критичность дефекта", titleStyle);
        sheet.setColumnWidth(0, 5*256); //6 characters wide
        sheet.setColumnWidth(1, 45*256); //5 characters wide
        sheet.setColumnWidth(2, 45*256); //40 characters wide
        sheet.setColumnWidth(3, 10*256); //15 characters wide
        sheet.setColumnWidth(4, 18*256); //20 characters wide
        sheet.setColumnWidth(5, 45*256); //50 characters wide
        sheet.setColumnWidth(6, 14*256); //20 characters wide
        sheet.setColumnWidth(7, 9*256); //20 characters wide
        sheet.setColumnWidth(8, 15*256); //20 characters wide
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

    private static XSSFCellStyle createStyle2ForRecord() {
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

    private static String getScenario (int i) {
        String result = "Сценарий " + Values.runs.get(i).getPart() + ", " +
                Values.runs.get(i).getDescription() + ".\r\n" +
                getRussianLanguage(Values.runs.get(i).getLanguage()) +
                " язык интерфейса, оплата в " +
                Values.runs.get(i).getCurrency() + ".\r\n" +
                "Браузер - " + Values.runs.get(i).getBrowser() +
                ", разрешение - " + Values.runs.get(i).getResolution();
        return result;
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

    private static String getCritical(int i) {
        switch (Values.runs.get(i).getStatus()) {
            case "failed" : {
                String lastStep = Values.runs.get(i).getLastStep();
                if (lastStep.indexOf(",") >= 0) {
                    lastStep = lastStep.substring(0, lastStep.indexOf(",")).replaceAll("\\D+", "");
                    int step = Integer.parseInt(lastStep);
                    if (step < 6) return "игнорируемый";
                    else return "блокирующий";
                } else return "игнорируемый"; //для шагов, у которых нет текста "Действие №,..."
            }
            case "broken" : {
                return "важный";
            }
            case "passed" : {
                return "некритичный";
            }
        }
        return "не определено";
    }

    private static XSSFCellStyle getStyleByStatus(String critical, XSSFCellStyle st) {
        switch (critical) {
            case "блокирующий" : {
                st.setFillForegroundColor(IndexedColors.RED.index);
                break;
            }
            case "важный" : {
                st.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
                break;
            }
            case "игнорируемый" : {
                st.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
                break;
            }
            case "некритичный" : {
                st.setFillForegroundColor(IndexedColors.PALE_BLUE.index);
                break;
            }
            case "не определено" : {
                st.setFillForegroundColor(IndexedColors.WHITE.index);
            }
        }
        return st;
    }

}
