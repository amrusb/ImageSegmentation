package SegmentationAlgortithms;

import UserInterface.BottomPanel;
import ImageOperations.ImageReader;
import ImageOperations.ImageSaver;
import Utils.Calculations;
import Utils.Pixel;
import Utils.Point;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class RegionGrowingAlgorithm {
    private final Pixel[][] pixelArray;
    private final boolean[][] visited;
    private final Queue<Point> points = new LinkedList<Point>();
    private final int WIDTH;
    private final int HEIGHT;
    private int alpha = 4;
    private double threshold = 0.2/alpha;
    public RegionGrowingAlgorithm(BufferedImage image, ArrayList<Point> seeds){
        pixelArray = ImageReader.get2DPixelArray(image);

        WIDTH = image.getWidth();
        HEIGHT = image.getHeight();
        visited = new boolean[WIDTH][HEIGHT];

        for (boolean[] row:visited) {
            Arrays.fill(row, false);
        }

        HSLbasedRegionGrowing(seeds);
    }
    public RegionGrowingAlgorithm(BufferedImage image, ArrayList<Point> seeds, int alpha){
        pixelArray = ImageReader.get2DPixelArray(image);
        this.alpha = alpha;
        threshold = 0.2/alpha;

        WIDTH = image.getWidth();
        HEIGHT = image.getHeight();
        visited = new boolean[WIDTH][HEIGHT];

        for (boolean[] row:visited) {
            Arrays.fill(row, false);
        }

        HSLbasedRegionGrowing(seeds);
    }

    private void HSLbasedRegionGrowing(ArrayList<Point> seeds){
        BottomPanel.setProgress(0);
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

            double I_seed = (R+ G+ B) / 3.0;
            I_seed/=256;

            double S_seed = 1 - (1/I_seed) * Math.min(Math.min(R, G), B);
            S_seed/=256;
            double H_seed  = (R - G/2.0 - B/2.0)/ Math.sqrt(R*R+G*G+B*B-R*G-R*B-G*B);
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


                double I =  (cR + cG + cB) / 3.0;
                I/=256;
                double S = 1 - (1/I) * Math.min(Math.min(cR, cG), cB);
                S/=256;
                double H = (cR - cG/2.0 - cB/2.0)/ Math.sqrt(cR*cR+cG*cG+cB*cB-cR*cG-cR*cB-cG*cB);
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

    /**
     * Sprawdza poprawność punktu o podanych współrzędnych.
     * @param x współrzędna osi odciętych obrazu
     * @param y współrzędna osi rzędnych obrazu
     * @return (1) true jeżeli punkt o współrzędnych (x, y) należy do obrazu (2) false w przeciwnym wypadku
     */
    private boolean checkIfValid(int x, int y){
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }
    /**
     * Zwraca obraz wyjściowy po zastosowaniu segmentacji metodą region growing.
     * @return obraz wyjściowy po segmentacji
     */
    public BufferedImage getOutputImage(){
        return ImageSaver.convertToBufferedImage(pixelArray, WIDTH, HEIGHT);
    }

}
