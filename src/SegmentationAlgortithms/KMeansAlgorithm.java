package SegmentationAlgortithms;

import UserInterface.BottomPanel;
import ImageOperations.ImageReader;
import ImageOperations.ImageSaver;
import RandomGenerator.LCGenerator;
import Utils.Calculations;
import Utils.Cluster;
import Utils.Pixel;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.*;


public class KMeansAlgorithm {
    private int clustersCount;
    private final ArrayList<Cluster> clusters;
    private final int MAX_ITERATIONS = 100;
    private final ArrayList<Pixel> pixelArray;
    private final int WIDTH;
    private final int HEIGHT;
    public KMeansAlgorithm(int k, BufferedImage image){
        clustersCount = k;
        this.pixelArray = ImageReader.getPixelArray(image);
        WIDTH = image.getWidth();
        HEIGHT = image.getHeight();
        clusters = new ArrayList<>(clustersCount);

        KMeansPP();
        HamerlySegmentation();
    }

    private void HamerlySegmentation() {
        var size = pixelArray.size();

        double[] upperBounds = new double[size];
        double[] lowerBounds = new double[size];
        double[] moves = new double[clustersCount];
        int[] assignments = new int[size];

        Arrays.fill(upperBounds, Double.MAX_VALUE);
        Arrays.fill(lowerBounds, 0.0);
        Arrays.fill(assignments, -1);

        int iteration;

        BottomPanel.setProgress(0);
        BottomPanel.setProgressMaximum(MAX_ITERATIONS - 1);
        BottomPanel.setProgressLabel("K-means...");
        for (iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            BottomPanel.incrementProgress();
            for (int index = 0; index < size; index++) {

                var pixel = pixelArray.get(index);
                if(iteration != 0){
                    upperBounds[index] += moves[assignments[index]];
                    lowerBounds[index] -= Arrays.stream(moves).max().getAsDouble();
                    lowerBounds[index] = Double.max(lowerBounds[index], 0.0);
                }
                if (upperBounds[index] > lowerBounds[index]) {
                    double minDistance = Double.MAX_VALUE;
                    double secondMinDistance = Double.MAX_VALUE;
                    int minIndex = 0;
                    for (int i = 0; i < clustersCount; i++) {
                        double distance = Calculations.calculateDistance(clusters.get(i), pixel);
                        if (distance < minDistance) {
                            secondMinDistance = minDistance;
                            minDistance = distance;
                            minIndex = i;
                        } else if (distance < secondMinDistance) {
                            secondMinDistance = distance;
                        }
                    }

                    upperBounds[index] = minDistance;
                    lowerBounds[index] = secondMinDistance;

                    if (assignments[index] != -1)
                        clusters.get(assignments[index]).decreaseSize();

                    assignments[index] = minIndex;
                    clusters.get(assignments[index]).increaseSize();

                }
            }

            int notChanged = 0;

            int[] R = new int[clustersCount];
            int[] G = new int[clustersCount];
            int[] B = new int[clustersCount];

            int index = 0;
            for (Pixel pixel : pixelArray) {
                R[assignments[index]] += pixel.getR();
                G[assignments[index]] += pixel.getG();
                B[assignments[index]] += pixel.getB();
                index++;
            }

            for (int i = 0; i < clustersCount; i++) {
                int cSize = clusters.get(i).getSize();

                if (cSize == 0)
                    continue;

                int new_x = R[i] / cSize;
                int new_y = G[i] / cSize;
                int new_z = B[i] / cSize;

                if (new_x != clusters.get(i).getX() || new_y != clusters.get(i).getY() || new_z != clusters.get(i).getZ()) {
                    int d_x = new_x - clusters.get(i).getX();
                    int d_y = new_y - clusters.get(i).getY();
                    int d_z = new_z - clusters.get(i).getZ();

                    double move = Math.sqrt(d_x * d_x + d_y * d_y + d_z * d_z);
                    moves[i] = move;

                    clusters.get(i).setX(new_x);
                    clusters.get(i).setY(new_y);
                    clusters.get(i).setZ(new_z);
                } else notChanged++;
            }

            if (notChanged == clustersCount)
                break;

        }
        BottomPanel.setProgress(MAX_ITERATIONS * size - 1);

        // Aktualizacja wartości pikseli
        int index = 0;
        for (Pixel pixel : pixelArray) {
            int clusterNumber = assignments[index];
            int R = clusters.get(clusterNumber).getX();
            int G = clusters.get(clusterNumber).getY();
            int B = clusters.get(clusterNumber).getZ();
            pixel.setPixelValue(R, G, B);
            index++;
        }
    }
    private void KMeansPP(){
        int pixelArraySize = pixelArray.size();
        LCGenerator random = new LCGenerator(System.nanoTime());
        int x = (int)(random.nextDouble() * 255);
        var temp_pixel = pixelArray.get(x);

        //Pierwszy centroid randomowy
        clusters.add(new Cluster(temp_pixel));


        double[] distances = new double[pixelArraySize];
        Arrays.fill(distances, Double.MAX_VALUE);

        BottomPanel.setProgressMaximum(clustersCount - 1);
        BottomPanel.setProgressLabel("Inicjalizajca klastrów...");
        main_for:
        for (int i = 1; i < clustersCount; i++) {
            BottomPanel.incrementProgress();
            double sum = 0.0;

            for (int j = 0; j < pixelArraySize; j++) {
                temp_pixel = pixelArray.get(j);

                double distance = Calculations.calculateDistanceSquared(clusters.get(i-1), temp_pixel);

                distances[j] = Math.min(distances[j], distance);
                sum+= distances[j];
            }

            double d = (random.nextDouble() * sum);
            double pxl_value_sum = 0.0;
            int cluster_index = -1;

            while(pxl_value_sum <= d){
                if(++cluster_index >= pixelArraySize) {
                    clustersCount = i-1;
                    break main_for;
                }
                pxl_value_sum+=distances[cluster_index];

            }
            temp_pixel = pixelArray.get(cluster_index);
            clusters.add(new Cluster(temp_pixel));
        }
    }
    /**
     * Zwraca obraz wyjściowy po segmentowaniu obrazu algorytmem k-means.
     * @return obraz wyjściowy po segmentacji
     */
    public BufferedImage getOutputImage(){
        return ImageSaver.convertToBufferedImage(pixelArray, WIDTH, HEIGHT);
    }
}
