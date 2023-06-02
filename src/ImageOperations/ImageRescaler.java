package ImageOperations;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageRescaler {
    public static BufferedImage rescaleImage(BufferedImage image, double scale){
        int width = image.getWidth();
        int height = image.getHeight();

        width = (int) (scale * width);
        height = (int) (scale * height);

        BufferedImage after = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        AffineTransform scaleInstance = AffineTransform.getScaleInstance(scale, scale);
        AffineTransformOp scaleOp = new AffineTransformOp(scaleInstance, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        scaleOp.filter(image, after);
        return after;
    }
}
