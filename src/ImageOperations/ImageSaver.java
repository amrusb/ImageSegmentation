package ImageOperations;

import Utils.Pixel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageSaver {
    public static BufferedImage convertToBufferedImage(ArrayList<Pixel> array, int width, int height){
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int pixel_it = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                outputImage.setRGB(j, i, array.get(pixel_it++).getBinaryPixel());
            }
        }
        return outputImage;
    }

    public static BufferedImage convertToBufferedImage(Pixel[][] array, int width, int height){
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                outputImage.setRGB(j, i, array[j][i].getBinaryPixel());
            }
        }
        return outputImage;
    }

    public static BufferedImage convertToBufferedImage(double[][] array, int width, int height){
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = (255 << 24) | ((int)array[j][i] << 16) | ((int)array[j][i] << 8) | (int)array[j][i];
                outputImage.setRGB(j, i, pixel);
            }
        }
        return outputImage;
    }
}
