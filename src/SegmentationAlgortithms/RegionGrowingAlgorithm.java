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
    private Pixel[][] pixelArray;
    private boolean[][] visited;
    private Queue<Point> points = new LinkedList<Point>();
    private int WIDTH;
    private int HEIGHT;
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
        comboRegionGrowing(seeds);
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

            current_threshold = 60;

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
    private boolean checkIfValid(int x, int y){
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }
    private boolean checkIfSimilar(Pixel p1, Pixel p2){
        double distance = Calculations.calculateDistanceSquared(p1, p2);
        return distance <= 30;
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
        BufferedImage image = ImageSaver.array2BufferedImage(pixelArray, WIDTH, HEIGHT);
        return image;
    }

}
