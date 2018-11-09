import java.util.TreeSet;
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
        TreeSet<Integer> builds = createBuildList(args);

        //очистка папки result от json-файлов
        //распаковка скриншотов и json-файлов билдов
        //чтение параметров запуска билдов из xml
        for (int build : builds) {
            System.out.println("Build = " + build);
            if (!Unzip.unzipBuild(build)) {
                Unzip.deleteFilesFromResultFolder();
                continue; //пропустить билд, архив который не удалось распаковать
            }
            Xml.readBuild(build);
            Unzip.deleteFilesFromResultFolder();
        }
        System.out.println(Values.runs.toString());

        //формирование результирующего Excel-файла
        Excel.writeReportIntoExcel("result\\final_report.xlsx");
        //формирование файла с отклонениями
        DeviationReportExcel.writeReportIntoExcel("result\\deviation_report.xlsx");
        //формирование файла проверки даты/времени перелета (раздел 9)
        //CheckDateExcel.writeReportIntoExcel("result\\checkdate_report.xlsx");

        Convert.pngToJpg();
    }

    public static void Sleep(int time){
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static TreeSet<Integer> createBuildList(String args[]){
        TreeSet<Integer> builds = new TreeSet<>();
        for (int i=0; i<args.length; i++) {
            if (args[i].contains("-")){
                int start = Integer.parseInt(args[i].substring(0, args[i].indexOf("-")));
                int end = Integer.parseInt(args[i].substring(args[i].indexOf("-")+1));
                for (int j=start; j<=end; j++) builds.add(j);
            }else {
                builds.add(Integer.parseInt(args[i]));
            }
        }
        return builds;
    }

}
