import Dialogs.ClusterInputDialog;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
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
    private static final JMenuItem undo = new JMenuItem("Cofnij");
    private static JFileChooser imageChooser = null;
    private static Frame owner;
    public MainMenuBar(Frame frame){
        owner = frame;
        imageMenu.add(openItem);
        imageMenu.setFont(MainFrame.getBasicFont());
        openItem.setFont(MainFrame.getBasicFont());
        openItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        openItem.addActionListener(e->{
            imageChooser = new JFileChooser();
            imageChooser.setCurrentDirectory(new File("./images"));
            imageChooser.setFileFilter(new FileNameExtensionFilter("Pliki obrazów", "jpg", "png"));
            int result = imageChooser.showOpenDialog(null);

            if(result == JFileChooser.APPROVE_OPTION){
                String filePath = imageChooser.getSelectedFile().getAbsolutePath();

                ImageReader.setFilePath(filePath);
                Main.setImage(ImageReader.readImage());

                MainFrame.setImageLabel(Main.getImage());
            }
            reload();
        });

        imageMenu.add(saveItem);
        saveItem.setFont(MainFrame.getBasicFont());
        saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        saveItem.addActionListener(e->{
            String filePath = ImageReader.getFilePath();
            File output = new File(filePath);
            try{
                BufferedImage out = Main.saveImage();
                ImageIO.write(out, "png", output);
            }
            catch (IOException ex){
                System.out.println(ex.getMessage());
            }
        });

        imageMenu.add(saveAsItem);
        saveAsItem.setFont(MainFrame.getBasicFont());
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke("ctrl shift S"));
        saveAsItem.addActionListener(e->{
            imageChooser = new JFileChooser();
            imageChooser.setCurrentDirectory(new File("./images"));
            imageChooser.setFileFilter(new FileNameExtensionFilter("Pliki obrazów", "jpg", "png"));

            int result = imageChooser.showSaveDialog(null);
            if(result == JFileChooser.APPROVE_OPTION){
                String filePath = imageChooser.getSelectedFile().getAbsolutePath();
                File output = new File(filePath);
                try{
                    BufferedImage out = Main.saveImage();
                    ImageIO.write(out, "png", output);
                }
                catch (IOException ex){
                    System.out.println(ex.getMessage());
                }
            }
        });
        add(imageMenu);

        segmentationMenu.add(kmeanItem);
        kmeanItem.addActionListener(e->{
            Main.setPixelArray();
            ClusterInputDialog dialog = new ClusterInputDialog(owner);
            dialog.setVisible(true);

            int clusterCount = dialog.getClusterCount();
            if(clusterCount > 0){
                var segmentation = new KMeansSegmentation(clusterCount);
                //t.standardSegmentation();
                long start = System.currentTimeMillis();
                segmentation.hamerlySegmentation();
                long elapsedTimeMillis = System.currentTimeMillis()-start;
                float elapsedTimeSec = elapsedTimeMillis/1000F;

                System.out.println("Czas trwania algortymu: " + elapsedTimeSec + " sec");
                undo.setEnabled(true);
                BufferedImage output = Main.saveImage();
                MainFrame.setImageLabel(output);
            }

        });
        segmentationMenu.setFont(MainFrame.getBasicFont());
        kmeanItem.setFont(MainFrame.getBasicFont());

        segmentationMenu.addSeparator();

        segmentationMenu.add(undo);
        undo.setFont(MainFrame.getBasicFont());
        undo.setAccelerator(KeyStroke.getKeyStroke("ctrl Z"));
        undo.setEnabled(false);
        undo.addActionListener(e->{
            MainFrame.setImageLabel(Main.getImage());
            Main.clearPixelArray();
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
}

