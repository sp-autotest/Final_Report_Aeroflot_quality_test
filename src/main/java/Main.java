import java.io.File;
import java.io.IOException;

/**
 * Created by mycola on 03.05.2018.
 */
public class Main {


    public static void main(String args[]) {

        String archive = Values.buildPath + "131" + Values.archiveName;

        Unzip.runUp();
        Unzip.unzip(archive);
        for (File myFile : new File("result\\").listFiles())
            if (myFile.isFile()) Json.getStatus(myFile.getPath());

        try {
            Excel.writeIntoExcel("result\\report.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
