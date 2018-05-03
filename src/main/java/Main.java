/**
 * Created by mycola on 03.05.2018.
 */
public class Main {


    public static void main(String args[]) {

        String archive = Values.buildPath + "131" + Values.archiveName;

        Unzip.runUp();
        Unzip.unzip(archive);

    }

}
