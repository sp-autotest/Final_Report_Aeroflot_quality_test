import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by mycola on 26.10.2018.
 */
public class Convert {

    private static String DIR_NAME = "result\\screenshot\\";

    public static void pngToJpg() {
        BufferedImage bufferedImage;
        String buf;
        System.out.print("Start converting png to jpg ");
        for (File pngFile : new File(DIR_NAME).listFiles()) {
            if (pngFile.isFile()) {
                buf = pngFile.getAbsolutePath(); // читаем текущее имя файла
                if(buf.endsWith(".png")) {  // если заканчивается на .png, то продолжаем
                    try {
                        //Считываем изображение в буфер
                        bufferedImage = ImageIO.read(pngFile);

                        // создаем пустое изображение RGB, с тай же шириной высотой и белым фоном
                        BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                                bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);

                        // записываем новое изображение в формате jpg
                        ImageIO.write(newBufferedImage, "jpg", new File(buf.replaceFirst(".png", ".jpg")));
                        pngFile.delete();
                        System.out.print(".");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("\nDone!");
    }
}
