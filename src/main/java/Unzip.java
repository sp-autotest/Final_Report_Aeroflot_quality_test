import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by mycola on 03.05.2018.
 */

public class Unzip {


    public static void unzip(String arg) {

        File file = new File(arg);
        if (!file.exists() || !file.canRead()) {
            System.out.println("File cannot be read");
            return;
        }

        try {
            ZipFile zip = new ZipFile(arg);
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
        }
    }

    public static void runUp() {
        for (File myFile : new File("result\\").listFiles())
            if (myFile.isFile()) myFile.delete();
        new File("result\\screenshot").mkdir();
    }

    public static void mkk(String arg) {
        try {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(arg));
            ZipEntry entry;
            String name;
            while((entry=zin.getNextEntry())!=null) {

                name = entry.getName(); // получим название файла
                System.out.println(name + "  -  " + entry.isDirectory());

                // распаковка
                /*FileOutputStream fout = new FileOutputStream("result\\" + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }*/
                //fout.flush();
                zin.closeEntry();
                //fout.close();
            }
        } catch(Exception ex){
                System.out.println(ex.getMessage());
        }
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
