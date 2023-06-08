package SegmentationAlgortithms;

import GUIparts.BottomPanel;
import ImageOperations.ImageReader;
import ImageOperations.ImageSaver;
import Utils.Calculations;
import Utils.Pixel;
import Utils.Point;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class RegionGrowingAlgorithm {
    private static final int colour_threshold = 55;
    private static double current_threshold;
    private final Pixel[][] pixelArray;
    private final boolean[][] visited;
    private Queue<Point> points = new LinkedList<Point>();
    private final int WIDTH;
    private final int HEIGHT;
    private final int alpha = 4;
    private final double threshold = 0.2/alpha;
    public RegionGrowingAlgorithm(BufferedImage image, ArrayList<Point> seeds){
        pixelArray = ImageReader.get2DPixelArray(image);

        WIDTH = image.getWidth();
        HEIGHT = image.getHeight();
        visited = new boolean[WIDTH][HEIGHT];

        for (boolean[] row:visited) {
            Arrays.fill(row, false);
        }

        //Region-growing
        //standardRegionGrowing(seeds);
        //graySpaceRegionGrowing(seeds);
        //comboRegionGrowing(seeds);
        HSLbasedRegionGrowing(seeds);
    }

    private void standardRegionGrowing( ArrayList<Point> seeds){
        BottomPanel.setProgress(0);
        BottomPanel.setProgressMaximum(seeds.size() - 1);
        BottomPanel.setProgressLabel("Region Growing...");
        for (Point seed: seeds) {

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    BottomPanel.incrementProgress();
                }
            });
            var pixel_seed = pixelArray[seed.getX()][seed.getY()];

            ArrayList<Pixel> pixelList = new ArrayList<>();
            pixelList.add(pixel_seed);
            points.add(seed);

            int R = pixel_seed.getR();
            int G = pixel_seed.getG();
            int B = pixel_seed.getB();


            BottomPanel.setProgressMaximum(100);

            current_threshold = 10;

            while(!points.isEmpty()){

                var current = points.poll();
                int x = current.getX();
                int y = current.getY();
                var current_pixel = pixelArray[x][y];
                if(visited[x][y]) continue;
                visited[x][y] =  true;

                R+= current_pixel.getR();
                G+= current_pixel.getG();
                B+= current_pixel.getB();

                pixelList.add(current_pixel);


                if(checkIfValid(x, y+1)){
                    var neighbour_pixel = pixelArray[x][y+1];
                    if(checkIfSimilar(neighbour_pixel, current_pixel))
                        points.add(new Point(x, y+1));
                }
                if(checkIfValid(x, y-1)){
                    var neighbour_pixel = pixelArray[x][y-1];
                    if(checkIfSimilar(neighbour_pixel, current_pixel)){
                        points.add(new Point(x, y-1));
                    }

                }
                if(checkIfValid(x-1, y)){
                    var neighbour_pixel = pixelArray[x-1][y];
                    if(checkIfSimilar(neighbour_pixel, current_pixel))
                        points.add(new Point(x-1, y));
                }
                if(checkIfValid(x+1, y)){
                    var neighbour_pixel = pixelArray[x+1][y];
                    if(checkIfSimilar(neighbour_pixel, current_pixel))
                        points.add(new Point(x+1, y));
                }
            }

            R/=pixelList.size();
            G/=pixelList.size();
            B/=pixelList.size();
            System.out.println(pixelList.size());
            for (Pixel p: pixelList) {
                p.setPixelValue(R, G, B);
            }
        }
    }
    private void graySpaceRegionGrowing(ArrayList<Point> seeds){
        BottomPanel.setProgress(1);
        BottomPanel.setProgressMaximum(seeds.size());
        BottomPanel.setProgressLabel("Region Growing...");
        for (Point seed: seeds) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    BottomPanel.incrementProgress();
                }
            });

            var pixel_seed = pixelArray[seed.getX()][seed.getY()];

            ArrayList<Pixel> pixelList = new ArrayList<>();
            pixelList.add(pixel_seed);
            points.add(seed);

            int R = pixel_seed.getR();
            int G = pixel_seed.getG();
            int B = pixel_seed.getB();

            double graySpaceValue = 0.299 * R + 0.587 * G + 0.114 * B;

            double mean = getMeanGrayScale(pixelArray);
            double sigma = Math.sqrt(getVarianceGrayScale(pixelArray));
            current_threshold = sigma;

            while(!points.isEmpty()){
                var current = points.poll();
                int x = current.getX();
                int y = current.getY();
                if(visited[x][y]) continue;
                visited[x][y] =  true;

                var current_pixel = pixelArray[x][y];

                double pixelGraySpaceVal = 0.3 * current_pixel.getR() + 0.59 * current_pixel.getG()  + 0.11 * current_pixel.getB() ;
                double distance = Math.abs(pixelGraySpaceVal - graySpaceValue);
                if(distance <= current_threshold){
                    R+= current_pixel.getR();
                    G+= current_pixel.getG();
                    B+= current_pixel.getB();
                    pixelList.add(current_pixel);

                    if(checkIfValid(x, y+1)) points.add(new Point(x, y+1));
                    if(checkIfValid(x, y-1)) points.add(new Point(x, y-1));
                    if(checkIfValid(x-1, y)) points.add(new Point(x-1, y));
                    if(checkIfValid(x+1, y)) points.add(new Point(x+1, y));
                }
            }

            R/=pixelList.size();
            G/=pixelList.size();
            B/=pixelList.size();
            for (Pixel p: pixelList) {
                p.setPixelValue(R, G, B);
            }
        }
        BottomPanel.setProgress(seeds.size());
    }
    private void comboRegionGrowing(ArrayList<Point> seeds){
        BottomPanel.setProgress(1);
        BottomPanel.setProgressMaximum(seeds.size());
        BottomPanel.setProgressLabel("Region Growing...");
        for (Point seed: seeds) {
            SwingUtilities.invokeLater(BottomPanel::incrementProgress);

            var pixel_seed = pixelArray[seed.getX()][seed.getY()];

            ArrayList<Pixel> pixelList = new ArrayList<>();
            pixelList.add(pixel_seed);
            points.add(seed);

            int R = pixel_seed.getR();
            int G = pixel_seed.getG();
            int B = pixel_seed.getB();

            double graySpaceValue = 0.299 * R + 0.587 * G + 0.114 * B;

            double sigma = Math.sqrt(getVarianceGrayScale(pixelArray));
            current_threshold = sigma;

            while(!points.isEmpty()){
                var current = points.poll();
                int x = current.getX();
                int y = current.getY();
                if(visited[x][y]) continue;
                visited[x][y] =  true;

                var current_pixel = pixelArray[x][y];

                double pixelGraySpaceVal = 0.3 * current_pixel.getR() + 0.59 * current_pixel.getG()  + 0.11 * current_pixel.getB() ;
                double grayScale_distance = Math.abs(pixelGraySpaceVal - graySpaceValue);
                if(grayScale_distance <= current_threshold){
                    double distance = Calculations.calculateDistance(pixel_seed, current_pixel);
                    if(distance <= colour_threshold){
                        R+= current_pixel.getR();
                        G+= current_pixel.getG();
                        B+= current_pixel.getB();
                        pixelList.add(current_pixel);

                        if(checkIfValid(x, y+1)) points.add(new Point(x, y+1));
                        if(checkIfValid(x, y-1)) points.add(new Point(x, y-1));
                        if(checkIfValid(x-1, y)) points.add(new Point(x-1, y));
                        if(checkIfValid(x+1, y)) points.add(new Point(x+1, y));
                    }

                }
            }

            R/=pixelList.size();
            G/=pixelList.size();
            B/=pixelList.size();
            for (Pixel p: pixelList) {
                p.setPixelValue(R, G, B);
            }
        }
        BottomPanel.setProgress(seeds.size());
    }
    private void HSLbasedRegionGrowing(ArrayList<Point> seeds){
        BottomPanel.setProgress(1);
        BottomPanel.setProgressMaximum(seeds.size());
        BottomPanel.setProgressLabel("Region Growing...");
        for (Point seed: seeds) {
            SwingUtilities.invokeLater(BottomPanel::incrementProgress);

            var pixel_seed = pixelArray[seed.getX()][seed.getY()];

            ArrayList<Pixel> pixelList = new ArrayList<>();
            pixelList.add(pixel_seed);
            points.add(seed);

            int R = pixel_seed.getR();
            int G = pixel_seed.getG();
            int B = pixel_seed.getB();


            double I_seed = 0.299 * R + 0.587 * G  + 0.114 * B;
            I_seed/=256;
            double S_seed = 1 - (1/I_seed) * Math.min(Math.min(R, G), B);
            S_seed/=256;
            double H_seed  = (R - G/2 - B/2)/ Math.sqrt(R*R+G*G+B*B-R*G-R*B-G*B);
            H_seed  = Math.acos(H_seed );

            if(B>G) H_seed = 360 - H_seed;
            H_seed /= 360;
            H_seed/=256;
            while(!points.isEmpty()){
                var current = points.poll();
                int x = current.getX();
                int y = current.getY();
                if(visited[x][y]) continue;
                visited[x][y] =  true;

                var current_pixel = pixelArray[x][y];
                int cR = current_pixel.getR();
                int cG = current_pixel.getG();
                int cB = current_pixel.getB();


                double I =  0.299 * cR + 0.587 * cG  + 0.114 * cB;
                I/=256;
                double S = 1 - (1/I) * Math.min(Math.min(cR, cG), cB);
                S/=256;
                double H = (cR - cG/2 - cB/2)/ Math.sqrt(cR*cR+cG*cG+cB*cB-cR*cG-cR*cB-cG*cB);
                H = Math.acos(H);

                if(cB > cG) H = 360 - H;
                H/=360;
                H/=256;

                double distance = (H-H_seed)*(H-H_seed) + Math.pow(S-S_seed, alpha) + (I-I_seed)*(I-I_seed);

                if(distance <= threshold){
                    R+= current_pixel.getR();
                    G+= current_pixel.getG();
                    B+= current_pixel.getB();
                    pixelList.add(current_pixel);

                    if(checkIfValid(x, y+1)) points.add(new Point(x, y+1));
                    if(checkIfValid(x, y-1)) points.add(new Point(x, y-1));
                    if(checkIfValid(x-1, y)) points.add(new Point(x-1, y));
                    if(checkIfValid(x+1, y)) points.add(new Point(x+1, y));
                }
            }

            R/=pixelList.size();
            G/=pixelList.size();
            B/=pixelList.size();
            for (Pixel p: pixelList) {
                p.setPixelValue(R, G, B);
            }
        }
        BottomPanel.setProgress(seeds.size());
    }
    private boolean checkIfValid(int x, int y){
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }
    private boolean checkIfSimilar(Pixel p1, Pixel p2){
        double distance = Calculations.calculateDistanceSquared(p1, p2);
        return distance <= 10;
    }
    private double getMeanGrayScale(Pixel[][] pixelArray){
        double mean = 0;
        int size = 0;
        for (Pixel[] pixelRow: pixelArray) {
            for (Pixel pixel: pixelRow) {
                size++;
                double graySpaceValue = 0.299 * pixel.getR() + 0.587 * pixel.getG()  + 0.114 * pixel.getB();
                mean += graySpaceValue;
            }
        }
        return mean/size;
    }

    private double getVarianceGrayScale(Pixel[][] pixelArray){
        double mean = getMeanGrayScale(pixelArray);
        double sigma = 0;
        int size = 0;
        for (Pixel[] pixelRow: pixelArray) {
            for (Pixel pixel: pixelRow) {
                size++;
                double graySpaceValue = 0.299 * pixel.getR() + 0.587 * pixel.getG()  + 0.114 * pixel.getB();
                graySpaceValue-=mean;

                sigma += (graySpaceValue*graySpaceValue);
            }
        }
        return  sigma/size;
    }
    public BufferedImage getOutputImage(){
        BufferedImage image = ImageSaver.convertToBufferedImage(pixelArray, WIDTH, HEIGHT);
        return image;
    }

}
