import RandGenerator.LCGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class SilhouetteScore {
    public static double calculateSilhouetteScore(Cluster[] clusters){
        double a = 0;
        double b;

        double cluster_distance = 0.0;

        for (int i = 0; i < clusters.length; i++) {
            for (int j = i+1; j < clusters.length; j++) {
                cluster_distance+= Math.sqrt(calculateDistance(clusters[i], clusters[j]));
            }

        }
        b = cluster_distance/clusters.length;

        int MAX = 10000;
        ArrayList<Pixel[]> clusterLists = new ArrayList<>(clusters.length);
        for (int i = 0; i < clusters.length; i++) {
            clusterLists.add(new Pixel[MAX]);
        }
        double[] pixels_distance = new double[clusters.length];

        int rand_i;

        int[] cluster_capacity = new int[clusterLists.size()];
        int counter = 0;

        while(counter <= MAX * clusterLists.size()){
            LCGenerator random = new LCGenerator(System.nanoTime());
            rand_i = (int)(random.nextDouble() * Main.getPixelArraySize());
            var pixel = Main.getPixel(rand_i);

            if(cluster_capacity[pixel.getAssignment()] != MAX){
                var temp = clusterLists.get(pixel.getAssignment());
                temp[cluster_capacity[pixel.getAssignment()]++] = pixel;
                clusterLists.set(pixel.getAssignment(), temp);
            }
            else{
                counter++;
            }
        }

        for (int i = 0; i < clusters.length; i++) {
            var pixels = clusterLists.get(i);
            main:
            for (int j = 0; j < MAX - 1; j++) {
                for (int k = j+1; k < MAX; k++) {
                    if(pixels[k] == null) continue main;
                    pixels_distance[i] +=  Math.sqrt(calculateDistance(pixels[j], pixels[k]));
                }
            }
            pixels_distance[i]/=clusters[i].getSize();
        }

        for (int i = 0; i < clusters.length; i++) {
            a += pixels_distance[i];
        }

        a/=clusters.length;
        return (b-a) / Math.max(a,b);
    }

    private static double calculateDistance(Point p1, Point p2){
        int d_x = p1.getX() - p2.getX();
        int d_y = p1.getY() - p2.getY();
        int d_z = p1.getZ() - p2.getZ();

        return d_x*d_x+d_y*d_y+d_z*d_z;
    }
}