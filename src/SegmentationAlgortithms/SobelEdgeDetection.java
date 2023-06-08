package SegmentationAlgortithms;
import ImageOperations.ImageReader;
import ImageOperations.ImageSaver;

import java.awt.image.BufferedImage;

public class SobelEdgeDetection {
    private double[][] grayScalePixelArray;
    private final int WIDTH;
    private final int HEIGHT;

    public SobelEdgeDetection(BufferedImage image) {
        WIDTH = image.getWidth();
        HEIGHT = image.getHeight();
        grayScalePixelArray = ImageReader.convertToGrayScale(image);
        applySobelOperator();
    }

    public void applySobelOperator() {
        int[][] sobelMaskX = {
                { -1, 0, 1 },
                { -2, 0, 2 },
                { -1, 0, 1 }
        };

        int[][] sobelMaskY = {
                { 1, 2, 1 },
                { 0, 0, 0 },
                { -1, -2, -1 }
        };

        double[][] gradientMagnitude = new double[WIDTH][HEIGHT];

        for (int y = 1; y < HEIGHT - 1; y++) {
            for (int x = 1; x < WIDTH - 1; x++) {
                double gradientX = 0;
                double gradientY = 0;

                for (int j = -1; j <= 1; j++) {
                    for (int i = -1; i <= 1; i++) {
                        double pixelValue = grayScalePixelArray[x + i][y + j];
                        gradientX += sobelMaskX[j + 1][i + 1] * pixelValue;
                        gradientY += sobelMaskY[j + 1][i + 1] * pixelValue;
                    }
                }

                double gradientMagnitudeValue = Math.sqrt(gradientX * gradientX + gradientY * gradientY);
                gradientMagnitude[x][y] = gradientMagnitudeValue;
            }
        }

        grayScalePixelArray = gradientMagnitude;
    }

    public BufferedImage getOutputImage() {
        BufferedImage image = ImageSaver.convertToBufferedImage(grayScalePixelArray,WIDTH,HEIGHT );
        return image;
    }
}
