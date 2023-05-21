import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageReader {
    private static String filePath;
    public static void setFilePath(String filePath) {
        ImageReader.filePath = filePath;
    }
    public static BufferedImage readImage(){
        File file;
        BufferedImage image = null;
        try{
            file = new File(filePath);
            image = ImageIO.read(file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return image;
    }
    public static String getFilePath(){ return filePath; }

    public static BufferedImage rescaleImage(){
        File file;
        BufferedImage image = null;
        try{
            file = new File(filePath);
            image = ImageIO.read(file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        int frameWidth = MainFrame.getFrameWidth();
        int frameHeight = MainFrame.getFrameHeight();

        int width = image.getWidth();
        int height = image.getHeight();


        if (width >= frameWidth || height >= frameHeight) {
            double scale = Math.min((double) frameWidth / (width), (double) frameHeight / (height));
            width = (int) (scale * width);
            height = (int) (scale * height);
            image = (BufferedImage)image.getScaledInstance(width, height, Image.SCALE_FAST);
        }

        return image;
    }

}
