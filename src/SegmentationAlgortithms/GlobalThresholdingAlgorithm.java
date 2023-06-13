package SegmentationAlgortithms;

import UserInterface.BottomPanel;
import ImageOperations.ImageReader;
import ImageOperations.ImageSaver;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class GlobalThresholdingAlgorithm {
    private final double[][] grayScalePixelArray;
    private final int WIDTH;
    private final int HEIGHT;
    private static final int L = 256;

    public GlobalThresholdingAlgorithm(BufferedImage image){
        WIDTH = image.getWidth();
        HEIGHT = image.getHeight();

        grayScalePixelArray = ImageReader.convertToGrayScale(image);
        int threshold = OtsuMethod();
        thresholding(threshold);

    }

    private void thresholding(int threshold){
        BottomPanel.setProgress(1);
        BottomPanel.setProgressMaximum(L*L);
        BottomPanel.setProgressLabel("Thresholding...");
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                BottomPanel.incrementProgress();
                if(grayScalePixelArray[i][j] <= threshold){
                    grayScalePixelArray[i][j] = 0;
                }
                else grayScalePixelArray[i][j] = 255;
            }
        }
    }
    /**
     * Oblicza znormalizowany histogram obrazu.
     * @return znormalizowany histogram obrazu
     */
    private double[] getNormalizedHistogram(){
        double[] histogram = new double[L];
        int N = WIDTH*HEIGHT;

        BottomPanel.setProgress(1);
        BottomPanel.setProgressMaximum(WIDTH*HEIGHT);
        BottomPanel.setProgressLabel("Tworzenie histogramu...");

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                BottomPanel.incrementProgress();
                int value = (int)Math.ceil(grayScalePixelArray[i][j]);
                histogram[value]++;
            }
        }
        BottomPanel.setProgress(1);
        BottomPanel.setProgressMaximum(L);
        BottomPanel.setProgressLabel("Normalizacja histogramu...");
        for (int i = 0; i < L; i++) {
            BottomPanel.incrementProgress();
            histogram[i] = histogram[i] / N;
        }

        return  histogram;
    }
    /**
     * Metoda Otsu, która oblicza optymalną wartość progową.
     * @return optymalna wartość progowa
     */
    private int OtsuMethod(){
        double[] p = getNormalizedHistogram();
        double[] omega = new double[L];
        double[] mean = new double[L];
        omega[0] = p[0];
        mean[0] = 0;
        BottomPanel.setProgress(0);
        BottomPanel.setProgressMaximum(L*L*L);
        BottomPanel.setProgressLabel("Szukanie progu...");
        for (int i = 1; i < L; i++) {
            BottomPanel.incrementProgress();
            omega[i] = omega[i-1] + p[i];
            mean[i] = mean[i-1] + (i * p[i]);
        }

        double mean_lvl = mean[L - 1];

        double[] variances= new double[L];
        for (int i = 0; i < L; i++){
            BottomPanel.incrementProgress();
            double numerator = mean_lvl*omega[i] - mean[i];
            double denominator = omega[i]*(1-omega[i]);
            if(denominator != 0.0)
                variances[i] = numerator*numerator / denominator;
            else variances[i] = 0.0;
        }

        int threshold = 0;
        for (int i = 1; i < L; i++) {
            BottomPanel.incrementProgress();
            if(variances[i] > variances[threshold]) threshold = i;
        }

        return threshold;
    }
    /**
     * Zwraca obraz wyjściowy po zastosowaniu progowania globalnego.
     * @return obraz wyjściowy po segmentacji
     */
    public BufferedImage getOutputImage() {
        return ImageSaver.convertToBufferedImage(grayScalePixelArray, WIDTH, HEIGHT);
    }

}
