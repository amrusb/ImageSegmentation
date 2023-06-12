package UserInterface;

import ImageOperations.*;
import SegmentationAlgortithms.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainMenuBar extends JMenuBar {
    private static final JMenu programMenu = new JMenu("Program");
    private static final JMenuItem exitItem = new JMenuItem("Wyjście");
    private static final JMenu imageMenu = new JMenu("Plik");
    private static final JMenuItem openItem = new JMenuItem("Otwórz");
    private static final JMenuItem saveItem = new JMenuItem("Zapisz");
    private static final JMenuItem saveAsItem = new JMenuItem("Zapisz jako");
    private static final JMenu segmentationMenu = new JMenu("Segmentacja");
    private static final JMenuItem kmeanItem = new JMenuItem("K-means");
    private static final JMenuItem RegionGrowingItem = new JMenuItem("Region Growing");
    private static final JMenu ThresholdingItem= new JMenu("Progowanie");
    private static final JMenuItem GlobalThresholdingItem = new JMenuItem("Globalne");
    private static final JMenuItem LocalThresholdingItem = new JMenuItem("Lokalne");
    private static final JMenuItem undo = new JMenuItem("Cofnij");
    private static JFrame owner;
    private static final String DEFAULT_EXTENSION = "JPG";
    public MainMenuBar(JFrame frame){
        owner = frame;
        imageMenu.add(openItem);
        imageMenu.setFont(MainFrame.getBasicFont());
        openItem.setFont(MainFrame.getBasicFont());
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        openItem.addActionListener(new OpenAction());

        imageMenu.add(saveItem);
        saveItem.setFont(MainFrame.getBasicFont());
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        saveItem.addActionListener(new SaveAction());

        imageMenu.add(saveAsItem);
        saveAsItem.setFont(MainFrame.getBasicFont());
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK));
        saveAsItem.addActionListener(new SaveAsAction());
        add(imageMenu);

        segmentationMenu.add(kmeanItem);
        kmeanItem.addActionListener(new KMeansAction());
        segmentationMenu.setFont(MainFrame.getBasicFont());
        kmeanItem.setFont(MainFrame.getBasicFont());
        segmentationMenu.add(RegionGrowingItem);
        RegionGrowingItem.setFont(MainFrame.getBasicFont());

        RegionGrowingItem.addActionListener(new RegionGrowingAction());

        segmentationMenu.add(ThresholdingItem);
        ThresholdingItem.add(GlobalThresholdingItem);
        GlobalThresholdingItem.setFont(MainFrame.getBasicFont());
        GlobalThresholdingItem.addActionListener(new GlobalThresholdingAction());
        ThresholdingItem.add(LocalThresholdingItem);
        LocalThresholdingItem.addActionListener(new LocalThresholdingAction());
        LocalThresholdingItem.setFont(MainFrame.getBasicFont());
        ThresholdingItem.setFont(MainFrame.getBasicFont());

        segmentationMenu.addSeparator();

        segmentationMenu.add(undo);
        undo.setFont(MainFrame.getBasicFont());
        undo.setAccelerator(KeyStroke.getKeyStroke("ctrl Z"));
        undo.setEnabled(false);
        undo.addActionListener(e->{
            BufferedImage image = MainFrame.getImage();
            MainFrame.setSegmentedImage(null);

            MainFrame.setImageLabel(image);
            undo.setEnabled(false);
        });

        add(segmentationMenu);

        programMenu.setFont(MainFrame.getBasicFont());
        programMenu.add(exitItem);
        exitItem.addActionListener(e-> System.exit(0));
        exitItem.setFont(MainFrame.getBasicFont());
        add(programMenu);

        reload();
    }

    /**
     * Metoda ustawia dostepnosc elementow paska menu w zależności od tego czy użytkownik wybrał plik*/
    public static void reload(){
        if(MainFrame.hasImage()){
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
    private static class OpenAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser imageChooser = new JFileChooser();
            imageChooser.setCurrentDirectory(new File("."));
            imageChooser.addChoosableFileFilter(new ImageFilter());
            imageChooser.setAcceptAllFileFilterUsed(false);

            int result = imageChooser.showOpenDialog(null);
            owner.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if(result == JFileChooser.APPROVE_OPTION){
                MainFrame.setRescaledImage(null);
                MainFrame.setImage(null);
                MainFrame.setSegmentedImage(null);
                BottomPanel.setDurationInfoVisible(false);
                BottomPanel.setFileNameVisible(true);
                BottomPanel.clear();

                String fileName = imageChooser.getSelectedFile().getName();
                BottomPanel.setFileName(fileName);
                ImageReader.setFileName(fileName);

                String filePath = imageChooser.getSelectedFile().getAbsolutePath();
                ImageReader.setFilePath(filePath);
                MainFrame.setImage(ImageReader.readImage());

                MainFrame.setImageLabel(MainFrame.getImage());
            }
            reload();
            owner.setCursor(Cursor.getDefaultCursor());
        }
    }
    private static class SaveAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            owner.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            String filePath = ImageReader.getFilePath();
            File output = new File(filePath);
            String fileName = ImageReader.getFileName();
            String formatName = fileName.substring(fileName.indexOf('.'));
            formatName = fileName.substring(fileName.indexOf('.') + 1 );
            try{
                BufferedImage image;
                if(MainFrame.hasSegmentedImage()) image = MainFrame.getSegmentedImage();
                else image = MainFrame.getImage();

                ImageIO.write(image, formatName, output);
            }
            catch (IOException ex){
                System.out.println(ex.getMessage());
            }
            owner.setCursor(Cursor.getDefaultCursor());
        }
    }
    private static class SaveAsAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser imageChooser = new JFileChooser();
            imageChooser.setCurrentDirectory(new File("./images"));
            imageChooser.addChoosableFileFilter(new ImageFilter());
            imageChooser.setAcceptAllFileFilterUsed(false);

            int result = imageChooser.showSaveDialog(null);
            owner.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if(result == JFileChooser.APPROVE_OPTION){
                String filePath = imageChooser.getSelectedFile().getAbsolutePath();
                String formatName;
                try{
                    String fileName = imageChooser.getSelectedFile().getName();
                    formatName = fileName.substring(fileName.indexOf('.'));
                    formatName = fileName.substring(fileName.indexOf('.') + 1 );
                }
                catch(StringIndexOutOfBoundsException ex){
                    formatName = DEFAULT_EXTENSION;
                    formatName = formatName.toLowerCase();
                    filePath+= "." + formatName;
                }

                File output = new File(filePath);
                try{
                    BufferedImage image;
                    if(MainFrame.hasSegmentedImage()) image = MainFrame.getSegmentedImage();
                    else image = MainFrame.getImage();

                    ImageIO.write(image, formatName, output);
                }
                catch (IOException ex){
                    System.out.println(ex.getMessage());
                }
            }
            owner.setCursor(Cursor.getDefaultCursor());
        }
    }
    private static class KMeansAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {

            BottomPanel.setDurationInfoVisible(false);
            if(MainFrame.hasSegmentedImage()){
                MainFrame.setImage(MainFrame.getSegmentedImage());
            }
            ClusterInputDialog dialog = new ClusterInputDialog(owner, MainFrame.hasRescaledImage());
            dialog.setVisible(true);

            int clusterCount = dialog.getClusterCount();
            boolean original = dialog.checkImageSource();

            if(clusterCount > 0){
                new Thread(() -> {
                    owner.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    BottomPanel.setProgressBarVisible(true);
                    KMeansAlgorithm segmentation;

                    long start = System.currentTimeMillis();
                    if (original) segmentation = new KMeansAlgorithm(clusterCount, MainFrame.getImage());
                    else segmentation = new KMeansAlgorithm(clusterCount, MainFrame.getRescaledImage());

                    long elapsedTimeMillis = System.currentTimeMillis()-start;
                    float elapsedTimeSec = elapsedTimeMillis/1000F;
                    BottomPanel.setDurationTime(elapsedTimeSec);

                    undo.setEnabled(true);
                    BufferedImage output = segmentation.getOutputImage();

                    MainFrame.setImageLabel(output);
                    if (!original){
                        double scale = (double)MainFrame.getImage().getWidth() / (double)output.getWidth();
                        output = ImageRescaler.rescaleImage(output, scale);
                    }
                    MainFrame.setSegmentedImage(output);
                    owner.setCursor(Cursor.getDefaultCursor());
                    BottomPanel.setProgressBarVisible(false);
                    BottomPanel.setDurationInfoVisible(true);
                }).start();
            }
        }
    }
    private static class RegionGrowingAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            BottomPanel.setDurationInfoVisible(false);
            if(MainFrame.hasSegmentedImage()){
                MainFrame.setImage(MainFrame.getSegmentedImage());
            }
            BufferedImage working_image;

            if(MainFrame.hasRescaledImage()) working_image = MainFrame.getRescaledImage();
            else working_image = MainFrame.getImage();

            double scale;
            if(MainFrame.hasRescaledImage()) scale = (double)MainFrame.getImage().getWidth() / (double)MainFrame.getRescaledImage().getWidth();
            else scale = 1;
            RegionChooseDialog dialog = new RegionChooseDialog(owner, working_image, scale);
            dialog.setVisible(true);

            var seedsArray = dialog.getRegions();
            int alpha = dialog.getAlpha();
            if(seedsArray.size() > 0){
                new Thread(() -> {
                    BottomPanel.setProgressBarVisible(true);
                    owner.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    long start = System.currentTimeMillis();
                    var segmentation = new RegionGrowingAlgorithm(MainFrame.getImage(), seedsArray, alpha);
                    undo.setEnabled(true);

                    long elapsedTimeMillis = System.currentTimeMillis() - start;
                    float elapsedTimeSec = elapsedTimeMillis / 1000F;
                    BottomPanel.setDurationTime(elapsedTimeSec);

                    BufferedImage output = segmentation.getOutputImage();
                    MainFrame.setSegmentedImage(output);
                    MainFrame.setImageLabel(output);
                    owner.setCursor(Cursor.getDefaultCursor());
                    BottomPanel.setProgressBarVisible(false);
                    BottomPanel.setDurationInfoVisible(true);
                }).start();
            }
        }
    }
    private static class GlobalThresholdingAction implements  ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            BottomPanel.setDurationInfoVisible(false);
            if(MainFrame.hasSegmentedImage()){
                MainFrame.setImage(MainFrame.getSegmentedImage());
            }
            new Thread(() -> {
                owner.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                BottomPanel.setProgressBarVisible(true);
                long start = System.currentTimeMillis();
                var segmentation = new GlobalThresholdingAlgorithm(MainFrame.getImage());
                undo.setEnabled(true);

                long elapsedTimeMillis = System.currentTimeMillis() - start;
                float elapsedTimeSec = elapsedTimeMillis / 1000F;
                BottomPanel.setDurationTime(elapsedTimeSec);

                BufferedImage output = segmentation.getOutputImage();
                MainFrame.setSegmentedImage(output);
                MainFrame.setImageLabel(output);
                owner.setCursor(Cursor.getDefaultCursor());
                BottomPanel.setProgressBarVisible(false);
                BottomPanel.setDurationInfoVisible(true);
            }).start ();
        }
    }
    private static class LocalThresholdingAction implements  ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            BottomPanel.setDurationInfoVisible(false);
            if(MainFrame.hasSegmentedImage()){
                MainFrame.setImage(MainFrame.getSegmentedImage());
            }
            new Thread(() -> {
                owner.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                BottomPanel.setProgressBarVisible(true);
                long start = System.currentTimeMillis();
                undo.setEnabled(true);
                var segmentation = new AdaptiveThresholdingAlgorithm(MainFrame.getImage());

                long elapsedTimeMillis = System.currentTimeMillis() - start;
                float elapsedTimeSec = elapsedTimeMillis / 1000F;
                BottomPanel.setDurationTime(elapsedTimeSec);

                BufferedImage output = segmentation.getOutputImage();
                MainFrame.setSegmentedImage(output);
                MainFrame.setImageLabel(output);
                owner.setCursor(Cursor.getDefaultCursor());
                BottomPanel.setProgressBarVisible(false);
                BottomPanel.setDurationInfoVisible(true);
            }).start ();
        }
    }
    private static class ImageFilter extends FileFilter {
        public final static String JPEG = "jpeg";
        public final static String JPG = "jpg";
        public final static String TIFF = "tiff";
        public final static String TIF = "tif";
        public final static String PNG = "png";

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = getExtension(f);
            if (extension != null) {
                return extension.equals(TIFF) ||
                        extension.equals(TIF) ||
                        extension.equals(JPEG) ||
                        extension.equals(JPG) ||
                        extension.equals(PNG);
            }
            return false;
        }

        @Override
        public String getDescription() {
            return "Pliki obrazów";
        }

        String getExtension(File f) {
            String extension = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 &&  i < s.length() - 1) {
                extension = s.substring(i+1).toLowerCase();
            }
            return extension;
        }
    }
}

