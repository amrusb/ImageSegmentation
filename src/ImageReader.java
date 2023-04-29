import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageReader {
    private static BufferedImage image = null;
    private static String filePath;
    public ImageReader(){
        File file;
        try{
            file = new File(filePath);
            image = ImageIO.read(file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void setFilePath(String filePath) {
        ImageReader.filePath = filePath;
    }

    public static BufferedImage getImage() {
        return image;
    }
}
