import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by mycola on 03.05.2018.
 */

public class Unzip {


    public static boolean unzipBuild(int build) {
        String archive = Values.buildPath + build + Values.archiveName;
        File file = new File(archive);
        if (!file.exists() || !file.canRead()) {
            System.out.println("File cannot be read");
            return false;
        }

        try {
            ZipFile zip = new ZipFile(archive);
            Enumeration entries = zip.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.getName().contains("attachments")) {
                    System.out.println(entry.getName());
                    String s = new File(entry.getName()).getName();
                    write(zip.getInputStream(entry),
                            new BufferedOutputStream(new FileOutputStream(
                                    new File("result\\screenshot\\" + s))));
                }
                if (entry.getName().contains("test-cases")) {
                    System.out.println(entry.getName());
                    String s = new File(entry.getName()).getName();
                    write(zip.getInputStream(entry),
                            new BufferedOutputStream(new FileOutputStream(
                                    new File("result\\" + s))));
                }
            }
            zip.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void deleteFilesFromResultFolder() {
        for (File myFile : new File("result\\").listFiles())
            if (myFile.isFile()) {
                boolean r = myFile.delete();
                System.out.println("Delete file " + myFile.getName() + " " + r);
            }
    }

    public static void deleteFilesFromScreenshotFolder() {
        for (File myFile : new File("result\\screenshot\\").listFiles())
            if (myFile.isFile()) {
                boolean r = myFile.delete();
                System.out.println("Delete file " + myFile.getName() + " " + r);
            }
    }

    public static void makeScreenshotDir() {
        new File("result\\screenshot").mkdir();
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
