package ImageOperations;

import Utils.Pixel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

    public static ArrayList<Pixel> getPixelArray(BufferedImage image){
        int image_width = image.getWidth();
        int image_height = image.getHeight();
        ArrayList<Pixel> array = new ArrayList<Pixel>(image_width * image_height);
        for (int i = 0; i < image_height; i++) {
            for (int j = 0; j < image_width; j++) {
                array.add(new Pixel(image.getRGB(j, i)));
            }
        }
        return array;
    }
    public static Pixel[][] get2DPixelArray(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        Pixel[][] array = new Pixel[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                array[j][i] = new Pixel(image.getRGB(j, i));
            }
        }
        return array;
    }

    public static double[][] convert2GrayScale(BufferedImage image){
        Pixel[][] pixels = get2DPixelArray(image);
        double[][] output = new double[pixels.length][pixels[0].length];

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                output[i][j] = 0.299 * pixels[i][j].getR() + 0.587 * pixels[i][j].getG()  + 0.114 * pixels[i][j].getB();
            }
        }

        return  output;
    }
}
