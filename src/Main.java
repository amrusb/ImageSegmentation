import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

public class Main {
    private static BufferedImage image = null;
    private static BufferedImage rescaledImage = null;
    private static ArrayList<Pixel> pixelArray = null;
    private static final MainFrame frame = new MainFrame();
    public static void main(String[] args){
        EventQueue.invokeLater(()->{
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }

    public static void setImage(BufferedImage newImage){
        image = newImage;
    }
    public static BufferedImage getImage(){
        return image;
    }

    /*
    * Na podstawie obiektu BufferedImage image tworzy tablice pikseli i zapisuje ja w polu
    * pixelArray*/
    public static void setPixelArray(){
        int image_width = image.getWidth();
        int image_height = image.getHeight();
        pixelArray = new ArrayList<Pixel>(image_width * image_height);
        for (int i = 0; i < image_height; i++) {
            for (int j = 0; j < image_width; j++) {
                pixelArray.add(new Pixel(image.getRGB(j, i)));
            }
        }
    }

    public static Pixel getPixel(int x){
        return pixelArray.get(x);
    }
    public static int getPixelArraySize(){
        return pixelArray.size();
    }
    public static ArrayList<Pixel> getPixelArrayList(){
        return pixelArray;
    }
    public static void clearPixelArray(){
        pixelArray = null;
    }
    public static BufferedImage saveImage(){
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int pixel_it = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                outputImage.setRGB(j, i, pixelArray.get(pixel_it++).getBinaryPixel());
            }
        }

        return outputImage;
    }

    public static BufferedImage getOutputImage(){
        return image;
    }
    /*
    * @return (1) true jezeli obiekt image przechowuje obrazek
    *         (2) false w przeciwnym wypadku*/
    public static boolean hasImage(){
        return image != null;
    }
}
