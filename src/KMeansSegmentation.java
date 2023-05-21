import RandGenerator.GaussianRandomGenerator;
import RandGenerator.LCGenerator;
import Dialogs.ProgressDialog;

import java.util.*;


public class KMeansSegmentation {
    private final int clustersCount;
    private final Cluster[] clusters;
    private final int maxIterations = 200;


    public KMeansSegmentation(int k){
        clustersCount = k;
        clusters = new Cluster[clustersCount];
        long start = System.currentTimeMillis();
        //clustersInitialization();
        KMeansPP();
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        float elapsedTimeSec = elapsedTimeMillis/1000F;
        System.out.println("Czas trwania inicjalizacji: " + elapsedTimeSec + " sec");

    }

    public void standardSegmentation(){
        var pixelArrayList = Main.getPixelArrayList();

        int[] clustersSize = new int[clustersCount];

        int iteration;
        for (iteration = 0; iteration < maxIterations; iteration++){
            var pxl_it = pixelArrayList.iterator();
            while(pxl_it.hasNext()){
                var distancesArray = new ArrayList<Double>();
                var pixel = pxl_it.next();

                //liczenie odleglosci od klastrow
                for (int i = 0; i < clustersCount; i++) {
                    int x = clusters[i].getX();
                    int y = clusters[i].getY();
                    int z = clusters[i].getZ();
                    double distance = Math.sqrt(calculateDistance(clusters[i], pixel));
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
            pxl_it = pixelArrayList.iterator();
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

        for (Pixel pixel : pixelArrayList) {
            int cluster_number = pixel.getAssignment();
            int R = clusters[cluster_number].getX();
            int G = clusters[cluster_number].getY();
            int B = clusters[cluster_number].getZ();
            pixel.setPixelValue(R, G, B);
        }
    }
    public void hamerlySegmentation() {
        var pixelArrayList = Main.getPixelArrayList();
        var size = Main.getPixelArraySize();

        double[] upperBounds = new double[size];
        double[] lowerBounds = new double[size];
        double[] moves = new double[clustersCount];

        Arrays.fill(upperBounds, Double.MAX_VALUE);
        Arrays.fill(lowerBounds, 0.0);
        int distanc_counter = 0;
        int iteration;
        for (iteration = 0; iteration < maxIterations; iteration++) {
            for (int index = 0; index < size; index++) {
                var pixel = Main.getPixel(index);
                if(iteration != 0){
                    upperBounds[index] += moves[pixel.getAssignment()];
                    lowerBounds[index] -= Arrays.stream(moves).max().getAsDouble();
                    lowerBounds[index] = Double.max(lowerBounds[index], 0.0);
                }
                if (upperBounds[index] > lowerBounds[index]) {
                    double minDistance = Double.MAX_VALUE;
                    double secondMinDistance = Double.MAX_VALUE;
                    int minIndex = 0;
                    distanc_counter++;
                    for (int i = 0; i < clustersCount; i++) {
                        double distance = Math.sqrt(calculateDistance(clusters[i], pixel));
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

                    if (pixel.getAssignment() != -1)
                        clusters[pixel.getAssignment()].decreaseSize();

                    pixel.setAssignment(minIndex);
                    clusters[pixel.getAssignment()].increaseSize();

                }
            }

            int notChanged = 0;

            int[] R = new int[clustersCount];
            int[] G = new int[clustersCount];
            int[] B = new int[clustersCount];

            for (Pixel pixel : pixelArrayList) {
                R[pixel.getAssignment()] += pixel.getR();
                G[pixel.getAssignment()] += pixel.getG();
                B[pixel.getAssignment()] += pixel.getB();
            }

            for (int i = 0; i < clustersCount; i++) {
                if (clusters[i].getSize() == 0)
                    continue;

                int cSize = clusters[i].getSize();
                int new_x = R[i] / cSize;
                int new_y = G[i] / cSize;
                int new_z = B[i] / cSize;

                if (new_x != clusters[i].getX() || new_y != clusters[i].getY() || new_z != clusters[i].getZ()) {
                    int d_x = new_x - clusters[i].getX();
                    int d_y = new_y - clusters[i].getY();
                    int d_z = new_z - clusters[i].getZ();

                    double move = Math.sqrt(d_x * d_x + d_y * d_y + d_z * d_z);
                    moves[i] = move;

                    clusters[i].setX(new_x);
                    clusters[i].setY(new_y);
                    clusters[i].setZ(new_z);
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
        for (Pixel pixel : pixelArrayList) {
            int clusterNumber = pixel.getAssignment();
            int R = clusters[clusterNumber].getX();
            int G = clusters[clusterNumber].getY();
            int B = clusters[clusterNumber].getZ();
            pixel.setPixelValue(R, G, B);
        }



    }

    private void clustersInitialization(){
        int pixelArraySize = Main.getPixelArraySize();
        for (int i = 0; i < clustersCount; i++) {
            int x = (int)Math.abs((GaussianRandomGenerator.nextDouble()) * pixelArraySize ) % pixelArraySize;
            var temp = Main.getPixel(x);
            clusters[i] = new Cluster(temp);
        }
    }
    private void KMeansPP(){
        int pixelArraySize = Main.getPixelArraySize();
        LCGenerator random = new LCGenerator(System.nanoTime());
        int x = (int)(random.nextDouble() * 255);
        var temp_pixel = Main.getPixel(x);
        //Pierwszy centroid randomowy
        clusters[0] = new Cluster(temp_pixel);


        double[] distances = new double[pixelArraySize];
        Arrays.fill(distances, Double.MAX_VALUE);
        for (int i = 1; i < clustersCount; i++) {

            double sum = 0.0;

            for (int j = 0; j < pixelArraySize; j++) {
                temp_pixel = Main.getPixel(j);

                double distance = calculateDistance(clusters[i-1], temp_pixel);

                distances[j] = Math.min(distances[j], distance);
                sum+= distances[j];
            }

            double d = (int)(random.nextDouble() * sum);
            double pxl_value_sum = 0.0;
            int cluster_index = -1;

            while(pxl_value_sum <= d){
                pxl_value_sum+=distances[++cluster_index];
            }
            temp_pixel = Main.getPixel(cluster_index);
            clusters[i] = new Cluster(temp_pixel);
        }

        System.out.println(Arrays.toString(clusters));
    }



    private double calculateDistance(Point p1, Point p2){
        int d_x = p1.getX() - p2.getX();
        int d_y = p1.getY() - p2.getY();
        int d_z = p1.getZ() - p2.getZ();

        return d_x*d_x+d_y*d_y+d_z*d_z;
    }
}
