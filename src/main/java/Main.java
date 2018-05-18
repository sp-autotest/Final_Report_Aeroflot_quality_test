import java.util.concurrent.TimeUnit;

/**
 * Created by mycola on 03.05.2018.
 */
public class Main {


    public static void main(String args[]) {

        String encoding = System.getProperty("console.encoding", "utf-8");
        System.out.println("encoding = " + encoding);
        //создание пустой папки для скриншотов
        Unzip.makeScreenshotDir();
        Unzip.deleteFilesFromResultFolder();
        Unzip.deleteFilesFromScreenshotFolder();

        //очистка папки result от json-файлов
        //распаковка скриншотов и json-файлов билдов
        //чтение параметров запуска билдов из xml
        for (int i=0; i<args.length; i++) {
            System.out.println("\nBuild = " + args[i]);
            Unzip.unzipBuild(args[i]);
            Xml.readBuild(args[i]);
            Unzip.deleteFilesFromResultFolder();
        }
        System.out.println(Values.runs.toString());

        //формирование результирующего Excel-файла
        Excel.writeIntoExcel("result\\report.xlsx");

    }

    public static void Sleep(int time){
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
