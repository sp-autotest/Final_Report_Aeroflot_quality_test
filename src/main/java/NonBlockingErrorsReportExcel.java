import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by mycola on 12.12.2018.
 */
public class NonBlockingErrorsReportExcel {

    private static int globalRow = 1;
    private static XSSFWorkbook book = new XSSFWorkbook();
    private static XSSFSheet sheet = book.createSheet("Неблокирующие ошибки");

    public static void writeReportIntoExcel(String file) {
        createTitle();// Создаем заголовок
        for (int i = 0; i < Values.runs.size(); i++) {
            if (Values.runs.get(i).getPart().equals("9")) continue;
            if (null != Values.runs.get(i).getLastStep()) {
                if (Values.runs.get(i).getLastStep().equals("Неблокирующие ошибки")) {
                    createRecord(i, createStyleForRecord());// Создаем строку отчета
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

    private static void createRecord(int i, XSSFCellStyle style1) {
        // Создаем запись отчета
        String[] errors = Values.runs.get(i).getLastSubStep().split("\r\n");
        for (int n=0; n<errors.length; n++) {
            String e = errors[n].substring(15);
            String step = Values.runs.get(i).getPeriodicity().split("\r\n")[n];
            XSSFRow row = sheet.createRow(globalRow);
            createCellAndSetValue(row, 0, ""+globalRow, style1); //№
            createCellAndSetValue(row, 1, "", style1); //Название отклонения
            createCellAndSetValue(row, 2, e, style1); //описание отклонения
            createCellAndSetValue(row, 3, "Раздел " + Values.runs.get(i).getPart(), style1); //Раздел регресса
            createCellAndSetValue(row, 4, "EPR", style1); //К какой системе относится
            createCellAndSetValue(row, 5, getActionName(i, step), style1); //Название проверки
            createCellAndSetValue(row, 6, Values.jenkinsPath + Values.runs.get(i).getBuild() +
                    "/allure/#testresult/" + Values.runs.get(i).getUid(), style1); //ссылка на прохождение
            globalRow++;

        }
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
        createCellAndSetValue(rowHeader, 1, "Название отклонения", titleStyle);
        createCellAndSetValue(rowHeader, 2, "Краткое описание отклонение", titleStyle);
        createCellAndSetValue(rowHeader, 3, "Раздел регресса", titleStyle);
        createCellAndSetValue(rowHeader, 4, "К какой системе относится", titleStyle);
        createCellAndSetValue(rowHeader, 5, "Название проверки", titleStyle);
        createCellAndSetValue(rowHeader, 6, "Ссылка на тест-кейс", titleStyle);
        sheet.setColumnWidth(0, 5*256); //6 characters wide
        sheet.setColumnWidth(1, 45*256); //5 characters wide
        sheet.setColumnWidth(2, 45*256); //40 characters wide
        sheet.setColumnWidth(3, 10*256); //15 characters wide
        sheet.setColumnWidth(4, 12*256); //20 characters wide
        sheet.setColumnWidth(5, 45*256); //50 characters wide
        sheet.setColumnWidth(6, 30*256); //20 characters wide
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

    private static XSSFCellStyle createStyleForRecord() {
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

    private static String getActionName(int i, String step) {
        String actionName = "";
        String archive = Values.buildPath + Values.runs.get(i).getBuild() + Values.archiveName;
        try {
            ZipFile zip = new ZipFile(archive);
            Enumeration entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.getName().contains(Values.runs.get(i).getUid()+".json")) {
                    String s = new File(entry.getName()).getName();
                    write(zip.getInputStream(entry),
                            new BufferedOutputStream(new FileOutputStream(
                                    new File("result\\" + s))));
                }
            }
            zip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File("result\\"+Values.runs.get(i).getUid()+".json");
        JSONParser parser = new JSONParser();
        try {
            BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
            Object obj = parser.parse(fr);
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject test = (JSONObject) jsonObject.get("testStage");
            JSONArray steps = (JSONArray) test.get("steps");
            for (int j=steps.size()-1; j>=0; j--) {
                JSONObject jsonObjectRow = (JSONObject) steps.get(j);
                String name = (String) jsonObjectRow.get("name");
                if (name.contains("Действие " + step.substring(0,2))) {
                    JSONArray steps1 = (JSONArray) jsonObjectRow.get("steps");
                    JSONObject jsonObjectRow1 = (JSONObject) steps1.get(Integer.parseInt(step.substring(2))-1);
                    String name1 = (String) jsonObjectRow1.get("name");
                    actionName = name + "\r\nШаг: " + name1;
                    break;
                }
            }
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        file.delete();
        return actionName;
    }

    private static void write(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0)
            out.write(buffer, 0, len);
        out.close();
        in.close();
    }

}
