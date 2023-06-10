import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

public class Main {
    private static BufferedImage originalImage = null;
    private static BufferedImage rescaledImage = null;
    private static BufferedImage segmentedImage = null;
    private static final MainFrame frame = new MainFrame();
    public static void main(String[] args){
        EventQueue.invokeLater(()->{
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }

    public static void setImage(BufferedImage newImage){
        originalImage = newImage;
    }
    public static BufferedImage getImage(){
        return originalImage;
    }

    public static void setRescaledImage(BufferedImage newImage){
        rescaledImage = newImage;
    }

    public static BufferedImage getSegmentedImage() {
        return segmentedImage;
    }

    public static void setSegmentedImage(BufferedImage segmentedImage) {
        Main.segmentedImage = segmentedImage;
    }

    public static BufferedImage getRescaledImage(){
        return rescaledImage;
    }

    /*
    * @return (1) true jezeli obiekt image przechowuje obraz
    *         (2) false w przeciwnym wypadku*/
    public static boolean hasImage(){
        return originalImage != null;
    }
    public static boolean hasRescaledImage(){
        return rescaledImage != null;
    }
    public static boolean hasSegmentedImage() {return segmentedImage != null; }

}
