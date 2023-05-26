package SegmentationAlgortithms;

import ImageOperations.ImageReader;
import ImageOperations.ImageSaver;
import RandGenerator.LCGenerator;
import Utils.Calculations;
import Utils.Cluster;
import Utils.Pixel;

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
        clusters = new ArrayList<Cluster>(clustersCount);
        long start = System.currentTimeMillis();
        KMeansPP();
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        float elapsedTimeSec = elapsedTimeMillis/1000F;
        System.out.println("Czas trwania inicjalizacji: " + elapsedTimeSec + " sec");
    }

    /*public void standardSegmentation(){

        int[] clustersSize = new int[clustersCount];
        int[] assignments = new int[size];

        int iteration;
        for (iteration = 0; iteration < maxIterations; iteration++){
            var pxl_it = pixelArray.iterator();
            while(pxl_it.hasNext()){
                var distancesArray = new ArrayList<Double>();
                var pixel = pxl_it.next();

                //liczenie odleglosci od klastrow
                for (int i = 0; i < clustersCount; i++) {
                    int x = clusters[i].getX();
                    int y = clusters[i].getY();
                    int z = clusters[i].getZ();
                    double distance = Math.sqrt(Calculations.calculateDistanceSquared(clusters[i], pixel));
                    distancesArray.add(distance);
                }
                //znalezienie klastra o najmniejszej odleglosci
                int min_index = 0;
                for (int i = 1; i < clustersCount; i++) {
                    if(distancesArray.get(i) < distancesArray.get(min_index))
                        min_index = i;
                }
                //dodanie piksela do odpowiedniego klastra
                if(pixel.getAssignment() != -1)
                    clustersSize[pixel.getAssignment()]--;
                pixel.setAssignment(min_index);
                clustersSize[pixel.getAssignment()]++;
            }
            //recenter
            int changes = 0;

            int[] R = new int[clustersCount];
            int[] G = new int[clustersCount];
            int[] B = new int[clustersCount];
            pxl_it = pixelArray.iterator();
            while(pxl_it.hasNext()){
                var pixel = pxl_it.next();
                int cluster_index = pixel.getAssignment();
                R[cluster_index] += pixel.getR();
                G[cluster_index] += pixel.getG();
                B[cluster_index] += pixel.getB();
            }
            //
            for (int i = 0; i < clustersCount; i++) {
                if(clustersSize[i] == 0) continue;
                int new_x = R[i] / clustersSize[i];
                int new_y = G[i] / clustersSize[i];
                int new_z = B[i] / clustersSize[i];

                if(new_x == clusters[i].getX()
                        && new_y == clusters[i].getY()
                        && new_z == clusters[i].getZ()){
                    changes++;
                }

                clusters[i].setX(new_x);
                clusters[i].setY(new_y);
                clusters[i].setZ(new_z);

            }
            if(changes == clustersCount) break;
        }

        System.out.println("Liczba iteracji: " + iteration);
        //REVALUE PIXEL

        for (Pixel pixel : pixelArray) {
            int cluster_number = pixel.getAssignment();
            int R = clusters[cluster_number].getX();
            int G = clusters[cluster_number].getY();
            int B = clusters[cluster_number].getZ();
            pixel.setPixelValue(R, G, B);
        }
    }*/
    public void hamerlySegmentation() {
        var size = pixelArray.size();

        double[] upperBounds = new double[size];
        double[] lowerBounds = new double[size];
        double[] moves = new double[clustersCount];
        int[] assignments = new int[size];

        Arrays.fill(upperBounds, Double.MAX_VALUE);
        Arrays.fill(lowerBounds, 0.0);
        Arrays.fill(assignments, -1);

        int distanc_counter = 0;
        int iteration;
        for (iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
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
                    distanc_counter++;
                    for (int i = 0; i < clustersCount; i++) {
                        double distance = Math.sqrt(Calculations.calculateDistanceSquared(clusters.get(i), pixel));
                        //sprawdzić
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
                } else {
                    notChanged++;
                }
            }

            if (notChanged == clustersCount)
                break;

        }

        System.out.println("Liczba obliczeń dystansu: " + distanc_counter);
        System.out.println("Liczba iteracji: " + iteration);
        System.out.println("Liczba iteracji * pixele: " + iteration * size);

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

        main_for:
        for (int i = 1; i < clustersCount; i++) {

            double sum = 0.0;

            for (int j = 0; j < pixelArraySize; j++) {
                temp_pixel = pixelArray.get(j);

                double distance = Calculations.calculateDistanceSquared(clusters.get(i-1), temp_pixel);

                distances[j] = Math.min(distances[j], distance);
                sum+= distances[j];
            }

            double d = (int)(random.nextDouble() * sum);
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

    public BufferedImage getOutputImage(){
        BufferedImage image = ImageSaver.array2BufferedImage(pixelArray, WIDTH, HEIGHT);
        return image;
    }
}
