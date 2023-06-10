package ImageOperations;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageRescaler {
    /**
     * Skaluje obraz zgodnie z podanym współczynnikiem skali.
     *
     * @param image obiekt BufferedImage reprezentujący obraz do skalowania
     * @param scale współczynnik skali
     * @return skalowany obraz jako obiekt BufferedImage
     */
    public static BufferedImage rescaleImage(BufferedImage image, double scale){
        int width = image.getWidth();
        int height = image.getHeight();

        width = (int) (scale * width);
        height = (int) (scale * height);

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        AffineTransform scaleInstance = AffineTransform.getScaleInstance(scale, scale);
        AffineTransformOp transformOp = new AffineTransformOp(scaleInstance, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        transformOp.filter(image, outputImage);
        return outputImage;
    }
}
