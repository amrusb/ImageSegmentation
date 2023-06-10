package SegmentationAlgortithms;

import UserInterface.BottomPanel;
import ImageOperations.ImageReader;
import ImageOperations.ImageSaver;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class AdaptiveThresholdingAlgorithm {
    private final int s;
    private final int t = 15;
    private final double[][] grayScalePixelArray;
    private final int WIDTH;
    private final int HEIGHT;

    public AdaptiveThresholdingAlgorithm(BufferedImage image) {
        WIDTH = image.getWidth();
        HEIGHT = image.getHeight();
        s = WIDTH / 8;
        grayScalePixelArray = ImageReader.convertToGrayScale(image);

        thresholding();
    }

    private void thresholding() {
        int[][] integralImage = createIntegralImage();

        BottomPanel.setProgress(1);
        BottomPanel.setProgressMaximum(WIDTH*HEIGHT);
        BottomPanel.setProgressLabel("Thresholding...");

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                SwingUtilities.invokeLater(BottomPanel::incrementProgress);
                int x1 = Math.max(0, x - s / 2);
                int y1 = Math.max(0, y - s / 2);
                int x2 = Math.min(WIDTH - 1, x + s / 2);
                int y2 = Math.min(HEIGHT - 1, y + s / 2);

                int count = (x2 - x1 + 1) * (y2 - y1 + 1);
                int sum = integralImage[x2][y2] - integralImage[x2][y1] - integralImage[x1][y2] + integralImage[x1][y1];

                if ((grayScalePixelArray[x][y] * count) <= (sum * (100 - t)  / 100.0)) {
                    grayScalePixelArray[x][y] = 0;
                } else {
                    grayScalePixelArray[x][y] = 255;
                }
            }
        }
    }
    /**
     * Tworzy obraz całkowy wykorzystywany w algorytmie adaptacyjnego progowania.
     * @return tablica dwuwymiarowa przechowująca obraz całkowy
     */
    private int[][] createIntegralImage() {
        BottomPanel.setProgress(1);
        BottomPanel.setProgressMaximum(WIDTH*HEIGHT);
        BottomPanel.setProgressLabel("Tworzenie obrazu całkowego...");

        int[][] integralImage = new int[WIDTH][HEIGHT];
        integralImage[0][0] = (int)grayScalePixelArray[0][0];

        for (int x = 1; x < WIDTH; x++) {
            SwingUtilities.invokeLater(BottomPanel::incrementProgress);
            integralImage[x][0] = integralImage[x - 1][0] + (int)grayScalePixelArray[x][0];
        }

        for (int y = 1; y < HEIGHT; y++) {
            SwingUtilities.invokeLater(BottomPanel::incrementProgress);
            integralImage[0][y] = integralImage[0][y - 1] + (int)grayScalePixelArray[0][y];
        }

        for (int x = 1; x < WIDTH; x++) {
            for (int y = 1; y < HEIGHT; y++) {
                SwingUtilities.invokeLater(BottomPanel::incrementProgress);
                integralImage[x][y] = integralImage[x - 1][y] + integralImage[x][y - 1]
                        - integralImage[x - 1][y - 1] + (int)grayScalePixelArray[x][y];
            }
        }
        return integralImage;
    }
    /**
     * Zwraca obraz wyjściowy po zastosowaniu adaptacyjnego progowania.
     * @return obraz wyjściowy po segmentacji
     */
    public BufferedImage getOutputImage() {
        BufferedImage image = ImageSaver.convertToBufferedImage(grayScalePixelArray, WIDTH, HEIGHT);
        return image;
    }
}
