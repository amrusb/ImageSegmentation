package SegmentationAlgortithms;

import ImageOperations.ImageReader;
import ImageOperations.ImageSaver;
import Utils.Calculations;
import Utils.Pixel;
import Utils.Point;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.*;

public class RegionGrowingAlgorithm {
    private static final int THRESHOLD = 10;
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
        double d = OtsuMethod(pixelArray);
        //Region-growing
        /*for (Point seed: seeds) {
            var pixel_seed = pixelArray[seed.getX()][seed.getY()];

            ArrayList<Pixel> pixelList = new ArrayList<>();
            pixelList.add(pixel_seed);
            points.add(seed);

            int R = pixel_seed.getR();
            int G = pixel_seed.getG();
            int B = pixel_seed.getB();

            while(!points.isEmpty()){
                var current = points.poll();
                int x = current.getX();
                int y = current.getY();
                if(visited[x][y]) continue;
                visited[x][y] =  true;

                var current_pixel = pixelArray[x][y];
                double distance = Calculations.calculateDistance(pixel_seed, current_pixel);
                if(distance <= THRESHOLD){
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
        }*/
        //standardRegionGrowing(seeds);
        graySpaceRegionGrowing(seeds);
    }

    private void standardRegionGrowing( ArrayList<Point> seeds){
        for (Point seed: seeds) {
            var pixel_seed = pixelArray[seed.getX()][seed.getY()];

            ArrayList<Pixel> pixelList = new ArrayList<>();
            pixelList.add(pixel_seed);
            points.add(seed);

            int R = pixel_seed.getR();
            int G = pixel_seed.getG();
            int B = pixel_seed.getB();

            while(!points.isEmpty()){
                var current = points.poll();
                int x = current.getX();
                int y = current.getY();
                if(visited[x][y]) continue;
                visited[x][y] =  true;

                var current_pixel = pixelArray[x][y];
                double distance = Calculations.calculateDistance(pixel_seed, current_pixel);
                if(distance <= THRESHOLD){
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
    }

    private void graySpaceRegionGrowing(ArrayList<Point> seeds){
        for (Point seed: seeds) {
            var pixel_seed = pixelArray[seed.getX()][seed.getY()];

            ArrayList<Pixel> pixelList = new ArrayList<>();
            pixelList.add(pixel_seed);
            points.add(seed);

            int R = pixel_seed.getR();
            int G = pixel_seed.getG();
            int B = pixel_seed.getB();

            double graySpaceValue = 0.299 * R + 0.587 * G + 0.114 * B;

            while(!points.isEmpty()){
                var current = points.poll();
                int x = current.getX();
                int y = current.getY();
                if(visited[x][y]) continue;
                visited[x][y] =  true;

                var current_pixel = pixelArray[x][y];

                double pixelGraySpaceVal = 0.3 * current_pixel.getR() + 0.59 * current_pixel.getG()  + 0.11 * current_pixel.getB() ;
                double distance = Math.abs(pixelGraySpaceVal - graySpaceValue);
                if(distance <= THRESHOLD){
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
    }
    private boolean checkIfValid(int x, int y){
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    private double OtsuMethod(Pixel[][] pixelArray){
        int[] histogram = new int[256];
        double threshold = 0;
        for (Pixel[] pixelRow: pixelArray) {
            for (Pixel pixel: pixelRow) {
                double graySpaceValue = 0.299 * pixel.getR() + 0.587 * pixel.getG()  + 0.114 * pixel.getB();
                graySpaceValue = Math.floor(graySpaceValue);
                histogram[(int)graySpaceValue]++;
            }
        }
        HistogramChart.createHistogram(histogram);
        return threshold;
    }
    public BufferedImage getOutputImage(){
        BufferedImage image = ImageSaver.array2BufferedImage(pixelArray, WIDTH, HEIGHT);
        return image;
    }

}
