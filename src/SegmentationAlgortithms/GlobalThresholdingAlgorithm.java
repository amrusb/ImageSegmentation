package SegmentationAlgortithms;

import GUIparts.BottomPanel;
import ImageOperations.ImageReader;
import ImageOperations.ImageSaver;

import java.awt.image.BufferedImage;

public class GlobalThresholdingAlgorithm {
    private double[][] grayScalePixelArray;
    private final int WIDTH;
    private final int HEIGHT;
    private static final int L = 256;

    public GlobalThresholdingAlgorithm(BufferedImage image){
        WIDTH = image.getWidth();
        HEIGHT = image.getHeight();

        grayScalePixelArray = ImageReader.convert2GrayScale(image);
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

    private double[] getNormalizedHistogram(){
        double[] histogram = new double[L];
        int N = WIDTH*HEIGHT;

        BottomPanel.setProgress(1);
        BottomPanel.setProgressMaximum(N*L);
        BottomPanel.setProgressLabel("Tworzenie histogramu...");

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                BottomPanel.incrementProgress();
                int value = (int)Math.ceil(grayScalePixelArray[i][j]);
                histogram[value]++;
            }
        }

        for (int i = 0; i < L; i++) {
            BottomPanel.incrementProgress();
            histogram[i] = histogram[i] / N;
        }

        return  histogram;
    }

    private int OtsuMethod(){
        double[] p = getNormalizedHistogram();
        int N = WIDTH*HEIGHT;
        int[] k = new int[L];
        double[] omega = new double[L];
        double[] my = new double[L];
        omega[0] = p[0];
        my[0] = 0;
        BottomPanel.setProgress(1);
        BottomPanel.setProgressMaximum(L*L);
        BottomPanel.setProgressLabel("Szukanie progu...");
        for (int i = 1; i < L; i++) {
            omega[i] = omega[i-1] + p[i];
            my[i] = my[i-1] + (i * p[i]);
        }

        double mean_lvl = my[L - 1];

        double[] variances= new double[L];
        for (int i = 0; i < L; i++){
            BottomPanel.incrementProgress();
            double numerator = mean_lvl*omega[i] - my[i];
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
    public BufferedImage getOutputImage() {
        BufferedImage image = ImageSaver.array2BufferedImage(grayScalePixelArray, WIDTH, HEIGHT);
        return image;
    }

}
