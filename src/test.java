import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class test {
    public test(){
        BufferedImage image = null;
        File f = null;
        int w = 0, h = 0;
        try{
            image = ImageIO.read(new File("images/Untitled.jpg"));
            System.out.println("DONE");
            w = image.getWidth();
            h = image.getHeight();
            System.out.println("Wymiary obrazu: ");
            System.out.println("Width: " + w);
            System.out.println("Height: " + h);
            //Integer[][] binaryArr = new Integer[w][h];
//            for (int i = 0; i < h; i++) {
//                for (int j = 0; j < w; j++) {
//                    binaryArr[j][i] = image.getRGB(j, i);
//                }
//            }
            var pixelArr = new Pixel[w][h];
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    pixelArr[j][i] = new Pixel(image.getRGB(j, i));
                }
            }
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    System.out.print(pixelArr[j][i]);
                    System.out.print(" ");
                }
                System.out.println();
            }

            BufferedImage boutput = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    boutput.setRGB(j, i, pixelArr[j][i].getBinaryPixel());
                }
            }
            File output = new File("images/output.png");
            ImageIO.write(boutput, "png", output);
        }catch(IOException e){
            System.out.println("Error: " + e);
        }
    }
}
