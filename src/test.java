import RandGenerator.GaussianRandomGenerator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


public class test {
    public static void main(String[] args){
/*        SortedMap<Double, Integer> map = new TreeMap<>(Collections.reverseOrder());
        map.put(23.43, 4);
        map.put(223.43, 2);
        map.put(-23.43, 3);
        map.put(33.43, 1);
        map.put(93.43, 5);

        System.out.println(map);

        System.out.println(map.get(map.firstKey()));
        map.remove(map.firstKey());
        System.out.println(map.get(map.firstKey()));
        map.remove(map.firstKey());
        System.out.println(map.get(map.firstKey()));*/

        int[] d = new int[6];
        d[0] = 12;
        d[1] = -23;
        d[2] = 0;
        d[3] = -2;
        d[4] = -23;
        d[5] = -21;
        int minDistance = Integer.MAX_VALUE;
        int secondMinDistance = Integer.MAX_VALUE;
        int minIndex = 0;
        for (int i = 0; i < 6; i++) {
            if (d[i] < minDistance) {
                secondMinDistance = minDistance;
                minDistance = d[i];
                minIndex = i;
            } else if (d[i] < secondMinDistance) {
                secondMinDistance = d[i];
            }
        }

        System.out.println("Min: " + minDistance);
        System.out.println("Scnd -min: " + secondMinDistance);
    }
}
