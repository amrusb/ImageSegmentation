import GUIparts.BottomPanel;
import GUIparts.ClusterInputDialog;
import GUIparts.RegionChooseDialog;
import ImageOperations.ImageReader;
import ImageOperations.ImageRescaler;
import SegmentationAlgortithms.KMeansAlgorithm;
import SegmentationAlgortithms.RegionGrowingAlgorithm;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainMenuBar extends JMenuBar {
    private static final JMenu programMenu = new JMenu("Program");
    private static final JMenuItem infoItem = new JMenuItem("Informacje");
    private static final JMenuItem exitItem = new JMenuItem("Wyjście");
    private static final JMenu imageMenu = new JMenu("Plik");
    private static final JMenuItem openItem = new JMenuItem("Otwórz");
    private static final JMenuItem saveItem = new JMenuItem("Zapisz");
    private static final JMenuItem saveAsItem = new JMenuItem("Zapisz jako");
    private static final JMenu segmentationMenu = new JMenu("Segmentacja");
    private static final JMenuItem kmeanItem = new JMenuItem("K-means");
    private static final JMenuItem RegionGrowingItem = new JMenuItem("Region-Growing");
    private static final JMenuItem undo = new JMenuItem("Cofnij");
    private static JFileChooser imageChooser = null;
    private static JFrame owner;
    public MainMenuBar(JFrame frame){
        owner = frame;
        imageMenu.add(openItem);
        imageMenu.setFont(MainFrame.getBasicFont());
        openItem.setFont(MainFrame.getBasicFont());
        openItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        openItem.addActionListener(e->{
            Main.setRescaledImage(null);
            Main.setImage(null);
            Main.setSegmentedImage(null);
            BottomPanel.setDurationInfoVisible(false);
            BottomPanel.setFileNameVisible(true);

            imageChooser = new JFileChooser();
            imageChooser.setCurrentDirectory(new File("./images"));
            imageChooser.setFileFilter(new FileNameExtensionFilter("Pliki obrazów", "jpg", "png"));
            int result = imageChooser.showOpenDialog(null);
            owner.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if(result == JFileChooser.APPROVE_OPTION){
                BottomPanel.clear();
                String fileName = imageChooser.getSelectedFile().getName();
                BottomPanel.setFileName(fileName);

                String filePath = imageChooser.getSelectedFile().getAbsolutePath();
                ImageReader.setFilePath(filePath);
                Main.setImage(ImageReader.readImage());

                MainFrame.setImageLabel(Main.getImage());
            }
            reload();
            owner.setCursor(Cursor.getDefaultCursor());
        });

        imageMenu.add(saveItem);
        saveItem.setFont(MainFrame.getBasicFont());
        saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        saveItem.addActionListener(e->{
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            String filePath = ImageReader.getFilePath();
            File output = new File(filePath);
            try{
                BufferedImage image;
                if(Main.hasSegmentedImage()) image = Main.getSegmentedImage();
                else image = Main.getImage();

                ImageIO.write(image, "png", output);
            }
            catch (IOException ex){
                System.out.println(ex.getMessage());
            }
            frame.setCursor(Cursor.getDefaultCursor());
        });

        imageMenu.add(saveAsItem);
        saveAsItem.setFont(MainFrame.getBasicFont());
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke("ctrl shift S"));
        saveAsItem.addActionListener(e->{
            imageChooser = new JFileChooser();
            imageChooser.setCurrentDirectory(new File("./images"));
            imageChooser.setFileFilter(new FileNameExtensionFilter("Pliki obrazów", "jpg", "png"));

            int result = imageChooser.showSaveDialog(null);
            owner.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if(result == JFileChooser.APPROVE_OPTION){
                String filePath = imageChooser.getSelectedFile().getAbsolutePath();
                File output = new File(filePath);
                try{
                    BufferedImage image;
                    if(Main.hasSegmentedImage()) image = Main.getSegmentedImage();
                    else image = Main.getImage();

                    ImageIO.write(image, "png", output);
                }
                catch (IOException ex){
                    System.out.println(ex.getMessage());
                }
            }
            owner.setCursor(Cursor.getDefaultCursor());
        });
        add(imageMenu);

        segmentationMenu.add(kmeanItem);
        kmeanItem.addActionListener(new kMeansAction());
        segmentationMenu.setFont(MainFrame.getBasicFont());
        kmeanItem.setFont(MainFrame.getBasicFont());
        segmentationMenu.add(RegionGrowingItem);
        RegionGrowingItem.setFont(MainFrame.getBasicFont());

        RegionGrowingItem.addActionListener(new regionGrowingAction());

        segmentationMenu.addSeparator();

        segmentationMenu.add(undo);
        undo.setFont(MainFrame.getBasicFont());
        undo.setAccelerator(KeyStroke.getKeyStroke("ctrl Z"));
        undo.setEnabled(false);
        undo.addActionListener(e->{
            BufferedImage image = Main.getImage();
            Main.setSegmentedImage(null);

            MainFrame.setImageLabel(image);
            undo.setEnabled(false);
        });

        add(segmentationMenu);

        programMenu.setFont(MainFrame.getBasicFont());
        programMenu.add(infoItem);
        infoItem.setFont(MainFrame.getBasicFont());
        programMenu.add(exitItem);
        exitItem.addActionListener(e->{
            System.exit(0);
        });
        exitItem.setFont(MainFrame.getBasicFont());
        add(programMenu);

        reload();
    }

    public static Frame getOwner() {
        return owner;
    }

    public static void reload(){
        if(Main.hasImage()){
            saveItem.setEnabled(true);
            saveAsItem.setEnabled(true);
            segmentationMenu.setEnabled(true);
        }
        else{
            saveItem.setEnabled(false);
            saveAsItem.setEnabled(false);
            segmentationMenu.setEnabled(false);
        }
    }

    private static class kMeansAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {

            BottomPanel.setDurationInfoVisible(false);
            if(Main.hasSegmentedImage()){
                Main.setImage(Main.getSegmentedImage());
            }
            ClusterInputDialog dialog = new ClusterInputDialog(owner, Main.hasRescaledImage());
            dialog.setVisible(true);

            int clusterCount = dialog.getClusterCount();
            boolean original = dialog.checkImageSource();

            if(clusterCount > 0){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        owner.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        BottomPanel.setProgressBarVisible(true);
                        KMeansAlgorithm segmentation;

                        long start = System.currentTimeMillis();
                        if (original) segmentation = new KMeansAlgorithm(clusterCount, Main.getImage());
                        else segmentation = new KMeansAlgorithm(clusterCount, Main.getRescaledImage());

                        long elapsedTimeMillis = System.currentTimeMillis()-start;
                        float elapsedTimeSec = elapsedTimeMillis/1000F;
                        System.out.println("Czas trwania algortymu: " + elapsedTimeSec + " sec");
                        BottomPanel.setDurationTime(elapsedTimeSec);

                        undo.setEnabled(true);
                        BufferedImage output = segmentation.getOutputImage();

                        MainFrame.setImageLabel(output);
                        if (!original){
                            double scale = (double)Main.getImage().getWidth() / (double)output.getWidth();
                            output = ImageRescaler.rescaleImage(output, scale);
                        }
                        Main.setSegmentedImage(output);
                        owner.setCursor(Cursor.getDefaultCursor());
                        BottomPanel.setProgressBarVisible(false);
                        BottomPanel.setDurationInfoVisible(true);
                    }
                }).start();
            }
        }
    }
    private static class regionGrowingAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            BottomPanel.setDurationInfoVisible(false);
            if(Main.hasSegmentedImage()){
                Main.setImage(Main.getSegmentedImage());
            }
            BufferedImage working_image;

            if(Main.hasRescaledImage()) working_image = Main.getRescaledImage();
            else working_image = Main.getImage();

            double scale;
            if(Main.hasRescaledImage()) scale = (double)Main.getImage().getWidth() / (double)Main.getRescaledImage().getWidth();
            else scale = 1;
            RegionChooseDialog dialog = new RegionChooseDialog(owner, working_image, scale);
            dialog.setVisible(true);
            var seedsArray = dialog.getRegions();

            if(seedsArray.size() > 0){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BottomPanel.setProgressBarVisible(true);
                        owner.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        long start = System.currentTimeMillis();
                        var segmentation = new RegionGrowingAlgorithm(Main.getImage(), seedsArray);
                        undo.setEnabled(true);

                        long elapsedTimeMillis = System.currentTimeMillis() - start;
                        float elapsedTimeSec = elapsedTimeMillis / 1000F;
                        BottomPanel.setDurationTime(elapsedTimeSec);

                        BufferedImage output = segmentation.getOutputImage();
                        Main.setSegmentedImage(output);
                        MainFrame.setImageLabel(output);
                        owner.setCursor(Cursor.getDefaultCursor());
                        BottomPanel.setProgressBarVisible(false);
                        BottomPanel.setDurationInfoVisible(true);
                    }
                }).start();
            }
        }
    }
}

